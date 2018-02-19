package com.example.geehy.hangerapplication.DialogFragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.geehy.hangerapplication.MainPageActivity;
import com.example.geehy.hangerapplication.R;
import com.example.geehy.hangerapplication.RequestActivity;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by JHS on 2018-02-13.
 */

public class CodiInfoFragment extends DialogFragment{
    private SharedPreferences appData; //전체 프래그먼트에서 공유하는 값을 사용할 수 있음
    private Dialog dialog;
    private View view;
    private String top;
    private String bottom;
    private EditText codiName;
    private ImageView topView;
    private ImageView bottomView;
    private ImageButton likes;
    private ImageButton editbtn;
    private ImageButton deletebtn;
    private String name;
    private int codi_no;
    private String id;
    BackgroundTaskDelete task;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //list = ((MainPageActivity) getActivity()).getList();

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);
        Bundle mbundle = getArguments();

        top = mbundle.getString("TOP");
        bottom = mbundle.getString("BOTTOM");
        name = mbundle.getString("NAME");
        codi_no = mbundle.getInt("NO");

        appData = this.getActivity().getSharedPreferences("appData", MODE_PRIVATE);     //설정값을 가지고 온다
        id = appData.getString("ID", "");//username 받아오기
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.activity_codi_info, null);
        dialog.setContentView(view);
        init();

        return dialog;
    }

    private void init() {
        codiName = (EditText) view.findViewById(R.id.edit_codi_name);
        topView = (ImageView)view.findViewById(R.id.codiiffo_top);
        bottomView = (ImageView)view.findViewById(R.id.codiinfo_bottom);
        codiName = (EditText)view.findViewById(R.id.edit_codi_name);
        likes = (ImageButton)view.findViewById(R.id.likebtn);
        editbtn=(ImageButton)view.findViewById(R.id.codi_info_edit_btn);

        deletebtn=(ImageButton)view.findViewById(R.id.codi_info_delete_btn);
        codiName.setText(name);
        Glide.with(getActivity())
                .load("http://218.38.52.180/Android_files/"+top)
                .override(600, 500)
                .into(topView);
        Glide.with(getActivity())
                .load("http://218.38.52.180/Android_files/"+bottom)
                .override(600,500)
                .into(bottomView);

        event();
    }

    private void event() {
        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likes.setImageResource(R.drawable.like_magenta);

            }
        });
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addcoordyFragment를 연결시키는 게 좋을 듯

            }
        });
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCodi();
            }
        });
    }

    private void deleteCodi() {
        task = new BackgroundTaskDelete();
        task.execute();

    }

    class BackgroundTaskDelete extends AsyncTask<String, Integer, String> {//편집할 내용 서버로 보내기
        String url = "http://218.38.52.180/delete_codi.php";
        String json= sendObject_delete();//편집할 내용 받아옴

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
            if (s.equals("성공")) {
                /////성공할 시에 서버에 결과를 보낸다
                //isChanged = true;
                Toast.makeText(getContext(), "delete!", Toast.LENGTH_SHORT).show();
                dismiss();
            }else{
                Toast.makeText(getContext(), "fail..", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String sendObject_delete() {
        JSONObject jsonp = new JSONObject();
        try {
            jsonp.put("Username", id+"_coordy");
            jsonp.put("coordy_no", codi_no );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonp.toString();
    }

}
