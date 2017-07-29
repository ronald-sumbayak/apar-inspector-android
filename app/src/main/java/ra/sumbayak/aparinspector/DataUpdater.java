package ra.sumbayak.aparinspector;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.SparseArray;

import java.util.List;

import ra.sumbayak.aparinspector.api.Apar;
import ra.sumbayak.aparinspector.api.ApiInterfaceBuilder;
import ra.sumbayak.aparinspector.api.Inspection;
import ra.sumbayak.aparinspector.api.QRCallback;
import retrofit2.Response;

public class DataUpdater {
    
    public static void update (final FragmentActivity context, final OnDataUpdateListener listener) {
        ApiInterfaceBuilder
            .build (context)
            .aparAll ()
            .enqueue (new QRCallback<List<Apar>> () {
                @Override
                public void onSuccessful (@NonNull Response<List<Apar>> response) {
                    final List<Apar> aparList = response.body ();
                
                    ApiInterfaceBuilder
                        .build (context)
                        .inspectionAll ()
                        .enqueue (new QRCallback<List<Inspection>> () {
                            @Override
                            public void onSuccessful (@NonNull Response<List<Inspection>> response) {
                                final List<Inspection> inspectionList = response.body ();
                                SparseArray<Apar> aparMap  = new SparseArray<> ();
                                SparseArray<Inspection> inspectionMap = new SparseArray<> ();
                                assert inspectionList != null;
                                assert aparList != null;
                                
                                for (Inspection inspection : inspectionList)
                                    inspectionMap.put (inspection.id, inspection);

                                for (Apar apar : aparList) {
                                    if (apar.inspeksi > 0)
                                        apar.inspection = inspectionMap.get (apar.inspeksi);
                                    aparMap.put (apar.id, apar);
                                }

                                BaseActivity.aparList = aparList;
                                BaseActivity.aparMap = aparMap;
                                BaseActivity.inspectionList = inspectionList;
                                listener.onUpdate ();
                            }
    
                            @Override
                            protected void onFailure () {
                                listener.onUpdateFailed ();
                            }
                        });
                }
    
                @Override
                protected void onFailure () {
                    listener.onUpdateFailed ();
                }
            });
    }
    
    public interface OnDataUpdateListener {
        void onUpdate ();
        void onUpdateFailed ();
    }
}
