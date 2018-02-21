package com.example.geehy.hangerapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.geehy.hangerapplication.Fragments.HomeFragment;
import com.example.geehy.hangerapplication.temppa.CameraSurfaceView;
import com.example.geehy.hangerapplication.temppa.MediaScanning;

public class CameraActivity extends AppCompatActivity {
    private FrameLayout previewFrame;
    private Button takeBTN;
    private Button saveBTN;
    private Button retakeBTN;
    private CameraSurfaceView cameraView;

    private boolean isCameraRunning;
    private String outUriStr;

    private Camera cntCamera;
    private  Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        init();

        previewFrame.addView(cameraView);
        event();

    }

    private void init(){
        isCameraRunning = true;
        cameraView = new CameraSurfaceView(getApplicationContext());
        previewFrame = (FrameLayout) findViewById(R.id.previewFrame);
        takeBTN = (Button) findViewById(R.id.Camera_takeBTN);
        saveBTN = (Button) findViewById(R.id.Camera_saveBTN);
        retakeBTN = (Button) findViewById(R.id.Camera_retakeBTN);
    }

    private void event(){
        takeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraView.capture(new Camera.PictureCallback(){

                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        try{
                            cntCamera = camera;
                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                            // 화면 회전을 위한 matrix객체 생성
                            Matrix m = new Matrix();
                            // matrix객체에 회전될 정보 입력
                            m.setRotate(90, (float) bitmap.getWidth(), (float) bitmap.getHeight());
                            // 기존에 저장했던 bmp를 Matrix를 적용하여 다시 생성
                            Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, false);
                            // 기존에 생성했던 bmp 자원해제
                            bitmap.recycle();

                            outUriStr = MediaStore.Images.Media.insertImage(getContentResolver(), rotateBitmap,
                                    "Captured Image", "Captured Image using Camera");
                            outUriStr.replace("content:", "");
                            if(outUriStr == null){
                                Toast.makeText(getApplicationContext(), "카메라 이미지를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                                Log.e("Camera Activity", "Camera Image Error");
                                return;
                            }else{
                                //String folder = Environment.getExternalStorageDirectory().getAbsolutePath();
                                 uri = Uri.parse(outUriStr);
                                 Log.d("cameraTest", "uri:"+uri);
                                Log.d("cameraTest", "outUriStr:"+outUriStr);
                                getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

                                new MediaScanning(getApplicationContext(), uri);

                                Toast.makeText(getApplicationContext(), "갤러리 저장 완료!", Toast.LENGTH_SHORT).show();
                                isCameraRunning =false;
                            }
                        }catch (Exception e){
                            Log.e("Camera Activity", "Camera Capture Error~~!!!", e);
                        }
                    }
                });
            }
        });

        retakeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCameraRunning){
                    Toast.makeText(getApplicationContext(), "카메라가 실행되고 있습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    cntCamera.startPreview();
                }
            }
        });

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), HomeFragment.class);
                intent.putExtra("URI", outUriStr);
                setResult(RESULT_OK, intent);

                finish();
            }
        });





        /*cameraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cntCamera.autoFocus (new Camera.AutoFocusCallback() {
                    public void onAutoFocus(boolean success, Camera camera) {
                        if(success){
                            Toast.makeText(getApplicationContext(),"Auto Focus Success",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Auto Focus Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });*/
    }
}
