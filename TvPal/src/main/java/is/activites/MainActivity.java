package is.activites;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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

        // set a custom shadow that overlays the main content when the drawer opens
        _DrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        _DrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, _ChannelTitles));
        _DrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        _DrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                _DrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
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

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        Intent intent;
        switch (position)
        {
            case 0:
                intent = new Intent(this, DisplayRuvActivity.class);
                break;
            case 1:
                intent = new Intent(this, DisplayStod2Activity.class);
                break;
            case 5:
                intent = new Intent(this, DisplaySkjarinnActivity.class);
                break;
            default:
                intent = new Intent(this, MainActivity.class);
        }
        startActivity(intent);

        // update selected item and title, then close the drawer
        _DrawerList.setItemChecked(position, true);
        _DrawerLayout.closeDrawer(_DrawerList);
    }

    public void getSchedulesRuv_clickEvent(View view)
    {
        try
        {
            boolean networkAvailable = _connectivityListener.isNetworkAvailable(this);

            if (networkAvailable)
            {
                Intent intent = new Intent(this, DisplayRuvActivity.class);
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

    public void getSchedulesStod2_clickEvent(View view)
    {
        try
        {
            boolean networkAvailable = _connectivityListener.isNetworkAvailable(this);

            if (networkAvailable)
            {
                Intent intent = new Intent(this, DisplayStod2Activity.class);
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

    public void getSchedulesSkjarinn_clickEvent(View view)
    {
        try
        {
            boolean networkAvailable = _connectivityListener.isNetworkAvailable(this);

            if (networkAvailable)
            {
                Intent intent = new Intent(this, DisplaySkjarinnActivity.class);
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
