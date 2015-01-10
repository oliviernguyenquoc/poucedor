package fr.poucedor.poucedor;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;


/**
 * Created by loic on 08/01/15.
 */
public class PoucedorProvider extends ContentProvider {
    public static final int POSITIONS = 1;
    public static final int FURTHEST_POSITION = 2;
    private static final UriMatcher URI_MATCHER = buildUriMatcher();
    private PoucedorDB poucedorDB;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        // TODO Fix authority in DatabaseContract
        final String authority = "";
//        final String authority = DatabaseContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "positions", POSITIONS);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        poucedorDB = new PoucedorDB(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        // TODO check value
        return "PositionList";
    }


    // Not used
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    // Not used
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (URI_MATCHER.match(uri) == POSITIONS) {
            SQLiteDatabase db = poucedorDB.getReadableDatabase();
            Cursor cursor = db.query(DatabaseContract.Team.TABLE_NAME, projection, selection, selectionArgs, null, null, DatabaseContract.Team.COLUMN_NAME_FD);
            return cursor;
        } else {
            return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (URI_MATCHER.match(uri) == POSITIONS) {
            SQLiteDatabase db = poucedorDB.getWritableDatabase();
            db.insertWithOnConflict(DatabaseContract.Team.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            return null;
        } else {
            return null;
        }
    }
}
