package com.example.geehy.hangerapplication;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by JHS on 2018-02-20.
 */
//코디 파일 업로드를 위한 인터페이스 정의
public interface UploadImageForCodiInterface {

    @Multipart
    @POST("/testfile10.php")  //기존의 ->> testfile4.php
    Call<UploadObject> upload(
            @Part("name") RequestBody name,
            @Part MultipartBody.Part file
    );
}
