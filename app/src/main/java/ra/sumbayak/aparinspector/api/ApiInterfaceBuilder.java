package ra.sumbayak.aparinspector.api;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ra.sumbayak.aparinspector.Constant.*;

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
