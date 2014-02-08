package is.gui.movies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;

import is.gui.base.BaseFragmentActivity;
import is.tvpal.R;

public class SearchMoviesActivity extends BaseFragmentActivity
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
        SearchPagerAdapter mScheduleAdapter = new SearchPagerAdapter(getSupportFragmentManager());

        getActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager = (ViewPager) findViewById(R.id.pagerView);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trending_movies, menu);
    }

    public class SearchPagerAdapter extends FragmentStatePagerAdapter
    {
        public SearchPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position)
            {
                case 0:
                    return SearchMovieFragment.newInstance();
                case 1:
                    return TrendingMoviesFragment.newInstance();
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
