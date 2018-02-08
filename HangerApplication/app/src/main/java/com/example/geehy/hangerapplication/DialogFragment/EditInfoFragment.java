//회원정보 수정 dialog

package com.example.geehy.hangerapplication.DialogFragment;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.geehy.hangerapplication.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by MIN on 2018-02-07.
 */



public class EditInfoFragment extends DialogFragment {
    private Dialog dialog;
    private View view;
    private Button finishBTN;
    private Button cancelBTN;
    private EditText usernametext;
    private EditText pwtext;
    private SharedPreferences appData;
    private String id;


    @Override
public Dialog onCreateDialog(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    dialog = super.onCreateDialog(savedInstanceState);
    dialog.setCanceledOnTouchOutside(false);
    LayoutInflater inflater = getActivity().getLayoutInflater();
    view = inflater.inflate(R.layout.activity_edit_info, null);
    dialog.setContentView(view);

    init();
    return dialog;
}

    private void init() {

        finishBTN = (Button) view.findViewById(R.id.finish_button);
        cancelBTN = (Button) view.findViewById(R.id.cancel_button);
        usernametext = (EditText)view.findViewById(R.id.username_edittext);
        pwtext = (EditText)view.findViewById(R.id.Pw_edittext);
        appData = this.getActivity().getSharedPreferences("appData", MODE_PRIVATE);
        id = appData.getString("ID", "");//username 받아오기
        usernametext.setText(id);


        Event();
    }

    private void Event() {

        finishBTN.setOnClickListener(new View.OnClickListener() {//완료 버튼
            @Override
            public void onClick(View view) {
                //서버 저장
                dismiss(); //dialog 닫기
            }
        });

        cancelBTN.setOnClickListener(new View.OnClickListener() {//취소 버튼
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
