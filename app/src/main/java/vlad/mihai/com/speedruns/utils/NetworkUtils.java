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

    private final static String SCANNER_DELIMITER = "\\A";
    private final static String SPEEDRUN_BASE_URL = "https://www.speedrun.com/api/v1/games";
    private final static String RECORDS_URL_SEGMENT = "records";
    private final static String PARAM_CREATED_KEY = "orderby";
    private final static String PARAM_CREATED_VALUE = "released";
    private final static String PARAM_DIRECTION_KEY = "direction";
    private final static String PARAM_DIRECTION_VALUE = "desc";
    private final static String PARAM_MAX_RESULTS_KEY = "max";
    private final static String PARAM_MAX_RESULTS_VALUE = "50";

    public static URL buildUrlForGamesByCreationDate(){
        return buildUrlForGames(PARAM_CREATED_KEY, PARAM_CREATED_VALUE);
    }

    public static URL buildUrlForGames(String paramName, String paramValue){

        URL url = null;

        Uri builtUri = Uri.parse(SPEEDRUN_BASE_URL).buildUpon()
//                .appendQueryParameter(paramName, paramValue)
//                .appendQueryParameter(PARAM_DIRECTION_KEY, PARAM_DIRECTION_VALUE)
                .appendQueryParameter(PARAM_MAX_RESULTS_KEY, PARAM_MAX_RESULTS_VALUE)
                .build();

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildUrlForSpecificGame(String gameId){

        URL url = null;

        Uri builtUri = Uri.parse(SPEEDRUN_BASE_URL).buildUpon()
                .appendEncodedPath(gameId)
                .build();

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL convertStringToUrl(String urlString){
        URL url = null;
        Uri builtUri = convertStringToUri(urlString);
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static Uri convertStringToUri(String urlString){

        Uri builtUri = Uri.parse(urlString).buildUpon()
                .build();
        return builtUri;
    }

    public static URL getUrlForGameLeaderboards(String gameID){
        URL url = null;

        Uri builtUri = Uri.parse(SPEEDRUN_BASE_URL).buildUpon()
                .appendEncodedPath(gameID)
                .appendEncodedPath(RECORDS_URL_SEGMENT)
//                .appendQueryParameter(paramName, paramValue)
//                .appendQueryParameter(PARAM_DIRECTION_KEY, PARAM_DIRECTION_VALUE)
//                .appendQueryParameter(PARAM_MAX_RESULTS_KEY, PARAM_MAX_RESULTS_VALUE)
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
