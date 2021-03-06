package com.example.geehy.hangerapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Target;

public class SignupActivity extends AppCompatActivity {

    private EditText Username;
    private EditText Email;
    private EditText PassWord;
    private ImageView btn_send;
    private Button checkDupli;
    BackgroundTask task;
    BackgroundTask2 task2;
    private boolean isUsernameChecked = false;
    private boolean isEmailChecked = false;
    private boolean ispwdChecked = false;
    private EditText tall;
    private EditText weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Username = (EditText) findViewById(R.id.username);
        Email = (EditText) findViewById(R.id.ID);
        PassWord = (EditText) findViewById(R.id.password);
        tall = (EditText)findViewById(R.id.tallTextview);
        weight = (EditText)findViewById(R.id.weightTextView);

        btn_send = (ImageView) findViewById(R.id.signin_next);
        checkDupli = (Button) findViewById(R.id.button);

        Log.d("text", Email.getText() +" ");
        event();

    }


    private String sendObject(){

        JSONObject jsonpost = new JSONObject();
        try{
            jsonpost.put("Username", Username.getText());
            jsonpost.put("Email", Email.getText());
            jsonpost.put("PassWord", PassWord.getText());
            jsonpost.put("Tall",tall.getText() );
            jsonpost.put("Weight", weight.getText());
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

                    if(isEmailChecked) {
                        if(!(Email.equals("")) && !(Username.equals("")) && !(PassWord.equals(""))) {
                            task = new BackgroundTask();
                            task.execute();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "모두 입력해주세요!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "중복 체크를 해주세요", Toast.LENGTH_SHORT).show();
                    }

            }
        });

        checkDupli.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Email.equals("")) {
                    task2 = new BackgroundTask2();
                    task2.execute();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Email을 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    private String sendObject2(){

        JSONObject jsonpost = new JSONObject();
        try{
            jsonpost.put("Email", Email.getText());
        }catch (JSONException e){
            e.printStackTrace();
        }
        return jsonpost.toString();
    }


    class BackgroundTask2 extends AsyncTask<String, Integer, String> {
        String url = "http://218.38.52.180/emailcheck.php";
        String json=sendObject2();

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
            Log.d("sis", s);
            if(s.equals("true")){
                Toast.makeText(getApplicationContext(), "이미 있음 ", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "없음  ", Toast.LENGTH_SHORT).show();
                isEmailChecked = true;
            }
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.

        }
    }





}





