package fr.poucedor.poucedor;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by loic on 08/01/15.
 */
public class PoucedorDB extends SQLiteOpenHelper {

    /**
     * Database name
     */
    private static String DBNAME = "poucedor";

    /**
     * Database version
     */
    private static int VERSION = 1;

    public PoucedorDB(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.SQL_CREATE_UNIVERSITY_TABLE);
        db.execSQL(DatabaseContract.SQL_CREATE_TEAM_TABLE);
        db.execSQL(DatabaseContract.SQL_CREATE_MYPOSITION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Team.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.University.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.MyPosition.TABLE_NAME);
        onCreate(db);
    }
}
