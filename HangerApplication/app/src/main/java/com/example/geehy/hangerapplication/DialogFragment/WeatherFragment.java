package com.example.geehy.hangerapplication.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;


import com.bumptech.glide.Glide;
import com.example.geehy.hangerapplication.R;
import com.example.geehy.hangerapplication.RequestActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by MIN on 2018-02-19.
 */

public class WeatherFragment  extends DialogFragment {
    private Dialog dialog;
    private View view;
    private Button okBTN;
    private TextView textView;
    private ImageView imageView1;
    private ImageView imageView2;
    //BackgroundTask task;
    private SharedPreferences appData;
    private String id;
    private Spinner citySpinner;
    private String selectCity = "Seoul";

    private double lati = 37.540705;
    private double longi= 126.956764;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        final LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
//        try{
//            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                    100,
//                    1,
//                    mLocationListener);
//            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                    100,
//                    1,
//                    mLocationListener);
//
//        }catch(SecurityException e){
//            e.printStackTrace();
//        }

        dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.activity_weather, null);

        init();

        dialog.setContentView(view);
        return dialog;
    }
/*
    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

            Log.d("test", "onLocationChanged, location:" + location);
            double longitude = location.getLongitude(); //경도
            double latitude = location.getLatitude();   //위도
//            geovariable.setLatitude(latitude); // 클래스 변수에 위도 대입
//            geovariable.setLongitube(longitude);  // 클래스 변수에 경도 대입
            Log.d("location_", longitude +","+latitude +"");
        }

        public void onProviderDisabled(String provider) {
            // Disabled시
            Log.d("test", "onProviderDisabled, provider:" + provider);
        }

        public void onProviderEnabled(String provider) {
            // Enabled시
            Log.d("test", "onProviderEnabled, provider:" + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 변경시
            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }
    };*/

    private void init() {
        okBTN = (Button) view.findViewById(R.id.ok_button);
        imageView1 = (ImageView) view.findViewById(R.id.image1);
        imageView2 = (ImageView) view.findViewById(R.id.image2);
        textView = (TextView) view.findViewById(R.id.weather);

        citySpinner = (Spinner)view.findViewById(R.id.citiesSpinner);
        ArrayAdapter cityAdapter = ArrayAdapter.createFromResource(getActivity().getApplication(),
                R.array.cities,
                android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);

        // 설정값 불러오기
        appData = this.getActivity().getSharedPreferences("appData", MODE_PRIVATE);

        Event();
//        getWeatherData(lati, longi);

        //recommend();
    }
