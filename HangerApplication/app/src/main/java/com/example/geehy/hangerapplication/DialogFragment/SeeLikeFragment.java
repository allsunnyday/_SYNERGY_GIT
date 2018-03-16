//좋아요 보기


package com.example.geehy.hangerapplication.DialogFragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;import java.util.ArrayList;

import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.geehy.hangerapplication.R;
import com.example.geehy.hangerapplication.RequestActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class SeeLikeFragment  extends DialogFragment {
    private JSONArray photos = null;

    private Dialog dialog;
    private View view;
    private Button cancelBTN;
    private String sharedCodiPath="";
    private String likes;
    private String id="";
    private String temptext="";
    private String like="";
    private String temps;
    private GridAdapter adapter;
    private GridView gridView;
    private int index = 0;
    String[] imgarr = new String[500];
    private  int imgarrIndex=0;
    BackgroundTaskGetlikes getlikestask;
    BackgroundTaskCancel canceltask;
    private SharedPreferences appData;
    private ArrayList<String> likelist;
    private JSONArray mylikes;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLike();

        dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.getlikes, null);
        dialog.setContentView(view);


        return dialog;
    }

    private void init() {
        gridView = (GridView) view.findViewById(R.id.getlikes_gridview);//그리드 뷰의 객체를 가져오기
        adapter = new GridAdapter(getActivity(), R.layout.item_getlikes, likelist);//그리드 뷰의 디자인의 객체를 생성
        gridView.setAdapter(adapter);//그리드 뷰의 객체에 그리드 뷰의 디자인을 적용

        Event();
    }

    private void Event() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                temps = likelist.get(position);

                Log.d("tempstest", temps);
                like = temps.substring(0,temps.indexOf(";"));
                Log.d("like", like);

                if (!gridView.isItemChecked(position)) { // 좋아요 취소한 경우
                    like_cancel();

                }

            }


        });


    }




    //*********************************************좋아요 가져오기 *******************************************//
    private void getLike() {
        getlikestask = new BackgroundTaskGetlikes();
        getlikestask.execute();
    }


    class BackgroundTaskGetlikes extends AsyncTask<String, Integer, String> {
        String url = "http://218.38.52.180/getlikes2.php";
        //getimgpath_forsharedcodi.php
        String json = sendObject_codi();

        @Override
        protected  void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected String doInBackground (String...params){
            super.onPreExecute();
            String result; // 요청 결과를 저장할 변수.
            RequestActivity requestHttpURLConnection = new RequestActivity();
            result = requestHttpURLConnection.request(url, json); // 해당 URL로 부터 결과물을 얻어온다.
            return result;   //--> onPostExecute()
        }

        @Override
        protected void onPostExecute (String s) {
            super.onPostExecute(s);

            if(!(s.equals("no like"))){
                likes = s;
                save(likes);
                change();
                init();
            }
        }
    }


    private String sendObject_codi() {
        JSONObject jsonpost = new JSONObject();
        // 설정값 불러오기
        appData = this.getActivity().getSharedPreferences("appData", MODE_PRIVATE);

        id = appData.getString("ID", "");
        try {
            jsonpost.put("Username", id+"_likesCodi"); //sharedpreference에 저장되었던 username 서버로 보내기(해당 유저 이미지 가져오기 위해)

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonpost.toString();
    }



    private void save(String s) {
        SharedPreferences.Editor editor = appData.edit(); // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        Log.d("save_likes",s);
        // 에디터객체.put타입( 저장시킬 이름, 저장시킬 값 )
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        //  editor.putBoolean("SAVE_LOGIN_DATA", checkBox.isChecked());
        editor.putString("SharedCodiPath", s);                  // CoordyPath라는 appdat에 넣음 >> 앱전체에서 사용할 수 있도록
        //    editor.putString("PWD", pwtext.getText().toString().trim());
        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
    }


    private void change() {     //서버에서 가져온 이미지 url 배열에 저장
        sharedCodiPath = appData.getString("SharedCodiPath", "");


        Log.d("SharedCodiPath:", sharedCodiPath);


        try {
            Log.d("liketest", likes);
            likelist = new ArrayList<>();
            likelist.clear();
            if (!(likes.equals("no like"))) {
                JSONObject getlike = new JSONObject(likes); //좋아요 가져오기
                mylikes = getlike.getJSONArray("result");
                Log.d("myliketest_2", mylikes.toString());

                int i;
                for ( i = 0; i < mylikes.length(); i++) {
                    JSONObject jo = mylikes.getJSONObject(i);
                    temps = jo.getString("mylikes")+";"+jo.getString("info");
                    Log.d("temps", temps);
                    likelist.add(temps);

                }
                index = i;
            }

        }catch(JSONException e){
            e.printStackTrace();
        }
    }



    //**************************************좋아요 취소하기 ********************************************//
    private void like_cancel() {
        canceltask = new BackgroundTaskCancel();
        canceltask.execute();
    }



    class BackgroundTaskCancel extends AsyncTask<String, Integer, String> {
        String url = "http://218.38.52.180/cancel_like.php";
        String json = sendObjec();

        @Override
        protected  void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected String doInBackground (String...params){
            super.onPreExecute();
            String result; // 요청 결과를 저장할 변수.
            RequestActivity requestHttpURLConnection = new RequestActivity();
            result = requestHttpURLConnection.request(url, json); // 해당 URL로 부터 결과물을 얻어온다.
            return result;   //--> onPostExecute()
        }

        @Override
        protected void onPostExecute (String s) {
            super.onPostExecute(s);

            if(!(s.equals("success"))){
                Toast.makeText(getActivity(),"서버 에러",Toast.LENGTH_SHORT).show();
            }else{
                //  getLike();
                //  tempAdapter.notifyDataSetChanged();
            }
        }
    }
    private String sendObjec() {
        JSONObject jsonpost = new JSONObject();
        try {
            jsonpost.put("Username", id+"_likesCodi"); //sharedpreference에 저장되었던 username 서버로 보내기(해당 유저 이미지 가져오기 위해)
            //   jsonpost.put("codino", userLikecoditem.getNo());
            jsonpost.put("codipath", like);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonpost.toString();
    }



    //**************************************Adapter ********************************************//

    //가져온 이미지 gridview에 set

    public class GridAdapter extends BaseAdapter {

        LayoutInflater layoutInflater;
        private Context context;
        private int layout;
        private ArrayList<String> list;
        private boolean isSelectLikeInAdapter;
        private TextView  textView;
        private ImageView imageView;
        private String tempimage;
        private String temptext;
        private String tempstring;


        public GridAdapter(Context context, int layout, ArrayList<String> list) {
            this.context  = context;
            this.layout = layout;
            this.list = list;
            isSelectLikeInAdapter = false;
            layoutInflater = ( LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }


        public boolean getIsSelectLikeInAdapter(){
            return this.isSelectLikeInAdapter;
        }
        @Override
        public int getCount() {

            Log.d("index2eee", index + " ");
            return index;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null) {
                convertView = layoutInflater.inflate(layout, null);
            }
            imageView = (ImageView) convertView.findViewById(R.id.tempGrid_imgVIew);
            textView = (TextView) convertView.findViewById(R.id.codiNameVIew);
//            numlikes = (TextView) convertView.findViewById(R.id.likeNumView);
//            imageButton = (ImageButton)convertView.findViewById(R.id.imageButtonlike);
            tempstring = list.get(position);
            Log.d("tempstring", tempstring);
            tempimage = tempstring.substring(0,tempstring.indexOf(";"));
            temptext = tempstring.substring(tempstring.indexOf(";")+1);
            Log.d("tempimage", tempimage);
            Log.d("temptext", temptext);


            //numlikes.setText(likesnum+"명");
            if(tempimage.equals("")||tempimage.equals("null")){
                imageView.setImageResource(R.drawable.tempimg);
            }
            else{

                textView.setText(temptext);
                //numlikes.setText(ci.getLikes() + "명");

                Glide.with(getActivity())
                        .load("http://218.38.52.180/Android_files/"+ tempimage)
                        .override(2000, 2300)
                        .fitCenter()
                        .into(imageView);//보여줄 이미지 파일

                gridView.setItemChecked(position, true);


            }
            return convertView;
        }
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}