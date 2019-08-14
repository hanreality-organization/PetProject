package com.punuo.pet.cirlce.publish.util;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.punuo.pet.cirlce.R;
import com.punuo.sys.sdk.PnApplication;
import com.punuo.sys.sdk.util.StatusBarUtil;

/**
 * Created by Kuiya on 2019/8/8.
 */

public class CommonUtil {
    public static int getWidth() {
        DisplayMetrics dm = PnApplication.getInstance().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getHeight() {
        DisplayMetrics dm = PnApplication.getInstance().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static int dip2px(float dpValue) {
        final float scale = PnApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static void hideKeyboard(Activity act) {
        if (act != null && act.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) act.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
