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
    @SerializedName ("jenis") public String jenis;
    @SerializedName ("kapasitas") private Integer kapasitas;
    @SerializedName ("kadaluarsa") private String kadaluarsa;
    
    @SerializedName ("inspeksi") public int inspeksi;
    public Inspection inspection = new Inspection ();
    
    public String header () {
        return String.format ("%s (%s)", lokasi, nomorLokasi);
    }
    
    public String kapasitas () {
        return String.format ("%s Kg", kapasitas);
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
}
