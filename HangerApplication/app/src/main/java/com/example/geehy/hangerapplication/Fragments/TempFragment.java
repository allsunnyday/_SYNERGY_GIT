//날씨 메뉴

package com.example.geehy.hangerapplication.Fragments;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

import com.example.geehy.hangerapplication.DialogFragment.CalendarFragment;
import com.example.geehy.hangerapplication.DialogFragment.WeatherFragment;
import com.example.geehy.hangerapplication.MainPageActivity;
import com.example.geehy.hangerapplication.R;
import com.example.geehy.hangerapplication.RequestActivity;
import com.example.geehy.hangerapplication.gridview_home.AutoScrollAdapter;
import com.example.geehy.hangerapplication.gridview_home.CoordyItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */


public class TempFragment extends Fragment {
    private FragmentManager manager;
    private View view;
    private Button weatherBTN;
    private AutoScrollViewPager autoViewPager;
    private ArrayList<String> data = new ArrayList<>(); //이미지 url를 저장하는 arraylist
    private SharedPreferences appData;
    private String id;
    private BackgroundTask3 task3;
    private String path;
    private JSONArray coordies;
   // private ArrayList<CoordyItem> coordyItemslist;
    //private int numberOfCoordy;
    private String dressitemPath;

    public TempFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data.add("1519195427506.png");
        //coordyItemslist = ((MainPageActivity)getActivity()).getCoordy();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view  = inflater.inflate(R.layout.fragment_temp, container, false);
        appData = this.getActivity().getSharedPreferences("appData", MODE_PRIVATE);     //설정값을 가지고 온다
        load();

        change(); //이미 path가 존재하면
        if(path.equals("") ) {      // path = coordyPath
            Log.d("path", "ready to getCoody");
            loadCodi();
        }
        init();

        return view;
    }
    private void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )  ID : Username
        id = appData.getString("ID", "");
        dressitemPath = appData.getString("Path", "");
        Log.d("SharedPreferences", id);
        //서버에서 코디의 정보를 불러온다.
    }

    private void loadCodi() {
        task3 = new BackgroundTask3();
        task3.execute();
    }

    public void init() {

        weatherBTN = (Button) view.findViewById(R.id.getWeatherBTN);
        autoViewPager = (AutoScrollViewPager)view.findViewById(R.id.autoViewPager);

        AutoScrollAdapter scrollAdapter = new AutoScrollAdapter(getActivity().getApplicationContext(), data);

        autoViewPager.setAdapter(scrollAdapter); //Auto Viewpager에 Adapter 장착
        autoViewPager.setInterval(3000); // 페이지 넘어갈 시간 간격 설정
        autoViewPager.startAutoScroll(); //Auto Scroll 시작


        event();
    }

    public void event() {

    }



    private void change() {     //서버에서 가져온 이미지 url 배열에 저장
        path = appData.getString("CoordyPath", "");
        Log.d("CoordyPath:", path);

        try{
            JSONObject jsonObject = new JSONObject(path);
            coordies = jsonObject.getJSONArray("result");
            int i;
            //coordyItemslist.clear();
            for (i=0; i < coordies.length(); i++){
                JSONObject jo = coordies.getJSONObject(i);
                CoordyItem ci = new CoordyItem();
                ci.setTopImgURL(jo.getString("topPath"));
                ci.setBottomImgURL(jo.getString("bottomPath"));
                ci.setCodiName(jo.getString("name"));
                ci.setNo(jo.getInt("codi_no"));
                ci.setFullCodiImgURL(jo.getString("fullCodiPath"));
                ci.setLikes(jo.getInt("hit"));
                if(ci.getLikes()!=0) {
                    //coordyItemslist.add(ci);
                    data.add(ci.getFullCodiImgURL());
                }
               // ((MainPageActivity)getActivity()).setCoordylist(coordyItemslist);
                Log.d("finish_", "no: "+ci.getNo()+" codiURI:"+ci.getFullCodiImgURL()+" hit:"+ci.getLikes());
            }
            //numberOfCoordy = i;
        }catch(JSONException e){
            e.printStackTrace();
        }
    }


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
                //coordyAdapter.notifyDataSetChanged();
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
}