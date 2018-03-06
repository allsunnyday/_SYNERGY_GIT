package com.example.geehy.hangerapplication.temppa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by LG on 2018-02-24.
 */

public class ImageData {

    //Uri => FilePath
    public String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    //FilePath => URI


    //File => URI
    public Uri getUriFromFile(File photoFile, Activity activity){
        Uri photoUri = null;
        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(activity, activity.getPackageName(), photoFile);
        }
        return photoUri;
    }

    //비트맵 => RealUri
    public Uri getUriFromBitmap(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    //StringUri => 비트맵
    public Bitmap getBitmapFromStringUri(String stringURI, String name, Intent intent,Activity activity) {
        Uri realURI = null;
        Bitmap bitmap = null;
        realURI = Uri.parse(intent.getStringExtra(name));
        Log.d("stirngURI", " " + stringURI);
        Log.d("realURI", " " + realURI);
        try {
            bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), realURI);
        }catch (Exception e) {
            Log.d("getBitmapFromUri","Exception"+e);
        }
        return bitmap;
    }
}
