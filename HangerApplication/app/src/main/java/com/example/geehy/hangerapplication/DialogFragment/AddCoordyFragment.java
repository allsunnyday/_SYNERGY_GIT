package com.example.geehy.hangerapplication.DialogFragment;
//코디 추가하기
/**
 * Created by JHS on 2018-02-09.
 */
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.example.geehy.hangerapplication.Fragments.DashFragment;

import com.example.geehy.hangerapplication.Fragments.HomeFragment;
import com.example.geehy.hangerapplication.MainPageActivity;
import com.example.geehy.hangerapplication.R;
import com.example.geehy.hangerapplication.RequestActivity;
import com.example.geehy.hangerapplication.UploadImageForCodiInterface;
import com.example.geehy.hangerapplication.UploadImageInterface;
import com.example.geehy.hangerapplication.UploadObject;
import com.example.geehy.hangerapplication.gridview_home.CoordyItem;
import com.example.geehy.hangerapplication.gridview_home.dressItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


public class AddCoordyFragment extends DialogFragment{
    private SharedPreferences appData; //전체 프래그먼트에서 공유하는 값을 사용할 수 있음
    private Dialog dialog;
    private View view;
    private String id;
    private String path;
    private JSONArray dressJsonArray;
    private int dresslistindex;
    private Spinner categarySpinner;
    private GridView gridView;
    private String item;
    private JSONArray photos;
    private ArrayList<dressItem> list;
    private int index;
    private AddCodiAdapter addcodiAdapter;
    private int spinnerNUMBER=0;
    private int test=0;
    private ImageButton save, cancle, share;
    private ImageView topview;
    private ImageView bottomview;
    private EditText coordyname;
    private String selectTop="";
    private String selectBottom="";
    private String selectShare="";
    //private BackgroundTask_codi task;
    private int xDelta;
    private int yDelta;
    private ViewGroup codiLayout;
    private static final int REQUEST_GALLERY_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;
    private static final String SERVER_PATH = "http://218.38.52.180/";//파일 업로드시
    private AbstractWindowedCursor cursor;

