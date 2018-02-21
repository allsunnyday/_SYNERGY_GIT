//날씨 메뉴

package com.example.geehy.hangerapplication.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

import com.example.geehy.hangerapplication.DialogFragment.CalendarFragment;
import com.example.geehy.hangerapplication.DialogFragment.WeatherFragment;
import com.example.geehy.hangerapplication.R;
import com.example.geehy.hangerapplication.gridview_home.AutoScrollAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */


public class TempFragment extends Fragment {
    private FragmentManager manager;
   private View view;
   private Button weatherBTN;
    private AutoScrollViewPager autoViewPager;
   private ArrayList<String> data = new ArrayList<>(); //이미지 url를 저장하는 arraylist

    public TempFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data.add("temper.jpg");
        data.add("tem6.jpg");
        data.add("tem5.jpg");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view  = inflater.inflate(R.layout.fragment_temp, container, false);
        init();

        return view;
    }
    public void init() {

        weatherBTN = (Button) view.findViewById(R.id.getWeatherBTN);
        autoViewPager = (AutoScrollViewPager)view.findViewById(R.id.autoViewPager);
        AutoScrollAdapter scrollAdapter = new AutoScrollAdapter(getActivity().getApplicationContext(), data);
        autoViewPager.setAdapter(scrollAdapter); //Auto Viewpager에 Adapter 장착
        autoViewPager.setInterval(2500); // 페이지 넘어갈 시간 간격 설정
        autoViewPager.startAutoScroll(); //Auto Scroll 시작


        event();
    }

    public void event() {
        weatherBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                WeatherFragment weatherfragment = new WeatherFragment();
                manager = getFragmentManager();
                weatherfragment.show(getActivity().getSupportFragmentManager(), "weatherFragment");//dialogfragment 띄우기

            }
        });

    }
}