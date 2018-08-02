package vlad.mihai.com.speedruns.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import static vlad.mihai.com.speedruns.data.GameContract.GameEntry.TABLE_NAME;

/**
 * Created by Vlad
 */

public class GameContentProvider extends ContentProvider {

    private GameDbHelper gameDbHelper;
    public static final int GAMES = 100;
    public static final int GAME_WITH_ID = 101;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(GameContract.AUTHORITY, GameContract.PATH_GAMES, GAMES);
        uriMatcher.addURI(GameContract.AUTHORITY, GameContract.PATH_GAMES + "/#", GAME_WITH_ID);
        return uriMatcher;

    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        gameDbHelper = new GameDbHelper(context);
        return true;
    }


    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = gameDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case GAMES:
                long id = db.insert(TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(GameContract.GameEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = gameDbHelper.getReadableDatabase();

        int match = uriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            // Query for the tasks directory
            case GAMES:
                retCursor =  db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = gameDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int gamesDeleted;
        switch (match) {
            case GAMES:
                gamesDeleted = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case GAME_WITH_ID:

                String id = uri.getPathSegments().get(1);
                gamesDeleted = db.delete(TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        if (gamesDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return gamesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
