package ra.sumbayak.aparinspector;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ProgressBar;

import java.util.List;

import ra.sumbayak.aparinspector.api.Apar;
import ra.sumbayak.aparinspector.api.Inspection;
import ra.sumbayak.aparinspector.login.LoginActivity;

import static ra.sumbayak.aparinspector.Constant.*;

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
            case R.id.refresh:
                showProgressDialog ();
                DataUpdater.update (this, this);
                return true;
            case R.id.logout:
                logout ();
                return true;
            default: return super.onOptionsItemSelected (item);
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
