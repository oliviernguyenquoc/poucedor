package fr.poucedor.poucedor.provider;

import android.content.ContentResolver;
import android.content.ContentValues;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by loic on 23/01/15.
 */
public class RestFactoryProvider extends RestFactory {

    private String token;
    private ContentResolver contentResolver;

    public RestFactoryProvider(String token, ContentResolver contentResolver) {
        super();
        this.token = token;
        this.contentResolver = contentResolver;
    }

    public void getServerData() {
        Callback<List<Team>> callback = new Callback<List<Team>>() {
            @Override
            public void success(List<Team> teams, Response response) {
                ContentValues values;
                for (Team team : teams) {
                    values = new ContentValues();
                    values.put(DatabaseContract.Team._ID, team.id);
                    values.put(DatabaseContract.Team.COLUMN_NAME_NAME, team.name);
                    values.put(DatabaseContract.Team.COLUMN_NAME_FD_LATITUDE, team.positions.furthest.location.lat);
                    values.put(DatabaseContract.Team.COLUMN_NAME_FD_LONGITUDE, team.positions.furthest.location.lon);
                    values.put(DatabaseContract.Team.COLUMN_NAME_LAST_LATITUDE, team.positions.last.location.lat);
                    values.put(DatabaseContract.Team.COLUMN_NAME_LAST_LONGITUDE, team.positions.last.location.lon);
                    values.put(DatabaseContract.Team.COLUMN_NAME_FD, team.positions.furthest.distance);
                    values.put(DatabaseContract.Team.COLUMN_NAME_STUDENT1_NAME, team.student1.name);
                    values.put(DatabaseContract.Team.COLUMN_NAME_STUDENT2_NAME, team.student2.name);
                    values.put(DatabaseContract.Team.COLUMN_NAME_UNIVERSITY_ID, team.university.id);
                    values.put(DatabaseContract.University.TABLE_NAME + "." + DatabaseContract.University.COLUMN_NAME_NAME, team.university.name);
                    values.put(DatabaseContract.University.TABLE_NAME + "." + DatabaseContract.University.COLUMN_NAME_LATITUDE, team.university.location.lat);
                    values.put(DatabaseContract.University.TABLE_NAME + "." + DatabaseContract.University.COLUMN_NAME_LONGITUDE, team.university.location.lon);
                    contentResolver.insert(PoucedorProvider.CONTENT_URI, values);

                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        };
        pouceRest.teams(token, callback);
    }
}
