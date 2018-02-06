package com.example.geehy.hangerapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignupApproved extends AppCompatActivity {
    private Button backtologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_approved);
    }


    private void backtosignin() {
        backtologin = (Button) findViewById(R.id.loginagain_button);
        backtologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupApprovedActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
