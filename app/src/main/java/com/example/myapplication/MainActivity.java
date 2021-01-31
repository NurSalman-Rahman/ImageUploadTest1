package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.ModelClass.ResponseImage;
import com.example.myapplication.api.ApiClient;
import com.example.myapplication.api.ApiService;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    Button upload;
    ImageView imgShow, imgUpload;
    private final int SELECT_PICTURE = 1;
    private Retrofit retrofit;// = ApiClient.getClient();
    private ApiService api;//= retrofit.create(ApiService.class);
    private MultipartBody.Part part;
    private Bitmap bitmap = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intFunction();
        intListener();


    }

    private void intFunction() {
        retrofit = ApiClient.getClient();
        api = retrofit.create(ApiService.class);


        upload = findViewById(R.id.B1);
        imgShow = findViewById(R.id.sal);
        imgUpload = findViewById(R.id.Ic1);
    }

    private void intListener() {
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Testing", "onClick: 1");
                if (part != null)
                    call_api();
                Log.d("Testing", "onClick: 2");


            }
        });
        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // chooseFromGallery();
                isStoragePermissionGranted();




            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                imgShow.setImageURI(selectedImageUri);


                String path = getRealPathFromURI(selectedImageUri);

                if (path == null) return;

                File imgFile = new File(path);
                try {
                    imgFile = new Compressor(this).compressToFile(imgFile);

                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                try {

                    bitmap = new Compressor(this).compressToBitmap(imgFile);
                    //  dialog.show();
                    if (bitmap == null) {
                        //profileIV.setImageBitmap(bitmap);
                        Toast.makeText(this, "Please Select Image Again !", Toast.LENGTH_SHORT).show();
                    }

//                    alarDialog("profile");
                    //selectPhotoTV.setText("Change Photo");
                } catch (IOException e) {
                    return;
                }


             //   fileReqBody = RequestBody.create(MediaType.parse("image/*"), imgFile);

                RequestBody requestFile =
                        RequestBody.create(
                                MediaType.parse(getContentResolver().getType(selectedImageUri)),
                                imgFile
                        );
                part = MultipartBody.Part.createFormData("avatar", imgFile.getName(), requestFile);


            }
        }
    }


    //api


    private void call_api() {

        //creating a file
        //  File file = new File(getRealPathFromURI(fileUri));

        // RequestBody id = RequestBody.create(MediaType.parse("text/plain"), userId);
        Log.d("Testing", "onResponse: ok  1");

        Call<ResponseImage> call;
        call = api.imageUploadFile(part);
        Log.d("Testing", "onResponse: ok  2");

        //finally performing the call
        call.enqueue(new Callback<ResponseImage>() {
            @Override
            public void onResponse(Call<ResponseImage> call, Response<ResponseImage> response) {

                Log.d("Testing", "onResponse: " + response.message().toString());
                Toast.makeText(MainActivity.this, "Upload Successfuly"+response.message().toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ResponseImage> call, Throwable t) {
                Log.d("Testing", "onResponse:error"+call.toString());
                Log.d("Testing", "onResponse:error"+t.getMessage());

            }
        });


    }


    public String getRealPathFromURI(Uri contentUri) {

        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri,
                proj,
                null,
                null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }



    public boolean isStoragePermissionGranted() {
        // Toast.makeText(this, "isStorage", Toast.LENGTH_SHORT).show();
        //   Log.d("image_check", "onClick: profile permission");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                chooseFromGallery();
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            chooseFromGallery();
            return true;
        }
    }

    private void chooseFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }


}


