package com.example.geehy.hangerapplication.DialogFragment;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.geehy.hangerapplication.Fragments.HomeFragment;
import com.example.geehy.hangerapplication.R;
import com.example.geehy.hangerapplication.RequestActivity;
import com.example.geehy.hangerapplication.gridview_home.dressItem;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class AddInfoFragment extends DialogFragment {
    private static dressItem dress;
    private static String dressTag="";
    private static String dressBrand="";
    private static String dressColor;
    private static String imgurl;
    private static String category;

    //rivate static int dressFlag;
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
    private TextView colortext;
    private TextView colorView1;
    private String vibantColor;
    private String mutedColor;
    private String id;
    private String item;
    private Drawable d;

    private int flagWho;
    BackgroundTask task;
    BackgroundTask2 task2;
    private Bitmap bitmap;
    private EditText tagText;
    private AutoCompleteTextView brandText;

    private final String[] brands = {"zara","uniqlo","flymodel","66girls" ,"nice","h&m", "mixxo", "팜므파탈", "스타일난다"}; //브랜드 및 쇼핑몰 이름>>  영문만 지원됨


    public static AddInfoFragment newInstance(dressItem ds) {
        AddInfoFragment addInfoFragment = new AddInfoFragment();
        //  Supply items as an argument.
        Bundle args = new Bundle();
        args.putSerializable("dress", ds);//homefragment에서 받아온 dress list
        imgurl = ds.getImgURL();//사진 img
        category = ds.getCat1();//카테고리 내용
        dressColor = ds.getDressColor();
        dressTag = ds.getDressTag();
        dressBrand = ds.getBrand();
        //dressFlag = ds.getColorFlag();
        addInfoFragment.setArguments(args);
        dress = new dressItem();
        dress = ds;

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

    //private void paintTextBackground() {    }

    private void init() {

        CompleteBTN = (Button) view.findViewById(R.id.additem_insertbutton);
        CancelBTN = (Button) view.findViewById(R.id.cancel_button);
        categorytext = (EditText)view.findViewById(R.id.categorytext2);
        colortext = (TextView) view.findViewById(R.id.color_Edittext);
        colorView1 = (TextView)view.findViewById(R.id.colorview1);
        tagText = (EditText)view.findViewById(R.id.AddPost_HashTag_Edittext);
        brandText = (AutoCompleteTextView)view.findViewById(R.id.brandEditText);
/*

        if(!dressTag.equals("") || !dressTag.equals("null")) {
            tagText.setText(dressTag);
        }
        else{
            tagText.setText("");
        }
        if(!dressBrand.equals("") || !dressBrand.equals("null")){
            brandText.setText(dressBrand);
        }
        else{
            brandText.setText("");
        }
*/


        brandText.setAdapter(  //현재 영어에 한해서만  자동완성 기능이 이루어지고 있음
                new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_dropdown_item_1line, brands));



        //*************************** 최초의 dialog create 할 때, 이미지의 색상을 추출 ********************************//
        int defaultValue = 0x000000;
        colorView1.setBackgroundColor(defaultValue);
        colortext.setBackgroundColor(defaultValue);


        img = (ImageView) view.findViewById(R.id.additem_imagethumb);
        Glide.with(getActivity())
                .load("http://218.38.52.180/Android_files/"+imgurl)
                .into(img);

        if( dressColor.equals("null")||dressColor.equals("")){  // 색깔을 안보이는 이유
            //try{ //로드한 이미지로부터 dominant colors 추출
                Glide.with(getActivity())
                        .load("http://218.38.52.180/Android_files/"+imgurl)
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>(){

                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                                Palette.from(resource).generate(new Palette.PaletteAsyncListener(){
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        int defaultValue = 0x000000;
                                        int vibrant = palette.getVibrantColor(defaultValue);
                                        int muted = palette.getMutedColor(defaultValue);

                                        vibantColor = String.format("#%06X", (0xFFFFFF & vibrant));
                                        mutedColor = String.format("#%06X", (0xFFFFFF & muted));
                                        // #000000 이런 식으로 값을 변환 -> 서버에 스트링으로 저장할 수 있음

                                        colorView1.setBackgroundColor(vibrant);
                                        colorView1.setText(vibantColor);
                                        colortext.setText(mutedColor);
                                        colortext.setBackgroundColor(muted);

                                        dress.setDressColor(vibrant+","+muted);
                                        task2 = new BackgroundTask2();
                                        task2.execute();

                                    }
                                });

/*

                                Picasso.with(context).load(uri).networkPolicy(NetworkPolicy.NO_CACHE)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                        .placeholder(R.drawable.bv_logo_default).stableKey(id)
                                        .into(viewImage_imageView);
*/

                            }
                        } );

          // }catch (NullPointerException e){
          //      Log.d("Bitmap","비트맵에러");
          //      return;
          //  }
        }
        //******************************************* 이미 서버에 색상을 가지고 있다면 ********************************//
        else if(! dressColor.equals("null")) {
            //int defaultValue = 0x000000;
            //int vibant;
            try {
                String colorStr[] = dressColor.split(",");
                int v = Integer.parseInt(colorStr[0]);
                int m = Integer.parseInt(colorStr[1]);
                vibantColor = String.format("#%06X", v);
                mutedColor = String.format("#%06X", m);
                colorView1.setText(vibantColor);
                colortext.setText(mutedColor);
                colorView1.setBackgroundColor(v);
                colortext.setBackgroundColor(m);
            }catch (NumberFormatException e){
                Log.d("numberFormatExcetion", "error");
            }catch (NullPointerException e){
                Log.d("null point exception", "null ");
            }

        }

        categorytext.setText(category);
        if(dressTag.equals("null")) {
            brandText.setText("");
        }
        else{
            brandText.setText(dressTag);
        }
        if(dressTag.equals("null")) {
            tagText.setText("");
        }else {
            tagText.setText(dressBrand);
        }
        Event();
    }

    private void Event() {


        CompleteBTN.setOnClickListener(new View.OnClickListener() {//편집 버튼
            @Override
            public void onClick(View view) {
                task = new BackgroundTask();
                task.execute();
                //dismiss();
            }
        });


        CancelBTN.setOnClickListener(new View.OnClickListener() {//취소 버튼
            @Override
            public void onClick(View view) {
                dismiss(); //화면 닫기
                getFragmentManager().beginTransaction()
                        .replace(R.id.content, new HomeFragment())
                        .commit();
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

//************************************ add info 편집한 내용을 서버에 저장 ************************************//
    private String sendObject(){
        JSONObject jsonpost = new JSONObject();
        try{
            appData =   this.getActivity().getSharedPreferences("appData", MODE_PRIVATE);
            id = appData.getString("ID", "");//username 받아오기
            //String colorSum = vibantColor + ","+ mutedColor;
            jsonpost.put("Username", id);//username
            jsonpost.put("Imgurl", imgurl);//imgUrl
            jsonpost.put("Category", categorytext.getText());//카테고리
            //jsonpost.put("Color", dressColor);//색깔
            jsonpost.put("Season",item);//계절
            jsonpost.put("Tag", tagText.getText());
            jsonpost.put("Brand", brandText.getText());
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
                /////성공할 시에 서버에 결과를 보낸다
                //isChanged = true;
                Toast.makeText(getContext(), "저장 성공", Toast.LENGTH_SHORT).show();
                dismiss();
                getFragmentManager().beginTransaction()
                        .replace(R.id.content, new HomeFragment())
                        .commit();



            }else{
                Toast.makeText(getContext(), "저장 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }

//*********************************************최초에 한번 color 정보를 서버에 보냄 *************************************//
    private String sendObject_color(){ //편집할 내용 json으로
        JSONObject jsonpost2 = new JSONObject();
        try{
            appData = this.getActivity().getSharedPreferences("appData", MODE_PRIVATE);
            id = appData.getString("ID", "");//username 받아오기
            String colorSum = vibantColor + ","+ mutedColor;
            jsonpost2.put("Username", id);//username
            jsonpost2.put("Imgurl", imgurl);//imgUrl
            jsonpost2.put("Color", dress.getDressColor());//색깔
        }catch (JSONException e){
            e.printStackTrace();
        }
        return jsonpost2.toString();
    }
    class BackgroundTask2 extends AsyncTask<String, Integer, String> {//편집할 내용 서버로 보내기
        String url2 = "http://218.38.52.180/addcolor.php";
        String json2= sendObject_color();//편집할 내용 받아옴

        @Override
        protected String doInBackground (String...params){
            String result; // 요청 결과를 저장할 변수.
            RequestActivity requestHttpURLConnection = new RequestActivity();
            result = requestHttpURLConnection.request(url2, json2); // 해당 URL로 부터 결과물을 얻어온다.
            return result;

        }


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}