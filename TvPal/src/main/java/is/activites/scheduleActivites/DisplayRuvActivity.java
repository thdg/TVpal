package is.activites.scheduleActivites;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import is.contracts.datacontracts.EventData;
import is.parsers.RuvScheduleParser;
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
public class DisplayRuvActivity extends FragmentActivity implements ActionBar.TabListener
{
    public static final String RuvUrl = "http://muninn.ruv.is/files/xml/ruv/";

    private List<EventData> _events;
    private ProgressDialog _waitingDialog;
    private String _workingDate;
    private SchedulePagerAdapter mScheduleAdapter;
    private ViewPager mViewPager;

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
        String ruvUrl = String.format("%s%s", RuvUrl, DateUtil.GetCorrectRuvUrlFormat());
        new DownloadRuvSchedules(this).execute(ruvUrl);

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void CreateTabViews()
    {
        mScheduleAdapter = new SchedulePagerAdapter(getSupportFragmentManager(), this);

        final ActionBar actionBar = getActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mViewPager = (ViewPager) findViewById(R.id.pager);
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
            return 6;
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

    private class DownloadRuvSchedules extends AsyncTask<String, Void, Boolean>
    {
        private Context ctx;

        public DownloadRuvSchedules(Context context)
        {
            this.ctx = context;
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
                Log.e(getClass().getName(), e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPreExecute()
        {
            _waitingDialog = new ProgressDialog(ctx);
            _waitingDialog.setMessage(ctx.getString(R.string.loadingSchedule));
            _waitingDialog.setCancelable(false);
            _waitingDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean successful)
        {
            if (successful)
            {
                CreateTabViews();
            }
            _waitingDialog.dismiss();
            //Todo: Return some error message for the user
        }

        private boolean GetSchedules(String myurl) throws IOException
        {
            try
            {
                RuvScheduleParser parser = new RuvScheduleParser(myurl);
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