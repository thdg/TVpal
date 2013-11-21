package is.activites.showActivities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import is.contracts.datacontracts.TraktMovieData;
import is.handlers.adapters.TraktMoviesAdapter;
import is.parsers.trakt.TraktParser;
import is.tvpal.R;

/**
 * Created by Arnar on 21.11.2013.
 */
public class TrendingMoviesFragment extends Fragment
{
    private Context mContext;
    private ListView mListView;
    private TraktMoviesAdapter mAdapter;
    private ProgressBar mProgressBar;

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
        mProgressBar = (ProgressBar) getView().findViewById(R.id.progressIndicator);

        new GetTrendingMovies().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_trakt_movies, container, false);
    }

    private class GetTrendingMovies extends AsyncTask<String, Void, List<TraktMovieData>>
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
            mAdapter = new TraktMoviesAdapter(mContext, R.layout.listview_trakt_movies, movies);
            mListView.setAdapter(mAdapter);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}
