package com.example.geehy.hangerapplication;

import android.content.ContentValues;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class UploadimgActivity {
    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";
    String s;
    public String request(File file){
        // HttpURLConnection 참조 변수.
        HttpURLConnection urlConn = null;
        // HttpURLConnection을 통해 데이터 보내기 & 가져오기기
        try {

            // String filename = file.getName();
            FileInputStream mFileInputStream = new FileInputStream(file);
            URL connectUrl = new URL("http://218.38.52.180/imgtest.php");//php파일
            // open connection
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
            // write data
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + "img" + "\"" + lineEnd);

            int bytesAvailable = mFileInputStream.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            byte[] buffer = new byte[bufferSize];
            int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            // read image
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = mFileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // close streams
            Log.e("Test", "File is written");
            mFileInputStream.close();
            dos.flush(); // finish upload...

            // get response
            int ch;
            InputStream is = conn.getInputStream();
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            s = b.toString();
            Log.e("imgUpload", "result = " + s);
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("imgUpload", "exception " + e.getMessage());

        }
        return s;


      /*
        try {

            FileInputStream mFileInputStream = new FileInputStream(absPath);
            URL url = new URL(_url);;//php파일
            String postdata="image"+absPath;
            // open connection
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Length", Integer.toString(mFileInputStream.available()));
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");

            conn.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
            // write data
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" +absPath+"\""+ lineEnd );

            int bytesAvailable = mFileInputStream.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            byte[] buffer = new byte[bufferSize];
            int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            // read image
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = mFileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // close streams

            mFileInputStream.close();
            dos.flush(); // finish upload...

            // get response
            int ch;
            InputStream is = conn.getInputStream();
            StringBuffer b =new StringBuffer();
            while( ( ch = is.read() ) != -1 ){
                b.append( (char)ch );
            }
            s=b.toString();

            dos.close();
        } catch (IOException e) {
            e.printStackTrace();


        }
        return s;
*/
    }

}