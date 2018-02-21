package com.example.geehy.hangerapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.geehy.hangerapplication.Fragments.HomeFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity2 extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    private Uri photoUri;

    private Button Subtract_btn;
    private Button Save_btn;
    private ImageView Capture_img;
    private Bitmap rotateBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);

     //   Subtract_btn = (Button) findViewById(R.id.Subtract_btn);
        Save_btn = (Button) findViewById(R.id.Save_btn);
    //    Capture_img = (ImageView) findViewById(R.id.Capture_img);

        sendTakePhotoIntent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = 4;
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath, bmOptions);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }
            rotateBitmap = rotate(bitmap, exifDegree);
            Capture_img.setImageBitmap(rotateBitmap);
        }else if(requestCode ==2 && resultCode == RESULT_OK){

        }
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);}
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }
/*
    public void Grab_onClick(View view) {
        Intent to_grab_intent = new Intent(getApplicationContext(), GrabcutActivity2.class);
        to_grab_intent.putExtra("URI", imageFilePath);
        Log.d("fff_ppp", imageFilePath +"");
        startActivityForResult(to_grab_intent, 2);
    }
*/
    public void Save_onClick(View v) {
        //event();
        Intent intent = new Intent(getApplicationContext(), HomeFragment.class);
        intent.putExtra("URI", imageFilePath);
        Log.d("fp", imageFilePath +"");
        setResult(RESULT_OK, intent);
        finish();
    }
}


/** 이벤트
 private void event(){
 outUriStr = MediaStore.Images.Media.insertImage(getContentResolver(), rotateBitmap,
 "Captured Image", "Captured Image using Camera");
 outUriStr.replace("content:", "");
 Log.d("outUriStr", outUriStr);
 if(outUriStr == null){
 Toast.makeText(getApplicationContext(), "카메라 이미지를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
 Log.e("Camera Activity", "Camera Image Error");
 return;
 }else{
 //String folder = Environment.getExternalStorageDirectory().getAbsolutePath();
 uri = Uri.parse(outUriStr);
 Log.d("outUriStr URI", uri +"");
 getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
 new MediaScanning(getApplicationContext(), uri);
 Toast.makeText(getApplicationContext(), "갤러리 저장 완료!", Toast.LENGTH_SHORT).show();
 isCameraRunning =false;
 }
 }
 **/

/** 썸네일
 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 super.onActivityResult(requestCode, resultCode, data);
 if (requestCode == 1 && resultCode == RESULT_OK) {

 bitmap = (Bitmap) data.getExtras().get("data");

 // 화면 회전을 위한 matrix객체 생성
 Matrix m = new Matrix();
 // matrix객체에 회전될 정보 입력
 m.setRotate(90, (float) bitmap.getWidth(), (float) bitmap.getHeight());
 // 기존에 저장했던 bmp를 Matrix를 적용하여 다시 생성
 rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, false);
 // 기존에 생성했던 bmp 자원해제
 bitmap.recycle();
 }**/





/**
 public void Grab_onClick(View view) {
 Intent to_grab_intent = new Intent(this, GrabcutActivity2.class);
 to_grab_intent.putExtra("bitmap", rotateBitmap);
 startActivityForResult(to_grab_intent,2);
 }

 public void Save_onClick(View v) {
 event();
 Intent intent = new Intent(getApplicationContext(), HomeFragment.class);
 intent.putExtra("URI", photoUri);
 setResult(RESULT_OK, intent);
 finish();
 }**/

