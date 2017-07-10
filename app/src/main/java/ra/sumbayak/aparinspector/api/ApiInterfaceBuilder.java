package ra.sumbayak.aparinspector.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

import okhttp3.*;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ra.sumbayak.aparinspector.Constant.SPKEY_TOKEN;
import static ra.sumbayak.aparinspector.Constant.SPNAME;

public class ApiInterfaceBuilder {
    
    public static ApiInterface build (Context context) {
        Retrofit.Builder builder = new Retrofit.Builder ()
            .baseUrl ("https://apar.herokuapp.com/apar/")
            .addConverterFactory (GsonConverterFactory.create ());
    
        final String token = context
            .getSharedPreferences (SPNAME, Context.MODE_PRIVATE)
            .getString (SPKEY_TOKEN, "null");
        
        if (!token.equals ("null")) {
            builder.client (new OkHttpClient.Builder ()
                .addInterceptor (new Interceptor () {
                    @Override
                    public Response intercept (@NonNull Chain chain) throws IOException {
                        return chain.proceed (chain.request ()
                            .newBuilder ()
                            .addHeader ("Authorization", "Token " + token)
                            .build ());
                    }
                })
                .build ());
        }
        
        return builder.build ().create (ApiInterface.class);
    }
}
