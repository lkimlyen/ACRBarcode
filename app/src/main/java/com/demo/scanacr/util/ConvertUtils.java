package com.demo.scanacr.util;

import android.os.Environment;

import com.demo.scanacr.R;
import com.demo.scanacr.app.CoreApplication;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

/**
 * Created by PC on 13-Apr-2018.
 */

public class ConvertUtils {

    public static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

    public static final String APP_DATETIME_FORMAT = "MM/dd/yyyy HH:mm:ss";

    public static int ConvertStringMoneyToInt(String s) {
        if (s.toString().trim().equals("")) {
            return 0;
        } else {
            String result = "";
            String[] listSp = s.split("\\.");
            for (String item : listSp) {
                result = result + item;
            }
            return Integer.parseInt(result);
        }
    }

    public static String ConvertStringToShortDate(String s) {
        Date date = null;
        String sDate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(APP_DATETIME_FORMAT);
        String expectedPattern = "dd/MM/yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);
        try {
            date = dateFormat.parse(s);
            sDate = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sDate;

    }

    public static String getDateTimeCurrent() {
        SimpleDateFormat formatter = new SimpleDateFormat(APP_DATETIME_FORMAT);
        String newFormat = formatter.format(new Date());
        return newFormat;
    }

    public static String exportRealmFile() {
        final Realm realm = Realm.getDefaultInstance();
        String filePath = "";
        try {
            final File file = new File(Environment.getExternalStorageDirectory().getPath().concat(
                    CoreApplication.getInstance().getString(R.string.text_path_file) + CoreApplication.getInstance()
                            .getString(R.string.text_name_database)));
            if (file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }

            realm.writeCopyTo(file);
            filePath = file.getParent();
        } catch (Exception e) {
            realm.close();
            e.printStackTrace();
        }
        return filePath;
    }
}
