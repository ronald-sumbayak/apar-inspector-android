package ra.sumbayak.aparinspector.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentStatePagerItemAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ra.sumbayak.aparinspector.R;

import static ra.sumbayak.aparinspector.Constant.*;

public class ReportFragment extends Fragment {
    
    @BindView (R.id.viewpager) ViewPager viewPager;
    @BindView (R.id.viewpagertab) SmartTabLayout tabLayout;
    private FragmentPagerItems.Creator creator;
    
    private void add (String title) {
        Bundle args = new Bundle ();
        args.putString (REPORT_PAGE_FILTER_TAG, title);
        creator.add (title, AparListPage.class, args);
    }
    
    @OnClick (R.id.scan_apar)
    public void scan () {
        startActivityForResult (new Intent (getContext (), ScanActivity.class), SCAN_REQUEST_CODE);
    }
    
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if (requestCode == SCAN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            final int id = data.getIntExtra (SCAN_DATA_KEY_ID, SCAN_DATA_DEFAULT_ID);
            
            HomeActivity activity = (HomeActivity) getActivity ();
            InspectionFragment.show (activity, id);
            activity.bnv.setSelectedItemId (R.id.bnv_report);
        }
    }
    
    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate (R.layout.fragment_report, container, false);
    }
    
    @Override
    public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);
        ButterKnife.bind (this, view);
        FragmentManager fm = ((AppCompatActivity) getContext ()).getSupportFragmentManager ();
        
        creator = FragmentPagerItems.with (getContext ());
        add (REPORT_PAGE_TITLE_EXPIRED);
        add (REPORT_PAGE_TITLE_BAD);
        add (REPORT_PAGE_TITLE_LT_6_MONTH);
        
        viewPager.setAdapter (new FragmentStatePagerItemAdapter (fm, creator.create ()));
        tabLayout.setViewPager (viewPager);
    }
}
