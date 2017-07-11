package ra.sumbayak.aparinspector.home;

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

import com.ericliu.asyncexpandablelist.CollectionView;
import com.ericliu.asyncexpandablelist.async.AsyncExpandableListView;
import com.ericliu.asyncexpandablelist.async.AsyncExpandableListViewCallbacks;
import com.ericliu.asyncexpandablelist.async.AsyncHeaderViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ra.sumbayak.aparinspector.R;
import ra.sumbayak.aparinspector.api.Apar;

import static ra.sumbayak.aparinspector.Constant.*;

public class AparListPage extends Fragment implements AsyncExpandableListViewCallbacks<String, Apar> {
    
    @BindView (R.id.aelv_aparlist) AsyncExpandableListView<String, Apar> aparListView;
    private List<Apar> aparList;
    private String filter;
    
    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        if (getArguments () != null) 
            filter = getArguments ().getString (REPORT_PAGE_FILTER_TAG);
    }
    
    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate (R.layout.fragment_aparlist, container, false);
    }
    
    @Override
    public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);
        ButterKnife.bind (this, view);
        populateInventory ();
    }
    
    private boolean filter (Apar item) {
        if (filter == null) return true;
        switch (filter) {
            case REPORT_PAGE_TITLE_EXPIRED: return item.isExpired ();
            case REPORT_PAGE_TITLE_BAD: return item.kondisi < 1;
            case REPORT_PAGE_TITLE_LT_6_MONTH: return item.isGT6M ();
            default: return false;
        }
    }
    
    void populateInventory () {
        CollectionView.Inventory<String, Apar> inventory = new CollectionView.Inventory<> ();
        aparList = HomeActivity.aparList;
    
        for (int i = 0; i < aparList.size (); i++)
            if (filter (aparList.get (i)))
                inventory
                    .newGroup (i)
                    .setHeaderItem (aparList.get (i).header ());
        
        aparListView.setHasFixedSize (true);
        aparListView.addItemDecoration (new DividerItemDecoration (getContext (), DividerItemDecoration.VERTICAL));
        aparListView.setLayoutManager (new LinearLayoutManager (getContext ()));
        aparListView.setCallbacks (this);
        aparListView.updateInventory (inventory);
    }
    
    @Override
    public void onStartLoadingGroup (int groupOrdinal) {
        List<Apar> items = new ArrayList<> ();
        items.add (aparList.get (groupOrdinal));
        aparListView.onFinishLoadingGroup (groupOrdinal, items);
    }
    
    @Override
    public AsyncHeaderViewHolder newCollectionHeaderView (Context context, int groupOrdinal, ViewGroup parent) {
        View itemView = LayoutInflater
            .from (context)
            .inflate (R.layout.headerview, parent, false);
        return new AparViewHolder.Header (itemView, groupOrdinal, aparListView);
    }
    
    @Override
    public RecyclerView.ViewHolder newCollectionItemView (Context context, int groupOrdinal, ViewGroup parent) {
        return new AparViewHolder.Item (LayoutInflater
            .from (context)
            .inflate (R.layout.itemview, parent, false));
    }
    
    @Override
    public void bindCollectionHeaderView (Context context, AsyncHeaderViewHolder holder, int groupOrdinal, String headerItem) {
        ((AparViewHolder.Header) holder).bind (headerItem);
    }
    
    @Override
    public void bindCollectionItemView (Context context, RecyclerView.ViewHolder holder, int groupOrdinal, Apar item) {
        ((AparViewHolder.Item) holder).bind (item);
    }
}
