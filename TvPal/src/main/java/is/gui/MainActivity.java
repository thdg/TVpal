package is.gui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import is.gui.base.BaseNavDrawer;
import is.contracts.datacontracts.StatisticData;
import is.handlers.database.DbEpisodes;
import is.tvpal.R;

public class MainActivity extends BaseNavDrawer
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initialize();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        new GetStatisticWorker(this).execute();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void Initialize()
    {
        InitializeNavDrawer();
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private class GetStatisticWorker extends AsyncTask<String, Void, StatisticData>
    {
        private Context mContext;

        public GetStatisticWorker(Context context)
        {
            this.mContext = context;
        }

        @Override
        protected StatisticData doInBackground(String... strings)
        {
            return GetStatisticData();
        }

        @Override
        protected void onPostExecute(StatisticData statisticData)
        {
            ((TextView) findViewById(R.id.mainNumberOfShows)).setText(String.format("You have %d shows in MyShows",
                                                                      statisticData.getNumberOfShows()));
            ((TextView) findViewById(R.id.mainShowsAiredYesterday)).setText(String.format("Yesterday %d of your shows were aired",
                                                                                  statisticData.getNumberOfAiredShows()));
            ((TextView) findViewById(R.id.mainNumberOfMoviesInWatchList)).setText(String.format("You have %d movies in your Watchlist",
                                                                            statisticData.getNumberOfMovies()));
        }

        private StatisticData GetStatisticData()
        {
            return new DbEpisodes(mContext).GetStatisticData();
        }
    }
}
