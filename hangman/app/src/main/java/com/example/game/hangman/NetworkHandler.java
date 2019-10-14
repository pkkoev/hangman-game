package com.example.game.hangman;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class NetworkHandler extends AsyncTask {

    private JSONObject urlParams;
    private URL url;

    public NetworkHandler(JSONObject urlParams, URL url) {
        this.urlParams = urlParams;
        this.url = url;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/json");
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            os.write(urlParams.toString().getBytes("UTF-8"));
            os.close();

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String response = "";
            String line;
            while ((line = rd.readLine()) != null) {
                response += (line);
            }
            rd.close();

            params[0] = response;

        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return params[0];
    }
}
