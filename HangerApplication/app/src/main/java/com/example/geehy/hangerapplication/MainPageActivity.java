//이미지 가져오기
package com.example.geehy.hangerapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.geehy.hangerapplication.DataClass.Account;
import com.example.geehy.hangerapplication.Fragments.AccFragment;
import com.example.geehy.hangerapplication.Fragments.DashFragment;
import com.example.geehy.hangerapplication.Fragments.DressFragment;
import com.example.geehy.hangerapplication.Fragments.HomeFragment;
import com.example.geehy.hangerapplication.Fragments.TempFragment;
import com.example.geehy.hangerapplication.gridview_home.dressItem;
import com.example.geehy.hangerapplication.temppa.BottomNavigationViewHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainPageActivity extends AppCompatActivity {

    private Account MainUserInfo;

    private AccFragment accFragment;
    private HomeFragment homeFragment;
    private DashFragment dashFragment;
    private TempFragment tempFragment;
    private DressFragment dressFragment;
    private FragmentManager manager;
    private SharedPreferences appData;
    private String id;
    private int flagWho;

    private ArrayList<dressItem> list;

    /*Get Set*/
    public ArrayList<dressItem> getList() {
        return list;
    }

    public void setList(ArrayList<dressItem> list) {
        this.list = list;
    }



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigationcloset:
                    manager.beginTransaction().replace(R.id.content, new HomeFragment()).commit();
                    flagWho = 0;
                    return true;
                case R.id.navigationcoordy:
                    manager.beginTransaction().replace(R.id.content, new DashFragment()).commit();
                    flagWho = 1;
                    return true;
                case R.id.navigationcalendar:
                    manager.beginTransaction().replace(R.id.content, new DressFragment()).commit();
                    flagWho = 2;
                    return true;
                case R.id.navigationfavorite:
                    manager.beginTransaction().replace(R.id.content, new TempFragment()).commit();
                    flagWho = 3;
                    return true;
                case R.id.navigationsetting:
                    manager.beginTransaction().replace(R.id.content, new AccFragment()).commit();
                    flagWho = 4;
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

      MainUserInfo = (Account) getIntent().getSerializableExtra("user");
        if(MainUserInfo == null){
            MainUserInfo = new Account();
        }else{
        }

        init();
        loadDressData();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.removeShiftMode(navigation);

        navigation.setSelectedItemId(R.id.navigationcloset);

    }

    private void init(){
        flagWho = -1;
        manager = getSupportFragmentManager();

        list = new ArrayList<dressItem>();


        event();
    }



    private void loadDressData(){
        for(int i = 0 ; i<5;i++){
            dressItem temp = new dressItem();
            temp.setDressName("Dress " + i);
            temp.setSeason(new int[]{1, 2});
            list.add(temp);
        }

        for(int i = 0;i<list.size();i++){
            Log.d("Data Recive", "Data : " + list.get(i).getDressName());
        }
    }




    private void event() {
    }


}
