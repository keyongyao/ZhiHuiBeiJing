package com.kk.zhbj.utils;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;

/**
 * 注释：  <br>
 * 作者： kk <br>
 * QQ  : <br>
 * 创建时间：2016/10/13 - 20:09 <br>
 */

public class ScreenSizeUtil {
    public static int getScreenWidth(Activity activity) {
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public static int getScreenHeight(Activity activity) {
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }
}
