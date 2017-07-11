package ra.sumbayak.aparinspector.api;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Apar implements Serializable {
    
    @SerializedName ("id") public Integer id;
    @SerializedName ("lokasi") public String lokasi;
    @SerializedName ("nomor_lokasi") public String nomorLokasi;
    @SerializedName ("kondisi") public Integer kondisi;
    
    @SerializedName ("jenis") public String jenis;
    @SerializedName ("kapasitas") private Integer kapasitas;
    @SerializedName ("kadaluarsa") private String kadaluarsa;
    @SerializedName ("pengecekan") private String pengecekan;
    @SerializedName ("inspector") private String inspector;
    @SerializedName ("catatan") private String catatan;
    
    public String header () {
        return String.format ("%s (%s)", lokasi, nomorLokasi);
    }
    
    public String kapasitas () {
        return String.format ("%s Kg", kapasitas);
    }
    
    public String pengecekan () {
        if (pengecekan != null) {
            SimpleDateFormat sdf = new SimpleDateFormat ("y-M-d", Locale.getDefault ());
            try {
                Date date = sdf.parse (pengecekan);
                sdf = new SimpleDateFormat ("d MMM y", Locale.getDefault ());
                return sdf.format (date);
            }
            catch (ParseException e) { e.printStackTrace (); }
        }
        return "-";
    }
    
    public String kadaluarsa () {
        SimpleDateFormat sdf = new SimpleDateFormat ("y-M-d", Locale.getDefault ());
        try {
            Date date = sdf.parse (kadaluarsa);
            sdf = new SimpleDateFormat ("d MMM y", Locale.getDefault ());
            return sdf.format (date);
        }
        catch (ParseException e) { e.printStackTrace (); }
        return "-";
    }
    
    public boolean isExpired () {
        SimpleDateFormat sdf = new SimpleDateFormat ("y-M-d", Locale.getDefault ());
        try {
            Date date = sdf.parse (kadaluarsa);
            Calendar today = Calendar.getInstance ();
            return date.before (today.getTime ());
        }
        catch (ParseException e) { e.printStackTrace (); }
        return false;
    }
    
    public boolean isGT6M () {
        if (pengecekan != null) {
            SimpleDateFormat sdf = new SimpleDateFormat ("y-M-d", Locale.getDefault ());
            try {
                Date date = sdf.parse (pengecekan);
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
    
    public String kondisi () {
        switch (kondisi) {
            case -1: return "Tidak Baik";
            case 0: return "-";
            case 1: return "Baik";
            default: return "-";
        }
    }
    
    public String inspector () {
        if (inspector == null) return "-";
        return inspector;
    }
    
    public String catatan () {
        if (catatan == null) return "-";
        return catatan;
    }
}
