package com.example.geehy.hangerapplication.DialogFragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.geehy.hangerapplication.Fragments.HomeFragment;
import com.example.geehy.hangerapplication.MainPageActivity;
import com.example.geehy.hangerapplication.R;
import com.example.geehy.hangerapplication.RequestActivity;
import com.example.geehy.hangerapplication.gridview_home.dressItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by JHS on 2018-02-19.
 */

public class EditCodiInfoFragment extends DialogFragment {
//
//    private SharedPreferences appData; //전체 프래그먼트에서 공유하는 값을 사용할 수 있음
//    private Dialog dialog;
//    private View view;
//    private String id;
//    private String path;
//    private JSONArray dressJsonArray;
//    private int dresslistindex;
//    private Spinner categarySpinner;
//    private GridView gridView;
//    private String item;
//    private JSONArray photos;
//    private ArrayList<dressItem> list;
//    private int index;
//    private int spinnerNUMBER=0;
//    private int test=0;
//    private Button save, cancle;
//    private ImageView topview;
//    private ImageView bottomview;
//    private EditText coordyname;
//
//    private String selectTop="";
//    private String selectBottom="";
//
//    private BackgroundTask_edit_codi task;
//    private String top;
//    private int codi_no;
//    private String name;
//    private String bottom;
//    private EditCodiAdapter codiAdapter;
//
///*
//
//    public static AddCoordyFragment newInstance(Bundle bundle) {
//        AddCoordyFragment addCoordyFragment = new AddCoordyFragment();
//
//        return addCoordyFragment;
//    }
//*/
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        list = ((MainPageActivity) getActivity()).getList();
//
//    }
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        super.onCreateDialog(savedInstanceState);
//        dialog = super.onCreateDialog(savedInstanceState);
//        appData = this.getActivity().getSharedPreferences("appData", MODE_PRIVATE);     //설정값을 가지고 온다
//
//        Bundle mbundle = getArguments();
//        top = mbundle.getString("TOP");
//        bottom = mbundle.getString("BOTTOM");
//        name = mbundle.getString("NAME");
//        codi_no = mbundle.getInt("NO");
//
//        load();         //->사용자의 id와 기존의 path를 불러온다.
//
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        view = inflater.inflate(R.layout.activity_add_coordy, null);
//
//        categarySpinner = (Spinner)view.findViewById(R.id.categorySpinner);
//        ArrayAdapter categoryAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
//                R.array.categories, android.R.layout.simple_spinner_item);
//        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        categarySpinner.setAdapter(categoryAdapter);
//        save = (Button)view.findViewById(R.id.saveBtn);
//        cancle = (Button)view.findViewById(R.id.noBtn);
//
//        topview =(ImageView)view.findViewById(R.id.top_View);
//        bottomview =(ImageView)view.findViewById(R.id.bottom_View);
//        coordyname = (EditText)view.findViewById(R.id.addcoordy_name);
//
//        // 변경하기 전의 코디 이미지를 불러 온다
//        Glide.with(getActivity())
//                .load("http://218.38.52.180/Android_files/"+ top)
//                .override(600, 500)
//                .into(topview);
//        Glide.with(getActivity())
//                .load("http://218.38.52.180/Android_files/"+bottom)
//                .override(600,500)
//                .into(bottomview);
//
//        coordyname.setText(name);
//        selectBottom = bottom;
//        selectTop = top;
//
//        //
//
//        gridView = (GridView)view.findViewById(R.id.add_gridview);
//        codiAdapter = new EditCodiAdapter(getActivity(),R.layout.item_codi_gridview, list); //그리드 뷰 한개씩을 여기에 붙임
//        init();
//
//        gridView.setAdapter(codiAdapter);
//        dialog.setContentView(view);
//
//        return dialog;
//    }
//
//
//    private void load() {
//        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )  ID : Username
//        id = appData.getString("ID", "");
//        path = appData.getString("Path", "");
//        Log.d("SharedPreferencesID", id);
//        if(!path.equals("")) {
//            Log.d("SharedPreferencesPATH", path);
//        }
//        else{
//            Log.d("codi", "path is null");
//        }
//    }
//
//    private void init() {
//        Log.d("Path:", path);
//        try {
//
//            JSONObject jObject = new JSONObject(path);//php 결과 json형식으로 저장
//            photos = jObject.getJSONArray("result");
//            int i=0;
//            index = i;
//            list.clear();
//            switch (spinnerNUMBER){
//                case 0:
//                    for(i=0; i < photos.length(); i++){
//                        JSONObject c = photos.getJSONObject(i);
//                        dressItem di = new dressItem();
//                        di.setImgURL( c.getString("path")); //서버에서 가져온 파일 경로 (이름) 저장
//                        di.setCat1(c.getString("category"));
//                        String str[]=di.getCat1().split("/");
//                        if(str[0].equals("TOP")){
//                            list.add(di);
//                            index++;
//                        }
//                        ((MainPageActivity) getActivity()).setList(list);
//                    }
//                    break;
//                case 1:
//                    for(i=0; i < photos.length(); i++){
//                        JSONObject c = photos.getJSONObject(i);
//                        dressItem di = new dressItem();
//                        di.setImgURL( c.getString("path")); //서버에서 가져온 파일 경로 (이름) 저장
//                        di.setCat1(c.getString("category"));
//                        String str[]=di.getCat1().split("/");
//                        if(str[0].equals("BOTTOM")){
//                            list.add(di);
//                            index++;
//                        }
//                        ((MainPageActivity) getActivity()).setList(list);
//                    }
//                case 2:
//                    for(i=0; i < photos.length(); i++){
//                        JSONObject c = photos.getJSONObject(i);
//                        dressItem di = new dressItem();
//                        di.setImgURL( c.getString("path")); //서버에서 가져온 파일 경로 (이름) 저장
//                        di.setCat1(c.getString("category"));
//                        String str[]=di.getCat1().split("/");
//                        if(str[0].equals("OUTER")){
//                            list.add(di);
//                            index++;
//                        }
//                        ((MainPageActivity) getActivity()).setList(list);
//                    }
//                    break;
//                case 3:
//                    for(i=0; i < photos.length(); i++){
//                        JSONObject c = photos.getJSONObject(i);
//                        dressItem di = new dressItem();
//                        di.setImgURL( c.getString("path")); //서버에서 가져온 파일 경로 (이름) 저장
//                        di.setCat1(c.getString("category"));
//                        String str[]=di.getCat1().split("/");
//                        if(str[0].equals("DRESS")){
//                            list.add(di);
//                            index++;
//                        }
//                        ((MainPageActivity) getActivity()).setList(list);
//                    }
//                    break;
//                case 4:
//                    for(i=0; i < photos.length(); i++){
//                        JSONObject c = photos.getJSONObject(i);
//                        dressItem di = new dressItem();
//                        di.setImgURL( c.getString("path")); //서버에서 가져온 파일 경로 (이름) 저장
//                        di.setCat1(c.getString("category"));
//                        String str[]=di.getCat1().split("/");
//                        if(str[0].equals("ACC")){
//                            list.add(di);
//                            index++;
//                        }
//                        ((MainPageActivity) getActivity()).setList(list);
//                    }
//                    break;
//            }
//
//
//        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }
//        event();
//
//    }
//
//    private void event() {
//        //카테고리 이벤트
//        categarySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                item = (String) parent.getSelectedItem();
//                switch (item){
//                    case "TOP":
//                        spinnerNUMBER=0;
//                        break;
//                    case "BOTTOM":
//                        spinnerNUMBER=1;
//                        break;
//                    case "OUTER":
//                        spinnerNUMBER=2;
//                        break;
//                    case "DRESS":
//                        spinnerNUMBER=3;
//                        break;
//                    case "ACC":
//                        spinnerNUMBER=4;
//                        break;
//                }
//                init();
//                codiAdapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        //그리드뷰의 아이템 이벤트
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int id, long position) { //position
//                dressItem ds = new dressItem();
//                String imgUri = "";
//                ds=list.get(id);
//                imgUri = ds.getImgURL();
//                Log.d("ImgURL", imgUri + "is uri");
//                topview.setBackgroundColor(Color.WHITE);
//                bottomview.setBackgroundColor(Color.WHITE);
//
//                if(spinnerNUMBER==0 || spinnerNUMBER==2||spinnerNUMBER==3||spinnerNUMBER==4){
//                    Glide.with(getActivity())
//                            .load("http://218.38.52.180/Android_files/"+ imgUri)
//                            .override(600, 500)
//                            .into(topview);
//                    selectTop=imgUri;
//                }
//                else{
//                    Glide.with(getActivity())
//                            .load("http://218.38.52.180/Android_files/"+ imgUri)
//                            .override(600, 500)
//                            .into(bottomview);
//                    selectBottom=imgUri;
//                }
//            }
//        });
//
//        save.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                saveServer();
//                //Toast.makeText(getActivity(),"저장하기", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        cancle.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//    }
//
//    private void saveServer() {
//        task = new BackgroundTask_edit_codi();
//        task.execute();
//    }
//    class BackgroundTask_edit_codi extends AsyncTask<String, Integer, String> {//편집할 내용 서버로 보내기
//        String url = "http://218.38.52.180/editcodi.php";
//
//        String json= sendObject_edit();//편집할 내용 받아옴
//
//        @Override
//        protected String doInBackground (String...params){
//
//            String result; // 요청 결과를 저장할 변수.
//            RequestActivity requestHttpURLConnection = new RequestActivity();
//            result = requestHttpURLConnection.request(url, json); // 해당 URL로 부터 결과물을 얻어온다.
//            return result;
//
//        }
//
//        @Override
//        protected void onPostExecute (String s) {
//            super.onPostExecute(s); //서버 결과
//            Log.d("present", s);
//            if (s.equals("성공")) {
//                /////성공할 시에 서버에 결과를 보낸다
//                //isChanged = true;
//                Toast.makeText(getContext(), "저장 성공", Toast.LENGTH_SHORT).show();
//                dismiss();
//                getFragmentManager().beginTransaction()
//                        .replace(R.id.content, new CodiInfoFragment())
//                        .commit();
//
//
//            }else{
//                Toast.makeText(getContext(), "저장 실패", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private String sendObject_edit() {
//        JSONObject jsonp = new JSONObject();
//        Log.d("present", selectBottom +", "+selectTop +","+coordyname.getText() +","+codi_no);
//        try {
//            jsonp.put("Username", id+"_coordy");
//            jsonp.put("Top", selectTop);
//            jsonp.put("Bottom", selectBottom);
//            jsonp.put("Name", coordyname.getText());
//            jsonp.put("NO", codi_no);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return jsonp.toString();
//    }
//
//    public class EditCodiAdapter extends BaseAdapter {
//        LayoutInflater layoutInflater;
//        private Context context;
//        private int layout;
//        private ArrayList<dressItem> list;
//
//        public EditCodiAdapter(Context context,int layout, ArrayList<dressItem> list){
//            this.context = context;
//            this.layout = layout;
//            this.list = list;
//            layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
//        }
//
//        @Override
//        public int getCount() {
//            //가져올 코디의 개수 ?
//            return index;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return list.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//
//        //항목의 수만큼 반복해서 호출 맨천음 호출했을 떄는 position이 0 그리드뷰 0번째 항목모양리턴
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent){ //항목의 수만큼 반복해서 호출 맨천음 호출했을 떄는 position이 0 그리드뷰 0번째 항목모양리턴
//
//            if(convertView == null) {
//                convertView = layoutInflater.inflate(layout, null);
//            }
//            ImageView imageViewTop = (ImageView) convertView.findViewById(R.id.codi_item);
//
//            final dressItem ci = list.get(position);
//            String str[] =  ci.getCat1().split("/");
//
//            if(ci.getImgURL().equals("")||ci.getImgURL().equals("null")){
//                imageViewTop.setImageResource(R.drawable.tempimg);
//            }else{
//
//                switch (spinnerNUMBER){
//                    case 0:
//                        if(str[0].equals("TOP")){
//                            Glide.with(getActivity())
//                                    .load("http://218.38.52.180/Android_files/"+ ci.getImgURL())
//                                    .into(imageViewTop);//보여줄 이미지 파일
//                        }
//                        break;
//                    case 1:
//                        if(str[0].equals("BOTTOM")){
//                            Glide.with(getActivity())
//                                    .load("http://218.38.52.180/Android_files/"+ ci.getImgURL())
//                                    .into(imageViewTop);//보여줄 이미지 파일
//                        }
//                        break;
//                    case 2:
//                        if(str[0].equals("OUTER")){
//                            Glide.with(getActivity())
//                                    .load("http://218.38.52.180/Android_files/"+ ci.getImgURL())
//                                    .into(imageViewTop);//보여줄 이미지 파일
//                        }
//                        break;
//                    case 3:
//                        if(str[0].equals("DRESS")){
//                            Glide.with(getActivity())
//                                    .load("http://218.38.52.180/Android_files/"+ ci.getImgURL())
//                                    .into(imageViewTop);//보여줄 이미지 파일
//                        }
//                        break;
//                    case 4:
//                        if(str[0].equals("ACC")){
//                            Glide.with(getActivity())
//                                    .load("http://218.38.52.180/Android_files/"+ ci.getImgURL())
//                                    .into(imageViewTop);//보여줄 이미지 파일
//                        }
//                        break;
//                }
//
////                Glide.with(getActivity())
////                        .load("http://218.38.52.180/Android_files/"+ ci.getImgURL())
////                        .into(imageViewTop);//보여줄 이미지 파일
//            }
//            return convertView;
//        }
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }
}
