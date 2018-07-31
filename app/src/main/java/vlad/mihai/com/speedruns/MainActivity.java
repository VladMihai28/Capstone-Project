package vlad.mihai.com.speedruns;

import android.annotation.SuppressLint;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import vlad.mihai.com.speedruns.model.Game;
import vlad.mihai.com.speedruns.utils.GameJsonParser;
import vlad.mihai.com.speedruns.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    private static final int ID_GAMES_QUERY_LOADER = 42;

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout)).setTitle(getString(R.string.app_name));
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        URL gamesQuery = NetworkUtils.buildUrlForGamesByCreationDate();
        Bundle bundleForLoader = new Bundle();
        bundleForLoader.putString(getString(R.string.gamesLoaderQueryKey), gamesQuery.toString());
        getSupportLoaderManager().initLoader(ID_GAMES_QUERY_LOADER, bundleForLoader, gamesLoaderCallback);
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
                        gameListResult = GameJsonParser.parseGames(gamesResult);

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
        public void onLoadFinished(Loader<List<Game>> loader, List<Game> movieListResult) {
//            loadingIndicator.setVisibility(View.INVISIBLE);
            int i = 0;
//            if (movieListResult != null) {
//                showMovieDataView();
//                movieAdapter.setMovieData(movieListResult);
//                movieList = movieListResult;
//            } else {
//                showErrorMessage();
//            }
        }

        @Override
        public void onLoaderReset(Loader<List<Game>> loader) {

        }
    };
}
