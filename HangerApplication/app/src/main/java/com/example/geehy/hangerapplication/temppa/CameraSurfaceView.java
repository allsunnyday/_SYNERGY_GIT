package com.example.geehy.hangerapplication.temppa;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by qewqs on 2017-11-05.
 */

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera camera = null;

    public CameraSurfaceView(Context context) {
        super(context);
        mHolder = getHolder();
        mHolder.addCallback(this);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camera = Camera.open();

        camera.setDisplayOrientation(90);
        Camera.Parameters parameters = camera.getParameters();
        camera.setParameters(parameters);

        try{
            camera.setPreviewDisplay(mHolder);
        }catch (Exception e){
            Log.e("CameraSurFaceView", "Failed to set camera preview.", e);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    public boolean capture(Camera.PictureCallback handler){
        if(camera != null){
            camera.takePicture(null, null, handler);
            return true;
        }else{
            return false;
        }
    }

}

