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
import vlad.mihai.com.speedruns.model.UserProfile;

/**
 * Created by Vlad
 */

public class UserProfileJsonParser {

    private Context context;

    public UserProfileJsonParser(Context context){
        this.context = context;
    }

    private Gson gson;

    public UserProfile parseUserProfile(String userProfileString){

        JSONObject userProfileJson = null;
        try {
            userProfileJson = new JSONObject(userProfileString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject userProfile = null;
        try {
            userProfile = userProfileJson.getJSONObject(context.getString(R.string.gameJsonDataTag));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        initializeGson();
        UserProfile userProfileResult = gson.fromJson(userProfile.toString(), UserProfile.class);

//        for(int i = 0; i < userProfile.length(); i++){
//            try {
//                JSONObject currentLeaderboardJson = userProfile.getJSONObject(i);
//                Leaderboard leaderboard = gson.fromJson(currentLeaderboardJson.toString(), Leaderboard.class);
//                userProfileResult.add(leaderboard);
//            } catch (JSONException e) {
//
//                e.printStackTrace();
//            }
//        }
        return userProfileResult;
    }

    private void initializeGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

}
