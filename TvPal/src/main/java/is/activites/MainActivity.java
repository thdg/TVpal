package is.activites;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import is.rules.ConnectionListener;
import is.rules.Helpers;
import is.tvpal.R;

public class MainActivity extends Activity
{
    public static final String EXTRA_STOD2 = "is.activites.STOD2";

    private ConnectionListener _connectivityListener;

    private DrawerLayout _DrawerLayout;
    private ListView _DrawerList;
    private ActionBarDrawerToggle _DrawerToggle;
    private String[] _ChannelTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initialize();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void Initialize()
    {
        _connectivityListener = new ConnectionListener();

        _ChannelTitles = getResources().getStringArray(R.array.channels_array);
        _DrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        _DrawerList = (ListView) findViewById(R.id.left_drawer);

        _DrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        _DrawerList.setAdapter(
                new ArrayAdapter<String>(this,R.layout.drawer_list_item, _ChannelTitles)
        );
        _DrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        _DrawerToggle = new ActionBarDrawerToggle(
                this,
                _DrawerLayout,
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
        _DrawerLayout.setDrawerListener(_DrawerToggle);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        _DrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        _DrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (_DrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position)
    {
        try
        {
            boolean networkAvailable = _connectivityListener.isNetworkAvailable(this);

            if(networkAvailable)
            {
                Intent intent;
                switch (position)
                {
                    case 0:
                        intent = new Intent(this, DisplayRuvActivity.class);
                        break;
                    case 1:
                        intent = new Intent(this, DisplayStod2Activity.class);
                        intent.putExtra(EXTRA_STOD2, getResources().getString(R.string.stod2BaseUrl));
                        break;
                    case 2:
                        intent = new Intent(this, DisplayStod2Activity.class);
                        intent.putExtra(EXTRA_STOD2, getResources().getString(R.string.stod2SportBaseUrl));
                        break;
                    case 3:
                        intent = new Intent(this, DisplayStod2Activity.class);
                        intent.putExtra(EXTRA_STOD2, getResources().getString(R.string.stod2BioBaseUrl));
                        break;
                    case 4:
                        intent = new Intent(this, DisplayStod2Activity.class);
                        intent.putExtra(EXTRA_STOD2, getResources().getString(R.string.stod3BaseUrl));
                        break;
                    case 5:
                        intent = new Intent(this, DisplaySkjarinnActivity.class);
                        break;
                    default:
                        intent = new Intent(this, MainActivity.class);
                }

                _DrawerList.setItemChecked(position, true);
                _DrawerLayout.closeDrawer(_DrawerList);

                startActivity(intent);
            }
            else
            {
                Helpers.showNetworkAlertDialog(this);
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
