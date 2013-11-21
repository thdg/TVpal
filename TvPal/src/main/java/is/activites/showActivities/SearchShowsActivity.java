package is.activites.showActivities;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;

import is.activites.baseActivities.BaseFragmentActivity;
import is.tvpal.R;

public class SearchShowsActivity extends BaseFragmentActivity implements ActionBar.TabListener
{
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_series);

        Initialize();
    }

    private void Initialize()
    {
        SearchPagerAdapter mScheduleAdapter = new SearchPagerAdapter(getSupportFragmentManager(), this);

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mScheduleAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {
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
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {}

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
                    return new SearchShowFragment(context);
                case 1:
                    return new TrendingEpisodesFragment(context);
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
