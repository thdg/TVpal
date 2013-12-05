package is.activites.movies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import is.contracts.datacontracts.trakt.TraktMovieData;
import is.handlers.adapters.TraktMoviesAdapter;
import is.parsers.trakt.TraktParser;
import is.tvpal.R;

/**
 * Created by Arnar on 21.11.2013.
 */
public class TrendingMoviesFragment extends Fragment implements AdapterView.OnItemClickListener
{
    public static final String EXTRA_MOVIEID = "is.activites.movieActivities.MOVIEID";
    public static final String EXTRA_MOVIEPOSTER = "is.activites.movieActivities.MOVIEPOSTER";

    private Context mContext;
    private ListView mListView;
    private TraktMoviesAdapter mAdapter;
    private ProgressBar mProgressBar;
    private TextView mNoResults;

    public TrendingMoviesFragment(Context context)
    {
        this.mContext = context;
    }

    public TrendingMoviesFragment() {}

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mListView = (ListView) getView().findViewById(R.id.trendingTrakt);
        mListView.setOnItemClickListener(this);
        mProgressBar = (ProgressBar) getView().findViewById(R.id.progressIndicator);
        mNoResults = (TextView) getView().findViewById(R.id.traktNoResults);

        new TrendingMoviesWorker().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_trakt_movies, container, false);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
    {
        TraktMovieData data = mAdapter.getItem(position);
        Intent intent = new Intent(mContext, DetailedMovieActivity.class);
        intent.putExtra(EXTRA_MOVIEID, data.getImdbId());
        intent.putExtra(EXTRA_MOVIEPOSTER, data.getImage().getPoster());
        startActivity(intent);
    }

    private class TrendingMoviesWorker extends AsyncTask<String, Void, List<TraktMovieData>>
    {
        @Override
        protected List<TraktMovieData> doInBackground(String... strings)
        {
            try {
                return new TraktParser().GetTrendingMovies();
            }
            catch (Exception ex)
            {
                return new ArrayList<TraktMovieData>();
            }
        }

        @Override
        protected void onPreExecute()
        {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<TraktMovieData> movies)
        {
            if (movies == null)
            {
                mNoResults.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }
            else
            {
                mAdapter = new TraktMoviesAdapter(mContext, R.layout.listview_trakt_movies, movies);
                mListView.setAdapter(mAdapter);
                mProgressBar.setVisibility(View.INVISIBLE);
                mNoResults.setVisibility(View.GONE);
            }
        }
    }
}
