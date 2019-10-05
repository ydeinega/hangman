package com.example.hangman.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtilities {

    private static final String WORD_DICTIONARY_URL = "http://app.linkedin-reach.io/words";
    private static final String DIFFICULTY = "difficulty";
    private static final String MIN_LENGTH = "minLength";
    private static final String MAX_LENGTH = "maxLength";
    private static final String START = "start";
    private static final String COUNT = "count";

    public static URL getURL() {
        Uri wordsUri = Uri.parse(WORD_DICTIONARY_URL).buildUpon()
                .appendQueryParameter(DIFFICULTY, Integer.toString(1))
                .appendQueryParameter(MIN_LENGTH, Integer.toString(4))
                .build();
        try {
            URL wordsUrl = new URL(wordsUri.toString());
            return wordsUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getResponseFromHttp(URL url) throws IOException {

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        try {
            String response = null;
            InputStream inputStream = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");
            if (scanner.hasNext()) {
                response = scanner.next();
            }
            scanner.close();

            return response;

        } finally {
            httpURLConnection.disconnect();
        }
    }
}
