package com.demo.architect.data.repository.base.local;

import android.os.AsyncTask;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.offline.ZipResponse;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Imark-N on 12/19/2015.
 */
public class CreateZip extends AsyncTask<Void, Void, ZipResponse> {


    private final String mSourcePath;
    private final String mTargetZipPath;
    private ZipResponse result;
    private Listener mListener;

    public CreateZip(String sourcePath, String targetZipPath) {
        this.mSourcePath = sourcePath;
        this.mTargetZipPath = targetZipPath;
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ZipResponse doInBackground(Void... params) {
        ZipResponse result = null;
        try {
            try {
                InputStream inputStream = new FileInputStream(mSourcePath);
                OutputStream outputStream = new FileOutputStream(mTargetZipPath);
                byte[] buff = new byte[1024];
                int length;
                while ((length = inputStream.read(buff)) > 0) {
                    outputStream.write(buff, 0, length);
                }
                outputStream.flush();
                outputStream.close();

                result = new ZipResponse(Constants.SUCCESS, mTargetZipPath);
            } catch (Exception e) {
                e.printStackTrace();
                result = new ZipResponse(Constants.FAIL, e.getMessage());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            result = new ZipResponse(Constants.FAIL, ex.getMessage());
        }
        return result;
    }


    @Override
    protected void onPostExecute(ZipResponse actionResult) {
        super.onPostExecute(actionResult);

        if (mListener != null)
            mListener.onPostExecute(actionResult);
    }

    public interface Listener {
        void onPostExecute(ZipResponse response);
    }
}
