package com.example.myapplication.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {





        private static Retrofit retrofit = null;


        public static final String BASE_URL = "https://alhasan.dev/interns/services/";


        public static Retrofit getClient() {
            if (retrofit == null) {
                OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
                clientBuilder.connectTimeout(5, TimeUnit.MINUTES);
                clientBuilder.readTimeout(5, TimeUnit.MINUTES);
                clientBuilder.writeTimeout(5, TimeUnit.MINUTES);

                OkHttpClient client = clientBuilder.build();

                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build();
            }
            return retrofit;

        }

    }





