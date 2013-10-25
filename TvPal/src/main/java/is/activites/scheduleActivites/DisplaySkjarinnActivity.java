package is.activites.scheduleActivites;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import is.datacontracts.EventData;
import is.handlers.adapters.EventAdapter;
import is.parsers.SkjarinnScheduleParser;
import is.rules.Helpers;
import is.tvpal.R;

/**
 * Created by Arnar
 *
 * This class handles the activity to show Skjárinn events.
 * It extends ListActivity to show it as a List.
 * It implements ItemClickListener to handle click events.
 * It implements SwipeGestureFilter.SimpleGestureListener to handle swipe events.
 * @see android.app.ListActivity
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DisplaySkjarinnActivity extends FragmentActivity implements ActionBar.TabListener
{
    public static final String EXTRA_TITLE = "is.activites.TITLE";
    public static final String EXTRA_DESCRIPTION = "is.activites.DESCRIPTION";
    public static final String EXTRA_START = "is.activites.START";
    public static final String EXTRA_DURATION = "is.activites.DURATION";
    public static final String skjarinnUrl = "http://www.skjarinn.is/einn/dagskrarupplysingar/?channel_id=7&weeks=1&output_format=xml";

    private ProgressDialog _waitingDialog;
    private static List<EventData> _events;
    private String _workingDate;

    private SchedulePagerAdapter mScheduleAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_schedules);

        Initialize();
    }

    private void Initialize()
    {
        _workingDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        new DownloadSkjarinnSchedules(this).execute(skjarinnUrl);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void CreateTabViews()
    {
        mScheduleAdapter = new SchedulePagerAdapter(getSupportFragmentManager(), this);

        final ActionBar actionBar = getActionBar();

        actionBar.setHomeButtonEnabled(false);

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
            Fragment fragment = new ScheduleFragment();
            Bundle args = new Bundle();

            String date;

            if (position == 0)
                date = _workingDate;
            else
                date = Helpers.AddDaysToDate(_workingDate, position);

            args.putString(ScheduleFragment.ARG_SCHEDULE_DAY, date);
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
                return Helpers.GetDateFormatForTabs(cxt, _workingDate);
            else
            {
                String date = Helpers.AddDaysToDate(_workingDate, position);
                return Helpers.GetDateFormatForTabs(cxt, date);
            }
        }
    }

    public class ScheduleFragment extends Fragment implements AdapterView.OnItemClickListener
    {
        public static final String ARG_SCHEDULE_DAY = "schedule_day";

        private EventAdapter _adapterView;

        public ScheduleFragment()
        {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.skjarinn_schedules, container, false);
            Bundle args = getArguments();
            String day = args.getString(ARG_SCHEDULE_DAY);
            List<EventData> _todaySchedule = new ArrayList<EventData>();

            for (EventData e : _events)
            {
                if (e.getEventDate().equalsIgnoreCase(day))
                    _todaySchedule.add(e);
            }

            _adapterView = new EventAdapter(inflater.getContext(), R.layout.listview_event, _todaySchedule);

            if (rootView != null)
            {
                ((ListView) rootView.findViewById(R.id.schedules)).setAdapter(_adapterView);
                ((ListView) rootView.findViewById(R.id.schedules)).setOnItemClickListener(this);
            }

            return rootView;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
        {
            EventData selectedEvent = _adapterView.getItem(i);

            Intent intent = new Intent(getApplicationContext(), DetailedEventActivity.class);
            intent.putExtra(EXTRA_TITLE, selectedEvent.getTitle());
            intent.putExtra(EXTRA_DESCRIPTION, selectedEvent.getDescription());
            intent.putExtra(EXTRA_START, selectedEvent.getStartTime());
            intent.putExtra(EXTRA_DURATION, selectedEvent.getDuration());

            startActivity(intent);
        }
    }

    private class DownloadSkjarinnSchedules extends AsyncTask<String, Void, String>
    {
        private Context ctx;

        public DownloadSkjarinnSchedules(Context context)
        {
            this.ctx = context;
        }

        @Override
        protected String doInBackground(String... urls)
        {
            try
            {
                return GetSchedules(urls[0]);
            }
            catch (IOException e)
            {
                return "Unable to retrieve web page. URL may be invalid";
            }
        }

        @Override
        protected void onPreExecute()
        {
            _waitingDialog = new ProgressDialog(ctx);
            _waitingDialog.setMessage(ctx.getString(R.string.loadingSchedule));
            _waitingDialog.show();
        }

        @Override
        protected void onPostExecute(String result)
        {
            CreateTabViews();
            _waitingDialog.dismiss();
        }

        private String GetSchedules(String myurl) throws IOException
        {
            try
            {
                SkjarinnScheduleParser parser = new SkjarinnScheduleParser(myurl);
                _events = parser.GetSchedules();

                _workingDate = _events.get(0).getEventDate();
            }
            catch (Exception ex)
            {
                ex.getMessage();
            }

            return "Successful";
        }
    }
}