package is.gui.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import is.gui.movies.CinemaActivity;
import is.gui.movies.SearchMoviesActivity;
import is.gui.movies.WatchlistActivity;
import is.gui.schedules.DisplayRuvActivity;
import is.gui.schedules.DisplaySkjarinnActivity;
import is.gui.schedules.DisplayStod2Activity;
import is.gui.shows.MyShowsActivity;
import is.gui.shows.SearchShowsActivity;
import is.gui.shows.UpcomingRecentActivity;
import is.contracts.datacontracts.DrawerListData;
import is.contracts.datacontracts.DrawerListHeader;
import is.contracts.datacontracts.IDrawerItem;
import is.handlers.adapters.DrawerListAdapter;
import is.parsers.cache.SchedulesCache;
import is.tvpal.R;

public class BaseNavDrawer extends Activity
{
    public static final String EXTRA_STOD2 = "is.activites.STOD2";
    public static final String EXTRA_TITLE = "is.activites.TITLE";
    public static final String EXTRA_SCHEDULESCACHE = "is.activites.STOD2CACHE";
    public static final String EXTRA_LATESTUPDATE = "is.activites.STOD2LATESTUPDATE";

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void InitializeNavDrawer()
    {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        List<IDrawerItem> items = new ArrayList<IDrawerItem>();
        items.add(new DrawerListHeader(getResources().getString(R.string.schedule_header)));
        items.add(new DrawerListData(getResources().getString(R.string.ruv), R.drawable.ruv_svartur_64, false));
        items.add(new DrawerListData(getResources().getString(R.string.stod_2), R.drawable.stod2_64, false));
        items.add(new DrawerListData(getResources().getString(R.string.stod_2_sport), R.drawable.stod2sport_64, false));
        items.add(new DrawerListData(getResources().getString(R.string.stod_2_sport2), R.drawable.stod2sport2_64, false));
        items.add(new DrawerListData(getResources().getString(R.string.stod_2_bio), R.drawable.stod2bio_64, false));
        items.add(new DrawerListData(getResources().getString(R.string.stod_2_gull), R.drawable.stod2gull_64, false));
        items.add(new DrawerListData(getResources().getString(R.string.stod_3), R.drawable.stod3_64, false));
        items.add(new DrawerListData(getResources().getString(R.string.skjar_einn), R.drawable.skjareinn_64, true));
        items.add(new DrawerListHeader(getResources().getString(R.string.shows_header)));
        items.add(new DrawerListData(getResources().getString(R.string.search_show), R.drawable.m_glass_64, false));
        items.add(new DrawerListData(getResources().getString(R.string.my_shows), R.drawable.eye_64, false));
        items.add(new DrawerListData(getString(R.string.upcoming_shows), R.drawable.calendar_64, true));
        items.add(new DrawerListHeader(getString(R.string.movieText)));
        items.add(new DrawerListData(getString(R.string.cinemaSchedules), R.drawable.cinema_64, false));
        items.add(new DrawerListData(getString(R.string.trakt_search_movies), R.drawable.m_glass_64, false));
        items.add(new DrawerListData(getString(R.string.watchlist), R.drawable.watchlist_64, true));

        mDrawerList.setAdapter(
                new DrawerListAdapter(this, items)
        );
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        return super.onOptionsItemSelected(item) || mDrawerToggle.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            StartActivity(position, view);
        }
    }

    private void StartActivity(int position, View view)
    {
        try
        {
            Intent intent = null;

            switch (position)
            {
                case 1:
                    intent = new Intent(this, DisplayRuvActivity.class);
                    break;
                case 2:
                    intent = new Intent(this, DisplayStod2Activity.class);
                    intent.putExtra(EXTRA_STOD2, getResources().getString(R.string.stod2BaseUrl));
                    intent.putExtra(EXTRA_TITLE, getResources().getString(R.string.stod_2));
                    intent.putExtra(EXTRA_SCHEDULESCACHE, SchedulesCache.Stod2Schedules);
                    intent.putExtra(EXTRA_LATESTUPDATE, SchedulesCache.Stod2LatestUpdate);
                    break;
                case 3:
                    intent = new Intent(this, DisplayStod2Activity.class);
                    intent.putExtra(EXTRA_STOD2, getResources().getString(R.string.stod2SportBaseUrl));
                    intent.putExtra(EXTRA_TITLE, getResources().getString(R.string.stod_2_sport));
                    intent.putExtra(EXTRA_SCHEDULESCACHE, SchedulesCache.Stod2SportSchedules);
                    intent.putExtra(EXTRA_LATESTUPDATE, SchedulesCache.Stod2SportLatestUpdate);
                    break;
                case 4:
                    intent = new Intent(this, DisplayStod2Activity.class);
                    intent.putExtra(EXTRA_STOD2, getResources().getString(R.string.stod_2_sport2_baseurl));
                    intent.putExtra(EXTRA_TITLE, getResources().getString(R.string.stod_2_sport2));
                    intent.putExtra(EXTRA_SCHEDULESCACHE, SchedulesCache.Stod2Sport2Schedules);
                    intent.putExtra(EXTRA_LATESTUPDATE, SchedulesCache.Stod2Sport2LatestUpdate);
                    break;
                case 5:
                    intent = new Intent(this, DisplayStod2Activity.class);
                    intent.putExtra(EXTRA_STOD2, getResources().getString(R.string.stod2BioBaseUrl));
                    intent.putExtra(EXTRA_TITLE, getResources().getString(R.string.stod_2_bio));
                    intent.putExtra(EXTRA_SCHEDULESCACHE, SchedulesCache.Stod2BioSchedules);
                    intent.putExtra(EXTRA_LATESTUPDATE, SchedulesCache.Stod2BioLatestUpdate);
                    break;
                case 6:
                    intent = new Intent(this, DisplayStod2Activity.class);
                    intent.putExtra(EXTRA_STOD2, getResources().getString(R.string.stod2gullBaseUrl));
                    intent.putExtra(EXTRA_TITLE, getResources().getString(R.string.stod_2_gull));
                    intent.putExtra(EXTRA_SCHEDULESCACHE, SchedulesCache.Stod2GullSchedules);
                    intent.putExtra(EXTRA_LATESTUPDATE, SchedulesCache.Stod2GullLatestUpdate);
                    break;
                case 7:
                    intent = new Intent(this, DisplayStod2Activity.class);
                    intent.putExtra(EXTRA_STOD2, getResources().getString(R.string.stod3BaseUrl));
                    intent.putExtra(EXTRA_TITLE, getResources().getString(R.string.stod_3));
                    intent.putExtra(EXTRA_SCHEDULESCACHE, SchedulesCache.Stod3Schedules);
                    intent.putExtra(EXTRA_LATESTUPDATE, SchedulesCache.Stod3LatestUpdate);
                    break;
                case 8:
                    intent = new Intent(this, DisplaySkjarinnActivity.class);
                    break;
                case 10:
                    intent = new Intent(this, SearchShowsActivity.class);
                    break;
                case 11:
                    intent = new Intent(this, MyShowsActivity.class);
                    break;
                case 12:
                    intent = new Intent(this, UpcomingRecentActivity.class);
                    break;
                case 14:
                    intent = new Intent(this, CinemaActivity.class);
                    break;
                case 15:
                    intent = new Intent(this, SearchMoviesActivity.class);
                    break;
                case 16:
                    intent = new Intent(this, WatchlistActivity.class);
                    break;
            }

            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerList);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            {
                ActivityOptions activityTransition = ActivityOptions.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight());
                startActivity(intent, activityTransition.toBundle());
            }
            else
            {
                startActivity(intent);
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
