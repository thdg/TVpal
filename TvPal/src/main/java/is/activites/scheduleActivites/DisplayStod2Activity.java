package is.activites.scheduleActivites;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import is.activites.baseActivities.BaseFragmentActivity;
import is.activites.MainActivity;
import is.contracts.datacontracts.EventData;
import is.parsers.schedules.Stod2ScheduleParser;
import is.utilities.ConnectionListener;
import is.utilities.DateUtil;
import is.tvpal.R;

/**
 * Created by Arnar
 *
 * This class handles the activity to show Stöð 2 events.
 * It extends ListActivity to show it as a List.
 * It implements ItemClickListener to handle click events.
 * @see android.app.ListActivity
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DisplayStod2Activity extends BaseFragmentActivity implements ActionBar.TabListener
{
    private List<EventData> _events;
    private String _workingDate;
    private SchedulePagerAdapter mScheduleAdapter;
    private ViewPager mViewPager;
    private ProgressBar mProgressBar;
    private TextView mNoResults;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_events);

        Initialize();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void Initialize()
    {
        _workingDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        mProgressBar = (ProgressBar) findViewById(R.id.progressSchedules);
        mNoResults = (TextView) findViewById(R.id.noSchedules);

        Intent intent = getIntent();
        new DownloadStod2Schedules(this).execute(intent.getStringExtra(MainActivity.EXTRA_STOD2));

        setTitle(intent.getStringExtra(MainActivity.EXTRA_TITLE));

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void CreateTabViews()
    {
        mScheduleAdapter = new SchedulePagerAdapter(getSupportFragmentManager(), this);

        final ActionBar actionBar = getActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mViewPager = (ViewPager) findViewById(R.id.pagerSchedules);
        mViewPager.setAdapter(mScheduleAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position)
            {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mScheduleAdapter.getCount(); i++)
        {
            actionBar.addTab(actionBar.newTab()
                    .setText(mScheduleAdapter.getPageTitle(i))
                    .setTabListener(this));
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}

    public class SchedulePagerAdapter extends FragmentStatePagerAdapter
    {
        private Context cxt;

        public SchedulePagerAdapter(FragmentManager fm, Context cxt)
        {
            super(fm);
            this.cxt = cxt;
        }

        @Override
        public Fragment getItem(int position)
        {
            Fragment fragment = new ScheduleFragment(cxt);
            Bundle args = new Bundle();

            String date;

            if (position == 0)
                date = _workingDate;
            else
                date = DateUtil.AddDaysToDate(_workingDate, position);

            ArrayList<EventData> _todaySchedule = new ArrayList<EventData>();

            for (EventData e : _events)
            {
                if (e.getEventDate().equalsIgnoreCase(date))
                    _todaySchedule.add(e);
            }

            args.putSerializable(ScheduleFragment.EXTRA_SCHEDULE_DAY, _todaySchedule);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount()
        {
            //TODO: Hardcoded length for now
            return 8;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            //TODO: Find better way to set tab titles, this is very gay
            if (position == 0)
                return DateUtil.GetDateFormatForTabs(cxt, _workingDate);
            else
            {
                String date = DateUtil.AddDaysToDate(_workingDate, position);
                return DateUtil.GetDateFormatForTabs(cxt, date);
            }
        }
    }

    private class DownloadStod2Schedules extends AsyncTask<String, Void, Boolean>
    {
        private Context mContext;

        public DownloadStod2Schedules(Context context)
        {
            this.mContext = context;
        }

        @Override
        protected Boolean doInBackground(String... urls)
        {
            try
            {
                return GetSchedules(urls[0]);
            }
            catch (IOException e)
            {
                return false;
            }
        }

        @Override
        protected void onPreExecute()
        {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            if (result)
            {
                CreateTabViews();
            }
            else
            {
                mNoResults.setVisibility(View.VISIBLE);

                boolean networkStatus = new ConnectionListener(mContext).isNetworkAvailable();

                if (!networkStatus)
                    Toast.makeText(mContext, "Turn on network", Toast.LENGTH_SHORT).show();
            }

            mProgressBar.setVisibility(View.GONE);
        }

        private Boolean GetSchedules(String myurl) throws IOException
        {
            try
            {
                Stod2ScheduleParser parser = new Stod2ScheduleParser(myurl);
                _events = parser.GetSchedules();

                _workingDate = _events.get(0).getEventDate();

                return true;
            }
            catch (Exception ex)
            {
                Log.e(getClass().getName(), ex.getMessage());
            }

            return false;
        }
    }
}