package com.example.game.hangman;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WordHandler extends AsyncTask {
    private URL url;

    public WordHandler(URL url) {
        this.url = url;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String response = "";
            String line;
            while ((line = rd.readLine()) != null) {
                response = line;
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
