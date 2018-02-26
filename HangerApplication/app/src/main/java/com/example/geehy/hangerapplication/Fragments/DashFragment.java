package com.example.geehy.hangerapplication.Fragments;

//코디 메뉴
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.geehy.hangerapplication.DialogFragment.AddCoordyFragment;
import com.example.geehy.hangerapplication.DialogFragment.CodiInfoFragment;
import com.example.geehy.hangerapplication.MainActivity;
import com.example.geehy.hangerapplication.MainPageActivity;
import com.example.geehy.hangerapplication.R;
import com.example.geehy.hangerapplication.RequestActivity;
import com.example.geehy.hangerapplication.gridview_home.CoordyItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
//import static com.example.geehy.hangerapplication.DialogFragment.AddCoordyFragment.newInstance;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashFragment extends Fragment {
    private View view;
    private GridView gridView;
    private FragmentManager manager;
    private ImageView img;
    private CoordyGridAdapter coordyAdapter;
    private boolean isVisible;
    private ArrayList<CoordyItem> coordyItemslist;
    private FloatingActionButton fab;
    private static final String SERVER_PATH = "http://218.38.52.180/";//파일 업로드시
    private Uri uri;
    private SharedPreferences appData;
    private String id;
    private String path = "";
    private JSONArray coordies = null;
    BackgroundTask3 task;
    private int numberOfCoordy;
    private Handler mhandle;

    public DashFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        coordyItemslist = ((MainPageActivity)getActivity()).getCoordy();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_dash, container, false);
        appData = this.getActivity().getSharedPreferences("appData", MODE_PRIVATE);     //설정값을 가지고 온다
        load();                     //Username을 가지고 옴
        change();                   //이미 불러온 coordyPath가 있는 경우
        if(path.equals("") ) {      // 로그인 후 앱 메인화면 처음 켜지는 경우 서버에서 이미지를 가져온다.
            Log.d("path", "ready to getCoody");
            getCoordtImg();         //이미지 가져오기
        }
        init();

        coordyAdapter= new CoordyGridAdapter( getActivity(), R.layout.item_coordy_gridview , coordyItemslist);
        gridView.setAdapter(coordyAdapter);


        return view;
    }

    private void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )  ID : Username
        id = appData.getString("ID", "");
        Log.d("SharedPreferences", id);
    }

    private void change() {     //서버에서 가져온 이미지 url 배열에 저장
        path = appData.getString("CoordyPath", "");
        Log.d("CoordyPath:", path);

        try{
           JSONObject jsonObject = new JSONObject(path);
           coordies = jsonObject.getJSONArray("result");
           int i;
           coordyItemslist.clear();
           for (i=0; i < coordies.length(); i++){
               JSONObject jo = coordies.getJSONObject(i);
               CoordyItem ci = new CoordyItem();
               ci.setTopImgURL(jo.getString("topPath"));
               ci.setBottomImgURL(jo.getString("bottomPath"));
               ci.setCodiName(jo.getString("name"));
               ci.setNo(jo.getInt("codi_no"));
               ci.setFullCodiImgURL(jo.getString("fullCodiPath"));
               ci.setLikes(jo.getInt("hit"));
               coordyItemslist.add(ci);
               ((MainPageActivity)getActivity()).setCoordylist(coordyItemslist);
           }
           numberOfCoordy = i;
       }catch(JSONException e){
           e.printStackTrace();
       }

    }


    private void getCoordtImg() {
        task = new BackgroundTask3();
        task.execute();

    }


    public void init() {
        //isVisible = false; //F : 안보임 T : 보임
        gridView = (GridView) view.findViewById(R.id.dash_gridview);
        //dashinsert = (LinearLayout) view.findViewById(R.id.dash_insert_layout); //플로팅 버튼을 위한 linerlayout이었음-> 삭제
        fab = (FloatingActionButton) view.findViewById(R.id.dash_fbtn);
        event();
    }


    public void event() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ///// AddCoordyFragment
//                Bundle bundle = new Bundle();
//                Log.d("Test Layout", "Show Fragment");
//                AddInfoFragment addfragment = newInstance(list.get(id));
//                manager = getFragmentManager();
//                addfragment.show(getActivity().getSupportFragmentManager(), "AddInfoFragment");
//                appData.edit().remove("Path").commit();
                Bundle bundle = new Bundle();
                Log.d("Test Layout", "Show  addcoordy Fragment");
                AddCoordyFragment addCoordyFragment = new AddCoordyFragment();
                addCoordyFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.content, new DashFragment())
                                .commit();
                    }
                });

                addCoordyFragment.show(getActivity().getFragmentManager(), "AddCoordyFragment");
                //getCoordtImg();
                appData.edit().remove("CoordyPath").commit(); //변경된 내용을 보여주기 위해서?
                change();
                coordyAdapter.notifyDataSetChanged();

               // refreshFrag();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                CoordyItem cd = coordyItemslist.get(position);
                // 코디 이름도 동일한 방식으로 넣어준다
                bundle.putString("TOP", cd.getTopImgURL());
                bundle.putString("BOTTOM", cd.getBottomImgURL());
                bundle.putString("NAME", cd.getCodiName());
                bundle.putString("FULL", cd.getFullCodiImgURL());
                bundle.putInt("NO", cd.getNo());
                bundle.putInt("LIKE", cd.getLikes());
                Log.d("Test Layout", "Show  CODIINFO Fragment");

                CodiInfoFragment codiInfoFragment = new CodiInfoFragment();
                codiInfoFragment.setArguments(bundle);
                codiInfoFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.content, new DashFragment())
                                .commit();
                    }
                });

                manager = getFragmentManager();

                codiInfoFragment.show(getActivity().getFragmentManager(), "CodiInfoFragment");
                appData.edit().remove("CoordyPath").commit(); //변경된 내용을 보여주기 위해서?

