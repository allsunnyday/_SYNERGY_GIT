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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;


import com.bumptech.glide.Glide;
import com.example.geehy.hangerapplication.R;
import com.example.geehy.hangerapplication.RequestActivity;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by MIN on 2018-02-19.
 */

public class WeatherFragment  extends DialogFragment {
    private Dialog dialog;
    private View view;
    private Button okBTN;
    private TextView textView;
    private ImageView imageView1;
    private ImageView imageView2;
    BackgroundTask task;
    private SharedPreferences appData;
    private String id;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.activity_weather, null);
        init();
        dialog.setContentView(view);
        return dialog;
    }

    private void init() {
        okBTN = (Button) view.findViewById(R.id.ok_button);
        imageView1 = (ImageView) view.findViewById(R.id.image1);
        imageView2 = (ImageView) view.findViewById(R.id.image2);
        textView = (TextView) view.findViewById(R.id.weather);

        // 설정값 불러오기
        appData = this.getActivity().getSharedPreferences("appData", MODE_PRIVATE);

        Event();
//        recommend();
    }



    private void Event() {

        okBTN.setOnClickListener(new View.OnClickListener() {//확인 버튼
            @Override
            public void onClick(View view) {
                dismiss(); //dialog 닫기
            }
        });

    }

    private void recommend(){//추천할 옷 서버에서 가져오기
        task = new BackgroundTask();
        task.execute();
    }


    class BackgroundTask extends AsyncTask<String, Integer, String> {
        String url = "http://218.38.52.180/recomend.php";//
        String json=sendObject();//username

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

            if (s.equals("success")) {

//                Glide.with(getActivity()).load("http://218.38.52.180/Android_files/"+ imgpath).into(imageView1);//TOP
//                Glide.with(getActivity()).load("http://218.38.52.180/Android_files/"+ imgpath).into(imageView2);//BOTTOM

            }

        }
    }

    private String sendObject() {
        id = appData.getString("ID", "");
        JSONObject jsonpost = new JSONObject();
        try {
            jsonpost.put("Username", id);//sharedpreference에 저장되었던 username 서버로 보내기 위해서 json 형식으로 변환
        } catch (JSONException e) {
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
