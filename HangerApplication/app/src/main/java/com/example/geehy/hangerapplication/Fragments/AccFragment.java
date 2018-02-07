//설정 메뉴

package com.example.geehy.hangerapplication.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.geehy.hangerapplication.DialogFragment.EditInfoFragment;
import com.example.geehy.hangerapplication.LoginActivity;
import com.example.geehy.hangerapplication.R;
import com.example.geehy.hangerapplication.RequestActivity;

import org.json.JSONException;
import org.json.JSONObject;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccFragment extends Fragment {

    private View view;
    private Button logoutbtn;
    private Button unregisterbtn;
    private Button editbtn;
    private Button qnabtn;
    private SharedPreferences appData;
    private String id;
    BackgroundTask task;
    private FragmentManager manager;


    public AccFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_acc, container, false);
        appData = this.getActivity().getSharedPreferences("appData", MODE_PRIVATE);

        init();

        return view;
    }

    public void init() {

        logoutbtn = (Button) view.findViewById(R.id.logout);
         logoutbtn = (Button) view.findViewById(R.id.logout);
        editbtn = (Button) view.findViewById(R.id.edit);
        unregisterbtn = (Button) view.findViewById(R.id.unregister);
        qnabtn = (Button) view.findViewById(R.id.unregister);

        event();
    }


    public void event() {
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appData.edit().clear().apply();//sharedpreference에 저장된 값 삭제하기
                Intent intent = new Intent(getContext().getApplicationContext(), LoginActivity.class);//로그인 페이지로 이동
                startActivity(intent);
                getActivity().finish();
            }
        });

        unregisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unregister();
            }
        });

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //회원 정보 수정 페이지로 이동

            }
        });

        qnabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //페이지 추가(?)
            }
        });
    }


    public void unregister(){//회원 탈퇴하기
        task = new BackgroundTask();
        task.execute();
    }

    //sharedpreference에 저장되었던 username 서버로 보내기
    private String sendObject() {
        JSONObject jsonpost = new JSONObject();
        try {
            jsonpost.put("Username", appData.getString(id,""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonpost.toString();
    }

    class BackgroundTask extends AsyncTask<String, Integer, String> {
        String url = "http://218.38.52.180/unregister.php";
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
            if(s.equals("success")) {
                Toast.makeText(getActivity().getApplication(), "탈퇴 완료", Toast.LENGTH_LONG).show();
            }
        }
    }


}
