///달력 메뉴

package com.example.geehy.hangerapplication.Fragments;


import android.content.Intent;
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

import com.example.geehy.hangerapplication.CameraActivity;
import com.example.geehy.hangerapplication.DialogFragment.CalendarFragment;
import com.example.geehy.hangerapplication.R;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */

public class DressFragment extends Fragment {
    private Button Clothes,Coordy;
    private FloatingActionButton fab;
    private LinearLayout dressinsert;
    private CalendarView calendar;
    private boolean isVisible;
    private FragmentManager manager;

    private View view;

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
        isVisible = false; //F : 안보임 T : 보임

        dressinsert = (LinearLayout) view.findViewById(R.id.dress_insert_Layout);
        Clothes = (Button) view.findViewById(R.id.dress_closet_btn);
        Coordy = (Button) view.findViewById(R.id.dress_coordy_btn);
        fab = (FloatingActionButton) view.findViewById(R.id.dress_fbtn);
        calendar = (CalendarView) view.findViewById(R.id.calendar);

        event();
    }

    public void event() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVisible) {
                    dressinsert.setVisibility(View.GONE);
                    isVisible = false;
                } else {
                    dressinsert.setVisibility(View.VISIBLE);
                    isVisible = true;
                }
            }
        });


        Clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Coordy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {//달력 날짜 클릭하면 CalendarFragment로 이동
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Bundle bundle = new Bundle();
                bundle.putString("Date", year+"/"+(month+1)+"/"+dayOfMonth);//날짜 정보 넣기("Date"를 키 값으로)

                CalendarFragment calendarfragment = new CalendarFragment();
                calendarfragment.setArguments(bundle);//날짜 정보 전달
                manager = getFragmentManager();
                calendarfragment.show(getActivity().getSupportFragmentManager(), "calendarFragment");//dialogfragment 띄우기


            }

        });

    }
}
