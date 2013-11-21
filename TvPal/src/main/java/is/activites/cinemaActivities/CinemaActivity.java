package is.activites.cinemaActivities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import java.util.List;
import is.activites.baseActivities.BaseActivity;
import is.contracts.datacontracts.Cinema.CinemaMovie;
import is.handlers.adapters.CinemaAdapter;
import is.parsers.cinema.CinemaParser;
import is.tvpal.R;

public class CinemaActivity extends BaseActivity implements AdapterView.OnItemClickListener
{
    public static final String EXTRA_MOVIE = "is.activites.cinemaActivities.MOVIE";

    private CinemaAdapter mAdapter;
    private ListView mListView;
    private ProgressBar mProgressBar;

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
        mListView.setOnItemClickListener(this);
        mProgressBar = (ProgressBar) findViewById(R.id.progressIndicator);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        new GetMovieScedules(this).execute();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
    {
        CinemaMovie movie = mAdapter.getItem(position);

        Intent intent = new Intent(this, DetailedMovieActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        startActivity(intent);
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
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<CinemaMovie> movies)
        {
            mAdapter = new CinemaAdapter(mContext, R.layout.listview_cinema_schedules, movies);
            mListView.setAdapter(mAdapter);
            mProgressBar.setVisibility(View.GONE);
        }

        public List<CinemaMovie> getMovies()
        {
            CinemaParser parser = new CinemaParser();
            return parser.GetMovieSchedules();
        }
    }
}
