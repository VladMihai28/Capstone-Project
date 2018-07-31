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
import vlad.mihai.com.speedruns.model.Leaderboard;

/**
 * Created by Vlad
 */

public class LeaderboardsJsonParser {

    private Context context;

    public LeaderboardsJsonParser(Context context){
        this.context = context;
    }

    private Gson gson;

    public List<Leaderboard> parseLeaderboards(String leaderboardsString){

        JSONObject leaderboardJsonList = null;
        try {
            leaderboardJsonList = new JSONObject(leaderboardsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray leaderboards = null;
        try {
            leaderboards = leaderboardJsonList.getJSONArray(context.getString(R.string.gameJsonDataTag));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<Leaderboard> leaderboardsList = new ArrayList<>();
        initializeGson();

        for(int i = 0; i < leaderboards.length(); i++){
            try {
                JSONObject currentLeaderboardJson = leaderboards.getJSONObject(i);
                Leaderboard leaderboard = gson.fromJson(currentLeaderboardJson.toString(), Leaderboard.class);
                leaderboardsList.add(leaderboard);
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
        return leaderboardsList;
    }

    private void initializeGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

}
