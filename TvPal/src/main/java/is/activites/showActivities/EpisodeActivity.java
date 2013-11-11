package is.activites.showActivities;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import is.datacontracts.EpisodeData;
import is.handlers.database.DbShowHandler;
import is.tvpal.R;

/**
 * An activity that display fragments in TabViews, each
 * fragment holds detailed information about a show.
 * @author Arnar
 * @see android.app.ActionBar.TabListener
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class EpisodeActivity extends FragmentActivity implements ActionBar.TabListener
{
    private String _seriesId;
    private String _seasonNr;
    private int _pos;
    private EpisodePagerAdapter mScheduleAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_swipe_view);

        Initialize();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void Initialize()
    {
        Intent intent = getIntent();

        //TODO: Find a better way to set title. (Siggi)
        setTitle(String.format("%s",""));

        _seriesId = intent.getStringExtra(SingleSeasonActivity.EXTRA_SERIESID);
        _seasonNr = intent.getStringExtra(SingleSeasonActivity.EXTRA_SEASONNR);
        _pos = intent.getIntExtra(SingleSeasonActivity.EXTRA_SELECTED, 0);

        CreateTabViews();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void CreateTabViews()
    {
        DbShowHandler db = new DbShowHandler(this);

        mScheduleAdapter = new EpisodePagerAdapter(getSupportFragmentManager(), db.GetCursorEpisodesDetailed(_seriesId, _seasonNr) ,this);

        final ActionBar actionBar = getActionBar();

        getActionBar().setDisplayHomeAsUpEnabled(true);
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

        actionBar.setSelectedNavigationItem(_pos);
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}

    public class EpisodePagerAdapter extends FragmentStatePagerAdapter
    {
        private Context cxt;
        private Cursor mCursor;

        public EpisodePagerAdapter(FragmentManager fm, Cursor cursor , Context cxt)
        {
            super(fm);
            this.cxt = cxt;
            mCursor = cursor;
        }

        @Override
        public Fragment getItem(int position)
        {
            mCursor.moveToPosition(position);

            Fragment fragment = new EpisodeFragment(cxt);
            Bundle args = new Bundle();

            EpisodeData episode = new EpisodeData();
            episode.setEpisodeName(mCursor.getString(Episodes.EpisodeName));
            episode.setSeasonNumber(mCursor.getString(Episodes.Season));
            episode.setOverview(mCursor.getString(Episodes.Overview));
            episode.setAired(mCursor.getString(Episodes.Aired));
            episode.setDirector(mCursor.getString(Episodes.Director));
            episode.setRating(mCursor.getString(Episodes.Rating));
            episode.setEpisodeId(mCursor.getString(Episodes.EpisodeId));
            episode.setSeen(mCursor.getString(Episodes.Seen));
            episode.setGuestStars(mCursor.getString(Episodes.GuestStars));

            args.putSerializable(EpisodeFragment.EPISODE_FRAGMENT, episode);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount()
        {
            return mCursor.getCount();
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
           mCursor.moveToPosition(position);

           return String.format("%s-%s", mCursor.getString(3), mCursor.getString(1));
        }
    }

    private interface Episodes
    {
        int EpisodeId = 0;
        int Episode = 1;
        int EpisodeName = 2;
        int Season = 3;
        int Overview = 4;
        int Aired = 5;
        int Director = 6;
        int Rating = 7;
        int Seen = 8;
        int GuestStars = 9;
    }
}