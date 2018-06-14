package com.demo.scanacr.util;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

public class DownloadUtils {
    public static void DownloadFile(Context context, String strUrl) {
        File f = new File(Environment.getExternalStorageDirectory().toString() +"/"+ Environment.DIRECTORY_DOWNLOADS + "/" + getFileName(strUrl));
        if (f.exists()) {
            f.delete();
        }
        // Start download
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        dm.enqueue(new DownloadManager.Request(Uri.parse(strUrl))
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                        DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setMimeType("application/vnd.android.package-archive")
                .setTitle(getFileName(strUrl))
                .setDescription("Đang tải xuống bản cập nhật.")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                        getFileName(strUrl)));
    }

    public static String getFileName(String filePath) {
        return filePath.substring(filePath.lastIndexOf("/")+1, filePath.length());
    }
}
