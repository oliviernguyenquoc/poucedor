package fr.poucedor.poucedor;

import android.provider.BaseColumns;

public final class DatabaseContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public DatabaseContract() {
    }

    // This table represents Universities
    public static abstract class University implements BaseColumns {
        public static final String TABLE_NAME = "university";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
    }

    // This table represents Teams
    public static abstract class Team implements BaseColumns {
        public static final String TABLE_NAME = "team";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_STUDENT1_NAME = "student1Name";
        public static final String COLUMN_NAME_STUDENT2_NAME = "student2Name";
        public static final String COLUMN_NAME_STUDENT1_EMAIL = "student1Email";
        public static final String COLUMN_NAME_STUDENT2_EMAIL = "student2Email";
        public static final String COLUMN_NAME_UNIVERSITY_ID = "universityId";
        public static final String COLUMN_NAME_FD = "farthestDistance";
        public static final String COLUMN_NAME_FD_LATITUDE = "fdLatitude";
        public static final String COLUMN_NAME_FD_LONGITUDE = "fdLongitude";
        public static final String COLUMN_NAME_LAST_LATITUDE = "lastLatitude";
        public static final String COLUMN_NAME_LAST_LONGITUDE = "lastLongitude";
        public static final String COLUMN_NAME_MY_TEAM = "myTeam";
    }

    // This table represents the positions of the device, with the synced value representing
    // if these positions have been sent to the server;
    public static abstract class MyPosition implements BaseColumns {
        public static final String TABLE_NAME = "myPosition";
        public static final String COLUMN_NAME_DATESTAMP = "datestamp";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_SYNCED = "synced";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_UNIVERSITY_TABLE =
            "CREATE TABLE " + University.TABLE_NAME + " (" +
                    University._ID + TEXT_TYPE + " PRIMARY KEY," +
                    University.COLUMN_NAME_NAME      + TEXT_TYPE + COMMA_SEP +
                    University.COLUMN_NAME_LATITUDE  + " double" + COMMA_SEP +
                    University.COLUMN_NAME_LONGITUDE + " double" + " );";

    public static final String SQL_CREATE_TEAM_TABLE =
            "CREATE TABLE " + Team.TABLE_NAME + " (" +
                    Team._ID + TEXT_TYPE + " PRIMARY KEY," +
                    Team.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    Team.COLUMN_NAME_STUDENT1_NAME  + TEXT_TYPE + COMMA_SEP +
                    Team.COLUMN_NAME_STUDENT1_EMAIL + TEXT_TYPE + COMMA_SEP +
                    Team.COLUMN_NAME_STUDENT2_NAME  + TEXT_TYPE + COMMA_SEP +
                    Team.COLUMN_NAME_STUDENT2_EMAIL + TEXT_TYPE + COMMA_SEP +
                    Team.COLUMN_NAME_MY_TEAM        + " BOOLEAN" + COMMA_SEP +
                    Team.COLUMN_NAME_FD             + " double"  + COMMA_SEP +
                    Team.COLUMN_NAME_FD_LATITUDE    + " double"  + COMMA_SEP +
                    Team.COLUMN_NAME_FD_LONGITUDE   + " double"  + COMMA_SEP +
                    Team.COLUMN_NAME_LAST_LATITUDE  + " double"  + COMMA_SEP +
                    Team.COLUMN_NAME_LAST_LONGITUDE + " double"  + COMMA_SEP +
                    "FOREIGN KEY(" + Team.COLUMN_NAME_UNIVERSITY_ID + ") REFERENCES " +
                    University.TABLE_NAME + "(" + University._ID + "));";

    public static final String SQL_CREATE_MYPOSITION_TABLE =
            "CREATE TABLE " + MyPosition.TABLE_NAME + " (" +
                    MyPosition._ID + " INTEGER PRIMARY KEY," +
                    MyPosition.COLUMN_NAME_DATESTAMP + " INTEGER" + COMMA_SEP +
                    MyPosition.COLUMN_NAME_LATITUDE  + " double"  + COMMA_SEP +
                    MyPosition.COLUMN_NAME_LONGITUDE + " double"  + COMMA_SEP +
                    MyPosition.COLUMN_NAME_SYNCED    + " BOOLEAN" + ");";
}
