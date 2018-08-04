package vlad.mihai.com.speedruns;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import vlad.mihai.com.speedruns.data.GameContract;
import vlad.mihai.com.speedruns.model.Category;
import vlad.mihai.com.speedruns.model.Game;
import vlad.mihai.com.speedruns.model.GameRun;
import vlad.mihai.com.speedruns.model.Leaderboard;
import vlad.mihai.com.speedruns.model.Player;
import vlad.mihai.com.speedruns.model.RunPlace;
import vlad.mihai.com.speedruns.model.UserProfile;
import vlad.mihai.com.speedruns.utils.CategoryJsonParser;
import vlad.mihai.com.speedruns.utils.LeaderboardsJsonParser;
import vlad.mihai.com.speedruns.utils.NetworkUtils;
import vlad.mihai.com.speedruns.utils.UserProfileJsonParser;

/**
 * Created by Vlad
 */

public class LeaderBoardsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private LeaderboardAdapter leaderboardAdapter;
    private FloatingActionButton fab;
    private TextView errorTextView;
    CollapsingToolbarLayout collapsingToolbarLayout;

    public String selection;
    public String[] selectionArgs;
    Game currentGame;
    List<Leaderboard> leaderboards;
    private String gameID;
    private boolean gameIsInFavorites;

    public static final String[] FAVORITE_GAMES_PROJECTION = {
            GameContract.GameEntry.COLUMN_GAME_ID,
            GameContract.GameEntry.COLUMN_GAME_ABBREVIATION,
            GameContract.GameEntry.COLUMN_GAME_WEBLINK,
    };

    private FirebaseAnalytics firebaseAnalytics;

    private int initiatedQueries;
    private int completedQueries;

    private StringBuilder categoryList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboards_activity);

        collapsingToolbarLayout = findViewById(R.id.leaderboards_collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle(getString(R.string.leaderboards_activity_name));
        toolbar = findViewById(R.id.leaderboards_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = findViewById(R.id.favourite_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFavorites(view);
            }
        });

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        errorTextView = findViewById(R.id.tv_leaderboards_error_message_display);
        initiatedQueries = 0;
        completedQueries = 0;
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);

        recyclerView = findViewById(R.id.leaderboards_recyclerview);
        recyclerView.setLayoutManager(layoutManager);

        leaderboardAdapter = new LeaderboardAdapter(this);
        recyclerView.setAdapter(leaderboardAdapter);

        Intent intent = getIntent();
        currentGame = null;
        if (null != intent) {
            currentGame = intent.getParcelableExtra(getString(R.string.intentExtraGameKey));
            gameID = currentGame.getGameID();
        }

        if (null != currentGame) {
            categoryList = new StringBuilder();
            categoryList.append(currentGame.getGameName().getInternationalName());
            categoryList.append(getString(R.string.widgetTitleSuffix));
            categoryList.append("\n");
            collapsingToolbarLayout.setTitle(getString(R.string.leaderboards_with_game_name) + currentGame.getGameName().getInternationalName());
            URL leaderboardsQuery = NetworkUtils.getUrlForGameLeaderboards(currentGame.getGameID());
            new GameLeaderBoardsQUeryTask().execute(leaderboardsQuery);

            selection = GameContract.GameEntry.COLUMN_GAME_ID + " = ? ";
            selectionArgs = new String[]{gameID};
            Cursor cursor = getFavoriteGameById(selection, selectionArgs);
            if (cursor.getCount() > 0) {
                markAsFavorite();
            }
            else {
                unmarkAsFavorite();
            }
        }
    }

    public class GameLeaderBoardsQUeryTask extends AsyncTask<URL, Void, List<Leaderboard>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Leaderboard> doInBackground(URL... urls) {

            URL targetUrl = urls[0];

            List<Leaderboard> leaderboardsResult;

            try {
                String leaderboardResult = NetworkUtils.getResponseFromHttpUrl(targetUrl);
                LeaderboardsJsonParser leaderboardsJsonParser = new LeaderboardsJsonParser(LeaderBoardsActivity.this);
                leaderboardsResult = leaderboardsJsonParser.parseLeaderboards(leaderboardResult);

            } catch (IOException e) {
                return null;
            }

            return leaderboardsResult;
        }

        @Override
        protected void onPostExecute(List<Leaderboard> leaderboardResult) {

            if (leaderboardResult != null) {
                updateLeaderBoardInformation(leaderboardResult);
            }
        }
    }

    private void updateLeaderBoardInformation(List<Leaderboard> leaderboardResult) {

        leaderboards = leaderboardResult;
        for (Leaderboard leaderboard : leaderboards) {
            List<RunPlace> runPlaceList = leaderboard.getRunPlaceList();
            for (RunPlace runPlace : runPlaceList) {
                GameRun gameRun = runPlace.getGameRun();
                List<Player> playerList = gameRun.getPlayers();
                for (Player player : playerList) {
                    if (null == player.getName()) {
                        URL playerUrl = NetworkUtils.convertStringToUrl(player.getUri());
                        initiatedQueries++;
                        new PlayerNameQueryTask().execute(playerUrl);
                    }
                }
            }
            URL categoryUrl = NetworkUtils.buildUrlForCategory(leaderboard.getCategory());
            initiatedQueries++;
            new CategoryQueryTask().execute(categoryUrl);
        }

    }

    public class PlayerNameQueryTask extends AsyncTask<URL, Void, UserProfile> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected UserProfile doInBackground(URL... urls) {

            URL targetUrl = urls[0];

            UserProfile userProfile;

            try {
                String userResult = NetworkUtils.getResponseFromHttpUrl(targetUrl);
                UserProfileJsonParser userProfileJsonParser = new UserProfileJsonParser(LeaderBoardsActivity.this);
                userProfile = userProfileJsonParser.parseUserProfile(userResult);

            } catch (IOException e) {
                return null;
            }

            return userProfile;
        }

        @Override
        protected void onPostExecute(UserProfile userProfile) {

            if (userProfile != null) {
                updateLeaderBoardsWithUserNames(userProfile);
            }
        }
    }

    public class CategoryQueryTask extends AsyncTask<URL, Void, Category> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            errorMessageDisplay.setVisibility(View.INVISIBLE);
