/**
 * Adapted from Google I/O 2014 App - Apache License, Version 2.0
 *
 * @author MOLLET-PADIER Loïc and NGUYEN QUOC Olivier
 */

package fr.poucedor.poucedor;


import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import fr.poucedor.poucedor.provider.PoucedorService;
import fr.poucedor.poucedor.provider.RestFactory;
import fr.poucedor.poucedor.provider.RestFactoryProvider;

/**
 * BaseActivity is an abstract class which are herited by all classes wich contains Action bar.
 * This activity add to child Activities ActionBar, NavigationDrawer ...
 * <p/>
 * To Add the navigation drawer, you must override getSelfNavDrawerItem() function.
 *
 * @see BaseActivity# getSelfNavDrawerItem() to have more explanation
 * <p/>
 * Too add item in the navigation drawer, you just have to add few things in lists and constants.
 */
public class BaseActivity extends AppCompatActivity {

    // Class Variables

    /**
     * Symbols for navdrawer items (indices must correspond to array below).
     * This is not a list of items that are necessarily *present* in the Nav Drawer;
     * rather, it's a list of all possible items.
     */
    protected static final int NAVDRAWER_ITEM_MAP = 0;
    protected static final int NAVDRAWER_ITEM_RANKING = 1;
    protected static final int NAVDRAWER_ITEM_SETTINGS = 2;
    protected static final int NAVDRAWER_ITEM_ALERT = 3;
    protected static final int NAVDRAWER_ITEM_LOG_OUT = 4;
    protected static final int NAVDRAWER_ITEM_INVALID = -1;
    protected static final int NAVDRAWER_ITEM_SEPARATOR = -2;
    protected static final int NAVDRAWER_ITEM_SEPARATOR_SPECIAL = -3;

    public static final String PREFS_NAME = "MyPrefsFile";

    /**
     * Titles for navdrawer items (indices must correspond to the above)
     */
    private static final int[] NAVDRAWER_TITLE_RES_ID = new int[]{
            R.string.navdrawer_item_map,
            R.string.navdrawer_item_ranking,
            R.string.navdrawer_item_settings,
            R.string.navdrawer_item_alert,
            R.string.navdrawer_item_log_out,
    };

    /**
     * Icons for navdrawer items (indices must correspond to above array)
     */
    private static final int[] NAVDRAWER_ICON_RES_ID = new int[]{
            R.drawable.ic_drawer_map,  // Map
            R.drawable.ic_drawer_ranking,  // Ranking
            R.drawable.ic_drawer_settings,
            R.drawable.ic_drawer_alert,
            R.drawable.ic_drawer_log_out,
    };

    /**
     * Delay to launch nav drawer item, to allow close animation to play
     */
    private static final int NAVDRAWER_LAUNCH_DELAY = 250;

    /**
     * Fade in durations for the main content when switching between different Activities
     * of the app through the Navigation Drawer
     */
    private static final int MAIN_CONTENT_FADEOUT_DURATION = 150;

    /**
     * Navigation drawer
     */
    private LinearLayout mAccountListContainer;
    private ViewGroup mDrawerItemsListContainer;
    private Handler mHandler;

    private Toolbar toolbar;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    protected Context mContext;

    /**
     * List of navigation drawer items that were actually added to the navigation drawer, in order
     */
    private ArrayList<Integer> mNavDrawerItems = new ArrayList<Integer>();

    /**
     * Views that correspond to each navdrawer item, null if not yet created
     */
    private View[] mNavDrawerItemViews = null;

    /**
     * RestFactory
     */
    private RestFactoryProvider restFactoryProvider;

    // Android Life Cycle Methods

    public static void setAccessibilityIgnore(View view) {
        view.setClickable(false);
        view.setFocusable(false);
        view.setContentDescription("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String token = getSharedPreferences(PREFS_NAME,0).getString("token", null);
        this.restFactoryProvider = new RestFactoryProvider(token, getContentResolver());
        restFactoryProvider.getServerData();
        mHandler = new Handler();

        mContext = BaseActivity.this;

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initToolbar();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpNav() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(BaseActivity.this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigationView = (NavigationView) findViewById(R.id.nav_view);


        // Setting Navigation View Item Selected Listener to handle the item
        // click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            public boolean onNavigationItemSelected(MenuItem menuItem) {

                // Checking if the item is in checked state or not, if not make
                // it in checked state
                if (menuItem.isChecked())
                    menuItem.setChecked(false);
                else
                    menuItem.setChecked(true);

                // Closing drawer on item click
                drawerLayout.closeDrawers();

                // Check to see which item was being clicked and perform
                // appropriate action
                Intent intent;
                switch (menuItem.getItemId()) {

                    case R.id.navdrawer_item_map:
                        return true;
                        // Handle the camera action
                    case R.id.navdrawer_item_ranking:
                        return true;
                    case R.id.navdrawer_item_settings:
                        return true;
                    case R.id.navdrawer_item_alert:
                        return true;
                    case R.id.navdrawer_item_log_out:
                        return true;
                }
                return false;
            }
        });

        // Setting the actionbarToggle to drawer layout

        // calling sync state is necessay or else your hamburger icon wont show
        // up
        drawerToggle.syncState();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        setUpNav();
        drawerToggle.syncState();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

/*    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navdrawer_item_map) {
            // Handle the camera action
        } else if (id == R.id.navdrawer_item_ranking) {

        } else if (id == R.id.navdrawer_item_settings) {

        } else if (id == R.id.navdrawer_item_alert) {

        } else if (id == R.id.navdrawer_item_log_out) {

        }

        DrawerLayout mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }*/
}
