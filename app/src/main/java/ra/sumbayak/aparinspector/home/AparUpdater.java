package ra.sumbayak.aparinspector.home;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.SparseArray;

import java.util.List;

import ra.sumbayak.aparinspector.R;
import ra.sumbayak.aparinspector.api.Apar;
import ra.sumbayak.aparinspector.api.ApiInterfaceBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ra.sumbayak.aparinspector.home.HomeActivity.aparList;
import static ra.sumbayak.aparinspector.home.HomeActivity.aparMap;

class AparUpdater {
    
    static void update (final FragmentActivity context) {
        Log.e ("AparUpdater", "update");
        ApiInterfaceBuilder
            .build (context)
            .all ()
            .enqueue (new Callback<List<Apar>> () {
                @Override
                public void onResponse (@NonNull Call<List<Apar>> call, @NonNull Response<List<Apar>> response) {
                    if (response.isSuccessful ()) {
                        HomeActivity.aparList = response.body ();
                        HomeActivity.aparMap = new SparseArray<> ();
                        for (Apar apar : aparList)
                            aparMap.put (apar.id, apar);
                    }
                }
                
                @Override
                public void onFailure (@NonNull Call<List<Apar>> call, @NonNull Throwable t) {
                    
                }
            });
    }
    
    static void update (final FragmentActivity context, final OnAparUpdateListener listener) {
        Log.e ("AparUpdater", "update");
        ApiInterfaceBuilder
            .build (context)
            .all ()
            .enqueue (new Callback<List<Apar>> () {
                @Override
                public void onResponse (@NonNull Call<List<Apar>> call, @NonNull Response<List<Apar>> response) {
                    Log.e ("AparUpdater", String.valueOf (response.code ()));
                    if (response.isSuccessful ()) {
                        HomeActivity.aparList = response.body ();
                        HomeActivity.aparMap = new SparseArray<> ();
                        for (Apar apar : aparList)
                            aparMap.put (apar.id, apar);
                        listener.onUpdate ();
                    }
                }
            
                @Override
                public void onFailure (@NonNull Call<List<Apar>> call, @NonNull Throwable t) {
                    t.printStackTrace ();
                }
            });
    }
    
    interface OnAparUpdateListener {
        void onUpdate ();
    }
}