    private DialogInterface.OnDismissListener onDismissListener;
    private TextView shareText;
    private static int isShare=0;

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener){
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(onDismissListener !=null){
            onDismissListener.onDismiss(dialog);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = ((MainPageActivity) getActivity()).getList();

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        dialog = super.onCreateDialog(savedInstanceState);
        appData = this.getActivity().getSharedPreferences("appData", MODE_PRIVATE);     //설정값을 가지고 온다
        isShare=0;
        load();         //->사용자의 id와 기존의 path를 불러온다.

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.activity_add_coordy, null);
        codiLayout = (RelativeLayout)view.findViewById(R.id.codi_main_layout);
        categarySpinner = (Spinner)view.findViewById(R.id.categorySpinner);
        ArrayAdapter categoryAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categarySpinner.setAdapter(categoryAdapter);
        save = (ImageButton)view.findViewById(R.id.saveBtn);
        cancle = (ImageButton)view.findViewById(R.id.noBtn);
        share = (ImageButton)view.findViewById(R.id.shareButton);
        shareText = (TextView)view.findViewById(R.id.shareInfoText);
        topview =(ImageView)view.findViewById(R.id.top_View);
        bottomview =(ImageView)view.findViewById(R.id.bottom_View);
        coordyname = (EditText)view.findViewById(R.id.addcoordy_name);
        gridView = (GridView)view.findViewById(R.id.add_gridview);
        addcodiAdapter = new AddCodiAdapter(getActivity(),R.layout.item_codi_gridview, list); //그리드 뷰 한개씩을 여기에 붙임
        init();

        gridView.setAdapter(addcodiAdapter);
        dialog.setContentView(view);

        return dialog;
    }


    private void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )  ID : Username
        id = appData.getString("ID", "");
        path = appData.getString("Path", "");
        Log.d("SharedPreferencesID", id);
        if(!path.equals("")) {
            Log.d("SharedPreferencesPATH", path);
        }
        else{
            Log.d("codi", "path is null");
        }
    }

    private void init() {
        Log.d("Path:", path);
        try {

            JSONObject jObject = new JSONObject(path);//php 결과 json형식으로 저장
            photos = jObject.getJSONArray("result");
            int i=0;
            index = i;
            list.clear();
            switch (spinnerNUMBER){
                case 0:
                    for(i=0; i < photos.length(); i++){
                        JSONObject c = photos.getJSONObject(i);
                        dressItem di = new dressItem();
                        di.setImgURL( c.getString("path")); //서버에서 가져온 파일 경로 (이름) 저장
                        di.setCat1(c.getString("category"));
                        String str[]=di.getCat1().split("/");
                        if(str[0].equals("TOP")){
                            list.add(di);
                            index++;
                        }
                        ((MainPageActivity) getActivity()).setList(list);
                    }
                    break;
                case 1:
                    for(i=0; i < photos.length(); i++){
                        JSONObject c = photos.getJSONObject(i);
                        dressItem di = new dressItem();
                        di.setImgURL( c.getString("path")); //서버에서 가져온 파일 경로 (이름) 저장
                        di.setCat1(c.getString("category"));
                        String str[]=di.getCat1().split("/");
                        if(str[0].equals("BOTTOM")){
                            list.add(di);
                            index++;
                        }
                        ((MainPageActivity) getActivity()).setList(list);
                    }
                case 2:
                    for(i=0; i < photos.length(); i++){
                        JSONObject c = photos.getJSONObject(i);
                        dressItem di = new dressItem();
                        di.setImgURL( c.getString("path")); //서버에서 가져온 파일 경로 (이름) 저장
                        di.setCat1(c.getString("category"));
                        String str[]=di.getCat1().split("/");
                        if(str[0].equals("OUTER")){
                            list.add(di);
                            index++;
                        }
                        ((MainPageActivity) getActivity()).setList(list);
                    }
                    break;
                case 3:
                    for(i=0; i < photos.length(); i++){
                        JSONObject c = photos.getJSONObject(i);
                        dressItem di = new dressItem();
                        di.setImgURL( c.getString("path")); //서버에서 가져온 파일 경로 (이름) 저장
                        di.setCat1(c.getString("category"));
                        String str[]=di.getCat1().split("/");
                        if(str[0].equals("DRESS")){
                            list.add(di);
                            index++;
                        }
                        ((MainPageActivity) getActivity()).setList(list);
                    }
                    break;
                case 4:
                    for(i=0; i < photos.length(); i++){
                        JSONObject c = photos.getJSONObject(i);
                        dressItem di = new dressItem();
                        di.setImgURL( c.getString("path")); //서버에서 가져온 파일 경로 (이름) 저장
                        di.setCat1(c.getString("category"));
                        String str[]=di.getCat1().split("/");
                        if(str[0].equals("ACC")){
                            list.add(di);
                            index++;
                        }
                        ((MainPageActivity) getActivity()).setList(list);
                    }
                    break;
            }


        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        event();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void event() {
        //카테고리 이벤트
        categarySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item = (String) parent.getSelectedItem();
                switch (item){
                    case "TOP":
                        spinnerNUMBER=0;
                        break;
                    case "BOTTOM":
                        spinnerNUMBER=1;
                        break;
                    case "OUTER":
                        spinnerNUMBER=2;
                        break;
                    case "DRESS":
                        spinnerNUMBER=3;
                        break;
                    case "ACC":
                        spinnerNUMBER=4;
                        break;
                }
                init();
                addcodiAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //그리드뷰의 아이템 이벤트
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int id, long position) { //position
                dressItem ds = new dressItem();
                String imgUri = "";
                ds=list.get(id);
                imgUri = ds.getImgURL();
                Log.d("ImgURL", imgUri + "is uri");
                topview.setBackgroundColor(Color.WHITE);
                bottomview.setBackgroundColor(Color.WHITE);

                if(spinnerNUMBER==0 || spinnerNUMBER==2||spinnerNUMBER==3||spinnerNUMBER==4){
                    Glide.with(getActivity())
                            .load("http://218.38.52.180/Android_files/"+ imgUri)
                            .override(500, 400)
                            .into(topview);
                    selectTop=imgUri;
                }
                else{
                    Glide.with(getActivity())
                            .load("http://218.38.52.180/Android_files/"+ imgUri)
                            .override(500, 400)
                            .into(bottomview);
                    selectBottom=imgUri;
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                picCapture(view);
               // saveServer();
                Toast.makeText(getActivity(),"이미지를 저장합니다.", Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),"이미지를 저장합니다.", Toast.LENGTH_SHORT).show();

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if(isShare == 0) {
                    share.setImageResource(R.drawable.ic_menu_share_pp);
                    isShare = 1;
                    Toast.makeText(getActivity().getApplicationContext(), "공유하기 선택", Toast.LENGTH_SHORT ).show();


                }
                else{
                    share.setImageResource(R.drawable.ic_menu_share);
                    isShare = 0;
                    Toast.makeText(getActivity().getApplicationContext(), "공유하기 취소", Toast.LENGTH_SHORT ).show();

                }

            }
        });

        cancle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        topview.setOnTouchListener(OnTouchListener());
        bottomview.setOnTouchListener(OnTouchListener());

    }

    private void picCapture(View view) {
        // WRITE_EXTERNAL_STORAGE 외부 공간 사용 권한 허용
        ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        codiLayout.buildDrawingCache();   // 캡처할 뷰를 지정하여 buildDrawingCache() 한다
        Bitmap captureView = codiLayout.getDrawingCache();   // 캡쳐할 뷰를 지정하여 getDrawingCache() 한다

        FileOutputStream fos;   // FileOutputStream 이용 파일 쓰기 한다
//        String strFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + CAPTURE_PATH;
        String strFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures";

        File folder = new File(strFolderPath);
        if(!folder.exists()) {  // 해당 폴더 없으면 만들어라
            folder.mkdirs();
        }

        String strFilePath = strFolderPath + "/" + System.currentTimeMillis() + ".png";

        File fileCacheItem = new File(strFilePath);

        try {
            fos = new FileOutputStream(fileCacheItem);
            captureView.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            //Toast.makeText(getActivity(), "영상을 캡쳐했습니다", Toast.LENGTH_SHORT).show();
            Log.d("test_", strFilePath);
            if(isShare == 1){
                selectShare = "Y";
            }
            else{
                selectShare = "N";
            }

            //strFIlePath->> 파일path
            uploadFile(strFilePath);
        }

    }
    private void uploadFile(String uri){

        if (EasyPermissions.hasPermissions(getActivity().getApplication(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //String filePath = getRealPathFromURIPath(uri, getActivity());
            String filePath = uri;
            File file = new File(filePath);
            Log.d(TAG, "name" + file.getName());

            RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", id + "+" + file.getName()+ "+" +selectTop + "+" + selectBottom +"+"+coordyname.getText() +"+"+ selectShare , mFile);//id값+파일이름 보내기
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), id + "+" + file.getName()+ "+" +selectTop + "+" + selectBottom +"+"+coordyname.getText()+"+"+ selectShare);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SERVER_PATH)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            //////업로드를 위한  custom interface >> 별도로 설정해줘야한다
            UploadImageForCodiInterface uploadImage = retrofit.create(UploadImageForCodiInterface.class);
            //////

            Call<UploadObject> fileUpload = uploadImage.upload(name, fileToUpload);
            Log.d("testname", id + "+" + file.getName());
            fileUpload.enqueue(new Callback<UploadObject>() {
                @Override
                public void onResponse(Call<UploadObject> call, Response<UploadObject> response) {
                    Toast.makeText(getActivity().getApplication(), "☆ 저장 성공 ☆ ", Toast.LENGTH_LONG).show();
                    dismiss();
                }
                @Override
                public void onFailure(Call<UploadObject> call, Throwable t) {
                    Log.d(TAG, "Error " + t.getMessage());
                }
            });
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }



    private View.OnTouchListener OnTouchListener() {
            return new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    final int x = (int) event.getRawX();
                    final int y = (int) event.getRawY();

                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_DOWN:
                            RelativeLayout.LayoutParams lparams =(RelativeLayout.LayoutParams) v.getLayoutParams();
                            xDelta = x - lparams.leftMargin;
                            yDelta = y - lparams.topMargin;
                            break;

                        case MotionEvent.ACTION_UP:
                            //Toast.makeText(getActivity(), "thanks", Toast.LENGTH_SHORT).show();
                            break;

                        case MotionEvent.ACTION_MOVE:
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)v.getLayoutParams();
                            layoutParams.leftMargin = x - xDelta;
                            layoutParams.topMargin = y - yDelta;
                            layoutParams.rightMargin = 0;
                            layoutParams.bottomMargin = 0;
                            v.setLayoutParams(layoutParams);
                            break;

                    }
                    codiLayout.invalidate();
                    return true;
                }
            };
    }


    public class AddCodiAdapter extends BaseAdapter {
        LayoutInflater layoutInflater;
        private Context context;
        private int layout;
        private ArrayList<dressItem> list;

        public AddCodiAdapter(Context context,int layout, ArrayList<dressItem> list){
            this.context = context;
            this.layout = layout;
            this.list = list;
            layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            //가져올 코디의 개수 ?
            return index;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        //항목의 수만큼 반복해서 호출 맨천음 호출했을 떄는 position이 0 그리드뷰 0번째 항목모양리턴
        @Override
        public View getView(int position, View convertView, ViewGroup parent){ //항목의 수만큼 반복해서 호출 맨천음 호출했을 떄는 position이 0 그리드뷰 0번째 항목모양리턴

            if(convertView == null) {
                convertView = layoutInflater.inflate(layout, null);
            }
            ImageView imageViewTop = (ImageView) convertView.findViewById(R.id.codi_item);

            final dressItem ci = list.get(position);
            String str[] =  ci.getCat1().split("/");

            if(ci.getImgURL().equals("")||ci.getImgURL().equals("null")){
                imageViewTop.setImageResource(R.drawable.tempimg);
            }else{

                switch (spinnerNUMBER){
                    case 0:
                        if(str[0].equals("TOP")){
                            Glide.with(getActivity())
                                    .load("http://218.38.52.180/Android_files/"+ ci.getImgURL())
                                    .into(imageViewTop);//보여줄 이미지 파일
                        }
                        break;
                    case 1:
                        if(str[0].equals("BOTTOM")){
                            Glide.with(getActivity())
                                    .load("http://218.38.52.180/Android_files/"+ ci.getImgURL())
                                    .into(imageViewTop);//보여줄 이미지 파일
                        }
                        break;
                    case 2:
                        if(str[0].equals("OUTER")){
                            Glide.with(getActivity())
                                    .load("http://218.38.52.180/Android_files/"+ ci.getImgURL())
                                    .into(imageViewTop);//보여줄 이미지 파일
                        }
                        break;
                    case 3:
                        if(str[0].equals("DRESS")){
                            Glide.with(getActivity())
                                    .load("http://218.38.52.180/Android_files/"+ ci.getImgURL())
                                    .into(imageViewTop);//보여줄 이미지 파일
                        }
                        break;
                    case 4:
                        if(str[0].equals("ACC")){
                            Glide.with(getActivity())
                                    .load("http://218.38.52.180/Android_files/"+ ci.getImgURL())
                                    .into(imageViewTop);//보여줄 이미지 파일
                        }
                        break;
                }

            }
            return convertView;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
