package vlad.mihai.com.speedruns.utils;

import android.content.Context;

import vlad.mihai.com.speedruns.R;

/**
 * Created by Vlad
 */

public class DisplayHelper {

    Context context;
    public DisplayHelper(Context context){
        this.context = context;
    }

    public String formatRunDuration(String runDurationString){

        StringBuilder formattedDuration = new StringBuilder();
        if (null != runDurationString) {
            int totalRunDurationInSeconds = Integer.parseInt(runDurationString);
            int hours = totalRunDurationInSeconds / 3600;
            int runDurationMinutesRemainder = totalRunDurationInSeconds % 3600;
            int minutes = runDurationMinutesRemainder / 60;
            int seconds = runDurationMinutesRemainder % 60;
            formattedDuration.append(context.getString(R.string.space));
            if (hours > 0){
                formattedDuration.append(Integer.toString(hours));
                formattedDuration.append(context.getString(R.string.hours));
            }
            if (minutes > 0){
                formattedDuration.append(Integer.toString(minutes));
                formattedDuration.append(context.getString(R.string.minutes));
            }
            if (seconds > 0){
                formattedDuration.append(Integer.toString(seconds));
                formattedDuration.append(context.getString(R.string.seconds));
            }

        }
        return formattedDuration.toString();
    }
}
