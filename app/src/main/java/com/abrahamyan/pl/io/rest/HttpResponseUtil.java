package com.abrahamyan.pl.io.rest;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by SEVAK on 29.06.2017.
 */

public class HttpResponseUtil {

    private static final String LOG_TAG = HttpResponseUtil.class.getSimpleName();

    public static String parseResponse(HttpURLConnection connection) {

        InputStreamReader streamReader = null;
        BufferedReader reader = null;
        String result = null;

        try {
            streamReader = new InputStreamReader(connection.getInputStream());
            reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }

            result = stringBuilder.toString();
            Log.d(LOG_TAG, result);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                connection.disconnect();

            try {
                if (reader != null)
                    reader.close();
                if (streamReader != null)
                    streamReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
