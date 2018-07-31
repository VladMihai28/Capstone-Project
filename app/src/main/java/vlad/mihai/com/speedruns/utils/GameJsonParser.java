package vlad.mihai.com.speedruns.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import vlad.mihai.com.speedruns.model.Game;

/**
 * Created by Vlad
 */

public class GameJsonParser {

    public GameJsonParser(){}

//    private Gson gson;

    public static List<Game> parseGames(String gamesString){

        JSONArray games = null;
        // it is not a json array but a single element which has an array in it
        try {
            games = new JSONArray(gamesString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<Game> gameList = new ArrayList<>();
        Gson gson = initializeGson();

        for(int i = 0; i < games.length(); i++){
            try {
                JSONObject currentGameJson = games.getJSONObject(i);
                Game game = gson.fromJson(currentGameJson.toString(), Game.class);
                gameList.add(game);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return gameList;
    }

    private static Gson initializeGson(){
        Gson gson;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        return gson;
    }
}
