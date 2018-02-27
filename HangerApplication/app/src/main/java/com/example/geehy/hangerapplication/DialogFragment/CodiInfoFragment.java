package com.example.geehy.hangerapplication.DialogFragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.example.geehy.hangerapplication.Fragments.DashFragment;
import com.example.geehy.hangerapplication.Fragments.HomeFragment;
import com.example.geehy.hangerapplication.MainPageActivity;
import com.example.geehy.hangerapplication.R;
import com.example.geehy.hangerapplication.RequestActivity;
import com.example.geehy.hangerapplication.gridview_home.CoordyItem;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
    private TextView codiName;
    private ImageView topView;
    //private ImageView bottomView;
    private ImageButton likes;
    private ImageButton editbtn;
    private ImageButton deletebtn;
    private String name;
    private int codi_no;
    private String id;
    BackgroundTaskDelete task;
    BackgroundTaskForSend shareTask;
    private String codi;
    private int hit;
    private BackgroundTaskForSendHit likesTask;

    private DialogInterface.OnDismissListener onDismissListener;
    private int share;

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
        codi = mbundle.getString("FULL");
        hit = mbundle.getInt("LIKE"); //초기값은 0  > 버튼 클릭시 1로 변경
        share = mbundle.getInt("SHARE");
        Log.d("ssssss", share + " ");

        appData = this.getActivity().getSharedPreferences("appData", MODE_PRIVATE);     //설정값을 가지고 온다
        id = appData.getString("ID", "");//username 받아오기
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.activity_codi_info, null);
        dialog.setContentView(view);
        init();

        return dialog;
    }

    private void init() {
        codiName = (TextView) view.findViewById(R.id.edit_codi_name);
        topView = (ImageView)view.findViewById(R.id.codiinfo_top);
        //bottomView = (ImageView)view.findViewById(R.id.codiinfo_bottom);


        likes = (ImageButton)view.findViewById(R.id.likebtn);
        editbtn=(ImageButton)view.findViewById(R.id.codi_info_edit_btn);

        deletebtn=(ImageButton)view.findViewById(R.id.codi_info_delete_btn);
        codiName.setText(name);

        Glide.with(getActivity())
                .load("http://218.38.52.180/Android_files/"+codi)
                .override(700, 1500)
                .into(topView);
//        Glide.with(getActivity())
//                .load("http://218.38.52.180/Android_files/"+codi)
//                .override(600,500)
//                .into(bottomView);
//        Glide.with(getActivity())
//                .load("http://218.38.52.180/Android_files/"+codi)
//                .into(codiView);
        if(hit!=0){
            likes.setImageResource(R.drawable.like_magenta);
        }

        if(share!=0){
            editbtn.setImageResource(R.drawable.ic_menu_share_pp);
        }
        else{
            editbtn.setImageResource(R.drawable.ic_menu_share);
        }

        event();
    }

    private void event() {

        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hit!=0) {
                    likes.setImageResource(R.drawable.like_blank);
                    hit=0;
                }
                else{
                    likes.setImageResource(R.drawable.like_magenta);
                    hit=1;
                }
                sendHitChange();



            }
        });
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "♥준비중♥",Toast.LENGTH_SHORT).show();
                if(share ==0) {
                    Toast.makeText(getActivity().getApplicationContext(), "공유할게요!♥",Toast.LENGTH_SHORT).show();
                    editbtn.setImageResource(R.drawable.ic_menu_share_pp);
                    sendShare();
                    share=1;
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "이미 공유했습니다",Toast.LENGTH_SHORT).show();
                }
//                Bundle bundle = new Bundle();
//                bundle.putString("TOP", top);
//                bundle.putString("BOTTOM", bottom);
//                bundle.putString("NAME", name);
//                bundle.putInt("NO", codi_no);
//
//                EditCodiInfoFragment editCodi = new EditCodiInfoFragment();
//                editCodi.setArguments(bundle);
//                editCodi.show(getActivity().getFragmentManager(), "EditCodiInfoFragment");
                //getFragmentManager().

//                CodiInfoFragment codiInfo = new CodiInfoFragment();
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.detach(codiInfo).attach(codiInfo).commit();

            }
        });
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCodi();
            }
        });
    }

    private void sendShare() {
        shareTask = new BackgroundTaskForSend();
        shareTask.execute();
    }

    private void sendHitChange() {
        likesTask = new BackgroundTaskForSendHit();
        likesTask.execute();

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
                //Toast.makeText(getContext(), "delete!", Toast.LENGTH_SHORT).show();
                dismiss();

            }else{
                //Toast.makeText(getContext(), "fail..", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String sendObject_delete() {
        JSONObject jsonp = new JSONObject();
        try {
            jsonp.put("Username", id+"_coordy");
            jsonp.put("coordy_no", codi_no );
            jsonp.put("codiPath", codi);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonp.toString();
    }

    private class BackgroundTaskForSendHit extends AsyncTask<String, Integer, String> {
        String url = "http://218.38.52.180/hit_change.php";
        String json= sendObject_hit();//편집할 내용 받아옴

        @Override
        protected String doInBackground(String... strings) {
            String result; // 요청 결과를 저장할 변수.
            RequestActivity requestHttpURLConnection = new RequestActivity();
            result = requestHttpURLConnection.request(url, json); // 해당 URL로 부터 결과물을 얻어온다.
            return result;
        }
    }

    private String sendObject_hit() {
        JSONObject jsonp = new JSONObject();
        try {
            jsonp.put("Username", id+"_coordy");
            jsonp.put("coordy_no", codi_no );
            jsonp.put("hit", hit);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonp.toString();
    }

    private class BackgroundTaskForSend extends AsyncTask<String, Integer, String> {
        String url = "http://218.38.52.180/info_codishare.php";
        String json = sendObject_share();//편집할 내용 받아옴

        @Override
        protected String doInBackground(String... strings) {
            String result; // 요청 결과를 저장할 변수.
            RequestActivity requestHttpURLConnection = new RequestActivity();
            result = requestHttpURLConnection.request(url, json); // 해당 URL로 부터 결과물을 얻어온다.
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getActivity().getApplicationContext(), "공유되었습니다", Toast.LENGTH_SHORT).show();
        }
    }

    private String sendObject_share() {
        JSONObject jsonp = new JSONObject();
        try {
            jsonp.put("Username", id+"_coordy");
            jsonp.put("codiPath", codi);
            jsonp.put("no", codi_no);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonp.toString();
    }

}
