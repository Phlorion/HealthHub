package com.example.healthhub.Utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A helper class for downloading streams.
 * We use this so the network operations don't run on the main UI thread.
 */
public class NetworkHelper {
    private static final String TAG = "NetworkHelper";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    public interface OnDownloadCompleteListener {
        void onDownloadComplete(String jsonString);
        void onDownloadFailed(IOException e);
    }

    /**
     * A method to download json data from url
     * @param strURL The url we are requesting the json from
     * @param listener The listener interface
     */
    public void downloadJson(String strURL, OnDownloadCompleteListener listener) {
        executorService.execute(() -> {
            StringBuilder response = new StringBuilder();
            HttpURLConnection httpConn = null;
            BufferedReader reader = null;
            IOException exception = null;

            try {
                URL url = new URL(strURL);
                httpConn = (HttpURLConnection) url.openConnection();
                int responseCode = httpConn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), StandardCharsets.UTF_8));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        //Log.d(TAG, line);
                        response.append(line);
                    }
                    String result = response.toString();
                    mainThreadHandler.post(() -> listener.onDownloadComplete(result));
                    return;
                } else {
                    exception = new IOException("HTTP Error Code: " + responseCode);
                }
            } catch (IOException e) {
                Log.e(TAG, "Error downloading JSON", e);
                exception = e;
            } finally {
                if (httpConn != null) {
                    httpConn.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error closing reader", e);
                }
            }
            if (exception != null) {
                IOException finalException = exception;
                mainThreadHandler.post(() -> listener.onDownloadFailed(finalException));
            }
        });
    }
}
