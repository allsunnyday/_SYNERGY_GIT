//회원정보 수정 dialog

package com.example.geehy.hangerapplication.DialogFragment;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.geehy.hangerapplication.R;
import com.example.geehy.hangerapplication.RequestActivity;

import org.json.JSONException;
import org.json.JSONObject;

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
    private EditText new_pwtext;
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
        new_pwtext = (EditText)view.findViewById(R.id.newpw_edittext);

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




    class BackgroundTask extends AsyncTask<String, Integer, String> {//편집할 내용 서버로 보내기
        String url = "http://218.38.52.180/editinfo.php";
        String json=sendObject();//편집할 내용 받아옴

        @Override
        protected String doInBackground (String...params){

            String result; // 요청 결과를 저장할 변수.
            RequestActivity requestHttpURLConnection = new RequestActivity();
            result = requestHttpURLConnection.request(url, json); // 해당 URL로 부터 결과물을 얻어온다.
            return result;

        }

        @Override
        protected void onPostExecute (String s) {
            super.onPostExecute(s); //서버 결과
            if (s.equals("success")) {
                Toast.makeText(getContext(), "완료", Toast.LENGTH_SHORT).show();
                dismiss();

            }else if(s.equals("incorrect")){
                Toast.makeText(getContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_LONG).show();
            }else
                Toast.makeText(getContext(), "저장 실패", Toast.LENGTH_SHORT).show();
            }

    }


    private String sendObject(){ //편집할 내용 json으로
        JSONObject jsonpost = new JSONObject();
        try{
            appData = this.getActivity().getSharedPreferences("appData", MODE_PRIVATE);
            id = appData.getString("ID", "");//username 받아오기
            //String colorSum = vibantColor + ","+ mutedColor;
            jsonpost.put("Username", usernametext.getText());//username
            jsonpost.put("now_pw", pwtext.getText());//현재 비밀번호
            jsonpost.put("new_pw", new_pwtext.getText());//새로운 비밀번호

        }catch (JSONException e){
            e.printStackTrace();
        }
        return jsonpost.toString();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }





}
