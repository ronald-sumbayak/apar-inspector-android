package ra.sumbayak.aparinspector.home.database;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import ra.sumbayak.aparinspector.home.AparViewHolder;
import ra.sumbayak.aparinspector.api.Apar;
import ra.sumbayak.aparinspector.api.ApiInterfaceBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatabaseFragment extends Fragment implements AsyncExpandableListViewCallbacks<String, Apar> {
    
    private final String LOG_TAG = "Database Fragment ";
    
    @BindView (R.id.aelv_aparlist) AsyncExpandableListView<String, Apar> aparListView;
    private List<Apar> aparList;
    
    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
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
        
        ApiInterfaceBuilder
            .build (getContext ())
            .all ()
            .enqueue (new Callback<List<Apar>> () {
                @Override
                public void onResponse (@NonNull Call<List<Apar>> call, @NonNull Response<List<Apar>> response) {
                    if (response.isSuccessful ())
                        populateInventory (response.body ());
                }
                
                @Override
                public void onFailure (@NonNull Call<List<Apar>> call, @NonNull Throwable t) {
                    t.printStackTrace ();
                }
            });
    }
    
    private void populateInventory (List<Apar> aparList) {
        CollectionView.Inventory<String, Apar> inventory = new CollectionView.Inventory<> ();
        for (int i = 0; i < aparList.size (); i++)
            inventory
                .newGroup (i)
                .setHeaderItem (aparList.get (i).header ());
        
        aparListView.setHasFixedSize (true);
        aparListView.addItemDecoration (new DividerItemDecoration (getContext (), DividerItemDecoration.VERTICAL));
        aparListView.setLayoutManager (new LinearLayoutManager (getContext ()));
        aparListView.setCallbacks (this);
        aparListView.updateInventory (inventory);
        this.aparList = aparList;
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
