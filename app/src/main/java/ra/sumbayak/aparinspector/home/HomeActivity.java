package ra.sumbayak.aparinspector.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ra.sumbayak.aparinspector.R;
import ra.sumbayak.aparinspector.api.Apar;
import ra.sumbayak.aparinspector.api.ApiInterface;
import ra.sumbayak.aparinspector.api.ApiInterfaceBuilder;
import ra.sumbayak.aparinspector.home.database.DatabaseFragment;
import ra.sumbayak.aparinspector.home.report.ReportFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, AparUpdater.OnAparUpdateListener {
    
    @BindView (R.id.bottom_navigation) BottomNavigationView bnv;
    public static List<Apar> aparList;
    public static SparseArray<Apar> aparMap;
    private final String LOG_TAG = "HomeActivity ";
    
    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_home);
        ButterKnife.bind (this);
        
        bnv.setOnNavigationItemSelectedListener (this);
        AparUpdater.update (this, this);
    }
    
    @Override
    public boolean onNavigationItemSelected (@NonNull MenuItem item) {
        Fragment fragment;
        
        switch (item.getItemId ()) {
            case R.id.bnv_database: fragment = new DatabaseFragment (); break;
            case R.id.bnv_report: fragment = new ReportFragment (); break;
            default: return false;
        }
    
        getSupportFragmentManager ()
            .beginTransaction ()
            .replace (R.id.fragment, fragment)
            .commit ();
        return true;
    }
    
    @Override
    public void onUpdate () {
        getSupportFragmentManager ()
            .beginTransaction ()
            .add (R.id.fragment, new AparListPage ())
            .commit ();
    }
}
