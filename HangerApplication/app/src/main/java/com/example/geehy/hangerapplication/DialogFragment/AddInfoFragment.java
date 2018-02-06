package com.example.geehy.hangerapplication.DialogFragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.geehy.hangerapplication.Fragments.HomeFragment;
import com.example.geehy.hangerapplication.GrabcutActivity;
import com.example.geehy.hangerapplication.R;
import com.example.geehy.hangerapplication.RequestActivity;
import com.example.geehy.hangerapplication.gridview_home.dressItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class AddInfoFragment extends DialogFragment {
    private SharedPreferences appData;
    private Dialog dialog;
    private View view;
    private FragmentManager manager;
    ArrayList<dressItem> mlist;
    private Button CompleteBTN;
    private Button CancelBTN;
    private Spinner seasonSpinner;
    private ImageView img;
    private EditText addTag;
    private EditText categorytext;
    private EditText colortext;
    private String id;
    private String item;
    static String imgurl;
    static String category;
    boolean isChanged;
    private int flagWho;
    BackgroundTask task;


    public static AddInfoFragment newInstance(dressItem ds) {
        AddInfoFragment addInfoFragment = new AddInfoFragment();
        //  Supply items as an argument.
        Bundle args = new Bundle();
        args.putSerializable("dress", ds);//homefragment에서 받아온 dress list
        imgurl = ds.getImgURL();//사진 img
        category = ds.getCat1();//카테고리 내용
        addInfoFragment.setArguments(args);

        return addInfoFragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.activity_edit_closet, null);
        dialog.setContentView(view);

        seasonSpinner = (Spinner) view.findViewById(R.id.seasonspinner);
        ArrayAdapter seasonAdapter = ArrayAdapter.createFromResource(getActivity().getApplication(),
                R.array.season, android.R.layout.simple_spinner_item);
        seasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seasonSpinner.setAdapter(seasonAdapter);
        init();
        return dialog;
    }

    private void init() {

        CompleteBTN = (Button) view.findViewById(R.id.additem_insertbutton);
        CancelBTN = (Button) view.findViewById(R.id.cancel_button);
        categorytext = (EditText)view.findViewById(R.id.categorytext2);
        colortext = (EditText)view.findViewById(R.id.color_Edittext);

        img = (ImageView) view.findViewById(R.id.additem_imagethumb);
        Glide.with(getActivity()).load("http://218.38.52.180/Android_files/"+ imgurl).into(img);//image set
        categorytext.setText(category);
        Event();
    }

    private void Event() {

        CompleteBTN.setOnClickListener(new View.OnClickListener() {//편집 버튼
            @Override
            public void onClick(View view) {
                task = new BackgroundTask();
                task.execute();
            }
        });


        CancelBTN.setOnClickListener(new View.OnClickListener() {//취소 버튼
            @Override
            public void onClick(View view) {
                dismiss(); //화면 닫기
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(
                        getContext().getApplicationContext(), // 현재 화면의 제어권자
                        GrabcutActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다

            }

        });
        seasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item = (String) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private String sendObject(){ //편집할 내용 json으로
        JSONObject jsonpost = new JSONObject();
        try{
            appData = this.getActivity().getSharedPreferences("appData", MODE_PRIVATE);
            id = appData.getString("ID", "");//username 받아오기
            jsonpost.put("Username", id);//username
            jsonpost.put("Imgurl", imgurl);//imgUrl
            jsonpost.put("Category", categorytext.getText());//카테고리
            jsonpost.put("Color", colortext.getText());//색깔
            jsonpost.put("Season",item);//계절
        }catch (JSONException e){
            e.printStackTrace();
        }
        return jsonpost.toString();
    }


    class BackgroundTask extends AsyncTask<String, Integer, String> {//편집할 내용 서버로 보내기
        String url = "http://218.38.52.180/addinfo.php";
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
            if (s.equals("성공")) {
                isChanged = true;
                Toast.makeText(getContext(), "저장 완료", Toast.LENGTH_SHORT).show();

                ///////homefragment로 보내기

            }else{
                Toast.makeText(getContext(), "저장 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}