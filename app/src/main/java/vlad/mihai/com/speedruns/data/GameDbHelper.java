package vlad.mihai.com.speedruns.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Vlad
 */

public class GameDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "game.db";
    private static final int DATABASE_VERSION = 1;

    public GameDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + GameContract.GameEntry.TABLE_NAME + " (" +
                GameContract.GameEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                GameContract.GameEntry.COLUMN_GAME_ID + " TEXT NOT NULL, " +
                GameContract.GameEntry.COLUMN_GAME_ABBREVIATION + " TEXT NOT NULL " +
                GameContract.GameEntry.COLUMN_GAME_WEBLINK + " TEXT NOT NULL " +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Use ALTER here when there is a need to upgrade the database
    }
}
