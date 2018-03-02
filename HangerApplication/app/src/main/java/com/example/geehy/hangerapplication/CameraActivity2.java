package com.example.geehy.hangerapplication;

import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.example.geehy.hangerapplication.Fragments.HomeFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity2 extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private static final int REQUEST_IMAGE_GRAPCUT = 673;
    private String imageFilePath;
    private Uri photoUri;
    private Uri bitmapUri; //카메라에서 받아온 사진을 비트맵으로 수정
    private Uri grabcutUri;

    private Button Subtract_btn;
    private Button Save_btn;
    private ImageView Capture_img;
    private Bitmap rotateBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);

        Subtract_btn = (Button) findViewById(R.id.Subtract_btn);
        Save_btn = (Button) findViewById(R.id.Save_btn);
        Capture_img = (ImageView) findViewById(R.id.Capture_img);

        sendTakePhotoIntent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            //Log.d("bitmapNN",""+photoUri);
            /**비트맵 샘플 사이즈
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = 6; //추후 모든 사진을 같은 크기로 리사이징할수 있도록 변경
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
           **/
            //bitmapUri = getImageUri(this,rotateBitmap);
            //Log.d("bitmapCC"," "+ bitmapUri);
            //Capture_img.setImageBitmap(rotateBitmap);


            // photoUri = data.getData(); extra에 phothoUri실어 보냈음.
            Glide.with(this).load(photoUri).into(Capture_img);




        }else if(requestCode ==REQUEST_IMAGE_GRAPCUT && resultCode == RESULT_OK){//그랩컷 결과 받아오기
            grabcutUri = Uri.parse(data.getStringExtra("grabcut_uri"));
            Glide.with(this).load(grabcutUri).into(Capture_img);
//            Capture_img.setImageURI(grabcutUri);

        }
    }

/**    private int exifOrientationToDegrees(int exifOrientation) {
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

    //비트맵에서 Uri얻기
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
**/

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
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Hanger_temp" + timeStamp + "_";
        //외부저장소,사용자가 앱을 삭제하면 getExternalFilesDir()의 디렉터리에 저장한 앱 파일에 한해서 시스템이 제거함
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        //종료시 임시파일 제거
        image.deleteOnExit();
        return image;
    }

    public void Grab_onClick(View view) {
        Intent to_grab_intent = new Intent(getApplicationContext(), GrabcutActivity2.class);
        to_grab_intent.putExtra("to_grab_filepath", imageFilePath); //원본사진의 절대경로
        to_grab_intent.putExtra("bitmap_uri", photoUri.toString());
        startActivityForResult(to_grab_intent, REQUEST_IMAGE_GRAPCUT);
    }

    public void Save_onClick(View v) {
        //event();
        Intent intent = new Intent(getApplicationContext(), HomeFragment.class);
        intent.putExtra("URI", imageFilePath);
        intent.putExtra("grabcut_uri", grabcutUri.toString());
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

