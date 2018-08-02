package vlad.mihai.com.speedruns;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import vlad.mihai.com.speedruns.model.GameRun;
import vlad.mihai.com.speedruns.model.RunLink;
import vlad.mihai.com.speedruns.model.RunVideo;
import vlad.mihai.com.speedruns.utils.NetworkUtils;

/**
 * Created by Vlad
 */

public class RunActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView playerTextView;
    private TextView runDurationTextView;
    private TextView runDateTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.run_activity);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.run_collapsing_toolbar_layout);
        toolbar = findViewById(R.id.run_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setTitle(getString(R.string.run_activity_name));

        playerTextView = findViewById(R.id.player_tv);
        runDurationTextView = findViewById(R.id.run_duration_tv);
        runDateTextView = findViewById(R.id.run_date_tv);



        Intent intent = getIntent();
        GameRun currentGameRun = null;
        if (null != intent) {
            currentGameRun = intent.getParcelableExtra(getString(R.string.intentExtraRunKey));
        }

        if (null != currentGameRun) {
            String playerName = currentGameRun.getPlayers().get(0).getName();
            updatePlayerName(playerName);
            String runDuration_primaryRunString = currentGameRun.getTime().getPrimary_t();
            updateRunDuration(runDuration_primaryRunString);
            String runDate = currentGameRun.getDate();
            updateRunDate(runDate);
            collapsingToolbarLayout.setTitle(currentGameRun.getPlayers().get(0).getName() + "'s Run");
            RunVideo runVideo = currentGameRun.getRunVideo();
            updateRunVideo(runVideo);

        }
    }

    private void updatePlayerName(String playerName){
        if (null != playerName){
            playerTextView.setText(playerName);
        }
    }

    private void updateRunDuration(String runDurationString){

        if (null != runDurationString) {
            int totalRunDurationInSeconds = Integer.parseInt(runDurationString);
            int hours = totalRunDurationInSeconds / 3600;
            int runDurationMinutesRemainder = totalRunDurationInSeconds % 3600;
            int minutes = runDurationMinutesRemainder / 60;
            int seconds = runDurationMinutesRemainder % 60;
            StringBuilder formattedDuration = new StringBuilder()
                    .append(getString(R.string.runDurationBaseTemplate));
            if (hours > 0){
                formattedDuration.append(Integer.toString(hours));
                formattedDuration.append(getString(R.string.hours));
            }
            if (minutes > 0){
                formattedDuration.append(Integer.toString(minutes));
                formattedDuration.append(getString(R.string.minutes));
            }
            if (seconds > 0){
                formattedDuration.append(Integer.toString(seconds));
                formattedDuration.append(getString(R.string.seconds));
            }

            runDurationTextView.setText(formattedDuration);
        }
    }

    private void updateRunDate(String runDate) {
        if (null != runDate){
            StringBuilder formattedDate = new StringBuilder()
                    .append(getString(R.string.runDateBaseTemplate))
                    .append(runDate);
            runDateTextView.setText(formattedDate);
        }
    }

    private void updateRunVideo(RunVideo runVideo){
        if (null != runVideo){
            List<RunLink> runLinks = runVideo.getRunLinksList();
            if (!runLinks.isEmpty()){
                createRunVideoButtons(runLinks);
            }
        }
    }

    private void createRunVideoButtons(List<RunLink> runLinks){
        LinearLayout runVideoLayout = findViewById(R.id.ll_videos);
        int index = 1;
        for (final RunLink runLink: runLinks){
            Button button = new Button(this);
            button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            button.setText("Play Run Video #" + Integer.toString(index));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playVideo(runLink.getLinkUri());
                }
            });
            runVideoLayout.addView(button);
            index++;
        }
    }

    private void playVideo(String videoLink){

        Uri videoUri = NetworkUtils.convertStringToUri(videoLink);
        Intent playRunVideoIntent = new Intent(Intent.ACTION_VIEW, videoUri);
        if (playRunVideoIntent.resolveActivity(getPackageManager()) != null){
            startActivity(playRunVideoIntent);
        }

    }
}
