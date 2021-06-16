package it.ads.app.wififinder.Networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    //Not sure why yet but using http doesnt work unless you set a security config
    //struggled for a while on this must look at later
    //https://developer.android.com/training/articles/security-config.html
    private static final String baseURL = "https://www.example.com";
    private static Retrofit instance;

    private ApiClient(){
    }

    public static Retrofit getInstance() {
        if(instance == null){

            instance = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return instance;
    }
}
