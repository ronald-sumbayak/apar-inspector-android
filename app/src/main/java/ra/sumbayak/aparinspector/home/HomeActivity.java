package ra.sumbayak.aparinspector.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ra.sumbayak.aparinspector.R;
import ra.sumbayak.aparinspector.api.Apar;
import ra.sumbayak.aparinspector.login.LoginActivity;

import static ra.sumbayak.aparinspector.Constant.SPKEY_TOKEN;
import static ra.sumbayak.aparinspector.Constant.SPNAME;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, AparUpdater.OnAparUpdateListener {
    
    public static List<Apar> aparList;
    public static SparseArray<Apar> aparMap;
    @BindView (R.id.bottom_navigation) BottomNavigationView bnv;
    @BindView (R.id.toolbar) Toolbar toolbar;
    private int bnvIndex;
    
    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_home);
        ButterKnife.bind (this);
        setupToolbar ();
        bnv.setOnNavigationItemSelectedListener (this);
        AparUpdater.update (this, this);
    }
    
    private void setupToolbar () {
        setSupportActionBar (toolbar);
        assert getSupportActionBar () != null;
        getSupportActionBar ().setDisplayShowTitleEnabled (false);
    }
    
    @Override
    public boolean onNavigationItemSelected (@NonNull MenuItem item) {
        switch (item.getItemId ()) {
            case R.id.bnv_database: bnvIndex = 0; replaceFragment (new AparListPage ()); return true;
            case R.id.bnv_report: bnvIndex = 1; replaceFragment (new ReportFragment ()); return true;
            default: return false;
        }
    }
    
    @Override
    public void onUpdate () {
        replaceFragment (new AparListPage ());
    }
    
    @Override
    public void onUpdateFailed () {
        
    }
    
    private void replaceFragment (Fragment fragment) {
        getSupportFragmentManager ()
            .beginTransaction ()
            .replace (R.id.fragment, fragment)
            .commit ();
    }
    
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater ().inflate (R.menu.toolbar, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId ()) {
            case R.id.refresh: AparUpdater.update (this, new AparUpdater.OnAparUpdateListener () {
                @Override
                public void onUpdate () {
                    Fragment fragment = bnvIndex == 0 ? new AparListPage () : new ReportFragment ();
                    replaceFragment (fragment);
                }
    
                @Override
                public void onUpdateFailed () {
        
                }
            }); return true;
            case R.id.logout: logout (); return true;
            default: return super.onOptionsItemSelected (item);
        }
    }
    
    private void logout () {
        getSharedPreferences (SPNAME, MODE_PRIVATE)
            .edit ()
            .remove (SPKEY_TOKEN)
            .apply ();
        startActivity (new Intent (this, LoginActivity.class));
        finish ();
    }
}
