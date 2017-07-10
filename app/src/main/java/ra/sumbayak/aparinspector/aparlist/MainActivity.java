package ra.sumbayak.aparinspector.aparlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ericliu.asyncexpandablelist.CollectionView;
import com.ericliu.asyncexpandablelist.async.AsyncExpandableListView;
import com.ericliu.asyncexpandablelist.async.AsyncExpandableListViewCallbacks;
import com.ericliu.asyncexpandablelist.async.AsyncHeaderViewHolder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ra.sumbayak.aparinspector.R;
import ra.sumbayak.aparinspector.api.Apar;
import ra.sumbayak.aparinspector.api.ApiInterface;
import ra.sumbayak.aparinspector.home.AparViewHolder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements AsyncExpandableListViewCallbacks<String, Apar> {
    
    @BindView (R.id.aparlist) AsyncExpandableListView<String, Apar> aparListView;
    CollectionView.Inventory<String, Apar> aparInventory;
    
    //    @BindView (R.id.aparlist) RecyclerView aparlistView;
    private List<Apar> aparList;
    private Retrofit retrofit;
    private String token;
    
    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        ButterKnife.bind (this);
        
        token = "2d9309d060c89526ecee0467d8953242fa413c69";
        setupRetrofit ();
        getAparList ();
    }
    
    private void loadToken () {
        SharedPreferences spref = getSharedPreferences ("aparinspector", MODE_PRIVATE);
        token = spref.getString ("token", "defValue");
    }
    
    private void setupRetrofit () {
        Gson gson = new GsonBuilder ()
            .setDateFormat ("yyyy-MM-dd'T'HH:mm:ssZ")
            .create ();
    
        OkHttpClient client = new OkHttpClient.Builder ()
            .addInterceptor (new Interceptor () {
                @Override
                public Response intercept (@NonNull Chain chain) throws IOException {
                    Request request = chain.request ()
                        .newBuilder ()
                        .addHeader ("Authorization", "Token " + token)
                        .build ();
                    return chain.proceed (request);
                }
            })
            .build ();
    
        retrofit = new Retrofit.Builder ()
            .baseUrl ("http://192.168.43.84/apar/")
            .addConverterFactory (GsonConverterFactory.create (gson))
            .client (client)
            .build ();
    }
    
    private void getAparList () {
        ApiInterface apiInterface = retrofit.create (ApiInterface.class);
        Call<List<Apar>> call = apiInterface.all ();
        call.enqueue (new Callback<List<Apar>> () {
            @Override
            public void onResponse (@NonNull Call<List<Apar>> call, @NonNull retrofit2.Response<List<Apar>> response) {
                aparList = response.body ();
                populateInventory ();
            }
    
            @Override
            public void onFailure (@NonNull Call<List<Apar>> call, @NonNull Throwable t) {
                
            }
        });
    }
    
    private void populateInventory () {
        aparInventory = new CollectionView.Inventory<> ();
        
        for (int i = 0; i < aparList.size (); i++) {
            Apar apar = aparList.get (i);
            CollectionView.InventoryGroup<String, Apar> group = aparInventory.newGroup (i);
            group.setHeaderItem (String.format ("%s (%s)", apar.lokasi, apar.nomorLokasi));
        }
        
        aparListView.setHasFixedSize (true);
        aparListView.addItemDecoration (new DividerItemDecoration (this, DividerItemDecoration.VERTICAL));
        aparListView.setCallbacks (this);
        aparListView.updateInventory (aparInventory);
    }
    
    @Override
    public void onStartLoadingGroup (int groupOrdinal) {
        List<Apar> items = new ArrayList<> ();
        items.add (aparList.get (groupOrdinal));
        aparListView.onFinishLoadingGroup (groupOrdinal, items);
    }
    
    @Override
    public AsyncHeaderViewHolder newCollectionHeaderView (Context context, int groupOrdinal, ViewGroup parent) {
        View itemView = LayoutInflater.from (context).inflate (R.layout.headerview, parent, false);
        return new AparViewHolder.Header (itemView, groupOrdinal, aparListView);
    }
    
    @Override
    public RecyclerView.ViewHolder newCollectionItemView (Context context, int groupOrdinal, ViewGroup parent) {
        return new AparViewHolder.Item (LayoutInflater.from (context).inflate (R.layout.itemview, parent, false));
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
