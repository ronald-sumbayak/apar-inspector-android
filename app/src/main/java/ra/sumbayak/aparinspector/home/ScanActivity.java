package ra.sumbayak.aparinspector.home;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static ra.sumbayak.aparinspector.Constant.*;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler, AparUpdater.OnAparUpdateListener {
    
    private ZXingScannerView scannerView;
    private int id;
    
    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        scannerView = new ZXingScannerView (this);
        setContentView (scannerView);
    }
    
    @Override
    public void handleResult (final Result result) {
        try {
            id = Integer.parseInt (result.getText ());
            if (HomeActivity.aparMap.indexOfKey (id) >= 0)
                returnData (id);
            else
                AparUpdater.update (this, this);
        }
        catch (NumberFormatException e) { showInvalidMessage (); }
    }
    
    private void returnData (int id) {
        Intent data = new Intent ();
        data.putExtra (SCAN_DATA_KEY_ID, id);
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
    
    @Override
    protected void onPause () {
        super.onPause ();
        scannerView.stopCamera ();
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
    public void onUpdate () {
        if (HomeActivity.aparMap.indexOfKey (id) >= 0)
            returnData (id);
        else
            showInvalidMessage ();
    }
    
    private void showInvalidMessage () {
        final AlertDialog.Builder builder = new AlertDialog.Builder (this);
        builder.setCancelable (false);
        builder.setMessage ("Kode tidak dikenali");
        builder.setPositiveButton ("OK", new DialogInterface.OnClickListener () {
            @Override
            public void onClick (DialogInterface dialog, int which) {
                dialog.dismiss ();
                scannerView.resumeCameraPreview (ScanActivity.this);
            }
        });
        builder.create ().show ();
    }
}
