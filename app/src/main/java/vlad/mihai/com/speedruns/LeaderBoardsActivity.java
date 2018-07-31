package vlad.mihai.com.speedruns;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import vlad.mihai.com.speedruns.model.Game;
import vlad.mihai.com.speedruns.model.Leaderboard;
import vlad.mihai.com.speedruns.utils.GameJsonParser;
import vlad.mihai.com.speedruns.utils.LeaderboardsJsonParser;
import vlad.mihai.com.speedruns.utils.NetworkUtils;

/**
 * Created by Vlad
 */

public class LeaderBoardsActivity extends AppCompatActivity implements
        LeaderboardAdapter.LeaderboardAdapterOnClickHandler{

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private LeaderboardAdapter leaderboardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboards_activity);

        ((CollapsingToolbarLayout) findViewById(R.id.leaderboards_collapsing_toolbar_layout)).setTitle(getString(R.string.leaderboards_activity_name));
        toolbar = findViewById(R.id.leaderboards_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);

        recyclerView = findViewById(R.id.leaderboards_recyclerview);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setHasFixedSize(true);

        leaderboardAdapter = new LeaderboardAdapter(this, this);
        recyclerView.setAdapter(leaderboardAdapter);

        Intent intent = getIntent();
        Game currentGame = null;
        if (null != intent){
            currentGame = intent.getParcelableExtra(getString(R.string.intentExtraGameKey));
        }

        if (null != currentGame){
            URL leaderboardsQuery = NetworkUtils.getUrlForGameLeaderboards(currentGame.getGameID());
            new GameLeaderBoardsQUeryTask().execute(leaderboardsQuery);
        }


//        URL gamesQuery = NetworkUtils.buildUrlForGamesByCreationDate();
//        Bundle bundleForLoader = new Bundle();
//        bundleForLoader.putString(getString(R.string.gamesLoaderQueryKey), gamesQuery.toString());
//        getSupportLoaderManager().initLoader(ID_GAMES_QUERY_LOADER, bundleForLoader, gamesLoaderCallback);
    }

    @Override
    public void onClick(Leaderboard currentLeaderboard) {

//        Context context = this;
//        Class destinationClass = LeaderBoardsActivity.class;
//        Intent intent = new Intent(context, destinationClass);
//        intent.putExtra(getString(R.string.intentExtraGameKey), currentLeaderboard);
//        startActivity(intent);
    }

    public class GameLeaderBoardsQUeryTask extends AsyncTask<URL, Void, List<Leaderboard>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            errorMessageDisplay.setVisibility(View.INVISIBLE);
//            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Leaderboard> doInBackground(URL... urls) {

            URL targetUrl = urls[0];

            List<Leaderboard> leaderboardsResult;

            try {
                String leaderboardResult =  NetworkUtils.getResponseFromHttpUrl(targetUrl);
                LeaderboardsJsonParser leaderboardsJsonParser = new LeaderboardsJsonParser(LeaderBoardsActivity.this);
                leaderboardsResult = leaderboardsJsonParser.parseLeaderboards(leaderboardResult);

            } catch (IOException e) {
                return null;
            }

            return leaderboardsResult;
        }

        @Override
        protected void onPostExecute(List<Leaderboard> leaderboardResult) {
//            loadingIndicator.setVisibility(View.INVISIBLE);

            if (leaderboardResult != null){
//                showMovieDataView();
                leaderboardAdapter.setLeaderboardData(leaderboardResult);
//                movieList = leaderboardResult;
            }
            else {
//                showErrorMessage();
            }
        }
    }

}
