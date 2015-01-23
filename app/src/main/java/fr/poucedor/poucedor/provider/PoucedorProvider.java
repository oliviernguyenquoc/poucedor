package fr.poucedor.poucedor.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import fr.poucedor.poucedor.provider.DatabaseContract;


/**
 * Created by loic on 08/01/15.
 */
public class PoucedorProvider extends ContentProvider {
    public static final int POSITIONS = 1;
    public static final int FURTHEST_POSITION = 2;
    private static final UriMatcher URI_MATCHER = buildUriMatcher();
    private PoucedorDB poucedorDB;

    private static final String AUTHORITY = "fr.poucedor.poucedor.provider";
    private static final String BASE_PATH = "positions";
    public static final Uri CONTENT_URI = Uri.parse("content://" + DatabaseContract.CONTENT_AUTHORITY + "/" + BASE_PATH);

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        // TODO Fix authority in DatabaseContract
        final String authority = DatabaseContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, BASE_PATH, POSITIONS);
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
            ContentValues universityValues = new ContentValues();

            String universityId =  values.getAsString(DatabaseContract.Team.COLUMN_NAME_UNIVERSITY_ID);
            universityValues.put(DatabaseContract.University._ID, universityId);

            String universityName = values.getAsString(DatabaseContract.University.TABLE_NAME + "." + DatabaseContract.University.COLUMN_NAME_NAME);
            values.remove(DatabaseContract.University.TABLE_NAME + "." + DatabaseContract.University.COLUMN_NAME_NAME);
            universityValues.put(DatabaseContract.University.COLUMN_NAME_NAME, universityName);

            String universityLat = values.getAsString(DatabaseContract.University.TABLE_NAME + "." + DatabaseContract.University.COLUMN_NAME_LATITUDE);
            values.remove(DatabaseContract.University.TABLE_NAME + "." + DatabaseContract.University.COLUMN_NAME_LATITUDE);
            universityValues.put(DatabaseContract.University.COLUMN_NAME_LATITUDE, universityLat);

            String universityLon = values.getAsString(DatabaseContract.University.TABLE_NAME + "." + DatabaseContract.University.COLUMN_NAME_LONGITUDE);
            values.remove(DatabaseContract.University.TABLE_NAME + "." + DatabaseContract.University.COLUMN_NAME_LONGITUDE);
            universityValues.put(DatabaseContract.University.COLUMN_NAME_LONGITUDE, universityLon);

            db.insertWithOnConflict(DatabaseContract.University.TABLE_NAME, null, universityValues, SQLiteDatabase.CONFLICT_REPLACE);
            db.insertWithOnConflict(DatabaseContract.Team.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            return null;
        } else {
            return null;
        }
    }
}
