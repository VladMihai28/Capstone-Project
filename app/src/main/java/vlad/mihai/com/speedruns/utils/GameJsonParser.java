package vlad.mihai.com.speedruns.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import vlad.mihai.com.speedruns.R;
import vlad.mihai.com.speedruns.model.Game;

/**
 * Created by Vlad
 */

public class GameJsonParser {

    private Context context;

    public GameJsonParser(Context context){
        this.context = context;
    }

    private Gson gson;

    public List<Game> parseGames(String gamesString){

        JSONObject gameJsonList = null;
        try {
            gameJsonList = new JSONObject(gamesString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray games = null;
        try {
            games = gameJsonList.getJSONArray(context.getString(R.string.gameJsonDataTag));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<Game> gameList = new ArrayList<>();
        initializeGson();

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

    private void initializeGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }
}
