package de.myself5.farbrechner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JSONGewebeValueAsyncTask extends AsyncTask<String, String, String> {

    private Activity mActivity;
    private String mValue;

    public JSONGewebeValueAsyncTask(Activity a, String value) {
        mActivity = a;
        mValue = value;
    }

    @Override
    protected String doInBackground(String... afile) {
        String file = new String(afile[0]);
        File f = new File(file);
        if (f.exists() && !f.isDirectory()) {
            String[] arr = {};
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                    stringBuilder.append(line).append("\n");
                bufferedReader.close();
                fileReader.close();
                publishProgress("" + 20);

                JSONObject obj = new JSONObject(stringBuilder.toString().trim());
                publishProgress("" + 60);
                String value = obj.getString(mValue);
                publishProgress("" + 100);
                return value;

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        Log.d("ANDRO_ASYNC", progress[0]);
    }

    @Override
    protected void onPostExecute(String unused){
    }
}