package de.myself5.farbverbrauch;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class JSONDownloader extends AsyncTask<String, String, String>{

    //    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    private Activity mActivity;
    private String mFilepath;
    private String mDialogtext;

    public JSONDownloader(Activity a, String path, String dialogtext) {
        mFilepath = MainActivity.FILE_PATH + path;
        mActivity = a;
        mDialogtext = dialogtext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setMessage(mDialogtext);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    protected String doInBackground(String... aurl) {
        int count;

        try {

            URL url = new URL(aurl[0]);
            URLConnection conexion = url.openConnection();
            conexion.connect();

            int lenghtOfFile = conexion.getContentLength();
            Log.d("Farbverbrauch", "Lenght of file: " + lenghtOfFile);

            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(mFilepath);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (Exception ignored) {
        }
        return null;

    }

    protected void onProgressUpdate(String... progress) {
        Log.d("Farbverbrauch", "download progress:" + progress[0]);
        mProgressDialog.setProgress(Integer.parseInt(progress[0]));
    }

    @Override
    protected void onPostExecute(String result) {
        mProgressDialog.dismiss();
    }

    public static void downloadJSON(Activity activ, String text, String path, String url) {
            new JSONDownloader(activ, path, text).execute(url);
    }
}