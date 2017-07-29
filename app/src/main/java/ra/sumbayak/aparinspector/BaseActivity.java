package ra.sumbayak.aparinspector;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.*;
import java.util.List;

import okhttp3.ResponseBody;
import ra.sumbayak.aparinspector.api.Apar;
import ra.sumbayak.aparinspector.api.ApiInterfaceBuilder;
import ra.sumbayak.aparinspector.api.Inspection;
import ra.sumbayak.aparinspector.login.LoginActivity;

import static ra.sumbayak.aparinspector.Constant.*;
import static ra.sumbayak.aparinspector.R.id.export_to_excel;

public abstract class BaseActivity extends AppCompatActivity implements DataUpdater.OnDataUpdateListener {
    
    public static List<Apar> aparList;
    public static SparseArray<Apar> aparMap;
    public static List<Inspection> inspectionList;
    private ProgressDialog dialog;
    
    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        initProgressDialog ();
    }
    
    public void showProgressDialog () {
        dialog.show ();
        dialog.setContentView (new ProgressBar (this), new WindowManager.LayoutParams (-2, -2));
        if (dialog.getWindow () != null)
            dialog.getWindow ().setBackgroundDrawable (new ColorDrawable (Color.TRANSPARENT));
    }
    
    public void dismissProgressDialog () {
        dialog.dismiss ();
    }
    
    private void initProgressDialog () {
        dialog = new ProgressDialog (this);
        dialog.setProgressStyle (ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable (false);
    }
    
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater ().inflate (R.menu.toolbar, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId ()) {
            case R.id.refresh: refresh (); return true;
            case R.id.logout: logout (); return true;
            case export_to_excel: exportToExcel (); return true;
            default: return super.onOptionsItemSelected (item);
        }
    }
    
    private void refresh () {
        showProgressDialog ();
        DataUpdater.update (this, this);
    }
    
    private void exportToExcel () {
        showProgressDialog ();
        
        ApiInterfaceBuilder
            .build (this)
            .export ()
            .enqueue (new retrofit2.Callback<ResponseBody> () {
                @Override
                public void onResponse (@NonNull retrofit2.Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                    if (response.isSuccessful ()) {
                        ResponseBody body = response.body ();
                        assert body != null;
                        File file = saveToFile (body.byteStream ());
                        
                        Uri uri = FileProvider.getUriForFile (BaseActivity.this, BuildConfig.APPLICATION_ID, file);
                        String mime = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                        
                        Intent intent = new Intent ();
                        intent.setAction (Intent.ACTION_VIEW);
                        intent.setDataAndType (uri, mime);
                        intent.addFlags (Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.addFlags (Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        
                        if (intent.resolveActivity (getPackageManager ()) != null)
                            startActivity (Intent.createChooser (intent, "Open Export File"));
                        else
                            Toast.makeText (BaseActivity.this, "Failed to open file", Toast.LENGTH_SHORT).show ();
                        dismissProgressDialog ();
                    }
                }
    
                @Override
                public void onFailure (@NonNull retrofit2.Call<ResponseBody> call, @NonNull Throwable t) {
                    t.printStackTrace ();
                    Toast.makeText (BaseActivity.this, "Failed to download file", Toast.LENGTH_SHORT).show ();
                    dismissProgressDialog ();
                }
            });
    }
    
    private File saveToFile (InputStream inputStream) {
        try {
            String path = getFilesDir () + "/export";
            
            File dir = new File (path);
            if (!dir.exists () && ! dir.mkdir ())
                return null;
            
            File file = new File (path + "/aparinspector-export.xlsx");
            OutputStream outputStream = new FileOutputStream (file);
            
            byte[] bytes = new byte[1024];
            int read;
            
            while ((read = inputStream.read(bytes)) != -1)
                outputStream.write (bytes, 0, read);
            
            inputStream.close ();
            outputStream.close ();
            return file;
        }
        catch (IOException e) {
            e.printStackTrace ();
            return null;
        }
    }
    
    private void logout () {
        getSharedPreferences (SPNAME, MODE_PRIVATE)
            .edit ()
            .remove (SPKEY_TOKEN)
            .remove (SPKEY_ACCESS_LEVEL)
            .apply ();
        startActivity (new Intent (this, LoginActivity.class));
        finish ();
    }
    
    protected void replaceFragment (Fragment fragment) {
        getSupportFragmentManager ()
            .beginTransaction ()
            .replace (R.id.fragment, fragment)
            .commit ();
    }
}
