package ra.sumbayak.aparinspector.supervisor;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ericliu.asyncexpandablelist.CollectionView;
import com.ericliu.asyncexpandablelist.async.AsyncExpandableListView;
import com.ericliu.asyncexpandablelist.async.AsyncExpandableListViewCallbacks;
import com.ericliu.asyncexpandablelist.async.AsyncHeaderViewHolder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ra.sumbayak.aparinspector.BaseActivity;
import ra.sumbayak.aparinspector.R;
import ra.sumbayak.aparinspector.api.Inspection;

public class InspectionListPage extends Fragment implements AsyncExpandableListViewCallbacks<String, Inspection>, InspectionViewHolder.Item.OnVerifyResponse {
    
    @BindView (R.id.aelv_list) AsyncExpandableListView<String, Inspection> inspectionListView;
    private List<Inspection> inspectionList;
    
    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate (R.layout.fragment_aelv, container, false);
    }
    
    @Override
    public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);
        ButterKnife.bind (this, view);
        populateInventory ();
    }
    
    void populateInventory () {
        CollectionView.Inventory<String, Inspection> inventory = new CollectionView.Inventory<> ();
        inspectionList = BaseActivity.inspectionList;
        
        for (int i = 0; i < inspectionList.size (); i++)
            if (inspectionList.get (i).verification == 0)
                inventory
                    .newGroup (i)
                    .setHeaderItem (inspectionList.get (i).header ());
        
        inspectionListView.setHasFixedSize (true);
        inspectionListView.addItemDecoration (new DividerItemDecoration (getContext (), DividerItemDecoration.VERTICAL));
        inspectionListView.setLayoutManager (new LinearLayoutManager (getContext ()));
        inspectionListView.setCallbacks (this);
        inspectionListView.updateInventory (inventory);
    }
    
    @Override
    public void onStartLoadingGroup (int groupOrdinal) {
        List<Inspection> items = new ArrayList<> ();
        items.add (inspectionList.get (groupOrdinal));
        inspectionListView.onFinishLoadingGroup (groupOrdinal, items);
    }
    
    @Override
    public AsyncHeaderViewHolder newCollectionHeaderView (Context context, int groupOrdinal, ViewGroup parent) {
        View itemView = LayoutInflater
            .from (context)
            .inflate (R.layout.headerview_inspection, parent, false);
        return new InspectionViewHolder.Header (itemView, groupOrdinal, inspectionListView);
    }
    
    @Override
    public RecyclerView.ViewHolder newCollectionItemView (Context context, int groupOrdinal, ViewGroup parent) {
        return new InspectionViewHolder.Item (getActivity (), LayoutInflater
            .from (context)
            .inflate (R.layout.itemview_inspection, parent, false), this);
    }
    
    @Override
    public void bindCollectionHeaderView (Context context, AsyncHeaderViewHolder holder, int groupOrdinal, String headerItem) {
        ((InspectionViewHolder.Header) holder).bind (headerItem);
    }
    
    @Override
    public void bindCollectionItemView (Context context, RecyclerView.ViewHolder holder, int groupOrdinal, Inspection item) {
        ((InspectionViewHolder.Item) holder).bind (item);
    }
    
    @Override
    public void onBegin () {
        ((BaseActivity) getActivity ()).showProgressDialog ();
    }
    
    @Override
    public void onSuccessful () {
        ((BaseActivity) getActivity ()).onUpdate ();
        Toast.makeText (getActivity (), "Success", Toast.LENGTH_SHORT).show ();
    }
    
    @Override
    public void onFailure () {
        ((BaseActivity) getActivity ()).onUpdateFailed ();
        Toast.makeText (getActivity (), "Failed. Please try again.", Toast.LENGTH_SHORT).show ();
    }
    
    public static class VerificationForm {
        @SerializedName ("id") int id;
        @SerializedName ("status") int status;
        
        VerificationForm (int id, int status) {
            this.id = id;
            this.status = status;
        }
    }
}
