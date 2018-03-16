package com.example.geehy.hangerapplication;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.geehy.hangerapplication.Fragments.HomeFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GalleryActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imgMain;
    private Button btnCamera, btnAlbum;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_ALBUM = 2;
    private static final int CROP_FROM_PICK = 3;
    private static final int GRAP_FROM_CROP =4;
    private Uri photoUri;
    private Uri bitmapUri;
    private Uri grabUri;

    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private static final int MULTIPLE_PERMISSIONS = 101;

    private String mCurrentPhotoPath;
    private File tempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        checkPermissions();
        initView();
    }

    private boolean checkPermissions() {
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(pm);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void initView() {
        imgMain = (ImageView) findViewById(R.id.img_test);
        btnCamera = (Button) findViewById(R.id.btn_camera);
        btnAlbum = (Button) findViewById(R.id.btn_album);

        btnCamera.setOnClickListener(this);
        btnAlbum.setOnClickListener(this);
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(GalleryActivity.this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, PICK_FROM_CAMERA);
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

        //".jpg"
        mCurrentPhotoPath = image.getAbsolutePath();
        //종료시 임시파일 제거
        image.deleteOnExit();
        return image;
    }

    private void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_camera:
                takePhoto();
                break;
            case R.id.btn_album:
                goToAlbum();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(this.permissions[0])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();
                            }
                        } else if (permissions[i].equals(this.permissions[1])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        } else if (permissions[i].equals(this.permissions[2])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        }
                    }
                } else {
                    showNoPermissionToastAndFinish();
                }
                return;
            }
        }
    }

    private void showNoPermissionToastAndFinish() {
        Toast.makeText(this, "권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);


        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == PICK_FROM_ALBUM) {
            if (data == null) {
                return;
            }
            photoUri = data.getData();
            cropImage();



        } else if (requestCode == PICK_FROM_CAMERA) {
            cropImage();
            // 갤러리에 나타나게
            MediaScannerConnection.scanFile(GalleryActivity.this,
                    new String[]{photoUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });



        } else if (requestCode == CROP_FROM_PICK) {
            //imgMain.setImageURI(null);
            //imgMain.setImageURI(photoUri);
            try {
                Log.d("bitmap_1.크롭후", "" + photoUri);
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = 4; //추후 모든 사진을 같은 크기로 리사이징할수 있도록 변경, 글라이드 이용???
                // 인터넷 사진일 경우 1
                // 일반 카메라 사진일 경우 4
                Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                bitmapUri = getImageUri(this, bitmap);
                Log.d("bitmap_2.리사이징후", " " + bitmapUri);
                Grab();//여기서 크롭된 좌표를 그랩에 보내주어야 한다......가 아니고 그냥 전체 이미지 받으면 되잖아?
             /*   int width = imgMain.getWidth();
                int height = imgMain.getHeight();

                BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
                bmpFactoryOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mCurrentPhotoPath, bmpFactoryOptions);

                int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);
                int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
                if (heightRatio > 1 || widthRatio > 1) {
                    if (heightRatio > widthRatio) {
                        bmpFactoryOptions.inSampleSize = heightRatio;
                    } else {
                        bmpFactoryOptions.inSampleSize = widthRatio;
                    }
                }

                bmpFactoryOptions.inJustDecodeBounds = false;
                Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmpFactoryOptions);
                bitmapUri = getImageUri(this, bitmap);
                Grab();*/
            }catch (NullPointerException e){}




        }
        else if(requestCode == GRAP_FROM_CROP) {
            //그랩컷 결과 받아오기
            grabUri = Uri.parse(data.getStringExtra("grabcut_uri"));
//            Log.d("bitmap_5. 그랩컷한거 받았음"," "+ grabUri);
//            //imgMain.setImageURI(grabUri);
//            Log.d("GAL_TAG","TOTAL MEMORY : "+(Runtime.getRuntime().totalMemory() / (1024 * 1024)) + "MB");
//            Log.d("GAL_TAG","MAX MEMORY : "+(Runtime.getRuntime().maxMemory() / (1024 * 1024)) + "MB");
//            Log.d("GAL_TAG","FREE MEMORY : "+(Runtime.getRuntime().freeMemory() / (1024 * 1024)) + "MB");
//            Log.d("GAL_TAG","ALLOCATION MEMORY : "+((Runtime.getRuntime().totalMemory() -Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + "MB");
            save();
        }
    }

    //Android N crop image
    public void cropImage() {
        if(photoUri == null)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.grantUriPermission("com.android.camera", photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            grantUriPermission(list.get(0).activityInfo.packageName, photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        int size = list.size();

        if (size == 0) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(this, "용량이 큰 사진의 경우 시간이 오래 걸릴 수 있습니다.", Toast.LENGTH_SHORT).show();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            intent.putExtra("crop", "true");
            //x,y비율 자유롭게
            //intent.putExtra("aspectX", 1);
            //intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            File croppedFileName = null;
            try {
                croppedFileName = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File folder = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File tempFile = new File(folder.toString(), croppedFileName.getName());

            photoUri = FileProvider.getUriForFile(this, getPackageName(), tempFile);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }

            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

            Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                grantUriPermission(res.activityInfo.packageName, photoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            startActivityForResult(i, CROP_FROM_PICK);
        }
    }


    public void Grab() {
        Intent to_grab_intent = new Intent(getApplicationContext(), GrabcutActivity2.class);
        to_grab_intent.putExtra("bitmap_uri", bitmapUri.toString());
        startActivityForResult(to_grab_intent, GRAP_FROM_CROP);
    }

    public void save() {
        //홈프래그먼트로 결과Uri 보내기
        Intent data = new Intent(this.getApplicationContext(), HomeFragment.class);
        //intent.putExtra("URI", imageFilePath);
        data.putExtra("grabcut_uri", grabUri.toString());
        setResult(RESULT_OK, data);
        finish();
    }




    //이미지프로세싱_글라이드로 대체

    //1.비트맵에서 Uri얻기
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try {inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        }catch (NullPointerException e){}
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);

        return Uri.parse(path);

    }


}
