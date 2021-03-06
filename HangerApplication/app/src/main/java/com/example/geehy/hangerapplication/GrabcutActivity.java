/*
package com.example.geehy.hangerapplication;

*/
/**
 * Created by geehy on 2017-12-13.
 *//*


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.geehy.hangerapplication.Fragments.ImageProcessing;

import java.io.ByteArrayOutputStream;

public class GrabcutActivity extends ActionBarActivity {
*/
/*

    private Bitmap croppedBitmap = null;
    private Bitmap bitmap= null;
    public static Bitmap gImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
    private static int RESULT_LOAD_IMG = 1;
    private static int REQUEST_IMAGE_CROP = 2;
    String imgDecodableString;
    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data
                Uri selectedImage = data.getData();
                Log.d("runtest",selectedImage.toString());
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView mImageView = (ImageView) findViewById(R.id.mainImg);
                int targetW = mImageView.getWidth();
                int targetH = mImageView.getHeight();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imgDecodableString, options);

                int srcWidth = options.outWidth;
                int srcHeight = options.outHeight;

// Only scale if the source is big enough. This code is just trying to fit a image into a certain width.
                if(targetW  > srcWidth)
                    targetW  = srcWidth;



// Calculate the correct inSampleSize/scale value. This helps reduce memory use. It should be a power of 2

                int inSampleSize = 1;
                while(srcWidth / 2 > targetW ){
                    srcWidth /= 2;
                    srcHeight /= 2;
                    inSampleSize *= 2;
                }

                float desiredScale = (float) targetW  / srcWidth;

// Decode with inSampleSize
                options.inJustDecodeBounds = false;
                options.inDither = false;
                options.inSampleSize = inSampleSize;
                options.inScaled = false;
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap sampledSrcBitmap = BitmapFactory.decodeFile(imgDecodableString, options);

// Resize
                Matrix matrix = new Matrix();
                matrix.postScale(desiredScale, desiredScale);
                bitmap = Bitmap.createBitmap(sampledSrcBitmap, 0, 0, sampledSrcBitmap.getWidth(), sampledSrcBitmap.getHeight(), matrix, true);
                sampledSrcBitmap = null;
                mImageView.setImageBitmap(bitmap);




            } else {
                if (requestCode != REQUEST_IMAGE_CROP){
                    Toast.makeText(this, "You haven't picked Image",
                            Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        if (requestCode == REQUEST_IMAGE_CROP && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            //get the cropped bitmap
            croppedBitmap = extras.getParcelable("data");
            ImageView mImageView = (ImageView) findViewById(R.id.mainImg);
            mImageView.setImageBitmap(croppedBitmap);

        }

    }
    public void performCrop(View view){
        ImageView mImageView = (ImageView) findViewById(R.id.mainImg);
        if(null!=mImageView.getDrawable()){
            try {
                Intent intent = new Intent("com.android.camera.action.CROP");
                Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
                Uri uri = getImageUri(this, bitmap);
                intent.setDataAndType(uri, "image*//*
*/
/*");
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("crop", "true");
                intent.putExtra("return-data", true);
                startActivityForResult(intent, REQUEST_IMAGE_CROP);
            }catch (ActivityNotFoundException e){
                //display an error message
                String errorMessage = "Whoops - your device doesn't support the crop action!";
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else{
            Toast.makeText(this, "Load an image", Toast.LENGTH_LONG)
                    .show();


        }

    }

    public  Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void imageProcessing(View view){
        Intent intent = new Intent(this, ImageProcessing.class);
        //myIntent.putExtra("key", value); //Optional parameters
        ImageView mImageView = (ImageView) findViewById(R.id.mainImg);
        if(null!=mImageView.getDrawable()) {
            Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();

            gImg=bitmap;
            this.startActivity(intent);
        }
        else{
            Toast.makeText(this, "Load an image", Toast.LENGTH_LONG)
                    .show();
        }
    }
    private void createShareImageIntent(){
        ImageView mImageView = (ImageView) findViewById(R.id.mainImg);
        Bitmap cBitmap =((BitmapDrawable) mImageView.getDrawable()).getBitmap();
        Uri sUri=getImageUri(this,cBitmap);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.putExtra(Intent.EXTRA_STREAM,sUri );
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "send"));

    }


    *//*
*/
/**
     * A placeholder fragment containing a simple view.
     *//*
*/
/*
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_grabcut, container, false);
            return rootView;
        }

    }*//*

}*/
