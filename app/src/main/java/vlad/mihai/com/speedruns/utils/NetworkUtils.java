package vlad.mihai.com.speedruns.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Vlad
 */

public class NetworkUtils {

    final static String SCANNER_DELIMITER = "\\A";
    final static String SPEEDRUN_BASE_URL = "https://www.speedrun.com/api/v1/games";
    final static String PARAM_CREATED_KEY = "orderby";
    final static String PARAM_CREATED_VALUE = "created";
    final static String PARAM_DIRECTION_KEY = "direction";
    final static String PARAM_DIRECTION_VALUE = "desc";

    public static URL buildUrlForGamesByCreationDate(){
        return buildUrlForGames(PARAM_CREATED_KEY, PARAM_CREATED_VALUE);
    }

    public static URL buildUrlForGames(String paramName, String paramValue){

        URL url = null;

        Uri builtUri = Uri.parse(SPEEDRUN_BASE_URL).buildUpon()
                .appendQueryParameter(paramName, paramValue)
                .appendQueryParameter(PARAM_DIRECTION_KEY, PARAM_DIRECTION_VALUE)
                .build();

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     * This method comes from the Udacity Android Nanodegree courses
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(5000);
        urlConnection.setReadTimeout(10000);
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter(SCANNER_DELIMITER);

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