/*

    private void getWeatherData(double lati, double longi) {
        String url = "http://api.openweathermap.org/data/2.5/weather?lat="+lati + "&lon=" +longi + "&units=metric&appid=92e7ce79a7a1d14ac2cae64319116b46";
        ReceiveWeatherTask receiveWeatherTask = new ReceiveWeatherTask();

    }
*/


    private void Event() {

        okBTN.setOnClickListener(new View.OnClickListener() {//확인 버튼
            @Override
            public void onClick(View view) {
                dismiss(); //dialog 닫기
            }
        });

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch ((String)parent.getSelectedItem()){
                    case "서울":
                        selectCity = "Seoul";
                        lati = 37.540705;
                        longi = 126.956764;
                        break;
                    case "인천":
                        selectCity = "Incheon";
                        lati = 37.469221;
                        longi = 126.573234;
                        break;
                    case "광주":
                        selectCity = "Gwangju";
                        lati = 35.126033;
                        longi = 126.831302;
                        break;
                    case "대구":
                        selectCity = "Daegu";
                        lati =35.798838;
                        longi=128.583052;

                        break;
                    case "울산":
                        selectCity = "Ulsan";
                        lati =35.519301;
                        longi=129.239078;
                        break;
                    case "대전":
                        selectCity = "Daejeon";
                        lati =36.321655;
                        longi=127.378953;
                        break;
                    case "부산":
                        selectCity = "Busan";
                        lati =35.198362;
                        longi=129.053922;
                        break;
                    case "경기":
                        selectCity = "Gyeonggido";
                        lati =37.567167;
                        longi=127.190292;
                        break;
                    case "강원도":
                        selectCity = "Gangwondo";
                        lati =37.555837;
                        longi=128.209315;
                        break;
                    case "충청남도":
                        selectCity = "Chungcheongnamdo";
                        lati =36.557229;
                        longi=126.779757;
                        break;
                    case "충청북도":
                        selectCity = "Chungcheongbukdo";
                        lati =36.628503;
                        longi=127.929344;
                        break;
                    case "경상북도":
                        selectCity = "Gyeongsangbukdo";
                        lati =36.248647;
                        longi=128.664734;
                        break;
                    case "경상남도":
                        selectCity = "Gyeongsangnamdo";
                        lati =35.259787;
                        longi=128.664734;
                        break;
                    case "전라북도":
                        selectCity = "Jeollabukdo";
                        lati =35.716705;
                        longi=127.144185;
                        break;
                    case "전라남도":
                        selectCity = "Jeonlanamdo";
                        lati =34.819400;
                        longi=126.893113;
                        break;
                    case "제주도":
                        selectCity = "Jeju";
                        lati =33.364805;
                        longi=126.542671;
                        break;
                }

                //서버에 위치 보내는 backgroundtack 부르기
                //recommend();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

//
//    private void recommend(){//추천할 옷 서버에서 가져오기
//        task = new BackgroundTask();
//        task.execute();
//    }
//
//
//
//
//    class BackgroundTask extends AsyncTask<String, Integer, String> {
//        String url = "http://218.38.52.180/recomend.php";//
//        String json=sendObject();//username
//
//        @Override
//        protected  void onPreExecute(){
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground (String...params){
//            super.onPreExecute();
//            String result; // 요청 결과를 저장할 변수.
//            RequestActivity requestHttpURLConnection = new RequestActivity();
//            result = requestHttpURLConnection.request(url, json); // 해당 URL로 부터 결과물을 얻어온다.
//
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute (String s) {
//            super.onPostExecute(s);
//         //   Log.d("sssss", s);
//            if (s.equals("성공")) {
//                /////성공할 시에 서버에 결과를 보낸다
//                //isChanged = true;
//                Toast.makeText(getContext(), "delete!", Toast.LENGTH_SHORT).show();
//                //dismiss();
//
//            }else{
//                Toast.makeText(getContext(), "fail..", Toast.LENGTH_SHORT).show();
//            }
//
//
//        }
//    }
//
//    private String sendObject() {
//        id = appData.getString("ID", "");
//        JSONObject jsonpost = new JSONObject();
//        try {
//            jsonpost.put("Username", id);//sharedpreference에 저장되었던 username 서버로 보내기 위해서 json 형식으로 변환
//            jsonpost.put("City", selectCity);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return jsonpost.toString();
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
/*
    private class ReceiveWeatherTask extends AsyncTask <String , Void , JSONObject>{


        @Override
        protected JSONObject doInBackground(String... datas) {
            try{
                HttpURLConnection conn = (HttpURLConnection)new URL(datas[0]).openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.connect();

                if(conn.getResponseCode() ==  HttpURLConnection.HTTP_OK){
                    InputStream is = conn.getInputStream();
                    InputStreamReader reader = new InputStreamReader(is);
                    BufferedReader in = new BufferedReader(reader);

                    String readed;
                    while((readed = in.readLine()) != null){
                        JSONObject jsonObject = new JSONObject(readed);
                        String result = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");

                        return jsonObject;
                    }
                }else{
                    return null;
                }
                return null;

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            Log.d("return::: ", result + " ");
            if(result != null){
               // String iconName =";" ;
                String nowTemp="";
                String description="";

                try{
                    //iconName = result.getJSONArray("weather").getJSONObject(0).getString("icon");
                    nowTemp = result.getJSONObject("main").getString("temp");
                    description = result.getJSONArray("weather").getJSONObject(0).getString("description");
                    Log.d("nowtemp", nowTemp);
                    Log.d("description", description);
                    textView.setText("현재날씨:"+nowTemp);
                }catch (JSONException e){
                    e.printStackTrace();
                }
                //description = trans
                //Toast.makeText()


            }
        }
    }*/
}
