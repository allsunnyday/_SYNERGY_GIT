//날씨 메뉴

package com.example.geehy.hangerapplication.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

import com.bumptech.glide.Glide;
import com.example.geehy.hangerapplication.DialogFragment.CalendarFragment;
import com.example.geehy.hangerapplication.DialogFragment.CodiInfoFragment;
import com.example.geehy.hangerapplication.DialogFragment.WeatherFragment;
import com.example.geehy.hangerapplication.MainPageActivity;
import com.example.geehy.hangerapplication.R;
import com.example.geehy.hangerapplication.RequestActivity;
import com.example.geehy.hangerapplication.gridview_home.AutoScrollAdapter;
import com.example.geehy.hangerapplication.gridview_home.CoordyItem;
import com.example.geehy.hangerapplication.gridview_home.ExpendableHeightGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */


public class TempFragment extends Fragment {
    private FragmentManager manager;
    private View view;
    private LinearLayout linearLayout;
    private EditText searchText;
    private Button weatherBTN;
    private ImageButton searchBTN;
    private AutoScrollViewPager autoViewPager;
    private ArrayList<CoordyItem> data = new ArrayList<>(); //이미지 url를 저장하는 arraylist
    private SharedPreferences appData;
    private String id;
    private String likes;
    BackgroundTask4 task3;
    private String sharedCodiPath="";
    private JSONArray coordies;
    private JSONArray mylikes;
    private ArrayList<CoordyItem> coordyItemslist;
    private ArrayList<String> likelist;
    //private int numberOfCoordy;
    private String dressitemPath;
    private String keyword;
    private int index;
    private TempGridAdater tempAdapter;
    //    private GridView gridView;
    private ExpendableHeightGridView gridView;
    private boolean isSelectLike;
    private CoordyItem userLikecoditem;
    BackgroundTaskForCodiTask coditask;
    BackgroundTaskCancel canceltask;
    BackgroundTaskGetlikes getlikestask;
    BackgroundTaskSearch searchtask;
    private String like ="";


    public TempFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //data.add("1519195427506.png");
        coordyItemslist = ((MainPageActivity)getActivity()).getCoordy();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view  = inflater.inflate(R.layout.fragment_temp, container, false);
        appData = this.getActivity().getSharedPreferences("appData", MODE_PRIVATE);     //설정값을 가지고 온다
        load();
//        change(); //이미 path가 존재하면
//        if(sharedCodiPath.equals("")) {      // path = coordyPath
//            loadCodi();
//        }
        getLike(); //좋아요한 정보 가져오기
        loadCodi(); //공유된 코디 가져오기
        init();

        tempAdapter = new TempGridAdater (getActivity(), R.layout.item_temp_gridview, coordyItemslist);
        gridView.setExpended(true);
        gridView.setAdapter(tempAdapter);

        return view;
    }
    private void load() {
        id = appData.getString("ID", "");
        Log.d("SharedPreferences", id);

    }

    private void loadCodi() {
        task3 = new BackgroundTask4();
        task3.execute();
    }

    public void init() {
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout2);
        weatherBTN = (Button) view.findViewById(R.id.getWeatherBTN);
        searchBTN = (ImageButton) view.findViewById(R.id.search_button);
        autoViewPager = (AutoScrollViewPager)view.findViewById(R.id.autoViewPager);
