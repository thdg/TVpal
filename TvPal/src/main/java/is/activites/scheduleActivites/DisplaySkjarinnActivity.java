package is.activites.scheduleActivites;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import is.activites.baseActivities.BaseFragmentActivity;
import is.contracts.datacontracts.EventData;
import is.parsers.schedules.SkjarinnScheduleParser;
import is.utilities.DateUtil;
import is.tvpal.R;

/**
 * Created by Arnar
 *
 * This class handles the activity to show Rúv events.
 * It extends FragmentActivity, it shows ListActivity for each Fragment
 * It implements ActionBar.TabListener to handle swipe between views.
 * @author Arnar
 * @see import android.support.v4.app.FragmentActivity;
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DisplaySkjarinnActivity extends BaseFragmentActivity implements ActionBar.TabListener
{
    public static final String skjarinnUrl = "http://www.skjarinn.is/einn/dagskrarupplysingar/?channel_id=7&weeks=1&output_format=xml";

    private static List<EventData> _events;
    private String _workingDate;
    private SchedulePagerAdapter mScheduleAdapter;
    private ViewPager mViewPager;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_events);

        Initialize();
    }

    private void Initialize()
    {
        _workingDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        mProgressBar = (ProgressBar) findViewById(R.id.progressSchedules);
        new DownloadSkjarinnSchedules().execute(skjarinnUrl);

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

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
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {}
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
            return 7;
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

    private class DownloadSkjarinnSchedules extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {
            try
            {
                return GetSchedules(urls[0]);
            }
            catch (IOException e)
            {
                Log.e(getClass().getName(), e.getMessage());
                return "Unable to retrieve web page. URL may be invalid";
            }
        }

        @Override
        protected void onPreExecute()
        {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String result)
        {
            if (result.equalsIgnoreCase("Successful"))
                CreateTabViews();

            //Return error message if doInBackground fails
            mProgressBar.setVisibility(View.GONE);
        }

        private String GetSchedules(String myurl) throws IOException
        {
            try
            {
                SkjarinnScheduleParser parser = new SkjarinnScheduleParser(myurl);
                _events = parser.GetSchedules();

                _workingDate = _events.get(0).getEventDate();

                return "Successful";
            }
            catch (Exception ex)
            {
                Log.e(getClass().getName(), ex.getMessage());
            }

            return "Error";
        }
    }
}