//
//                manager = getFragmentManager();
//                addfragment.show(getActivity().getSupportFragmentManager(), "AddInfoFragment");
//                appData.edit().remove("Path").commit();

                change();
                coordyAdapter.notifyDataSetChanged();
            //    refreshFrag();


            }


        });
//        {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                Log.d("Test Layout", "Show  CODIINFO Fragment");
//                CodiInfoFragment codiInfoFragment = new CodiInfoFragment();
//                codiInfoFragment.show(getActivity().getFragmentManager(), "CodiInfoFragment");
//
//            }
//        });

    }

   /* private void refreshFrag() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();

    }
*/

    private String sendObject_inCoordy() {
        JSONObject jsonpost = new JSONObject();
        try {
            String coordyid = id +"_coordy";
            jsonpost.put("Username", coordyid); //sharedpreference에 저장되었던 username 서버로 보내기(해당 유저 이미지 가져오기 위해)
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonpost.toString();
    }

    class BackgroundTask3 extends AsyncTask<String, Integer, String> {
        String url = "http://218.38.52.180/getimgpath4.php";
        String json = sendObject_inCoordy();

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
            //Log.d("getCOORDY:", s);
            if(!(s.equals("no path"))) {
                save(s);
                change();
                coordyAdapter.notifyDataSetChanged();
            }
        }
    }
    private void save(String s) {
        SharedPreferences.Editor editor = appData.edit(); // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        Log.d("CoordyPath_save()",s);
                                                            // 에디터객체.put타입( 저장시킬 이름, 저장시킬 값 )
                                                            // 저장시킬 이름이 이미 존재하면 덮어씌움
                                                            //  editor.putBoolean("SAVE_LOGIN_DATA", checkBox.isChecked());
        editor.putString("CoordyPath", s);                  // CoordyPath라는 appdat에 넣음 >> 앱전체에서 사용할 수 있도록
                                                            //    editor.putString("PWD", pwtext.getText().toString().trim());
                                                            // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
    }

/*

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (data == null)
//            return;
//        switch (requestCode) {
//            case 100:
//                if (resultCode == RESULT_OK) {
//                    path = getPathFromURI(data.getData());
//                    img.setImageURI(data.getData());
//                    //upload.setVisibility(View.VISIBLE);
//
//                }
//        }
    }

//    private String getPathFromURI(Uri contentUri) {
////        String[] proj = { MediaStore.Images.Media.DATA };
////        CursorLoader loader = new CursorLoader(getContext(), contentUri, proj, null, null, null);
////        Cursor cursor = loader.loadInBackground();
////        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
////        cursor.moveToFirst();
////        return cursor.getString(column_index);
//    }
*/


    public class CoordyGridAdapter extends BaseAdapter {
        LayoutInflater layoutInflater;
        private Context context;
        private int layout;
        private ArrayList<CoordyItem> coordyItemslist;
//        private ImageButton likes;

        public CoordyGridAdapter(Context context,int layout, ArrayList<CoordyItem> list){
            this.context = context;
            this.layout = layout;
            this.coordyItemslist = list;
            layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            //가져올 코디의 개수 ?
            return numberOfCoordy;
        }

        @Override
        public Object getItem(int position) {
            return coordyItemslist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        //항목의 수만큼 반복해서 호출 맨천음 호출했을 떄는 position이 0 그리드뷰 0번째 항목모양리턴
        @Override
        public View getView(int position, View convertView, ViewGroup parent){ //항목의 수만큼 반복해서 호출 맨천음 호출했을 떄는 position이 0 그리드뷰 0번째 항목모양리턴

            if(convertView == null) {
                convertView = layoutInflater.inflate(layout, null);
            }

            ImageView imageViewTop = (ImageView) convertView.findViewById(R.id.dash_top);
            //ImageView imageViewBottom = (ImageView) convertView.findViewById(R.id.dash_bottom);

            //final dressItem di = list.get(position);
            final CoordyItem ci =coordyItemslist.get(position);

            if(ci.getTopImgURL().equals("")||ci.getTopImgURL().equals("null")
                    || ci.getBottomImgURL().equals("")||ci.getBottomImgURL().equals("null")){
                            imageViewTop.setImageResource(R.drawable.tempimg);
                            //imageViewBottom.setImageResource(R.drawable.tempimg);
            }else{
                Glide.with(getActivity())
                        .load("http://218.38.52.180/Android_files/"+ ci.getFullCodiImgURL())
                        .override(1000,500)
                        .fitCenter()
                        .into(imageViewTop);//보여줄 이미지 파일
//                Glide.with(getActivity())
//                        .load("http://218.38.52.180/Android_files/"+ ci.getBottomImgURL())
//                        .override(600,200)
//                        .into(imageViewBottom);//보여줄 이미지 파일
            }



            return convertView;
        }
    }

}


