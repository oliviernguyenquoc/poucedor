/**
 * Adapted from Google I/O 2014 App - Apache License, Version 2.0
 *
 * @author MOLLET-PADIER Loïc and NGUYEN QUOC Olivier
 */

package fr.poucedor.poucedor;


import android.content.Intent;

import android.os.Handler;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.net.Uri;

/**
 * BaseActivity is an abstract class which are herited by all classes wich contains Action bar.
 * This activity add to child Activities ActionBar, NavigationDrawer ...
 * <p/>
 * To Add the navigation drawer, you must override getSelfNavDrawerItem() function.
 *
 * @see BaseActivity#getSelfNavDrawerItem() to have more explanation
 * <p/>
 * Too add item in the navigation drawer, you just have to add few things in lists and constants.
 */
public abstract class BaseActivity extends ActionBarActivity {

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
    private DrawerLayout drawer;
    private Handler mHandler;

    /**
     * Toolbar
     */
    private Toolbar toolbar;

    /**
     * List of navigation drawer items that were actually added to the navigation drawer, in order
     */
    private ArrayList<Integer> mNavDrawerItems = new ArrayList<Integer>();

    /**
     * Views that correspond to each navdrawer item, null if not yet created
     */
    private View[] mNavDrawerItemViews = null;

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

        mHandler = new Handler();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Setup of the navigation drawer
        setupNavDrawer();

