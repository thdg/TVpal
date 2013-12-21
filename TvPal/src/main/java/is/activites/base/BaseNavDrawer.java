package is.activites.base;

import android.annotation.TargetApi;
import android.app.Activity;
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

import is.activites.movies.CinemaActivity;
import is.activites.movies.SearchMoviesActivity;
import is.activites.movies.WatchlistActivity;
import is.activites.schedules.DisplayRuvActivity;
import is.activites.schedules.DisplaySkjarinnActivity;
import is.activites.schedules.DisplayStod2Activity;
import is.activites.shows.MyShowsActivity;
import is.activites.shows.SearchShowsActivity;
import is.activites.shows.UpcomingRecentActivity;
import is.contracts.datacontracts.DrawerListData;
import is.contracts.datacontracts.DrawerListHeader;
import is.contracts.datacontracts.IDrawerItem;
import is.handlers.adapters.DrawerListAdapter;
import is.tvpal.R;

/**
 * Created by Arnar on 21.12.2013.
 */
public class BaseNavDrawer extends Activity
{
    public static final String EXTRA_STOD2 = "is.activites.STOD2";
    public static final String EXTRA_TITLE = "is.activites.TITLE";

    private DrawerLayout _drawerLayout;
    private ListView _drawerList;
    private ActionBarDrawerToggle _drawerToggle;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void InitializeNavDrawer()
    {
        _drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        _drawerList = (ListView) findViewById(R.id.left_drawer);

        List<IDrawerItem> items = new ArrayList<IDrawerItem>();
        items.add(new DrawerListHeader(getResources().getString(R.string.schedule_header)));
        items.add(new DrawerListData(getResources().getString(R.string.ruv), R.drawable.ruv_svartur_64));
        items.add(new DrawerListData(getResources().getString(R.string.stod_2), R.drawable.stod2_64));
        items.add(new DrawerListData(getResources().getString(R.string.stod_2_sport), R.drawable.stod2sport_64));
        items.add(new DrawerListData(getResources().getString(R.string.stod_2_bio), R.drawable.stod2bio_64));
        items.add(new DrawerListData(getResources().getString(R.string.stod_3), R.drawable.stod3_64));
        items.add(new DrawerListData(getResources().getString(R.string.skjar_einn), R.drawable.skjareinn_64));
        items.add(new DrawerListHeader(getResources().getString(R.string.shows_header)));
        items.add(new DrawerListData(getResources().getString(R.string.search_show), R.drawable.m_glass_64));
        items.add(new DrawerListData(getResources().getString(R.string.my_shows), R.drawable.eye_64));
        items.add(new DrawerListData(getString(R.string.upcoming_shows), R.drawable.calendar_64));
        items.add(new DrawerListHeader(getString(R.string.movieText)));
        items.add(new DrawerListData(getString(R.string.cinemaSchedules), R.drawable.cinema_64));
        items.add(new DrawerListData(getString(R.string.trakt_search_movies), R.drawable.m_glass_64));
        items.add(new DrawerListData(getString(R.string.watchlist), R.drawable.watchlist_64));

        _drawerList.setAdapter(
                new DrawerListAdapter(this, items)
        );
        _drawerList.setOnItemClickListener(new DrawerItemClickListener());

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        _drawerToggle = new ActionBarDrawerToggle(
                this,
                _drawerLayout,
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
        _drawerLayout.setDrawerListener(_drawerToggle);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        _drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        _drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        return super.onOptionsItemSelected(item) || _drawerToggle.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            selectItem(position);
        }
    }

    private void selectItem(int position)
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
                    break;
                case 3:
                    intent = new Intent(this, DisplayStod2Activity.class);
                    intent.putExtra(EXTRA_STOD2, getResources().getString(R.string.stod2SportBaseUrl));
                    intent.putExtra(EXTRA_TITLE, getResources().getString(R.string.stod_2_sport));
                    break;
                case 4:
                    intent = new Intent(this, DisplayStod2Activity.class);
                    intent.putExtra(EXTRA_STOD2, getResources().getString(R.string.stod2BioBaseUrl));
                    intent.putExtra(EXTRA_TITLE, getResources().getString(R.string.stod_2_bio));
                    break;
                case 5:
                    intent = new Intent(this, DisplayStod2Activity.class);
                    intent.putExtra(EXTRA_STOD2, getResources().getString(R.string.stod3BaseUrl));
                    intent.putExtra(EXTRA_TITLE, getResources().getString(R.string.stod_3));
                    break;
                case 6:
                    intent = new Intent(this, DisplaySkjarinnActivity.class);
                    break;
                case 8:
                    intent = new Intent(this, SearchShowsActivity.class);
                    break;
                case 9:
                    intent = new Intent(this, MyShowsActivity.class);
                    break;
                case 10:
                    intent = new Intent(this, UpcomingRecentActivity.class);
                    break;
                case 12:
                    intent = new Intent(this, CinemaActivity.class);
                    break;
                case 13:
                    intent = new Intent(this, SearchMoviesActivity.class);
                    break;
                case 14:
                    intent = new Intent(this, WatchlistActivity.class);
                    break;
            }

            _drawerList.setItemChecked(position, true);
            _drawerLayout.closeDrawer(_drawerList);

            startActivity(intent);
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
