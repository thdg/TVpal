package is.activites.cinemaActivities;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import is.contracts.datacontracts.Cinema.CinemaMovie;
import is.handlers.adapters.CinemaAdapter;
import is.handlers.adapters.TraktAdapter;
import is.parsers.cinema.CinemaParser;
import is.tvpal.R;

public class CinemaActivity extends Activity
{
    private CinemaAdapter mAdapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema);

        Initialize();
    }

    private void Initialize()
    {
        mListView = (ListView) findViewById(R.id.cinemaSchedules);

        new GetMovieScedules(this).execute();
    }

    /**
     * A class which creates a new thread to Download Cinema Data
     * @author Arnar
     */
    private class GetMovieScedules extends AsyncTask<String, Void, List<CinemaMovie>>
    {
        private Context mContext;

        public GetMovieScedules(Context context)
        {
            this.mContext = context;
        }

        @Override
        protected List<CinemaMovie> doInBackground(String... strings)
        {
            return getMovies();
        }

        @Override
        protected void onPreExecute()
        {
            //TODO:Initialize the progress bar
        }

        @Override
        protected void onPostExecute(List<CinemaMovie> movies)
        {
            mAdapter = new CinemaAdapter(mContext, R.layout.listview_cinema_schedules, movies);
            mListView.setAdapter(mAdapter);
            //Todo: Close progressbar
        }

        public List<CinemaMovie> getMovies()
        {
            CinemaParser parser = new CinemaParser();
            return parser.GetMovieSchedules();
        }
    }
}
