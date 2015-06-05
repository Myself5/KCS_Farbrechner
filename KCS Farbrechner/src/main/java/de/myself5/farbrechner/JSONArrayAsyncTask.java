package de.myself5.farbrechner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class JSONArrayAsyncTask extends AsyncTask<String, String, String[]> {
    private Activity mActivity;
    private String mArrayName;

    public JSONArrayAsyncTask(Activity a, String array) {
        mActivity = a;
        mArrayName = array;
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
    }

    static <T> T[] append(T[] arr, T element) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + 1);
        arr[N] = element;
        return arr;
    }

    @Override
    protected String[] doInBackground(String... afile) {
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

                JSONObject obj = new JSONObject(stringBuilder.toString().trim());

                JSONArray array = obj.getJSONArray(mArrayName);
                for (int i = 0; i < array.length(); i++) {
                    publishProgress("" + ((i * 100) / array.length()));
                    arr = append(arr, array.getString(i));
                }
                return arr;

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
}
