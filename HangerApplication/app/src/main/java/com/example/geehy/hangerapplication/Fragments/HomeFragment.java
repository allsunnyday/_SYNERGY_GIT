//옷장 메뉴

package com.example.geehy.hangerapplication.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.geehy.hangerapplication.CameraActivity;
import com.example.geehy.hangerapplication.DialogFragment.AddInfoFragment;
import com.example.geehy.hangerapplication.DialogFragment.progressbar;
import com.example.geehy.hangerapplication.GalleryActivity;
import com.example.geehy.hangerapplication.MainPageActivity;
import com.example.geehy.hangerapplication.R;
import com.example.geehy.hangerapplication.RequestActivity;
import com.example.geehy.hangerapplication.UploadImageInterface;
import com.example.geehy.hangerapplication.UploadObject;
import com.example.geehy.hangerapplication.gridview_home.dressItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static android.content.Context.MODE_PRIVATE;
import static com.example.geehy.hangerapplication.DialogFragment.AddInfoFragment.newInstance;

/**
 * A simple {@link Fragment} subclass.
 */

public class HomeFragment extends Fragment implements EasyPermissions.PermissionCallbacks {
    final static int SIGNAL_toGallery = 4004;
    final static int SIGNAL_toCamera = 4005;
    private  String[] imgarr = new String[100]; //이미지저장

    private View view;
    private homeGridAdapter adapter;
    private ArrayList<dressItem> list;
    private GridView gridView;
    private FragmentManager manager;
    private boolean isVisible;
    private boolean isVisible_check;
    private LinearLayout insertLinearLayout;
    private Button Camera;
    private Button Gallery;
    private LinearLayout circlebar_layout;
    private ProgressBar circlebar;
    private static String UploadImgPath;
    private FloatingActionButton fab;
    private FloatingActionButton del;
    private FloatingActionButton undo;
    private byte[] data_byte;

    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final int REQUEST_GALLERY_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;
    private static final String SERVER_PATH = "http://218.38.52.180/";//파일 업로드시
    private Uri uri;
    private SharedPreferences appData;
    private String id;
    private String path = "";
    private JSONArray photos = null;
    // private boolean isChanged;
    private int index =0 ;
    BackgroundTask task;
    progressbar bar;

    private Button Gallery2;
    private Uri grabcut_uri;


    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = ((MainPageActivity) getActivity()).getList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        // 설정값 불러오기
        appData = this.getActivity().getSharedPreferences("appData", MODE_PRIVATE);
        load();
        change();//이미지 경로가 이미 있는 경우 path에 저장
        if(path.equals("") ) { // 로그인 후 앱 메인화면 처음 켜지는 경우 서버에서 이미지를 가져온다.
            getimg();//이미지 가져오기
        }
        init();
        adapter = new homeGridAdapter(getActivity(), R.layout.item_home_girdview, list);//그리드 뷰의 디자인의 객체를 생성
        gridView.setAdapter(adapter);//그리드 뷰의 객체에 그리드 뷰의 디자인을 적용
        return view;
    }


    public void init() {
        isVisible = false; //F : 안보임 T : 보임
        isVisible_check = false; //F : 안보임 T : 보임
        gridView = (GridView) view.findViewById(R.id.home_gridview);//그리드 뷰의 객체를 가져오기

        //insertBTN = (Button) view.findViewById(R.id.floatingActionButton);

        insertLinearLayout = (LinearLayout) view.findViewById(R.id.home_insert_Layout);
        Camera = (Button) view.findViewById(R.id.home_camera_btn);
        Gallery = (Button) view.findViewById(R.id.home_gallery_btn);
        Gallery2 = (Button) view.findViewById(R.id.home_gallery_btn2);

        fab = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        del = (FloatingActionButton) view.findViewById(R.id.floatingButton_delete);
        undo = (FloatingActionButton) view.findViewById(R.id.floatingButton_undo);


        event();
    }



    public void event() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVisible) {
                    insertLinearLayout.setVisibility(View.GONE);
                    isVisible = false;
                } else {
                    insertLinearLayout.setVisibility(View.VISIBLE);
                    isVisible = true;
                }
            }
        });

