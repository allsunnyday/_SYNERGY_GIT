///달력 메뉴

package com.example.geehy.hangerapplication.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.geehy.hangerapplication.CameraActivity;
import com.example.geehy.hangerapplication.DialogFragment.AddClothesFragment;
import com.example.geehy.hangerapplication.DialogFragment.CalendarFragment;
import com.example.geehy.hangerapplication.R;
import com.example.geehy.hangerapplication.RequestActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */

public class DressFragment extends Fragment {
    private Button Clothes;
    private Button CoordyBTN;
    private LinearLayout dressinsert;
    private CalendarView calendar;
    private FragmentManager manager;
    private String date;
    private View view;
    BackgroundTask task;
    private String id;
    private String img;
    private SharedPreferences appData;


    public DressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_dress, container, false);
        init();
        return view;
    }

    public void init() {

        dressinsert = (LinearLayout) view.findViewById(R.id.dress_insert_Layout);
        Clothes = (Button) view.findViewById(R.id.dress_closet_btn);
        CoordyBTN = (Button) view.findViewById(R.id.coordy_btn);
     //   fab = (FloatingActionButton) view.findViewById(R.id.dress_fbtn);
        calendar = (CalendarView) view.findViewById(R.id.calendar);
        dressinsert.setVisibility(View.GONE);
        // 설정값 불러오기
        appData = this.getActivity().getSharedPreferences("appData", MODE_PRIVATE);

        event();
    }

    public void event() {


        Clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("Date", date);//날짜 정보 넣기("Date"를 키 값으로)

                AddClothesFragment addClothesFragment = new AddClothesFragment();
                addClothesFragment.setArguments(bundle);//날짜 정보 전달
                manager = getFragmentManager();
                addClothesFragment.show(getActivity().getSupportFragmentManager(), "addClothesFragment");//dialogfragment로 이동

                dressinsert.setVisibility(View.GONE);//버튼 없애기

            }
        });

        CoordyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetDailyLook();//서버에 저장된 코디 가져오기

            }
        });


        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {//달력 날짜 클릭하면 CalendarFragment로 이동
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    dressinsert.setVisibility(View.VISIBLE);   //날짜 클릭시 메뉴 버튼 보여준다
                    date = year+"/"+(month+1)+"/"+dayOfMonth;
            }

        });


    }



    private void GetDailyLook(){
        task = new BackgroundTask();
        task.execute();
    }


    class BackgroundTask extends AsyncTask<String, Integer, String> {
        String url = "http://218.38.52.180/getCoordy.php";//
        String json=sendObject();//username & 해당 날짜

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
            return result;
        }

        @Override
        protected void onPostExecute (String s) {
            super.onPostExecute(s);

            if (s.equals("no img")) {//DB에 저장된 코디가 없는 경우
                Toast.makeText(getContext(), "저장된 코디가 없어요T_T 코디를 추가해주세요", Toast.LENGTH_LONG).show();
            }

            else{//저장된 코디가 있는 경우 CalendarFragment로 이동해여 이미지뷰에 보여준다

                Bundle bundle = new Bundle();
                bundle.putString("Date", date);//날짜 정보 넣기("Date"를 키 값으로)
                bundle.putString("Img", s);//코디 정보 넣기("Img"를 키 값으로)

                CalendarFragment calendarfragment = new CalendarFragment();
                calendarfragment.setArguments(bundle);//정보 전달
                manager = getFragmentManager();
                calendarfragment.show(getActivity().getSupportFragmentManager(), "calendarFragment");//dialogfragment 띄우기

                dressinsert.setVisibility(View.GONE);
            }
        }
    }


    private String sendObject() {
        id = appData.getString("ID", "");
        JSONObject jsonpost = new JSONObject();
        try {
            jsonpost.put("Username", id);//sharedpreference에 저장되었던 username 서버로 보내기 위해서 json 형식으로 변환
            jsonpost.put("Date", date);//달력에서 선택한 날짜 서버로 보내기 위해서 json 형식으로 변환
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonpost.toString();
    }


}
