package rot.user.tekno.com.rothrow.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by bukalapak on 1/7/18.
 */

public class Screen {
    public static int getWidth(Context ctx) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)ctx).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return width;
    }
    public  static int getHeight(Context ctx) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)ctx).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        return height;
    }
}
