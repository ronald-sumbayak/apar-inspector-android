package ra.sumbayak.aparinspector.home;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.*;

import com.google.gson.annotations.SerializedName;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ra.sumbayak.aparinspector.GlobalLoadingDialog;
import ra.sumbayak.aparinspector.R;
import ra.sumbayak.aparinspector.api.Apar;
import ra.sumbayak.aparinspector.api.ApiInterfaceBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ra.sumbayak.aparinspector.Constant.*;

public class InspectionFragment extends DialogFragment {
    
    @BindView (R.id.lokasi) TextView lokasi;
    @BindView (R.id.nomorlokasi) TextView nomorLokasi;
    @BindView (R.id.jenis) TextView jenis;
    @BindView (R.id.kapasitas) TextView kapasitas;
    @BindView (R.id.kondisi) RadioGroup kondisi;
    @BindView (R.id.catatan) TextInputEditText catatan;
    @BindView (R.id.confirm) Button confirm;
    private Apar apar;
    private View view;
    
    public static InspectionFragment newInstance (int id) {
        InspectionFragment fragment = new InspectionFragment ();
        Bundle args = new Bundle ();
        args.putSerializable (INSPECTION_FRAGMENT_DATA_KEY_APAR, HomeActivity.aparMap.get (id));
        fragment.setArguments (args);
        return fragment;
    }
    
    public static void show (FragmentActivity context, int id) {
        InspectionFragment fragment = newInstance (id);
        fragment.show (context.getSupportFragmentManager (), INSPECTION_FRAGMENT_TAG);
    }
    
    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        apar = (Apar) getArguments ().getSerializable (INSPECTION_FRAGMENT_DATA_KEY_APAR);
        setCancelable (false);
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder (getContext ());
        builder.setView (createView ());
        builder.setCancelable (false);
        return builder.create ();
    }
    
    private View createView () {
        view = View.inflate (getContext (), R.layout.fragment_inspection, null);
        ButterKnife.bind (this, view);
        lokasi.setText (apar.lokasi);
        nomorLokasi.setText (apar.nomorLokasi);
        jenis.setText (apar.jenis);
        kapasitas.setText (apar.kapasitas ());
        confirm.setEnabled (false);
        kondisi.setOnCheckedChangeListener (new RadioGroup.OnCheckedChangeListener () {
            @Override
            public void onCheckedChanged (RadioGroup group, @IdRes int checkedId) {
                confirm.setEnabled (true);
            }
        });
        return view;
    }
    
    @OnClick (R.id.cancel)
    public void cancel () {
        dismiss ();
    }
    
    @OnClick (R.id.confirm)
    public void confirm () {
        GlobalLoadingDialog.show (getContext ());
        
        ApiInterfaceBuilder
            .build (getContext ())
            .inspect (apar.id, new InspectionForm ())
            .enqueue (new Callback<Apar> () {
                @Override
                public void onResponse (@NonNull Call<Apar> call, @NonNull Response<Apar> response) {
                    if (response.isSuccessful ()) {
                        AparUpdater.update (getActivity ());
                        GlobalLoadingDialog.hide ();
                        dismiss ();
                    }
                    else {
                        GlobalLoadingDialog.hide ();
                        Toast.makeText (getContext (), "Save failed", Toast.LENGTH_SHORT).show ();
                    }
                }
    
                @Override
                public void onFailure (@NonNull Call<Apar> call, @NonNull Throwable t) {
                    GlobalLoadingDialog.hide ();
                    Toast.makeText (getContext (), "Save failed", Toast.LENGTH_SHORT).show ();
                }
            });
    }
    
    public class InspectionForm {
        
        @SerializedName ("kondisi") int kondisi;
        @SerializedName ("catatan") String catatan;
        
        InspectionForm () {
            String kondisi = ((RadioButton) view
                .findViewById (InspectionFragment.this.kondisi.getCheckedRadioButtonId ()))
                .getText ()
                .toString ();
            
            if (kondisi.equals ("Baik")) this.kondisi = 1;
            else this.kondisi = -1;
            
            catatan = InspectionFragment.this.catatan.getText ().toString ();
        }
    }
}
