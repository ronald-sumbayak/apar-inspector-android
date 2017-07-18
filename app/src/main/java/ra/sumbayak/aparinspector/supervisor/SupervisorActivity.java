package ra.sumbayak.aparinspector.supervisor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ra.sumbayak.aparinspector.BaseActivity;
import ra.sumbayak.aparinspector.DataUpdater;
import ra.sumbayak.aparinspector.R;

public class SupervisorActivity extends BaseActivity {
    
    @BindView (R.id.toolbar) Toolbar toolbar;
    
    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_supervisor);
        ButterKnife.bind (this);
        setupToolbar ();
        showProgressDialog ();
        DataUpdater.update (this, this);
    }
    
    private void setupToolbar () {
        setSupportActionBar (toolbar);
        assert getSupportActionBar () != null;
        getSupportActionBar ().setDisplayShowTitleEnabled (false);
    }
    
    @Override
    public void onUpdate () {
        replaceFragment (new InspectionListPage ());
        dismissProgressDialog ();
    }
    
    @Override
    public void onUpdateFailed () {
        dismissProgressDialog ();
    }
}