//
//        Gallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
//                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, SIGNAL_toGallery);
//
//            }
//        });

        Gallery2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), GalleryActivity.class);
                //intent.setType("image/*");//이게뭐지?
                //intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                //intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SIGNAL_toGallery);

            }
        });


        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CameraActivity.class);
                startActivityForResult(intent, SIGNAL_toCamera);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int id, long position) { //position
                Bundle bundle = new Bundle();
                Log.d("Test Layout", "Show Fragment");
                AddInfoFragment addfragment = newInstance(list.get(id));
                manager = getFragmentManager();
                addfragment.show(getActivity().getSupportFragmentManager(), "AddInfoFragment");
                appData.edit().remove("Path").commit();
                //getimg();
                //adapter.notifyDataSetChanged();
            }
        });




        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isVisible_check) {
                    // delete_show();
                    alert_show();

                } else {
                    undo.setVisibility(View.VISIBLE);
                    adapter = new homeGridAdapter(getActivity(), R.layout.item_home_gridview_check,list);
                    gridView.setAdapter(adapter);
                    isVisible_check = true;
                }
            }
        });

        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isVisible_check = false;
                adapter = new homeGridAdapter(getActivity(), R.layout.item_home_girdview, list);
                gridView.setAdapter(adapter);
                undo.setVisibility(View.GONE);
            }
        });




        /*
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isVisible_check) {
                 // delete_show();
                  alert_show();

                } else {
                    undo.setVisibility(View.VISIBLE); //checkBox 없애는 버튼
                    int count = gridView.getAdapter().getCount();

                    for (int i = 0; i < count; i++) {
                        LinearLayout itemLayout = (LinearLayout)gridView.getChildAt(i); // Find by under LinearLayout
                        LinearLayout itemLayout = (LinearLayout)gridView.getChildt(i); // Find by under LinearLayout
                  //      CheckBox checkbox = (CheckBox) gridView.findViewById(R.id.check);
                         CheckBox checkbox = (CheckBox) itemLayout.findViewById(R.id.check);
                         checkbox.setVisibility(View.VISIBLE);
                        if(checkbox.isChecked())
                        {
                            Log.d("Item "+String.valueOf(i), checkbox.getTag().toString());
                            Toast.makeText(getActivity(),checkbox.getTag().toString() ,Toast.LENGTH_LONG).show();
                        }

                    }
                    isVisible_check = true;
                }
            }
        });
*/

        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isVisible_check = false;
                adapter = new homeGridAdapter(getActivity(), R.layout.item_home_girdview, list);
                gridView.setAdapter(adapter);
                undo.setVisibility(View.GONE);
            }
        });

    }

    //파일 업로드
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, getActivity().getApplication());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGNAL_toGallery) {
            if (resultCode == Activity.RESULT_OK) {

                grabcut_uri = Uri.parse(data.getStringExtra("grabcut_uri"));
                Log.d("ddddd", grabcut_uri + " ");
                //fp = data.getStringExtra("URI");
                //파일 서버로 업로드 start
                //uri = data.getData();
                if (EasyPermissions.hasPermissions(getActivity().getApplication(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    //String filePath = getRealPathFromURIPath(uri, getActivity());
                    //File file = new File(fp);
                    String filePath = getRealPathFromURIPath(grabcut_uri, getActivity());
                    File file = new File(filePath);
                    Log.d(TAG, "name" + file.getName());
                    //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", id + "+" + file.getName(), mFile);//id값+파일이름 보내기
                    RequestBody name = RequestBody.create(MediaType.parse("text/plain"), id + "+" + file.getName());
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(SERVER_PATH)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    UploadImageInterface uploadImage = retrofit.create(UploadImageInterface.class);
                    Call<UploadObject> fileUpload = uploadImage.uploadFile(fileToUpload, name);
                    Log.d("testname", id + "+" + file.getName());
                    fileUpload.enqueue(new Callback<UploadObject>() {
                        @Override
                        public void onResponse(Call<UploadObject> call, Response<UploadObject> response) {

                            //  Toast.makeText(getActivity().getApplication(), "Response " + response.raw().message(), Toast.LENGTH_LONG).show();
                            Toast.makeText(getActivity().getApplication(), response.body().getSuccess(), Toast.LENGTH_LONG).show();
                            getimg();

                        }

                        @Override
                        public void onFailure(Call<UploadObject> call, Throwable t) {
                            Log.d(TAG, "Error " + t.getMessage());
                        }
                    });

                    android.util.Log.d("HOME_TAG","TOTAL MEMORY : "+(Runtime.getRuntime().totalMemory() / (1024 * 1024)) + "MB");
                    android.util.Log.d("HOME_TAG","MAX MEMORY : "+(Runtime.getRuntime().maxMemory() / (1024 * 1024)) + "MB");
                    android.util.Log.d("HOME_TAG","FREE MEMORY : "+(Runtime.getRuntime().freeMemory() / (1024 * 1024)) + "MB");
                    android.util.Log.d("HOME_TAG","ALLOCATION MEMORY : "+((Runtime.getRuntime().totalMemory() -Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + "MB");

                } else {
                    EasyPermissions.requestPermissions(this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
                }//end
/*
                data_byte = data.getByteArrayExtra("data");
                Log.d("Imgae Data : ", "Data : " + data.getData());
                Log.d("Imgae Data : ", "Byte : " + data_byte);
                dressItem di = new dressItem();
                di.setSeason(new int[]{1, 2});
                di.setImgURL(data.getData() + ""); //스트링을 넣기
                di.setDressName("Dress " + (list.size()));
                list.add(di);
                //   adapter.notifyDataSetChanged();
                ((MainPageActivity) getActivity()).setList(list);
*/

            } else {
                //실패
            }
            showUpLayout();
        }
        else if (requestCode == SIGNAL_toCamera) {
            if (resultCode == Activity.RESULT_OK) {

                Log.d("CameraDataSet", "Camera url : " + data.getStringExtra("URI").toString());
                dressItem di = new dressItem();
                di.setSeason(new int[]{1, 2});
                di.setImgURL(data.getStringExtra("URI").toString()); //스트링을 넣기
                di.setDressName("Dress " + (list.size()));
                list.add(di);
                adapter.notifyDataSetChanged();
                ((MainPageActivity) getActivity()).setList(list);

            } else {
                //실패
            }
            showUpLayout();
        } else {

        }

    }

    //파일 업로드 부분
    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (uri != null) {
            String filePath = getRealPathFromURIPath(uri, getActivity());
            File file = new File(filePath);
            RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SERVER_PATH)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            UploadImageInterface uploadImage = retrofit.create(UploadImageInterface.class);
            Call<UploadObject> fileUpload = uploadImage.uploadFile(fileToUpload, filename);
            fileUpload.enqueue(new Callback<UploadObject>() {
                @Override
                public void onResponse(Call<UploadObject> call, Response<UploadObject> response) {
                    Toast.makeText(getActivity().getApplication(), "Success " + response.message(), Toast.LENGTH_LONG).show();
                    Toast.makeText(getActivity().getApplication(), "Success " + response.body().toString(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<UploadObject> call, Throwable t) {
                    Log.d(TAG, "Error " + t.getMessage());
                }
            });
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "Permission has been denied");
    }//파일 업로드

    //sharedpreferences 설정값을 불러오는 함수
    private void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        id = appData.getString("ID", "");
        Log.d("SharedPreferences:", id);
    }
    /*
            //fragment refresh
            private void refresh(){

                FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
                transaction.detach(this).attach(this).commit();

            }
    */
    public void showUpLayout() {
        if (isVisible) {
            insertLinearLayout.setVisibility(View.GONE);
            isVisible = false;
        } else {
            insertLinearLayout.setVisibility(View.VISIBLE);
            isVisible = true;
        }
    }


    void delete_show(int total)
    {
        final int del = total;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(total+"개의 항목을 삭제하시겠습니까?");
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }


                });

        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

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

    //이미지path 서버에서 가져오기
    //username 서버로 보내기
    private String sendObject() {
        JSONObject jsonpost = new JSONObject();
        try {

            jsonpost.put("Username", id);//sharedpreference에 저장되었던 username 서버로 보내기(해당 유저 이미지 가져오기 위해)
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonpost.toString();
    }


    //서버에서 가져온 이미지 url 배열에 저장
    private void change() {
        path = appData.getString("Path", "");
        Log.d("Path:", path);
        try {
            JSONObject jObject = new JSONObject(path);//php 결과 json형식으로 저장
            photos = jObject.getJSONArray("result");
            int i;
            list.clear();
            for(i=0; i < photos.length(); i++){
                JSONObject c = photos.getJSONObject(i);
                dressItem di = new dressItem();
                di.setSeason(new int[]{1, 2});
                di.setCat1(c.getString("category"));
                di.setImgURL( c.getString("path")); //서버에서 가져온 파일 경로 (이름) 저장
                di.setDressColor(c.getString("color"));
                di.setBrand(c.getString("brand"));
                di.setDressTag(c.getString("tag"));
                list.add(di);
                ((MainPageActivity) getActivity()).setList(list);
            }
            index = i;


        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 설정값을 저장하는 함수
    private void save(String s) {
        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = appData.edit();
        // 에디터객체.put타입( 저장시킬 이름, 저장시킬 값 )
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        //   editor.putBoolean("SAVE_LOGIN_DATA", checkBox.isChecked());
        editor.putString("Path", s);
        //    editor.putString("PWD", pwtext.getText().toString().trim());
        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
    }

    class BackgroundTask extends AsyncTask<String, Integer, String> {
        String url = "http://218.38.52.180/getimgpath3.php";//원래는 getimgpath.php
        String json=sendObject();//username

        ProgressDialog asyncDialog = new ProgressDialog(getActivity());

        @Override
        protected  void onPreExecute(){
            /*
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//dialog
            asyncDialog.setMessage("Loading...");
            // show dialog
            asyncDialog.show();
*/

            super.onPreExecute();
        }

        @Override
        protected String doInBackground (String...params){
          /*
            //dialog 진행
            try {
                for (int i = 0; i < 5; i++) {
                    //asyncDialog.setProgress(i * 30);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
*/

            super.onPreExecute();
            String result; // 요청 결과를 저장할 변수.
            RequestActivity requestHttpURLConnection = new RequestActivity();
            result = requestHttpURLConnection.request(url, json); // 해당 URL로 부터 결과물을 얻어온다.
            return result;
        }

        @Override
        protected void onPostExecute (String s) {
            //        asyncDialog.dismiss();
            super.onPostExecute(s);
            Log.d("getimgtest:", s);
            //Toast.makeText(getContext(),s, Toast.LENGTH_SHORT).show();
            if(!(s.equals("no path"))) {
                save(s);
                change();
                adapter.notifyDataSetChanged();
            }
        }


    }

    public void getimg(){
        task = new BackgroundTask();
        task.execute();
    }


    //가져온 이미지 gridview에 set
    public class homeGridAdapter extends BaseAdapter {
        LayoutInflater layoutInflater;
        private Context context;
        private int layout;
        private ArrayList<dressItem> list;

        public homeGridAdapter(Context context,int layout,ArrayList<dressItem> list){
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

            ImageView imageView = (ImageView) convertView.findViewById(R.id.homeitem);

            final dressItem di = list.get(position);

            TextView tv = (TextView) convertView.findViewById(R.id.item_clothesname);

            String tempString = "";

            tempString+= (di.getCat1() + "\n");
            //tempString+= ("옷 색깔: " + di.getDressColor()+ "\n" );
            tempString+= ("계절 : ");
            int[] tempSeason = di.getSeason();
            for(int i = 0 ; i< tempSeason.length ; i++){
                if(tempSeason[i] == -1){
                    tempString+= ("미정 ");
                }else if(tempSeason[i] ==0){
                    tempString+= ("봄");
                }else if(tempSeason[i] ==1){
                    tempString+= ("여름");
                }else if(tempSeason[i] ==2){
                    tempString+= ("가을");
                }else if(tempSeason[i] ==3){
                    tempString+= ("겨울");
                }

                if(i != tempSeason.length -1){
                    tempString += ", ";
                }
            }
            tempString += "\n";
            tempString += ("#" + di.getDressTag()+ "\n" );
            tv.setText(tempString);


            if(di.getImgURL().equals("") || di.getImgURL().equals("null")){
                imageView.setImageResource(R.drawable.tempimg);
            }else{
                Glide.with(getActivity()).load("http://218.38.52.180/Android_files/"+ di.getImgURL()).into(imageView);//보여줄 이미지 파일
            }
            TextView color1 = convertView.findViewById(R.id.home_colorView1);
            TextView color2 = convertView.findViewById(R.id.home_colorView2);
            int defaultValue = 0x000000;

            if((di.getDressColor()).equals("") ||(di.getDressColor()).equals("null")){
                color1.setBackgroundColor(defaultValue);
                color2.setBackgroundColor(defaultValue);
            }
            else {
                try { //NumberFormatException 추가
                    String colorStr[] = di.getDressColor().split(",");
                    int v = Integer.parseInt(colorStr[0]);
                    int m = Integer.parseInt(colorStr[1]);
                    color1.setBackgroundColor(v);
                    color2.setBackgroundColor(m);
                }catch (NumberFormatException e){
                    Log.d("eeeeee","getDressColor null");
                }
            }


            return convertView;
        }
    }



}




