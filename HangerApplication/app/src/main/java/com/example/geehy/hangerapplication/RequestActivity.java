//php 통신을 위한 Activitiy
package com.example.geehy.hangerapplication;

import android.content.ContentValues;
import android.util.Log;
import android.webkit.CookieManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class RequestActivity {
    private static final String TAG = RequestActivity.class.getSimpleName();
    public String request(String _url, String json){

        // HttpURLConnection 참조 변수.
        HttpURLConnection urlConn = null;

        // HttpURLConnection을 통해 데이터 보내기 & 가져오기기
        try{
            URL url = new URL(_url);
            String postdata="json="+json;
            urlConn = (HttpURLConnection) url.openConnection();

            // [2-1]. urlConn 설정.
            urlConn.setRequestMethod("POST"); // URL 요청에 대한 메소드 설정 : POST.
            urlConn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
            urlConn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");

      /*      String cookieString = CookieManager.getInstance().getCookie(_url);//서버에서 쿠키값 받아오기
            if (cookieString != null) {
                //세션을 위한 쿠키값 셋팅
                Log.d("cookie",cookieString);
                urlConn.setRequestProperty("Cookie", cookieString);
            }
*/
            urlConn.setConnectTimeout(15000);
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);

            OutputStream os = urlConn.getOutputStream();
            os.write(postdata.getBytes("UTF-8")); // 출력 스트림에 출력.
            os.flush(); // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.
            os.close(); // 출력 스트림을 닫고 모든 시스템 자원을 해제.

            // 연결 요청 확인.
            // 실패 시 null을 리턴하고 메서드를 종료.
            if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;

            // 요청한 URL의 출력물을 BufferedReader로 받는다.
            StringBuilder jsonHtml = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

            // 출력물의 라인과 그 합에 대한 변수.
            String line;

            // 라인을 받아와 합친다.
            while ((line = reader.readLine()) != null)
                jsonHtml.append(line);

            reader.close();
            String result = jsonHtml.toString();
            urlConn.disconnect();
            return result;


        } catch (MalformedURLException e) { // for URL.
            e.printStackTrace();
        } catch (IOException e) { // for openConnection().
            e.printStackTrace();
        } finally {
            if (urlConn != null)
                urlConn.disconnect();
        }

        return null;

    }

}