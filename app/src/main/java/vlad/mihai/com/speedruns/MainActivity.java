package vlad.mihai.com.speedruns;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import vlad.mihai.com.speedruns.data.GameContract;
import vlad.mihai.com.speedruns.model.Game;
import vlad.mihai.com.speedruns.utils.GameJsonParser;
import vlad.mihai.com.speedruns.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity implements
        GameAdapter.GameAdapterOnClickHandler{

    private static final int ID_GAMES_QUERY_LOADER = 42;
    private static final int ID_FAVORITE_GAMES_LOADER = 43;

    public static final String[] FAVORITE_GAMES_PROJECTION = {
            GameContract.GameEntry.COLUMN_GAME_ID,
            GameContract.GameEntry.COLUMN_GAME_ABBREVIATION,
            GameContract.GameEntry.COLUMN_GAME_WEBLINK,
    };

    private FirebaseAnalytics firebaseAnalytics;
    private final static int INDEX_GAME_ID = 0;

    private List<Game> gameList;

    private GameAdapter gameAdapter;
    private RecyclerView recyclerView;
    private ProgressBar loadingIndicator;
    private TextView errorMessageDisplay;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout)).setTitle(getString(R.string.app_name));
        toolbar = findViewById(R.id.app_bar);
        loadingIndicator = findViewById(R.id.main_loading_indicator);
        errorMessageDisplay = findViewById(R.id.tv_error_message_display);
        setSupportActionBar(toolbar);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);



        recyclerView = findViewById(R.id.games_recyclerview);
        recyclerView.setLayoutManager(layoutManager);

        gameAdapter = new GameAdapter(this, this);
        recyclerView.setAdapter(gameAdapter);

        if(savedInstanceState == null || !savedInstanceState.containsKey(getString(R.string.outStateGameParcelableKey))) {
            URL gamesQuery = NetworkUtils.buildUrlForGamesByCreationDate();
            Bundle bundleForLoader = new Bundle();
            bundleForLoader.putString(getString(R.string.gamesLoaderQueryKey), gamesQuery.toString());
            getSupportLoaderManager().initLoader(ID_GAMES_QUERY_LOADER, bundleForLoader, gamesLoaderCallback);


        }
        else {
            gameList = savedInstanceState.getParcelableArrayList(getString(R.string.outStateGameParcelableKey));
            gameAdapter.setGameData(gameList);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(getString(R.string.outStateGameParcelableKey), (ArrayList)gameList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(Game currentGame) {

        Context context = this;
        Class destinationClass = LeaderBoardsActivity.class;
        Intent intent = new Intent(context, destinationClass);
        intent.putExtra(getString(R.string.intentExtraGameKey), currentGame);

        Bundle bundleForAnalytics = new Bundle();
        bundleForAnalytics.putString(FirebaseAnalytics.Param.ITEM_ID, currentGame.getGameID());
        bundleForAnalytics.putString(FirebaseAnalytics.Param.ITEM_NAME, currentGame.getGameName().getInternationalName());
        bundleForAnalytics.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "gameName");
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundleForAnalytics);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int menuItemThatWasSelected = item.getItemId();

        /* Perform data retrieval based on the sort method selected by the user */
        switch (menuItemThatWasSelected) {
            case R.id.popular: {
                URL gamesQuery = NetworkUtils.buildUrlForGamesByCreationDate();
                Bundle bundleForLoader = new Bundle();
                bundleForLoader.putString(getString(R.string.gamesLoaderQueryKey), gamesQuery.toString());
                getSupportLoaderManager().restartLoader(ID_GAMES_QUERY_LOADER, bundleForLoader, gamesLoaderCallback);
                break;
            }
            case R.id.favorites:
                getSupportLoaderManager().restartLoader(ID_FAVORITE_GAMES_LOADER, null, favoriteGamesLoaderCallback);
        }

        return super.onOptionsItemSelected(item);
    }

    private LoaderManager.LoaderCallbacks<List<Game>> gamesLoaderCallback = new LoaderManager.LoaderCallbacks<List<Game>>() {
        @SuppressLint("StaticFieldLeak")
        @Override
        public Loader<List<Game>> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<List<Game>>(MainActivity.this) {
                List<Game> gameData = null;
                URL targetUrl = null;

                @Override
                protected void onStartLoading() {

                    try {
                        targetUrl = new URL(args.get(getString(R.string.gamesLoaderQueryKey)).toString());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    if (gameData != null) {
                        deliverResult(gameData);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public List<Game> loadInBackground() {

                    List<Game> gameListResult;

                    try {
                        String gamesResult = NetworkUtils.getResponseFromHttpUrl(targetUrl);
                        GameJsonParser gameJsonParser = new GameJsonParser(this.getContext());
                        gameListResult = gameJsonParser.parseGames(gamesResult);

                    } catch (IOException e) {
                        return null;
                    }

                    return gameListResult;
                }

                public void deliverResult(List<Game> data) {
                    gameData = data;
                    super.deliverResult(gameData);
                }

            };
        }

        @Override
        public void onLoadFinished(Loader<List<Game>> loader, List<Game> gameListResult) {
            loadingIndicator.setVisibility(View.INVISIBLE);
            if (gameListResult != null) {
                gameAdapter.setGameData(gameListResult);
                gameList = gameListResult;
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Game>> loader) {

        }
    };


    private LoaderManager.LoaderCallbacks<List<Game>> favoriteGamesLoaderCallback = new LoaderManager.LoaderCallbacks<List<Game>>() {
        @SuppressLint("StaticFieldLeak")
        @Override
        public Loader<List<Game>> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<List<Game>>(MainActivity.this) {
                List<Game> gameData = null;

                @Override
                protected void onStartLoading() {

                    if (gameData != null) {
                        deliverResult(gameData);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public List<Game> loadInBackground() {

                    List<Game> gameListResult = new ArrayList<>();
                    Cursor favoriteGamesCursor = getFavoriteGames();
                    try {
                        while (favoriteGamesCursor.moveToNext()) {
                            String gameId = favoriteGamesCursor.getString(INDEX_GAME_ID);

                            try {
                                String gameDBResult = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildUrlForSpecificGame(gameId));
                                if (null != gameDBResult) {
                                    GameJsonParser gameJsonParser = new GameJsonParser(this.getContext());
                                    gameListResult.add(gameJsonParser.parseSpecificGame(gameDBResult));
                                }

                            } catch (IOException e) {
                                return null;
                            }
                        }
                    } finally {
                        favoriteGamesCursor.close();
                    }
                    return gameListResult;
                }

                public void deliverResult(List<Game> data) {
                    gameData = data;
                    super.deliverResult(gameData);
                }

            };

        }

        @Override
        public void onLoadFinished(Loader<List<Game>> loader, List<Game> gameListResult) {
            loadingIndicator.setVisibility(View.INVISIBLE);

            if (gameListResult != null) {
                gameAdapter.setGameData(gameListResult);
                gameList = gameListResult;
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Game>> loader) {

        }
    };

    private Cursor getFavoriteGames(){
        return getContentResolver().query(
                GameContract.GameEntry.CONTENT_URI,
                FAVORITE_GAMES_PROJECTION,
                null,
                null,
                null);
    }

    private void showGameDataView(){
        errorMessageDisplay.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }
    private void showErrorMessage() {
        recyclerView.setVisibility(View.INVISIBLE);
        loadingIndicator.setVisibility(View.INVISIBLE);
        errorMessageDisplay.setVisibility(View.VISIBLE);
    }

}
