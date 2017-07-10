package ra.sumbayak.aparinspector.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import ra.sumbayak.aparinspector.api.Apar;
import ra.sumbayak.aparinspector.api.ApiInterfaceBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ra.sumbayak.aparinspector.Constant.*;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    
    private ZXingScannerView scannerView;
    
    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        scannerView = new ZXingScannerView (this);
        setContentView (scannerView);
    }
    
    @Override
    protected void onResume () {
        super.onResume ();
        scannerView.setResultHandler (this);
        
        int permission = ContextCompat.checkSelfPermission (this, Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions (
                this,
                new String[]{ Manifest.permission.CAMERA},
                CAMERA_PERMISSION_REQUEST_CODE);
        scannerView.startCamera ();
    }
    
    @Override
    protected void onPause () {
        super.onPause ();
        scannerView.stopCamera ();
    }
    
    @Override
    public void handleResult (final Result result) {
        final int id = Integer.parseInt (result.getText ());
        if (HomeActivity.aparMap.indexOfKey (id) >= 0)
            returnData (id, true);
        else
            ApiInterfaceBuilder
                .build (this)
                .get (Integer.parseInt (result.getText ()))
                .enqueue (new Callback<Apar> () {
                    @Override
                    public void onResponse (@NonNull Call<Apar> call, @NonNull Response<Apar> response) {
                        if (response.isSuccessful ())
                            returnData (id, false);
                    }
        
                    @Override
                    public void onFailure (@NonNull Call<Apar> call, @NonNull Throwable t) {
                        
                    }
                });
    }
    
    private void returnData (int id, boolean offline) {
        Intent data = new Intent ();
        data.putExtra (SCAN_DATA_KEY_ID, id);
        data.putExtra (SCAN_DATA_KEY_OFFLINE, offline);
        setResult (RESULT_OK, data);
        finish ();
    }
    
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            int permission = ContextCompat.checkSelfPermission (this, Manifest.permission.CAMERA);
            if (permission == PackageManager.PERMISSION_GRANTED)
                scannerView.startCamera ();
        }
    }
}
