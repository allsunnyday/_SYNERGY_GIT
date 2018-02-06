package com.example.geehy.hangerapplication.temppa;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

/**
 * Created by hooney on 2017. 11. 8..
 */

public class MediaScanning implements MediaScannerConnection.MediaScannerConnectionClient {
    private MediaScannerConnection mConnection;
    private Uri mTargeturi;
    public MediaScanning(Context mContext, Uri uri) {
        this.mTargeturi = uri;
        mConnection = new MediaScannerConnection(mContext, this);
        mConnection.connect();
    }

    @Override public void onMediaScannerConnected() {
        Log.d("Media Scanning", "Media Scanning Start");
        mConnection.scanFile("content://media/external/images/media/", null);
    }

    @Override public void onScanCompleted(String path, Uri uri) {

        Log.d("Media Scanning", "Media Scanning Error : " + uri + " / " + this.mTargeturi);
        mConnection.disconnect();
    }
}
