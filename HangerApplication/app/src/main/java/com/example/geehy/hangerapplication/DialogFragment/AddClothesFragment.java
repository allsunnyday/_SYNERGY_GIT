//달력 메뉴에서 ♡Add Clothes♡ 눌렀을 때 실행

package com.example.geehy.hangerapplication.DialogFragment;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;import java.util.ArrayList;
import java.util.List;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.geehy.hangerapplication.R;
import com.example.geehy.hangerapplication.RequestActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by MIN on 2018-02-09.
 */

public class AddClothesFragment  extends DialogFragment {
    private JSONArray photos = null;
    private Dialog dialog;
    private View view;
    private Button cancelBTN;
    private Button okBTN;
    private ListView listView;
    private ImageView iv;
    private String date;
    private String path;
    private String imgpath="";
    private String id;
    private ListAdapter adapter;
    private ArrayAdapter new_adapter;
    static final String[] listmenu = {"OUTER","TOP", "BOTTOM","DRESS","ACC"} ;
    static final String[] outermenu = {"OUTER","bomber", "blazer","cardigan","coat","jacket","jersey","parka","poncho","precoat","robe","TOP", "BOTTOM","DRESS","ACC"} ;
    static final String[] topmenu = {"OUTER","TOP","blouse","henley","hoodie","shirt","skirt","tank","tee","top","tuterleneck", "BOTTOM","DRESS","ACC"} ;
    static final String[] bottommenu = {"OUTER","TOP","BOTTOM","chinos","jeans","leggings","long","mideum","short","skirt","sourt","wide","DRESS","ACC" } ;
    static final String[] dressmenu = {"OUTER","TOP","BOTTOM","DRESS","dress","homewear","jumpsuit","kimono","sarong","ACC"} ;
    String[] imgarr = new String[500];
    private  int imgarrIndex=0;
    BackgroundTask task;
//    ListBackgroundTask listtask;
    private SharedPreferences appData;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle mArgs = getArguments();
        date = mArgs.getString("Date");//DressFragement에서 날짜 가져오기

        dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.activity_add_clothes, null);
        dialog.setContentView(view);

        init();
        return dialog;
    }

    private void init() {

        //리스트뷰와 리스트를 연결하기 위해 사용되는 어댑터
    //    adapter = new ListAdapter(getActivity(), R.layout.item_dress_listview, listmenu);
        new_adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listmenu);

        listView = (ListView) view.findViewById(R.id.listview1);
        okBTN = (Button) view.findViewById(R.id.finish_button);
        cancelBTN = (Button) view.findViewById(R.id.cancel_button);
        iv = (ImageView) view.findViewById(R.id.add_dress);
        //listView.setAdapter(adapter) ;//adapter 연결
        listView.setAdapter(new_adapter) ;//adapter 연결

        // 설정값 불러오기
        appData = this.getActivity().getSharedPreferences("appData", MODE_PRIVATE);

        Event();
    }

    private void Event() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // get TextView's Text.
                String strText = (String) parent.getItemAtPosition(position);

                if (strText == "OUTER") {//기본 카테고리
                    new_adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, outermenu);
                    listView.setAdapter(new_adapter) ;//adapter 연결
                } else if (strText == "TOP") {
                    new_adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, topmenu);
                    listView.setAdapter(new_adapter) ;//adapter 연결
                } else if (strText == "BOTTOM") {
                    new_adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, bottommenu);
                    listView.setAdapter(new_adapter) ;//adapter 연결
                } else if (strText == "DRESS") {
                    new_adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, dressmenu);
                    listView.setAdapter(new_adapter) ;//adapter 연결
                } else if (strText == "ACC") {

                } else if (strText == "bomber" || strText =="blazer" || strText =="cardigan"|| strText =="coat"|| strText =="jacket"|| strText =="jersey"|| strText =="parka"|| strText =="poncho"|| strText =="precoat"|| strText =="robe") {//OUTER 메뉴에서 세부 카테고리 선택한 경우
                    getList("OUTER/"+strText);
                    adapter = new ListAdapter(getActivity(), R.layout.item_dress_listview, imgarr);
                    listView.setAdapter(adapter) ;//adapter 연결

                }else if (strText.equals("blouse") || strText.equals("henley") || strText.equals("hoodie") || strText.equals("shirt") || strText.equals("skirt") || strText.equals("tank") || strText.equals("tee") || strText.equals("top")|| strText.equals("tuterleneck")) {//TOP 메뉴에서 세부 카테고리 선택한 경우
                    getList("TOP/"+strText);
                    adapter = new ListAdapter(getActivity(), R.layout.item_dress_listview, imgarr);
                    listView.setAdapter(adapter) ;//adapter 연결

               }else if (strText.equals("chinos" )|| strText.equals("jeans") || strText.equals("leggings" )|| strText.equals("long" )|| strText.equals("mideum") || strText.equals("short") || strText.equals("skirt") || strText.equals("sourt") || strText.equals("wide")) {//BOTTOM 메뉴에서 세부 카테고리 선택한 경우
                    getList("BOTTOM/"+strText);
                    adapter = new ListAdapter(getActivity(), R.layout.item_dress_listview, imgarr);
                    listView.setAdapter(adapter) ;//adapter 연결

                }else if (strText.equals("dress" )|| strText.equals("homewear" )|| strText.equals("jumpsuit" )|| strText.equals("kimono") || strText.equals("sarong")) {//DRESS 메뉴에서 세부 카테고리 선택한 경우
                    getList("DRESS/"+strText);
                    adapter = new ListAdapter(getActivity(), R.layout.item_dress_listview, imgarr);
                    listView.setAdapter(adapter) ;//adapter 연결
               }else{
                    imgpath = imgarr[position];
                    Glide.with(getActivity()).load("http://218.38.52.180/Android_files/"+ imgpath).into(iv);//보여줄 이미지 파일
                }

            }
        }) ;


        okBTN.setOnClickListener(new View.OnClickListener() {//완료 버튼
            @Override
            public void onClick(View view) {

                if(imgpath.equals("") || imgpath.equals("null")){ //선택한 이미지가 없는 경우
                    Toast.makeText(getContext(), "저장할 사진을 선택해주세요", Toast.LENGTH_SHORT).show();
                }else{ //이미지를 선택한 경우
                    addCoordy();
                }

            }
        });

        cancelBTN.setOnClickListener(new View.OnClickListener() {//취소 버튼
            @Override
            public void onClick(View view) {
                dismiss(); //dialog 닫기
            }
        });
    }

    public void getList(String category){
        path = appData.getString("Path", "");
        Log.d("Path:", path);
        try {
            JSONObject jObject = new JSONObject(path);//php 결과 json형식으로 저장
            photos = jObject.getJSONArray("result");

            for(int i=0; i < photos.length(); i++){
                JSONObject c = photos.getJSONObject(i);

                if((c.getString("category")).equals(category)){ //카테고리를 비교하여 동일한 경우 이미지 경로 배열에 저장
                    imgarr[imgarrIndex]=c.getString("path");
                    imgarrIndex++;
                }
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void addCoordy(){
        task = new BackgroundTask();
        task.execute();
    }

    class BackgroundTask extends AsyncTask<String, Integer, String> {
        String url = "http://218.38.52.180/addCoordy.php";//
        String json=sendObject();//username & 해당 날짜

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

            if(s.equals("success")) { //해당 날짜에 코디 저장 완료
                Toast.makeText(getContext(), "저장 성공", Toast.LENGTH_SHORT).show();
                dismiss();
            }else {
                Toast.makeText(getContext(), "저장 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private String sendObject() {
        id = appData.getString("ID", "");
        JSONObject jsonpost = new JSONObject();
        try {
            jsonpost.put("Username", id);//sharedpreference에 저장되었던 username 서버로 보내기 위해서 json 형식으로 변환
            jsonpost.put("Date", date);//달력에서 선택한 날짜 서버로 보내기 위해서 json 형식으로 변환
            jsonpost.put("ImgPath", imgpath);//달력에 저장할 이미지 path
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonpost.toString();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }



    //AddClothesFragment의 Adapter

    public class ListAdapter extends BaseAdapter {
        private Context context;
        private int layout; //아이템 레이아웃 정보ㅓ
        private String[] list;
        LayoutInflater inf;

        public ListAdapter() {
            this.context = null;
            this.layout = 0;
            this.list = null;
        }

        public ListAdapter(Context context, int layout, String[] list) {
            this.context = context;
            this.layout = layout;
            this.list = list;

            inf=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return imgarrIndex;}

        @Override
        public Object getItem(int postion) {
            return list[postion];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int postion, View convertView, ViewGroup viewGroup) {
            if(convertView == null){
                convertView = inf.inflate(layout, null);
            }

            ImageView imageView = convertView.findViewById(R.id.listimg);
            Glide.with(getActivity()).load("http://218.38.52.180/Android_files/"+ list[postion]).into(imageView);//보여줄 이미지 파일

            return convertView;
        }
    }

}