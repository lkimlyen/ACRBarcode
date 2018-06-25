package com.demo.architect.data;

import android.content.Context;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.helper.SharedPreferenceHelper;

/**
 * Created by ACER on 7/23/2017.
 */

public class Def {
    private static Context context;

    public Def(Context context) {
        this.context = context;
    }

    public static final String HOST = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER,"https://www.google.com.vn");
}
