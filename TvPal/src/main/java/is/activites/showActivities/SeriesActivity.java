package is.activites.showActivities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import is.activites.baseActivities.BaseFragmentActivity;
import is.handlers.database.DbShowHandler;
import is.tvpal.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SeriesActivity extends BaseFragmentActivity
{
    private ViewPager mViewPager;
    private int seriesId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_pager_view);

        Initialize();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void Initialize()
    {
        Intent intent = getIntent();

        setTitle(intent.getStringExtra(MyShowsActivity.EXTRA_NAME));
        seriesId = intent.getIntExtra(MyShowsActivity.EXTRA_SERIESID, 0);

        SeriesPagerAdapter mScheduleAdapter = new SeriesPagerAdapter(getSupportFragmentManager(), this);

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

    public class SeriesPagerAdapter extends FragmentStatePagerAdapter
    {
        private Context context;

        public SeriesPagerAdapter(FragmentManager fm, Context context)
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
                    DbShowHandler db = new DbShowHandler(context);
                    return OverviewFragment.newInstance(context, db.GetCursorOverview(seriesId));
                case 1:
                    return SeasonFragment.newInstance(context, seriesId);
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
            return position == 0 ? "Overview" : "Seasons";
        }
    }
}