//            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Category doInBackground(URL... urls) {

            URL targetUrl = urls[0];

            Category category;

            try {
                String categoryResult = NetworkUtils.getResponseFromHttpUrl(targetUrl);
                CategoryJsonParser categoryJsonParser = new CategoryJsonParser(LeaderBoardsActivity.this);
                category = categoryJsonParser.parseCategory(categoryResult);

            } catch (IOException e) {
                return null;
            }

            return category;
        }

        @Override
        protected void onPostExecute(Category category) {

            if (category != null) {
                updateLeaderBoardsWithCategory(category);
            }
        }
    }


    private void updateLeaderBoardsWithUserNames(UserProfile userProfile) {
        for (Leaderboard leaderboard : leaderboards) {
            List<RunPlace> runPlaceList = leaderboard.getRunPlaceList();
            for (RunPlace runPlace : runPlaceList) {
                GameRun gameRun = runPlace.getGameRun();
                List<Player> playerList = gameRun.getPlayers();
                for (Player player : playerList) {
                    if (null != player.getId()) {
                        if (player.getId().equals(userProfile.getId())) {
                            player.setName(userProfile.getUserName().getInternationalName());
                            completedQueries++;
                        }
                    }
                }
            }
        }
        leaderboardAdapter.setLeaderboardData(leaderboards);

    }

    private void updateLeaderBoardsWithCategory(Category category) {

        for (Leaderboard leaderboard : leaderboards) {
            if (leaderboard.getCategory().equals(category.getCategoryId())) {
                leaderboard.setCategoryName(category.getName());
                categoryList.append(category.getName());
                categoryList.append("\n");
                completedQueries++;
            }
        }
        leaderboardAdapter.setLeaderboardData(leaderboards);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, SpeedRunsWidgetProvider.class));
        SpeedRunsWidgetProvider.updateSpeedrunsWidgets(this, appWidgetManager, categoryList.toString(), appWidgetIds);

    }

    private void showErrorMessage(){
        errorTextView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.INVISIBLE);
    }

    private void showResults(){
        errorTextView.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);

    }

    private void updateFavorites(View v) {
        if (gameIsInFavorites) {
            unmarkAsFavorite();
            removeFavoriteGame(gameID);
        } else {
            markAsFavorite();
            addNewGameToFavorites();
        }
    }


    private void removeFavoriteGame(String gameID) {
        int result = getContentResolver().delete(GameContract.GameEntry.CONTENT_URI, selection , selectionArgs);
        if (result > 0){
            Toast.makeText(getBaseContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
        }
    }

    private void addNewGameToFavorites() {

        ContentValues cv = new ContentValues();
        cv.put(GameContract.GameEntry.COLUMN_GAME_ID, currentGame.getGameID());
        cv.put(GameContract.GameEntry.COLUMN_GAME_ABBREVIATION, currentGame.getAbbreviation());
        cv.put(GameContract.GameEntry.COLUMN_GAME_WEBLINK, currentGame.getWebLink());
        Uri uri = getContentResolver().insert(GameContract.GameEntry.CONTENT_URI, cv);
        if(uri != null) {
            Toast.makeText(getBaseContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
        }
    }

    private void markAsFavorite() {
        fab.setImageResource(R.drawable.remove);
        gameIsInFavorites = true;

    }

    private void unmarkAsFavorite() {
        fab.setImageResource(R.drawable.favorite);
        gameIsInFavorites = false;

    }

    private Cursor getFavoriteGameById(String selection, String[] selectionArgs){
        return getContentResolver().query(
                GameContract.GameEntry.CONTENT_URI,
                FAVORITE_GAMES_PROJECTION,
                selection,
                selectionArgs,
                null);
    }
}