//        gridView = (GridView) view.findViewById(R.id.tempGridView);
        gridView = (ExpendableHeightGridView) view.findViewById(R.id.tempGridView);
        searchText = (EditText) view.findViewById(R.id.editText2);

        event();
    }

    public void event() {

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Bundle bundle = new Bundle();
                CoordyItem cd = coordyItemslist.get(position);
                like = cd.getFullCodiImgURL(); //누른 이미지

                if (gridView.isItemChecked(position)) { //좋아요 누른경우
                    saveToServer();

                } else { // 좋아요 취소한 경우
                    like_cancel();
                }

//                // 코디 이름도 동일한 방식으로 넣어준다
//                bundle.putString("NAME", cd.getCodiName());
//                bundle.putString("FULL", cd.getFullCodiImgURL());
//                bundle.putInt("NO", cd.getNo());
//                bundle.putInt("LIKE", cd.getLikes());
//                Log.d("Test Layout", "Show  CODIINFO Fragment");
//                tempAdapter.notifyDataSetChanged();

            }



        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(), "꾸욱", Toast.LENGTH_SHORT).show();
                return true;
            }
        });





        searchBTN.setOnClickListener(new View.OnClickListener() { //검색하기
            @Override
            public void onClick(View view) {
                keyword= searchText.getText().toString();
                search();
            }
        });

    }




    //*****************************************공유된 코디 가져오기 ********************************************//
    private void change() {     //서버에서 가져온 이미지 url 배열에 저장
        sharedCodiPath = appData.getString("SharedCodiPath", "");
        int userTall = Integer.parseInt(appData.getString("HEIGHT",""));
        int userWeight = Integer.parseInt(appData.getString("WEIGHT",""));

        Log.d("SharedCodiPath:", sharedCodiPath);

        try{
            Log.d("liketest",likes);
            likelist = new ArrayList<>();
            likelist.clear();
            if(!(likes.equals("no like"))) {
                JSONObject getlike = new JSONObject(likes); //좋아요 가져오기
                mylikes = getlike.getJSONArray("result");
                Log.d("myliketest", mylikes.toString());

                for (int i = 0; i < mylikes.length(); i++) {
                    JSONObject jo = mylikes.getJSONObject(i);
                    likelist.add(jo.getString("mylikes"));
                }
            }


            JSONObject jsonObject = new JSONObject(sharedCodiPath);//코디 가져오기
            coordies = jsonObject.getJSONArray("result");

            int i;
            coordyItemslist.clear();
            for (i=0; i < coordies.length(); i++){
                JSONObject jo = coordies.getJSONObject(i);
                CoordyItem ci = new CoordyItem();
                ci.setCodiName(jo.getString("username")); //여기에서 codiName은 해당 코디의 유저이름
                ci.setNo(jo.getInt("codi_no"));  //shared_codi DB의 key값
                ci.setFullCodiImgURL(jo.getString("codiPath"));
                ci.setLikes(jo.getInt("likes"));
                ci.setTall(jo.getInt("tall"));
                ci.setWeight(jo.getInt("weight"));
                coordyItemslist.add(ci);
                //** autoScrollView에는 user와 동일한 키, 몸무게를 가진 코디만 넘어간다 **//
                if( ci.getTall() == userTall && ci.getWeight() == userWeight){
                    data.add(ci);
                }
                ((MainPageActivity)getActivity()).setCoordylist(coordyItemslist);
                Log.d("finish_", "no: "+ci.getNo()+" codiURI:"+ci.getFullCodiImgURL()+" hit:"+ci.getLikes() + ci.getTall() +","+ci.getWeight());
            }
            index = i;
            //numberOfCoordy = i;
            Log.d("index1", index + " ");
        }catch(JSONException e){
            e.printStackTrace();
        }
        //tempAdapter.notifyDataSetChanged();

        AutoScrollAdapter scrollAdapter = new AutoScrollAdapter(getActivity().getApplicationContext(), data);
        autoViewPager.setAdapter(scrollAdapter); //Auto Viewpager에 Adapter 장착
        autoViewPager.setInterval(2500); // 페이지 넘어갈 시간 간격 설정
        autoViewPager.startAutoScroll(); //Auto Scroll 시작

    }


    private String sendObject_inCoordy() {
        JSONObject jsonpost = new JSONObject();
        try {
            jsonpost.put("Username", id); //sharedpreference에 저장되었던 username 서버로 보내기(해당 유저 이미지 가져오기 위해)
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonpost.toString();
    }

    class BackgroundTask4 extends AsyncTask<String, Integer, String> {
        String url = "http://218.38.52.180/getimgpath_forsharedcodi.php";
        //getimgpath_forsharedcodi.php
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
            Log.d("gett:", s);
            if(!(s.equals("no path"))) {
                save(s);
                change();
                //tempAdapter.notifyDataSetChanged();
            }
        }
    }
    private void save(String s) {
        SharedPreferences.Editor editor = appData.edit(); // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        Log.d("save_",s);
        // 에디터객체.put타입( 저장시킬 이름, 저장시킬 값 )
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        //  editor.putBoolean("SAVE_LOGIN_DATA", checkBox.isChecked());
        editor.putString("SharedCodiPath", s);                  // CoordyPath라는 appdat에 넣음 >> 앱전체에서 사용할 수 있도록
        //    editor.putString("PWD", pwtext.getText().toString().trim());
        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
    }



    //**************************************좋아요 취소하기 ********************************************//
    private void like_cancel() {
        canceltask = new BackgroundTaskCancel();
        canceltask.execute();
    }



    class BackgroundTaskCancel extends AsyncTask<String, Integer, String> {
        String url = "http://218.38.52.180/cancel_like.php";
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

            if(!(s.equals("success"))){
                Toast.makeText(getActivity(),"서버 에러",Toast.LENGTH_SHORT).show();
            }else{
                getLike();
                //  tempAdapter.notifyDataSetChanged();
            }
        }
    }



    //***********************************************좋아요 서버 보내기 *************************************************//
    private void saveToServer() {
        coditask = new BackgroundTaskForCodiTask();
        coditask.execute();
    }

    private String sendObject_codi() {
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


    class BackgroundTaskForCodiTask extends AsyncTask<String, Integer, String> {
        String url = "http://218.38.52.180/testshare.php";
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
            if(!(s.equals("success"))){
                Toast.makeText(getActivity(),"서버 에러",Toast.LENGTH_SHORT).show();
            }
        }
    }


    //*********************************************좋아요 가져오기 *******************************************//
    private void getLike() {
        getlikestask = new BackgroundTaskGetlikes();
        getlikestask.execute();
    }



    class BackgroundTaskGetlikes extends AsyncTask<String, Integer, String> {
        String url = "http://218.38.52.180/getlikes.php";
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

            if(!(s.equals("no likes"))){
                likes = s;
            }
        }
    }




    //*******************************************검색하기******************************************//
    private void search() {
        getLike(); //좋아요 가져오기
        searchtask = new BackgroundTaskSearch();
        searchtask.execute();
    }


    private String sendObject_search() {
        JSONObject jsonpost = new JSONObject();
        try {
            jsonpost.put("Username", id); //sharedpreference에 저장되었던 username 서버로 보내기(해당 유저 이미지 가져오기 위해)
            jsonpost.put("Search", keyword);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonpost.toString();
    }



    class BackgroundTaskSearch extends AsyncTask<String, Integer, String> {
        String url = "http://218.38.52.180/search.php";
        String json = sendObject_search();

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
            Log.d("gett:", s);
            if(!(s.equals("no path"))) {
                save(s);
                change();

                for(int i=0;i <index ; i++){
                    gridView.setItemChecked(i, false);
                }
                linearLayout.setVisibility(View.GONE);
                tempAdapter.notifyDataSetChanged();
                gridView.setAdapter(tempAdapter);
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("검색 결과가 없습니다.");
                builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.show();
            }
        }
    }


    //************************************그리드뷰 어뎁터****************************************//
    private class TempGridAdater extends BaseAdapter{

        LayoutInflater layoutInflater;
        private Context context;
        private int layout;
        private ArrayList<CoordyItem> list;
       // private boolean isSelectLikeInAdapter;
       // private int likesnum;
       // private TextView numlikes;
        private TextView  textView;
        private ImageView imageView;
//        private CoordyItem ci;

        public TempGridAdater(Context context, int layout, ArrayList<CoordyItem> list) {
            this.context  = context;
            this.layout = layout;
            this.list = list;
            //isSelectLikeInAdapter = false;
            layoutInflater = ( LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }


       // public boolean getIsSelectLikeInAdapter(){
//            return this.isSelectLikeInAdapter;
//        }
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

           final CoordyItem ci = list.get(position);
            Log.d("now_", ci.getNo() + " ");
           // ci.setLikes(likesnum);
            //numlikes.setText(likesnum+"명");
            if(ci.getFullCodiImgURL().equals("") || ci.getFullCodiImgURL().equals("null")){
                imageView.setImageResource(R.drawable.tempimg);
            }
            else{
                textView.setText(ci.getTall() +"/"+ci.getWeight());
                //numlikes.setText(ci.getLikes() + "명");

                Glide.with(getActivity())
                        .load("http://218.38.52.180/Android_files/"+ ci.getFullCodiImgURL())
                        .override(2000, 2300)
                        .fitCenter()
                        .into(imageView);//보여줄 이미지 파일

                if(!(likelist.isEmpty())){
                    for (int i =0; i <likelist.size(); i++) { //좋아요한 기록이 있는경우 좋아요가 눌린 상태로
                        if (ci.getFullCodiImgURL().equals(likelist.get(i))) {
                            gridView.setItemChecked(position, true);
                            Log.d("searchtest:", ci.getFullCodiImgURL());

                            break;
                        }
                    }
                }


            }
            return convertView;
        }
    }





}