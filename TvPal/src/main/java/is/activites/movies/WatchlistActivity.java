package is.activites.movies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import is.activites.baseActivities.BaseFragmentActivity;
import is.tvpal.R;

public class WatchlistActivity extends BaseFragmentActivity
{
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_pager_view);

        Initialize();
    }

    private void Initialize()
    {
        WatchlistPagerAdapter mWatchlistAdapter = new WatchlistPagerAdapter(getSupportFragmentManager());

        getActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager = (ViewPager) findViewById(R.id.pagerView);
        mViewPager.setAdapter(mWatchlistAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                mViewPager.setCurrentItem(position);
            }
        });
    }

    public class WatchlistPagerAdapter extends FragmentStatePagerAdapter
    {
        public WatchlistPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position)
            {
                case 0:
                    return WatchListFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount()
        {
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return "Watchlist";
        }
    }
}
