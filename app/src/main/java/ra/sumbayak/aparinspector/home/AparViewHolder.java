package ra.sumbayak.aparinspector.home;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ericliu.asyncexpandablelist.async.AsyncExpandableListView;
import com.ericliu.asyncexpandablelist.async.AsyncHeaderViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import ra.sumbayak.aparinspector.R;
import ra.sumbayak.aparinspector.api.Apar;

import static ra.sumbayak.aparinspector.R.id.pengecekan;

public class AparViewHolder {
    
    public static class Header extends AsyncHeaderViewHolder {
        
        @BindView (R.id.aparname) TextView title;
    
        public Header (View itemView, int groupOrdinal, AsyncExpandableListView asyncExpandableListView) {
            super (itemView, groupOrdinal, asyncExpandableListView);
            ButterKnife.bind (this, itemView);
        }
        
        public void bind (String title) {
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
    
    public static class Item extends RecyclerView.ViewHolder {
        
        @BindView (R.id.lokasi) TextView lokasi;
        @BindView (R.id.nomorlokasi) TextView nomorlokasi;
        @BindView (R.id.jenis) TextView jenis;
        @BindView (R.id.kapasitas) TextView kapasitas;
        @BindView (R.id.kadaluarsa) TextView kadaluarsa;
        @BindView (R.id.kondisi) TextView kondisi;
        @BindView (R.id.pengecekan) TextView pengecekan;
        @BindView (R.id.inspector) TextView inspector;
        @BindView (R.id.catatan) TextView catatan;
    
        public Item (View itemView) {
            super (itemView);
            ButterKnife.bind (this, itemView);
        }
        
        public void bind (Apar apar) {
            lokasi.setText (apar.lokasi);
            nomorlokasi.setText (apar.nomorLokasi);
            jenis.setText (apar.jenis);
            kapasitas.setText (apar.kapasitas ());
            pengecekan.setText (apar.pengecekan ());
            kondisi.setText (apar.kondisi ());
            kadaluarsa.setText (apar.kadaluarsa ());
            inspector.setText (apar.inspector ());
            catatan.setText (apar.catatan ());
        }
    }
}