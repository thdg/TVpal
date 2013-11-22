package is.activites.showActivities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import is.activites.baseActivities.BaseFragmentActivity;
import is.tvpal.R;

public class SearchMoviesActivity extends BaseFragmentActivity
{
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_search);

        Initialize();
    }

    private void Initialize()
    {
        SearchPagerAdapter mScheduleAdapter = new SearchPagerAdapter(getSupportFragmentManager(), this);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager = (ViewPager) findViewById(R.id.pagerSearch);
        mViewPager.setAdapter(mScheduleAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                mViewPager.setCurrentItem(position);
            }
        });
    }

    public class SearchPagerAdapter extends FragmentStatePagerAdapter
    {
        private Context context;

        public SearchPagerAdapter(FragmentManager fm, Context context)
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
                    return new SearchMovieFragment(context);
                case 1:
                    return new TrendingMoviesFragment(context);
            }
            return null;
        }

        @Override
        public int getCount()
        {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return position == 0 ? "Search" : "Trending";
        }
    }
}
