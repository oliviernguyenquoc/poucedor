/*
 *
 * Created by MOLLET-PADIER Loïc and NGUYEN QUOC Olivier
 *
 */

package fr.poucedor.poucedor;


import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.Toast;

import fr.poucedor.poucedor.provider.DatabaseContract;
import fr.poucedor.poucedor.provider.PoucedorProvider;


public class RankingActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    //UI Reference

    private static final int URL_LOADER = 0;

    SimpleCursorAdapter mAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        listView = (ListView)findViewById(R.id.ranking_list);

        getLoaderManager().initLoader(URL_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case URL_LOADER:
                return new CursorLoader(
                        this,
                        PoucedorProvider.CONTENT_URI,
                        new String[] {
                                DatabaseContract.Team._ID,
                                DatabaseContract.Team.COLUMN_NAME_FD,
                                DatabaseContract.Team.COLUMN_NAME_FD_LATITUDE,
                                DatabaseContract.Team.COLUMN_NAME_FD_LONGITUDE,
                                DatabaseContract.Team.COLUMN_NAME_LAST_LATITUDE,
                                DatabaseContract.Team.COLUMN_NAME_LAST_LONGITUDE,
                                DatabaseContract.Team.COLUMN_NAME_NAME,
                                DatabaseContract.Team.COLUMN_NAME_STUDENT1_NAME,
                                DatabaseContract.Team.COLUMN_NAME_STUDENT2_NAME
                        },
                        null,
                        null,
                        null
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        String[] fromColumns = new String[] {
                DatabaseContract.Team.COLUMN_NAME_NAME,
                DatabaseContract.Team.COLUMN_NAME_FD
        };
        int[] toViews = new int[] {
                R.id.ranking_team_name,
                R.id.ranking_distance
        };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.ranking_item, data, fromColumns, toViews, 0);
        listView.setAdapter(adapter);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}

