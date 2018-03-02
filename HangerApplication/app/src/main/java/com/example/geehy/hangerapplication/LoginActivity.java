package com.example.geehy.hangerapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText emailtext;
    private EditText pwtext;
    private Button sign;
    private TextView forgotpw;
    private CheckBox autoLogin;
    private TextView signup;
    private SharedPreferences appData;
    private String email;
    private String id;
    private boolean loginChecked;

    BackgroundTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //설정값 불러오기
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        loginChecked = appData.getBoolean("autologin", false);//자동로그인 여부 검사

        if(loginChecked){ //자동 로그인
            id = appData.getString("ID", "");//아이디 가져오기
            Toast.makeText(getApplication(), "♥" + id + "님 반갑습니다♥", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
            startActivity(intent);
            finish();
        }

        init();
        findpw();
        signup();
        event();

    }


    private void init(){
        emailtext = (EditText) findViewById(R.id.Login_email_edittext);
        pwtext = (EditText) findViewById(R.id.Login_password_edittext);
        sign = (Button) findViewById(R.id.email_sign_in_button);
        autoLogin = (CheckBox) findViewById(R.id.autoLoginCheck);
    }


    private  void findpw() {
        forgotpw = (TextView) findViewById(R.id.forgotpw_text);
        forgotpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotintnet = new Intent(getApplicationContext(), ForgotActivity.class);
                startActivity(forgotintnet);
                finish();
            }
        });

    }

    private  void signup(){
        signup = (TextView)findViewById(R.id.signup_text);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupintent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(signupintent);
                finish();
            }
        });
    }

    //아이디&비밀번호 json으로 보내기
    private String sendObject(){
        emailtext = (EditText) findViewById(R.id.Login_email_edittext);
        pwtext = (EditText) findViewById(R.id.Login_password_edittext);
        JSONObject jsonpost = new JSONObject();
        try{
            jsonpost.put("Email", emailtext.getText());
            jsonpost.put("PassWord", pwtext.getText());
        }catch (JSONException e){
            e.printStackTrace();
        }
        return jsonpost.toString();
    }

    // 설정값을 저장하는 함수
    private void save(String username) {
        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = appData.edit();
        String[] str = username.split(",");
        // 에디터객체.put타입( 저장시킬 이름, 저장시킬 값 )
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        editor.putBoolean("autologin", autoLogin.isChecked());
        editor.putString("ID", str[0]);
        editor.putString("WEIGHT", str[1]);
        editor.putString("HEIGHT", str[2]);
        //    editor.putString("PWD", pwtext.getText().toString().trim());
        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
        Log.d("username",str[0]+", "+str[1]+", "+str[2]);
    }


    class BackgroundTask extends AsyncTask<String, Integer, String> {
        String url = "http://218.38.52.180/login.php";
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
            Log.d("logintest",s);
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            if (s.equals("0")) {
                Toast.makeText(getApplication(), "이메일 인증이 완료되지 않았어요T_T", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();

            } else if (s.equals("-1")) {
                Toast.makeText(getApplication(), "로그인 실패T_T", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                save(s); //로그인 성공시 이메일 데이터 저장
                Toast.makeText(getApplication(), "♥" + s + "님 반갑습니다♥", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                startActivity(intent);
                finish();
            }

        }
    }


    private void event(){
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = new BackgroundTask();
                task.execute();
            }
        });

        autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }
}




