package ra.sumbayak.aparinspector.supervisor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ericliu.asyncexpandablelist.async.AsyncExpandableListView;
import com.ericliu.asyncexpandablelist.async.AsyncHeaderViewHolder;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ra.sumbayak.aparinspector.BaseActivity;
import ra.sumbayak.aparinspector.R;
import ra.sumbayak.aparinspector.api.Apar;
import ra.sumbayak.aparinspector.api.ApiInterfaceBuilder;
import ra.sumbayak.aparinspector.api.Inspection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class InspectionViewHolder {
    
    static class Header extends AsyncHeaderViewHolder {
        
        @BindView (R.id.aparname)
        TextView title;
        
        Header (View itemView, int groupOrdinal, AsyncExpandableListView asyncExpandableListView) {
            super (itemView, groupOrdinal, asyncExpandableListView);
            ButterKnife.bind (this, itemView);
        }
        
        void bind (String title) {
            this.title.setText (title);
        }
        
        @Override
        public void onGroupStartExpending () {
            
        }
        
        @Override
        public void onGroupExpanded () {
            
        }
        
        @Override
        public void onGroupCollapsed () {
            
        }
    }
    
    static class Item extends RecyclerView.ViewHolder {
        
        @BindView (R.id.lokasi) TextView lokasi;
        @BindView (R.id.nomorlokasi) TextView nomorlokasi;
        @BindView (R.id.jenis) TextView jenis;
        @BindView (R.id.kapasitas) TextView kapasitas;
        @BindView (R.id.kadaluarsa) TextView kadaluarsa;
        @BindView (R.id.kondisi) TextView kondisi;
        @BindView (R.id.pengecekan) TextView pengecekan;
        @BindView (R.id.inspector) TextView inspector;
        @BindView (R.id.catatan) TextView catatan;
        private Context context;
        private int id;
        private OnVerifyResponse listener;
        
        Item (Context context, View itemView, OnVerifyResponse listener) {
            super (itemView);
            ButterKnife.bind (this, itemView);
            this.context = context;
            this.listener = listener;
        }
        
        void bind (Inspection inspection) {
            Apar apar = BaseActivity.aparMap.get (inspection.idApar);
            id = inspection.id;
            lokasi.setText (apar.lokasi);
            nomorlokasi.setText (apar.nomorLokasi);
            jenis.setText (apar.jenis);
            kapasitas.setText (apar.kapasitas ());
            kadaluarsa.setText (apar.kadaluarsa ());
            pengecekan.setText (inspection.waktu_inspeksi ());
            kondisi.setText (inspection.kondisi ());
            inspector.setText (inspection.inspector ());
            catatan.setText (inspection.catatan ());
        }
        
        @OnClick (R.id.delete)
        public void delete () {
            listener.onBegin ();
            
            ApiInterfaceBuilder
                .build (context)
                .verify (new InspectionListPage.VerificationForm (id, 0))
                .enqueue (new Callback<JSONObject> () {
                    @Override
                    public void onResponse (@NonNull Call<JSONObject> call, @NonNull Response<JSONObject> response) {
                        if (response.isSuccessful ()) listener.onSuccessful ();
                        else listener.onFailure ();
                    }
    
                    @Override
                    public void onFailure (@NonNull Call<JSONObject> call, @NonNull Throwable t) {
                        listener.onFailure ();
                    }
                });
        }
        
        @OnClick (R.id.confirm)
        public void confirm () {
            listener.onBegin ();
            
            ApiInterfaceBuilder
                .build (context)
                .verify (new InspectionListPage.VerificationForm (id, 1))
                .enqueue (new Callback<JSONObject> () {
                    @Override
                    public void onResponse (@NonNull Call<JSONObject> call, @NonNull Response<JSONObject> response) {
                        if (response.isSuccessful ()) listener.onSuccessful ();
                        else listener.onFailure ();
                    }
    
                    @Override
                    public void onFailure (@NonNull Call<JSONObject> call, @NonNull Throwable t) {
                        listener.onFailure ();
                    }
                });
        }
        
        interface OnVerifyResponse {
            void onBegin ();
            void onSuccessful ();
            void onFailure ();
        }
    }
}
