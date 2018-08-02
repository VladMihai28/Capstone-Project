package vlad.mihai.com.speedruns.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Vlad
 */

public class GameContract {

    public static final String AUTHORITY = "vlad.mihai.com.speedruns";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_GAMES = "favoriteGames";

    public static final class GameEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_GAMES).build();

        public static final String TABLE_NAME = "favoriteGames";
        public static final String COLUMN_GAME_ID = "gameId";
        public static final String COLUMN_GAME_ABBREVIATION = "gameAbbreviation";
        public static final String COLUMN_GAME_WEBLINK = "gameWeblink";
    }
}
