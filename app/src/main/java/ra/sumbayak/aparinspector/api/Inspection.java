package ra.sumbayak.aparinspector.api;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ra.sumbayak.aparinspector.BaseActivity;

public class Inspection {
    
    @SerializedName ("id") public int id;
    @SerializedName ("inspector") private String inspector;
    @SerializedName ("kondisi") public int kondisi = -1;
    @SerializedName ("catatan") private String catatan;
    @SerializedName ("waktu_inspeksi") private String waktu_inspeksi;
    @SerializedName ("aparid") public int idApar;
    @SerializedName ("verification") public Integer verification;
    
    public String header () {
        Apar apar = BaseActivity.aparMap.get (idApar);
        return String.format ("%s (%s)", apar.lokasi, apar.nomorLokasi);
    }
    
    public String kondisi () {
        if (kondisi == 1) return "Baik";
        if (kondisi == -1) return "-";
        return "Tidak Baik";
    }
    
    public String catatan () {
        if (catatan == null) return "-";
        return catatan;
    }
    
    public String inspector () {
        if (inspector == null) return "-";
        return inspector;
    }
    
    public String waktu_inspeksi () {
        if (waktu_inspeksi != null) {
            SimpleDateFormat sdf = new SimpleDateFormat ("y-M-d", Locale.getDefault ());
            try {
                Date date = sdf.parse (waktu_inspeksi);
                sdf = new SimpleDateFormat ("d MMM y", Locale.getDefault ());
                return sdf.format (date);
            }
            catch (ParseException e) { e.printStackTrace (); }
        }
        return "-";
    }
    
    public boolean isGT6M () {
        if (waktu_inspeksi != null) {
            SimpleDateFormat sdf = new SimpleDateFormat ("y-M-d", Locale.getDefault ());
            try {
                Date date = sdf.parse (waktu_inspeksi);
                Calendar pengecekan = Calendar.getInstance ();
                pengecekan.setTime (date);
                Calendar today = Calendar.getInstance ();
                
                int yearDiff = today.get (Calendar.YEAR) - pengecekan.get (Calendar.YEAR);
                int monthDiff = (yearDiff * 12) + today.get (Calendar.MONTH) - pengecekan.get (Calendar.MONTH);
                return monthDiff > 6;
            }
            catch (ParseException e) { e.printStackTrace (); }
        }
        return true;
    }
}
