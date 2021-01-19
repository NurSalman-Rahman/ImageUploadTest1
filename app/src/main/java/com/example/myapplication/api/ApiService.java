package com.example.myapplication.api;


import com.example.myapplication.ModelClass.ResponseImage;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {




    @Multipart
    @POST("image.php")
    Call<ResponseImage>  imageUploadFile( @Part MultipartBody.Part  file );

}
