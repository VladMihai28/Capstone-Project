package vlad.mihai.com.speedruns.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import vlad.mihai.com.speedruns.R;
import vlad.mihai.com.speedruns.model.Category;

/**
 * Created by Vlad
 */

public class CategoryJsonParser {

    private Context context;

    public CategoryJsonParser(Context context){
        this.context = context;
    }

    private Gson gson;

    public Category parseCategory(String categoryString){

        JSONObject categoryJson = null;
        try {
            categoryJson = new JSONObject(categoryString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject categoryJsonObject = null;
        try {
            categoryJsonObject = categoryJson.getJSONObject(context.getString(R.string.gameJsonDataTag));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        initializeGson();
        Category category = gson.fromJson(categoryJsonObject.toString(), Category.class);

        return category;
    }

    private void initializeGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }
}
