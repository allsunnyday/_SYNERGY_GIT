//달력 메뉴에서 ♡Daily Look♡ 눌렀을 때 실행

package com.example.geehy.hangerapplication.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.geehy.hangerapplication.MainPageActivity;
import com.example.geehy.hangerapplication.R;
import com.example.geehy.hangerapplication.RequestActivity;
import com.example.geehy.hangerapplication.gridview_home.dressItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class CalendarFragment extends DialogFragment {
    private Dialog dialog;
    private View view;
    private Button okBTN;
    private ImageButton delBTN;
    private TextView datetext;
    private String date;
    private String temp;
    private String id;
    private  boolean isSelected = false;
    private int index;
    private JSONArray calendar = null;
    private GridAdapter adapter;
    private GridView gridView;
    private ArrayList<dressItem> list = new ArrayList<>();
     private boolean isCheck[] = new boolean[1000];
    private SharedPreferences appData;
    BackgroundTask task;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();
        date = mArgs.getString("Date");//DressFragement에서 날짜 가져오기
        temp= mArgs.getString("Img");//DressFragement에서 imgpath   가져오기
        getcoodi();
        dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.activity_calendar, null);
        init();
        adapter = new GridAdapter(getActivity(),R.layout.item_calendar_grid,list);//그리드 뷰의 디자인의 객체를 생성
        gridView.setAdapter(adapter);//그리드 뷰의 객체에 그리드 뷰의 디자인을 적용
        dialog.setContentView(view);
       // list = ((MainPageActivity) getActivity()).getList();

        return dialog;
    }

    private void init() {

     //   okBTN = (Button) view.findViewById(R.id.ok_button);
        delBTN = (ImageButton) view.findViewById(R.id.delete_btn);
        datetext = (TextView) view.findViewById(R.id.date_text);
        gridView = (GridView) view.findViewById(R.id.calendar_gridview);//그리드 뷰의 객체를 가져오기
        datetext.setText(date); //해당 날짜 set

        Event();
    }

    private void Event() {
/*
        okBTN.setOnClickListener(new View.OnClickListener() {//확인 버튼
            @Override
            public void onClick(View view) {
                dismiss(); //dialog 닫기
            }
        });
*/
        delBTN.setOnClickListener(new View.OnClickListener() {//삭제 버튼

            @Override
            public void onClick(View view) {
                adapter.notifyDataSetChanged();
                int total=0;
                for(int position=0; position < isCheck.length; position++) {
                    if (isCheck[position] == true) {//체크박스가 선택됐을 때
                        Toast.makeText(getActivity(), "position:" + position, Toast.LENGTH_LONG).show();
                        total+=1;
                    }
                }
                if(total == 0) {//선택 안됐을 때
                    alert_show();//선택하라는 Alert창
                }else{
                    delete_show(total); //삭제 확인 Alert창
                }

            }
        });

    }


    void delete_show(int total)
    {
        final int del = total;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(total+"개의 항목을 삭제하시겠습니까?");
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       gridView.setAdapter(adapter);
                        for (int i=0; i <isCheck.length;i++){//체크상태 초기화
                            isCheck[i] = false;
                        }
                    }


                });

        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       // delete_daily();//삭제
                        gridView.setAdapter(adapter);
                        for (int i=0; i <isCheck.length;i++){//체크상태 초기화
                            isCheck[i] = false;
                        }
                    }
                });

        builder.show();
    }

    void alert_show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("삭제할 코디를 선택해주세요 ");
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }



    public void getcoodi(){

        try {
            JSONObject jObject = new JSONObject(temp);//bundle로 받아온 php 결과 json형식으로 저장
            calendar = jObject.getJSONArray("daily");
            list.clear();
            int i;
            for(i=0; i < calendar.length(); i++){
                JSONObject c = calendar.getJSONObject(i);
                dressItem di = new dressItem();
                di.setCat1(c.getString("category"));//카테고리
                di.setImgURL( c.getString("coordy")); //코디
                list.add(di);
            }
            index = i;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void delete_daily(){
        task = new BackgroundTask();
        task.execute();
    }


    class BackgroundTask extends AsyncTask<String, Integer, String> {//편집할 내용 서버로 보내기
        String url = "http://218.38.52.180/delete_Daily.php";
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
            if (s.equals("fail")) {//실패
                Toast.makeText(getActivity(), "실패", Toast.LENGTH_SHORT).show();
            }else if(s.equals("no img")){//성공 & DB에 저장된 코디가 없는 경우
                dismiss();
                Toast.makeText(getContext(), "저장된 코디가 없어요T_T 코디를 추가해주세요", Toast.LENGTH_LONG).show();
            } else{//성공
                temp = s;
                getcoodi(); //list 갱신
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity(),"삭제 완료",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String sendObject(){ //편집할 내용 json으로
        JSONObject jsonpost = new JSONObject();
        try{
            appData = this.getActivity().getSharedPreferences("appData", MODE_PRIVATE);
            id = appData.getString("ID", "");//username 받아오기
            jsonpost.put("Username", id);//username
            jsonpost.put("Date", date);
            //   jsonpost.put("Imgurl", imgurl);//지울 사진

        }catch (JSONException e){
            e.printStackTrace();
        }
        return jsonpost.toString();
    }



    //가져온 이미지 gridview에 set
    public class GridAdapter extends BaseAdapter {
        LayoutInflater layoutInflater;
        private Context context;
        private int layout;
        private ArrayList<dressItem> list;



        public GridAdapter(Context context,int layout,ArrayList<dressItem> list){
            this.context = context;
            this.layout = layout;
            this.list = list;
            layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }
        public final  int getCount(){
            return index;
        }

        public final Object getItem (int position){
            return list.get(position);
        }

        public final long getItemId(int position){
            return position;
        }
        public View getView(final int position, View convertView, ViewGroup parent){ //항목의 수만큼 반복해서 호출 맨천음 호출했을 떄는 position이 0 그리드뷰 0번째 항목모양리턴
            if(position == index){
                return convertView;
            }

            if(convertView == null) {
                convertView = layoutInflater.inflate(layout, null);
            }


           final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.check);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.item_calendar2);
            TextView tv = (TextView) convertView.findViewById(R.id.item_calendar1);//카테고리 textview

            final dressItem di = list.get(position);
            String tempString = di.getCat1();
            if(!(tempString.equals("") || tempString.equals("null"))) {
                tv.setText(tempString);
            }

            String imgpath = di.getImgURL();
            if(imgpath.equals("") || imgpath.equals("null")){
                imageView.setImageResource(R.drawable.tempimg);
            }else{
                Glide.with(getActivity()).load("http://218.38.52.180/Android_files/"+ imgpath).into(imageView);//보여줄 이미지 파일
            }



            checkBox.setOnClickListener(new CheckBox.OnClickListener() {
                @Override
                public void onClick(View v) {

            //        if (((CheckBox)v).isChecked()) {
                        if (checkBox.isChecked()) {
                        isCheck[position] = true;
                    } else {
                        isCheck[position] = false;

                    }
                }
            }) ;

            return convertView;
        }


    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }





}