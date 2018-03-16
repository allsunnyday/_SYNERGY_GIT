package com.example.geehy.hangerapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.geehy.hangerapplication.Fragments.HomeFragment;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GrabcutActivity2 extends AppCompatActivity {

    static {
        System.loadLibrary("native-lib");

        //System.loadLibrary("opencv_java3");
    }

    private long gcapp;
    private int flags = 0; // 0범위，1전경，2배경, 4저장
    private Bitmap bitmap;
    private ImageView imageView;
    private Bitmap bm;
    private Bitmap bm2;
    private float s = 0;
    private String imageFilePath;
    private Uri bitmap_Uri;
    private Uri gracut_Uri;
    private String file_path;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grabcut2);
        Log.d("initGrabcut_맨처음", " " );

        // Example of a call to a native method

        imageView = (ImageView) findViewById(R.id.image_view);

        ImageView img_FORE = (ImageView) findViewById(R.id.img_fore);
        ImageView img_BACK = (ImageView) findViewById(R.id.img_back);

        img_FORE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flags =1;
            }
        });

        img_BACK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flags = 2;
            }
        });

        /**
         file_path = intent.getStringExtra("to_grab_filepath");
         BitmapFactory.Options bmOptions = new BitmapFactory.Options();
         bmOptions.inJustDecodeBounds = false;
         bmOptions.inSampleSize = 10;
         bitmap = BitmapFactory.decodeFile(file_path,bmOptions);**/
        //bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.happy2);
        //bitmap = BitmapFactory.decodeByteArray(getString().getBytes(), 0, data.length);

        Intent intent = getIntent();
        bitmap_Uri = Uri.parse(intent.getStringExtra("bitmap_uri"));
        Log.d("bitmap_3.그랩이받았음", " " + bitmap_Uri);

        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), bitmap_Uri);
        } catch (Exception e) {
        }


        bm = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        bm2 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Mat img = new Mat();
        Utils.bitmapToMat(bitmap, img);
        Imgproc.cvtColor(img, img, Imgproc.COLOR_RGBA2RGB);

        Log.d("img.객체", " " + img.nativeObj);
        gcapp = initGrabCut(img.nativeObj);
        //gcapp=img.getNativeObjAddr();
        //showImage(gcapp);
        Log.d("img.주소",  " " + img.getNativeObjAddr());

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //Log.d("이미지뷰_터치",""+event);
                    if (s == 0) {
                        s = imageView.getWidth() * 1.0f / bitmap.getWidth();
                    }
                    int x = (int) (event.getX() / s);
                    int y = (int) (event.getY() / s);
                    int type = event.getAction();

                    switch (type) {
                        case MotionEvent.ACTION_DOWN: {
                            Log.d("액션다운전", " ");
                            moveGrabCut(0, x, y, flags, gcapp);
                            Log.d("액션다운후", " ");
                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            Log.d("액션전", " ");
                            moveGrabCut(1, x, y, flags, gcapp);
                            Log.d("액션후", " ");
                            break;
                        }
                        case MotionEvent.ACTION_CANCEL: {
                            moveGrabCut(1, x, y, flags, gcapp);
                            //Log.d("액션캔슬", " ");
                            break;
                        }
                        case MotionEvent.ACTION_MOVE: {
                            moveGrabCut(2, x, y, flags, gcapp);
                            //Log.d("액션무브", " ");
                            break;
                        }
                    }

                    return true;
                }
            });




    }


    //비트맵에서 Uri얻기
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        Log.d("getImageUri"," "+inImage);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
/*


    public void onFlags(View view) {
        Log.d("onFlags"," ");
        Button button = (Button) view;
        if ("범위".equals(button.getText().toString())) {
            flags = 0;
            resultSetting(gcapp);
        } else if ("전경".equals(button.getText().toString())) {
            flags = 1;
        } else if ("배경".equals(button.getText().toString())) {
            flags = 2;
        }
    }
*/

    public void onReset(View view) {
        Log.d("onReset"," ");
        Log.d("grabCut", "reset开始");
        flags = 0;
        reset(gcapp);
        Log.d("grabCut", "reset处理");
    }



    public void onGrabCut(View view) {
        Log.d("onGrabCut"," ");
        final Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                Log.d("grabCut", "처리_<2>.시작");
                if (grabCut(gcapp)) {
                    Log.d("grabCut", "처리_<3>. grabCut(gcapp): "+grabCut(gcapp) );
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //resultSetting(gcapp);
                            grabCutOver(gcapp);
                            Log.d("grabCut", "처리_<5>. grabCutOver(gcapp);");
//                            try {Thread.sleep(1000);
                            Log.d("grabCut", "");
//                            }catch (InterruptedException e){}
//                            return;
                        }
                    });
                }
                Log.d("grabCut", "처리_<4>.if (grabCut(gcapp)) 빠져나옴");
                //return;
            }

        };
        thread.start();
        Log.d("grabCut", "처리_<1>.thread.start()");
    }


    ////////////////////////////////////////////////////////
    //AsyncTask 넣을 예정
    ////////////////////////////////////////////////////////


    public void showImage(long image) {

        Log.d("자바showImage()"," ");
        Mat img = new Mat(image);
        Utils.matToBitmap(img, bm);
        //Bitmap white_back;
        //white_back = makeBlackTransparent(bm);
        //Glide.with(this).load(getImageUri(this,bm)).override(300,300).into(imageView);
        imageView.setImageBitmap(bm);
        //img.release(); 한번 터치할때마다 showImamge 호출되는데, 메모리 걱정 안해도 되나?
        //bm.recycle();
    }

    /**
     private static Bitmap makeBlackTransparent(Bitmap image) {
     // convert image to matrix
     Mat src = new Mat(image.getWidth(), image.getHeight(), CvType.CV_8UC4);
     Utils.bitmapToMat(image, src);

     // init new matrices
     Mat dst = new Mat(image.getWidth(), image.getHeight(), CvType.CV_8UC4);
     Mat tmp = new Mat(image.getWidth(), image.getHeight(), CvType.CV_8UC4);
     Mat alpha = new Mat(image.getWidth(), image.getHeight(), CvType.CV_8UC4);

     // convert image to grayscale
     Imgproc.cvtColor(src, tmp, Imgproc.COLOR_BGR2GRAY);

     // threshold the image to create alpha channel with complete transparency in black background region and zero transparency in foreground object region.
     Imgproc.threshold(tmp, alpha, 10, 255, Imgproc.THRESH_BINARY);

     // split the original image into three single channel.
     List<Mat> rgb = new ArrayList<Mat>(3);
     Core.split(src, rgb);

     // Create the final result by merging three single channel and alpha(BGRA order)
     List<Mat> rgba = new ArrayList<Mat>(4);
     rgba.add(rgb.get(0));
     rgba.add(rgb.get(1));
     rgba.add(rgb.get(2));
     rgba.add(alpha);
     Core.merge(rgba, dst);

     // convert matrix to output bitmap
     Bitmap output = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
     Utils.matToBitmap(dst, output);
     return output;
     }**/


    //GalleryActivity로 데이터 보내기
    public void save(View view){
        resultSetting(gcapp);
        Log.d("save()"," ");
        Intent data = new Intent(getApplicationContext(), GalleryActivity.class);
        //bm2 = makeBlackTransparent(bm);
        //Log.d("bm"," "+bm);
        //Log.d("bm2"," "+bm2);

        gracut_Uri = getImageUri(this,bm);
        data.putExtra("grabcut_uri", gracut_Uri.toString());
        Log.d("bitmap_4. 그랩컷했음"," "+ gracut_Uri);
        setResult(RESULT_OK, data);
        finish(); //서브액티비티를 종료

//        Log.d("GRAB_TAG","TOTAL MEMORY : "+(Runtime.getRuntime().totalMemory() / (1024 * 1024)) + "MB");
//        Log.d("GRAB_TAG","MAX MEMORY : "+(Runtime.getRuntime().maxMemory() / (1024 * 1024)) + "MB");
//        Log.d("GRAB_TAG","FREE MEMORY : "+(Runtime.getRuntime().freeMemory() / (1024 * 1024)) + "MB");
//        Log.d("GRAB_TAG","ALLOCATION MEMORY : "+((Runtime.getRuntime().totalMemory() -Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + "MB");

        flags = 4;

    }

    public native long initGrabCut(long image);

    public native void moveGrabCut(int event, int x, int y, int flags, long gcapp);

    public native void reset(long gcapp);

    public native boolean grabCut(long gcapp);

    public native void grabCutOver(long gcapp);

    public native void resultSetting(long gcapp);

}