        // Setup of the little box on the top of the navigation drawer
        setupAccountBox();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(Gravity.START);
                return true;
            case R.id.about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        } else {
            super.onBackPressed();
        }
    }

    // Override this function to setup the action bar just after the usual instruction setContentView
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getActionBarToolbar();
    }

    /**
     * Set up OR NOT the navigation drawer.
     *
     * @see BaseActivity#getSelfNavDrawerItem() to have more explanation about setting either the
     * navigation drawer icon or a back button in the action bar
     */
    private void setupNavDrawer() {
        // What nav drawer item should be selected?
        int selfItem = getSelfNavDrawerItem();

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        if (drawer == null) {
            return;
        }

        drawer.setStatusBarBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        if (selfItem == NAVDRAWER_ITEM_INVALID) {
            // do not show a nav drawer
            if (drawer != null) {
                drawer.removeView(drawer);
            }
            drawer = null;
            return;
        }

        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //To avoid double item selection, we update the list every click thanks to "populateNavDrawer()"
                    populateNavDrawer();
                    drawer.openDrawer(Gravity.START);
                }
            });
        }

        drawer.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);

        // Populate the nav drawer with the correct items
        populateNavDrawer();


        //TODO It can be cool if we can do it
        // Easy to do
        /*
        // When the user runs the app for the first time, we want to land them with the
        // navigation drawer open. But just the first time.
        if (!PrefUtils.isWelcomeDone(this)) {
            // first run of the app starts with the nav drawer open
            PrefUtils.markWelcomeDone(this);
            drawer.openDrawer(Gravity.START);
        }
        */

    }

    /**
     * Returns the navigation drawer item that corresponds to this Activity. Subclasses
     * of BaseActivity override this to indicate what nav drawer item corresponds to them
     *
     * @return NAVDRAWER_ITEM_INVALID to mean that this Activity should not have a Nav Drawer.
     */
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_INVALID;
    }

    protected Toolbar getActionBarToolbar() {
        if (toolbar == null) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
            }
        }
        return toolbar;
    }

    /**
     * Populates the navigation drawer with the appropriate items.
     */
    private void populateNavDrawer() {
        mNavDrawerItems.clear();

        // Main items
        mNavDrawerItems.add(NAVDRAWER_ITEM_MAP);
        mNavDrawerItems.add(NAVDRAWER_ITEM_RANKING);

        // Indication (Can be usefull) : mNavDrawerItems.add(NAVDRAWER_ITEM_SEPARATOR);
        mNavDrawerItems.add(NAVDRAWER_ITEM_SEPARATOR_SPECIAL);

        // Other items
        mNavDrawerItems.add(NAVDRAWER_ITEM_SETTINGS);
        mNavDrawerItems.add(NAVDRAWER_ITEM_ALERT);
        mNavDrawerItems.add(NAVDRAWER_ITEM_LOG_OUT);

        createNavDrawerItems();
    }

    private void createNavDrawerItems() {
        mDrawerItemsListContainer = (ViewGroup) findViewById(R.id.navdrawer_items_list);
        if (mDrawerItemsListContainer == null) {
            return;
        }

        mNavDrawerItemViews = new View[mNavDrawerItems.size()];
        mDrawerItemsListContainer.removeAllViews();
        int i = 0;
        for (int itemId : mNavDrawerItems) {
            mNavDrawerItemViews[i] = makeNavDrawerItem(itemId, mDrawerItemsListContainer);
            mDrawerItemsListContainer.addView(mNavDrawerItemViews[i]);
            ++i;
        }
    }

    private View makeNavDrawerItem(final int itemId, ViewGroup container) {
        boolean selected = getSelfNavDrawerItem() == itemId;
        int layoutToInflate = 0;
        if (itemId == NAVDRAWER_ITEM_SEPARATOR) {
            layoutToInflate = R.layout.navdrawer_separator;
        } else if (itemId == NAVDRAWER_ITEM_SEPARATOR_SPECIAL) {
            layoutToInflate = R.layout.navdrawer_separator;
        } else {
            layoutToInflate = R.layout.navdrawer_item;
        }
        View view = getLayoutInflater().inflate(layoutToInflate, container, false);

        if (isSeparator(itemId)) {
            // we are done
            setAccessibilityIgnore(view); //TODO Check and verify. I think this is ok
            return view;
        }

        ImageView iconView = (ImageView) view.findViewById(R.id.icon);
        TextView titleView = (TextView) view.findViewById(R.id.title);
        int iconId = itemId >= 0 && itemId < NAVDRAWER_ICON_RES_ID.length ?
                NAVDRAWER_ICON_RES_ID[itemId] : 0;
        int titleId = itemId >= 0 && itemId < NAVDRAWER_TITLE_RES_ID.length ?
                NAVDRAWER_TITLE_RES_ID[itemId] : 0;

        // set icon and text
        iconView.setVisibility(iconId > 0 ? View.VISIBLE : View.GONE);
        if (iconId > 0) {
            iconView.setImageResource(iconId);
        }
        titleView.setText(getString(titleId));

        formatNavDrawerItem(view, itemId, selected);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavDrawerItemClicked(itemId);
            }
        });

        return view;
    }

    private boolean isSeparator(int itemId) {
        return itemId == NAVDRAWER_ITEM_SEPARATOR || itemId == NAVDRAWER_ITEM_SEPARATOR_SPECIAL;
    }

    private void formatNavDrawerItem(View view, int itemId, boolean selected) {
        if (isSeparator(itemId)) {
            // not applicable
            return;
        }

        ImageView iconView = (ImageView) view.findViewById(R.id.icon);
        TextView titleView = (TextView) view.findViewById(R.id.title);

        if (selected) {
            view.setBackgroundResource(R.drawable.selected_navdrawer_item_background);
        }

        // Configure its appearance according to whether or not it's selected
        titleView.setTextColor(selected ?
                getResources().getColor(R.color.navdrawer_text_color_selected) :
                getResources().getColor(R.color.navdrawer_text_color));
        iconView.setColorFilter(selected ?
                getResources().getColor(R.color.navdrawer_icon_tint_selected) :
                getResources().getColor(R.color.navdrawer_icon_tint));
    }

    private void onNavDrawerItemClicked(final int itemId) {
        if (itemId == getSelfNavDrawerItem()) {
            drawer.closeDrawer(Gravity.START);
            return;
        }

        if (isSpecialItem(itemId)) {
            goToNavDrawerItem(itemId);
        } else {
            // Launch the target Activity after a short delay, to allow the close animation to play
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToNavDrawerItem(itemId);
                }
            }, NAVDRAWER_LAUNCH_DELAY);

            // Change the active item on the list so the user can see the item changed
            setSelectedNavDrawerItem(itemId);
            // Fade out the main content
            View mainContent = findViewById(getLayoutResource());
            if (mainContent != null) {
                mainContent.animate().alpha(0).setDuration(MAIN_CONTENT_FADEOUT_DURATION);
            }
        }

        drawer.closeDrawer(Gravity.START);
    }

    private boolean isSpecialItem(int itemId) {
        return itemId == NAVDRAWER_ITEM_SETTINGS;
    }

    private void goToNavDrawerItem(int item) {
        Intent intent;
        switch (item) {
            case NAVDRAWER_ITEM_MAP:
                intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                finish();
                break;
            case NAVDRAWER_ITEM_RANKING:
                intent = new Intent(this, RankingActivity.class);
                startActivity(intent);
                break;
            case NAVDRAWER_ITEM_SETTINGS:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case NAVDRAWER_ITEM_ALERT:
                String number = "112";
                Uri call = Uri.parse("tel:" + number);
                Intent surf = new Intent(Intent.ACTION_DIAL, call);
                startActivity(surf);
                break;
            case NAVDRAWER_ITEM_LOG_OUT:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    /**
     * Sets up the given navigation drawer item's appearance to the selected state. Note: this could
     * also be accomplished (perhaps more cleanly) with state-based layouts.
     */
    private void setSelectedNavDrawerItem(int itemId) {
        if (mNavDrawerItemViews != null) {
            for (int i = 0; i < mNavDrawerItemViews.length; i++) {
                if (i < mNavDrawerItems.size()) {
                    int thisItemId = mNavDrawerItems.get(i);
                    formatNavDrawerItem(mNavDrawerItemViews[i], thisItemId, itemId == thisItemId);
                }
            }
        }
    }

    /*
        WARNING : Users are not allowed to upload their photos on the website yet.
        This features may be implemented in the futur.
        Any codes in comment can help the link between the website and the Android App
     */

    /**
     * Sets up the account box. The account box is the area at the top of the nav drawer that
     * shows which account the user is logged in as, and lets them switch accounts.
     */

    private void setupAccountBox() {
        mAccountListContainer = (LinearLayout) findViewById(R.id.account_list);

        if (mAccountListContainer == null) {
            //This activity does not have an account box
            return;
        }

        ImageView coverImageView = (ImageView) findViewById(R.id.profile_cover_image);
        TextView name1TextView = (TextView) findViewById(R.id.profile_name1_text);
        TextView name2TextView = (TextView) findViewById(R.id.profile_name2_text);
        TextView teamName = (TextView) findViewById(R.id.profile_team_name_text);

        /*
            Warning : Identification by photo may be include here
            After that the code will let the Program take the default photo
         */

        String name1 = "Olivier NGUYEN QUOC";
        String name2 = "Loïc MOLLET PADIER";  //TODO Link with the database -> Contester's names
        String team = "Golden Thumb super genial cool"; //TODO Link with the database -> Team name

        name1TextView.setText(name1);
        name2TextView.setText(name2);

        teamName.setText(team);

        coverImageView.setImageResource(R.drawable.default_cover);

    }

    protected boolean isNavDrawerOpen() {
        return drawer != null && drawer.isDrawerOpen(Gravity.START);
    }

    protected void closeNavDrawer() {
        if (drawer != null) {
            drawer.closeDrawer(Gravity.START);
        }
    }

    protected void toolbarSetUpCase() {
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.this.finish();
            }
        });
    }

    /**
     * Must be override to return the layout of child Activity
     *
     * @return the reference of the layout of child Activity
     */
    protected abstract int getLayoutResource();

}
