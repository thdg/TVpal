package is.gui.shows;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;

import is.gui.base.BaseFragmentActivity;
import is.contracts.datacontracts.EpisodeData;
import is.handlers.database.DbEpisodes;
import is.tvpal.R;

/**
 * An activity that display fragments in TabViews, each
 * fragment holds detailed information about a show.
 * @author Arnar
 * @see android.app.ActionBar.TabListener
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class EpisodeActivity extends BaseFragmentActivity
{
    private int _seriesId;
    private int _seasonNr;
    private int _pos;
    private ViewPager mViewPager;
    private PagerSlidingTabStrip mTabStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_strip_episodes);

        Initialize();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void Initialize()
    {
        Intent intent = getIntent();

        //TODO: Find a better way to set title. (Siggi)
        setTitle(String.format("%s",""));

        _seriesId = intent.getIntExtra(SingleSeasonActivity.EXTRA_SERIESID, 0);
        _seasonNr = intent.getIntExtra(SingleSeasonActivity.EXTRA_SEASONNR, 0);
        _pos = intent.getIntExtra(SingleSeasonActivity.EXTRA_SELECTED, 0);
        mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        CreateTabViews();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void CreateTabViews()
    {
        DbEpisodes db = new DbEpisodes(this);

        EpisodePagerAdapter mScheduleAdapter = new EpisodePagerAdapter(getSupportFragmentManager(), db.GetCursorEpisodesDetailed(_seriesId, _seasonNr));

        getActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager = (ViewPager) findViewById(R.id.pagerTabs);
        mViewPager.setAdapter(mScheduleAdapter);
        mTabStrip.setViewPager(mViewPager);
        mViewPager.setCurrentItem(_pos, false);
    }

    public class EpisodePagerAdapter extends FragmentStatePagerAdapter
    {
        private Cursor mCursor;

        public EpisodePagerAdapter(FragmentManager fm, Cursor cursor)
        {
            super(fm);
            mCursor = cursor;
        }

        @Override
        public Fragment getItem(int position)
        {
            mCursor.moveToPosition(position);

            EpisodeData episode = new EpisodeData();
            episode.setEpisodeName(mCursor.getString(Episodes.EpisodeName));
            episode.setSeasonNumber(mCursor.getInt(Episodes.Season));
            episode.setOverview(mCursor.getString(Episodes.Overview));
            episode.setAired(mCursor.getString(Episodes.Aired));
            episode.setDirector(mCursor.getString(Episodes.Director));
            episode.setRating(mCursor.getString(Episodes.Rating));
            episode.setEpisodeId(mCursor.getInt(Episodes.EpisodeId));
            episode.setSeen(mCursor.getInt(Episodes.Seen));
            episode.setGuestStars(mCursor.getString(Episodes.GuestStars));

            return EpisodeFragment.newInstance(episode);
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

           return String.format("%d-%d", mCursor.getInt(Episodes.Season), mCursor.getInt(Episodes.Episode));
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