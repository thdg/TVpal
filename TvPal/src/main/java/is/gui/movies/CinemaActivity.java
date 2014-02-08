package is.gui.movies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.haarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;
import java.util.List;
import is.gui.base.BaseActivity;
import is.contracts.datacontracts.cinema.CinemaMovie;
import is.handlers.adapters.CinemaAdapter;
import is.parsers.cinema.CinemaParser;
import is.tvpal.R;

public class CinemaActivity extends BaseActivity implements AdapterView.OnItemClickListener
{
    public static final String EXTRA_MOVIE = "is.activites.cinemaActivities.MOVIE";

    private CinemaAdapter mAdapter;
    private ListView mListView;
    private ProgressBar mProgressBar;
    private TextView mNoResults;

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
        mNoResults = (TextView) findViewById(R.id.cinemaNoResults);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        new MovieScedulesWorker(this).execute();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
    {
        CinemaMovie movie = mAdapter.getItem(position);

        Intent intent = new Intent(this, DetailedCinemaActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        startActivity(intent);
    }

    /**
     * A class which creates a new thread to Download Cinema Data
     * @author Arnar
     */
    private class MovieScedulesWorker extends AsyncTask<String, Void, List<CinemaMovie>>
    {
        private Context mContext;

        public MovieScedulesWorker(Context context)
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
            if (movies == null || movies.size() == 0)
            {
                mNoResults.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }
            else
            {
                mAdapter = new CinemaAdapter(mContext, R.layout.listview_cinema_schedules, movies);

                //Animations boy
                ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(mAdapter);
                scaleInAnimationAdapter.setAbsListView(mListView);
                mListView.setAdapter(scaleInAnimationAdapter);

                mProgressBar.setVisibility(View.GONE);
                mNoResults.setVisibility(View.GONE);
            }
        }

        public List<CinemaMovie> getMovies()
        {
            CinemaParser parser = new CinemaParser();
            return parser.GetMovieSchedules();
        }
    }
}
