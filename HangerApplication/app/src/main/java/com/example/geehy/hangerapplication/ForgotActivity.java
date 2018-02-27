package com.example.geehy.hangerapplication;


import android.os.AsyncTask;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

    public class ForgotActivity extends AppCompatActivity {
        private Button backtosignin;
        private EditText Email;
        BackgroundTask task;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        Email = (EditText) findViewById(R.id.ID);
        backtosignin();
    }
    private String sendObject(){

        JSONObject jsonpost = new JSONObject();
        try{
            jsonpost.put("Email", Email.getText());
        }catch (JSONException e){
            e.printStackTrace();
        }
        return jsonpost.toString();
    }

        class BackgroundTask extends AsyncTask<String, Integer, String> {
            String url = "http://218.38.52.180/forgotpw.php";
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
                    Toast.makeText(getApplication(), "메일을 확인해주세요", Toast.LENGTH_SHORT).show();
            }
        }

        private void backtosignin(){
        backtosignin = (Button)findViewById(R.id.loginagain_button);
        backtosignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = new BackgroundTask();
                task.execute();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}