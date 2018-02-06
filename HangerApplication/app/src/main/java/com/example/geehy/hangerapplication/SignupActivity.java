package com.example.geehy.hangerapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {

    private EditText Username;
    private EditText Email;
    private EditText PassWord;
    private ImageView btn_send;
    BackgroundTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Username = (EditText) findViewById(R.id.username);
        Email = (EditText) findViewById(R.id.ID);
        PassWord = (EditText) findViewById(R.id.password);
        btn_send = (ImageView) findViewById(R.id.signin_next);
        event();

    }


    private String sendObject(){

        JSONObject jsonpost = new JSONObject();
        try{
            jsonpost.put("Username", Username.getText());
            jsonpost.put("Email", Email.getText());
            jsonpost.put("PassWord", PassWord.getText());
        }catch (JSONException e){
            e.printStackTrace();
        }
        return jsonpost.toString();
    }


    class BackgroundTask extends AsyncTask<String, Integer, String> {
        String url = "http://218.38.52.180/join.php";
        String json=sendObject();

        @Override
        protected String doInBackground (String...params){

            String result; // 요청 결과를 저장할 변수.
            RequestActivity requestHttpURLConnection = new RequestActivity();
            result = requestHttpURLConnection.request(url, json); // 해당 URL로 부터 결과물을 얻어온다.
            return result;

        }

        @Override
        protected void onPostExecute (String s) {
            super.onPostExecute(s);

            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
             //   Toast.makeText(getApplication(),"♥이메일을 확인해주세요♥", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), SignupApprovedActivity.class);
            startActivity(intent);
            finish();


        }
    }


    private void event(){
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = new BackgroundTask();
                task.execute();
            }
        });}
}





