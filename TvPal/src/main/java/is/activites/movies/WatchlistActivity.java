package is.activites.movies;

import android.content.Context;
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
        WatchlistPagerAdapter mWatchlistAdapter = new WatchlistPagerAdapter(getSupportFragmentManager(), this);

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
        private Context context;

        public WatchlistPagerAdapter(FragmentManager fm, Context context)
        {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position)
            {
                case 0:
                    return new WatchListFragment(context);
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
