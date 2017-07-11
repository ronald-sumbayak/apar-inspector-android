package ra.sumbayak.aparinspector.home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ra.sumbayak.aparinspector.GlobalLoadingDialog;
import ra.sumbayak.aparinspector.api.Apar;
import ra.sumbayak.aparinspector.api.ApiInterfaceBuilder;
import ra.sumbayak.aparinspector.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ra.sumbayak.aparinspector.Constant.*;

class AparUpdater {
    
    static void update (final FragmentActivity context) {
        GlobalLoadingDialog.show (context);
        
        ApiInterfaceBuilder
            .build (context)
            .all ()
            .enqueue (new Callback<List<Apar>> () {
                @Override
                public void onResponse (@NonNull Call<List<Apar>> call, @NonNull Response<List<Apar>> response) {
                    if (response.isSuccessful ()) {
                        HomeActivity.aparList = response.body ();
                        HomeActivity.aparMap  = new SparseArray<> ();
                        for (Apar apar : HomeActivity.aparList)
                            HomeActivity.aparMap.put (apar.id, apar);
                    }
    
                    GlobalLoadingDialog.hide ();
                }
                
                @Override
                public void onFailure (@NonNull Call<List<Apar>> call, @NonNull Throwable t) {
                    t.printStackTrace ();
                    GlobalLoadingDialog.hide ();
                    Toast.makeText (context, "Loading data failed", Toast.LENGTH_SHORT).show ();
                }
            });
    }
    
    static void update (final FragmentActivity context, final OnAparUpdateListener listener) {
        GlobalLoadingDialog.show (context);
        
        ApiInterfaceBuilder
            .build (context)
            .all ()
            .enqueue (new Callback<List<Apar>> () {
                @Override
                public void onResponse (@NonNull Call<List<Apar>> call, @NonNull Response<List<Apar>> response) {
                    if (response.isSuccessful ()) {
                        HomeActivity.aparList = response.body ();
                        HomeActivity.aparMap  = new SparseArray<> ();
                        for (Apar apar : HomeActivity.aparList)
                            HomeActivity.aparMap.put (apar.id, apar);
                        listener.onUpdate ();
                    }
                    else if (response.code () == 401) {
                        context
                            .getSharedPreferences (SPNAME, Context.MODE_PRIVATE)
                            .edit ()
                            .remove (SPKEY_TOKEN)
                            .apply ();
                        context.startActivity (new Intent (context, LoginActivity.class));
                        context.finish ();
                    }
                    
                    GlobalLoadingDialog.hide ();
                }
            
                @Override
                public void onFailure (@NonNull Call<List<Apar>> call, @NonNull Throwable t) {
                    t.printStackTrace ();
                    HomeActivity.aparList = new ArrayList<> ();
                    HomeActivity.aparMap  = new SparseArray<> ();
                    GlobalLoadingDialog.hide ();
                    Toast.makeText (context, "Loading data failed", Toast.LENGTH_SHORT).show ();
                    listener.onUpdateFailed ();
                }
            });
    }
    
    interface OnAparUpdateListener {
        void onUpdate ();
        void onUpdateFailed ();
    }
}
