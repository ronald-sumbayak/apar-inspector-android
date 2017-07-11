package ra.sumbayak.aparinspector;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class GlobalLoadingDialog {
    
    private static ProgressDialog dialog;
    
    public static void show (Context context) {
        if (dialog == null)
            create (context);
        
        try { dialog.show (); }
        catch (Exception e) {
            create (context);
            dialog.show ();
        }
    
        dialog.setContentView (new ProgressBar (context), new WindowManager.LayoutParams (-2, -2));
        if (dialog.getWindow () != null)
            dialog.getWindow ().setBackgroundDrawable (new ColorDrawable (Color.TRANSPARENT));
    }
    
    public static void hide () {
        if (dialog != null && dialog.isShowing ())
            dialog.dismiss ();
    }
    
    private static void create (Context context) {
        dialog = new ProgressDialog (context);
        dialog.setProgressStyle (ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable (false);
    }
}
