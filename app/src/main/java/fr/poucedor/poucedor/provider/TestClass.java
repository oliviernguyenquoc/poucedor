package fr.poucedor.poucedor.provider;

import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import fr.poucedor.poucedor.provider.DatabaseContract;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by loic on 09/01/15.
 */
public class TestClass {

    public TestClass() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://www.poucedor.fr/") // The base API endpoint.
                .build();

        PoucedorService pouceRest = restAdapter.create(PoucedorService.class);


        Callback<List<Team>> callback = new Callback<List<Team>>() {
            @Override
            public void success(List<Team> teams, Response response) {
//                PoucedorProvider poucedorProvider = ;
                ContentValues values;
                for (Team team : teams) {
                    PoucedorProvider poucedorProvider = new PoucedorProvider();
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

                    // todo fixme!
                    poucedorProvider.insert(PoucedorProvider.CONTENT_URI, values);

                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        };

        pouceRest.teams("HELP", callback);
    }
}
