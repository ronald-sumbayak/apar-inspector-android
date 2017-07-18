package ra.sumbayak.aparinspector.inspector;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import ra.sumbayak.aparinspector.BaseActivity;
import ra.sumbayak.aparinspector.DataUpdater;
import ra.sumbayak.aparinspector.R;

public class InspectorActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    
    @BindView (R.id.bottom_navigation) BottomNavigationView bnv;
    @BindView (R.id.toolbar) Toolbar toolbar;
    private int bnvIndex;
    
    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_inspection);
        ButterKnife.bind (this);
        setupToolbar ();
        bnv.setOnNavigationItemSelectedListener (this);
        bnvIndex = 0;
        showProgressDialog ();
        DataUpdater.update (this, this);
    }
    
    private void setupToolbar () {
        setSupportActionBar (toolbar);
        assert getSupportActionBar () != null;
        getSupportActionBar ().setDisplayShowTitleEnabled (false);
    }
    
    @Override
    public boolean onNavigationItemSelected (@NonNull MenuItem item) {
        switch (item.getItemId ()) {
            case R.id.bnv_database:
                bnvIndex = 0;
                replaceFragment (new AparListPage ());
                return true;
            case R.id.bnv_report:
                bnvIndex = 1;
                replaceFragment (new ReportFragment ());
                return true;
            default: return false;
        }
    }
    
    @Override
    public void onUpdate () {
        if (bnvIndex == 0) replaceFragment (new AparListPage ());
        else replaceFragment (new ReportFragment ());
        dismissProgressDialog ();
    }
    
    @Override
    public void onUpdateFailed () {
        dismissProgressDialog ();
    }
}
