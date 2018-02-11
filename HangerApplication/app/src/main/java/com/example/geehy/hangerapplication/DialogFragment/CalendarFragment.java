package com.example.geehy.hangerapplication.DialogFragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.geehy.hangerapplication.R;

public class CalendarFragment extends DialogFragment {
    private Dialog dialog;
    private View view;
    private Button okBTN;
    private ImageView img;
    private EditText datetext;
    private String date;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();
        date = mArgs.getString("Date");//DressFragement에서 날짜 가져오기

        dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.activity_calender, null);
        dialog.setContentView(view);

        init();
        return dialog;
    }

    private void init() {

        okBTN = (Button) view.findViewById(R.id.ok_button);
        datetext = (EditText)view.findViewById(R.id.date_edittext);
        img = (ImageView) view.findViewById(R.id.calendar_img);
     //   Glide.with(getActivity()).load("http://218.38.52.180/Android_files/"+ imgurl).into(img);//image set
        datetext.setText(date); //해당 날짜 set

        Event();
    }

    private void Event() {

        okBTN.setOnClickListener(new View.OnClickListener() {//확인 버튼
            @Override
            public void onClick(View view) {
                dismiss(); //dialog 닫기
            }
        });



    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}