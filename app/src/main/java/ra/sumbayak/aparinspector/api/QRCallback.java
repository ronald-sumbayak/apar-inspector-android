package ra.sumbayak.aparinspector.api;

import android.support.annotation.NonNull;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class QRCallback<T> implements Callback<T> {
    
    @Override
    public void onResponse (@NonNull Call<T> call, @NonNull Response<T> response) {
        if (response.isSuccessful ()) onSuccessful (response);
        else if (response.code () == 401) on401 ();
        else onFailure ();
    }
    
    @Override
    public void onFailure (@NonNull Call<T> call, @NonNull Throwable t) {
        t.printStackTrace ();
        Log.e ("QRCallback", t.getMessage ());
        Log.e ("QRCallback", t.getLocalizedMessage ());
        Log.e ("QRCallback", t.toString ());
        onFailure ();
    }
    
    public abstract void onSuccessful (@NonNull Response<T> response);
    
    protected void on401 () {
        
    }
    
    protected void onFailure () {
        
    }
}
