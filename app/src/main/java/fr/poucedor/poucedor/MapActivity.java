/*
 * Adapted from OSMDroid Sample App - Apache License, Version 2.0
 *
 * Created by MOLLET-PADIER Loïc and NGUYEN QUOC Olivier
 *
 */

package fr.poucedor.poucedor;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapController;

import android.content.Intent;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.support.design.widget.NavigationView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import fr.poucedor.poucedor.UI.MyItemizedOverlay;
import fr.poucedor.poucedor.provider.DatabaseContract;
import fr.poucedor.poucedor.provider.PoucedorProvider;
import fr.poucedor.poucedor.picasso.CircleTransform;

public class MapActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private DrawerLayout drawerLayout;
    private View content;
    private NavigationView navigationView;

    MyItemizedOverlay myItemizedOverlay = null;

    /**
     * Identifies a particular Loader being used in this component
     */
    private static final int URL_LOADER = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        setMap();

        initFab();
        initToolbar();
        setupDrawerLayout();

        content = findViewById(R.id.content);

        final ImageView profile_image = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        //Picasso.with(this).load(AVATAR_URL).transform(new CircleTransform()).into(avatar);

        getLoaderManager().initLoader(URL_LOADER, null, this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findMyLocation();
                Toast.makeText(MapActivity.this, "Search location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent intent;
                switch (menuItem.getItemId()) {
                    case R.id.navdrawer_item_ranking:
                        intent = new Intent(MapActivity.this, RankingActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.navdrawer_item_settings:
                        intent = new Intent(MapActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        return true;
                    default:
                        Snackbar.make(content, menuItem.getTitle() + " pressed", Snackbar.LENGTH_LONG).show();
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case URL_LOADER:
                return new CursorLoader(
                        this,
                        PoucedorProvider.CONTENT_URI,
                        new String[]{
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
        if (data != null) {
            while (data.moveToNext()) {
                double furthestLatitude = data.getDouble(data.getColumnIndex(DatabaseContract.Team.COLUMN_NAME_FD_LATITUDE));
                double furthestLongitude = data.getDouble(data.getColumnIndex(DatabaseContract.Team.COLUMN_NAME_FD_LONGITUDE));
                String teamName = data.getString(data.getColumnIndex(DatabaseContract.Team.COLUMN_NAME_NAME));
                myItemizedOverlay.addItem(new GeoPoint(furthestLatitude, furthestLongitude), teamName, null);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        return;
    }

    public void setMap() {
        MapView mapView = (MapView) findViewById(R.id.mapview);

        MapController mapController = new MapController(mapView);

        mapController.setZoom(7);
        mapView.setMinZoomLevel(4);

        // Center over France
        double lat = 51 * 1000000;
        double lon = -5 * 1000000;
        GeoPoint p = new GeoPoint((int) lat, (int) lon);
        mapController.animateTo(p);

        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);

        Drawable marker = getResources().getDrawable(R.drawable.ic_drawer_place);
        int markerWidth = marker.getIntrinsicWidth();
        int markerHeight = marker.getIntrinsicHeight();
        marker.setBounds(0, markerHeight, markerWidth, 0);

        ResourceProxy resourceProxy = new DefaultResourceProxyImpl(getApplicationContext());

        myItemizedOverlay = new MyItemizedOverlay(marker, resourceProxy);
        mapView.getOverlays().add(myItemizedOverlay);

        GeoPoint myPoint1 = new GeoPoint(0 * 1000000, 0 * 1000000);
        myItemizedOverlay.addItem(myPoint1, "myPoint1", "myPoint1");
        GeoPoint myPoint2 = new GeoPoint((int) 48.8534100 * 1000000, (int) 2.3488000 * 1000000);
        myItemizedOverlay.addItem(myPoint2, "myPoint2", "myPoint2");
    }

    public void findMyLocation() {
        System.out.println("-----------------------ETAPE 1----------------------");
        GPSTracker gps = new GPSTracker(this);

        if (gps.canGetLocation()) {
            System.out.println("-----------------------ETAPE 2----------------------");
            GeoPoint myPoint1 = new GeoPoint((int) gps.getLatitude() * 1000000, (int) gps.getLongitude() * 1000000);
            myItemizedOverlay.addItem(myPoint1, "myPoint1", "myPoint1");
            System.out.println("-----------------------ETAPE 3----------------------");
            gps.stopUsingGPS();
            System.out.println("-----------------------ETAPE 4----------------------");
        } else {
            Toast.makeText(MapActivity.this, R.string.gps_not_enable, Toast.LENGTH_SHORT).show();
        }
        //setFloatingActionButton();

        getLoaderManager().initLoader(URL_LOADER, null, this);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Map Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://fr.poucedor.poucedor/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Map Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://fr.poucedor.poucedor/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}