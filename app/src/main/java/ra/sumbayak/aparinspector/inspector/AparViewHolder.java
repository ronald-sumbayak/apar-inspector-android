package ra.sumbayak.aparinspector.inspector;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ericliu.asyncexpandablelist.async.AsyncExpandableListView;
import com.ericliu.asyncexpandablelist.async.AsyncHeaderViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import ra.sumbayak.aparinspector.R;
import ra.sumbayak.aparinspector.api.Apar;

class AparViewHolder {
    
    static class Header extends AsyncHeaderViewHolder {
        
        @BindView (R.id.aparname) TextView title;
    
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
        
        @BindView (R.id.inspection_data) LinearLayout inspectiondata;
        @BindView (R.id.pengecekan) TextView pengecekan;
        @BindView (R.id.inspector) TextView inspector;
        @BindView (R.id.catatan) TextView catatan;
    
        Item (View itemView) {
            super (itemView);
            ButterKnife.bind (this, itemView);
        }
        
        void bind (Apar apar) {
            lokasi.setText (apar.lokasi);
            nomorlokasi.setText (apar.nomorLokasi);
            jenis.setText (apar.jenis);
            kapasitas.setText (apar.kapasitas ());
            kadaluarsa.setText (apar.kadaluarsa ());
            
            if (apar.inspeksi > 0) {
                inspectiondata.setVisibility (View.VISIBLE);
                kondisi.setText (apar.inspection.kondisi ());
                pengecekan.setText (apar.inspection.waktu_inspeksi ());
                inspector.setText (apar.inspection.inspector ());
                catatan.setText (apar.inspection.catatan ());
            }
            else {
                inspectiondata.setVisibility (View.GONE);
                kondisi.setText ("Unknown");
            }
        }
    }
}