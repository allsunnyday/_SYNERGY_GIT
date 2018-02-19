//달력 메뉴에서 ♡Daily Look♡ 눌렀을 때 실행

package com.example.geehy.hangerapplication.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.geehy.hangerapplication.MainPageActivity;
import com.example.geehy.hangerapplication.R;
import com.example.geehy.hangerapplication.gridview_home.dressItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CalendarFragment extends DialogFragment {
    private Dialog dialog;
    private View view;
    private Button okBTN;
    private TextView datetext;
    private String date;
    private String temp;
    private String category;
    private int index;
    private String img;
    private JSONArray calendar = null;
    private GridAdapter adapter;
    private GridView gridView;
    private ArrayList<dressItem> list = new ArrayList<>();

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

        okBTN = (Button) view.findViewById(R.id.ok_button);
        datetext = (TextView) view.findViewById(R.id.date_text);
        gridView = (GridView) view.findViewById(R.id.calendar_gridview);//그리드 뷰의 객체를 가져오기
        datetext.setText(date); //해당 날짜 set

        Event();
    }

    private void Event() {

        okBTN.setOnClickListener(new View.OnClickListener() {//확인 버튼
            @Override
            public void onClick(View view) {
                dismiss(); //dialog 닫기
            }
        });

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
            //    ((MainPageActivity) getActivity()).setList(list);
            }
            index = i;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

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
        public View getView(int position, View convertView, ViewGroup parent){ //항목의 수만큼 반복해서 호출 맨천음 호출했을 떄는 position이 0 그리드뷰 0번째 항목모양리턴

            if(convertView == null) {
                convertView = layoutInflater.inflate(layout, null);
            }

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

            return convertView;
        }
    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }





}