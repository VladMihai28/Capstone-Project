package vlad.mihai.com.speedruns;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;

import java.net.URL;

import vlad.mihai.com.speedruns.model.GameRun;
import vlad.mihai.com.speedruns.utils.NetworkUtils;

/**
 * Created by Vlad
 */

public class RunActivity extends AppCompatActivity {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.run_activity);

        CollapsingToolbarLayout collapsingToolbarLayout = ((CollapsingToolbarLayout) findViewById(R.id.run_collapsing_toolbar_layout));
        toolbar = findViewById(R.id.run_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setTitle(getString(R.string.run_activity_name));

        Intent intent = getIntent();
        GameRun currentGameRun = null;
        if (null != intent) {
            currentGameRun = intent.getParcelableExtra(getString(R.string.intentExtraRunKey));
        }

        if (null != currentGameRun) {
            collapsingToolbarLayout.setTitle(currentGameRun.getPlayers().get(0).getName());
        }
    }
}
