package is.activites.movies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import is.activites.baseActivities.BaseFragment;
import is.contracts.datacontracts.trakt.TraktMovieData;
import is.handlers.adapters.TraktMoviesAdapter;
import is.handlers.database.DbMovies;
import is.parsers.trakt.TraktParser;
import is.tvpal.R;

/**
 * Created by Arnar on 21.11.2013.
 */
public class TrendingMoviesFragment extends BaseFragment implements AdapterView.OnItemClickListener
{
    public static final String EXTRA_MOVIEID = "is.activites.movieActivities.MOVIEID";
    public static final String EXTRA_MOVIEPOSTER = "is.activites.movieActivities.MOVIEPOSTER";

    private Context mContext;
    private ListView mListView;
    private TraktMoviesAdapter mAdapter;
    private ProgressBar mProgressBar;
    private TextView mNoResults;

    public TrendingMoviesFragment() {}

    public static TrendingMoviesFragment newInstance()
    {
        TrendingMoviesFragment fragment = new TrendingMoviesFragment();

        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mContext = activity.getContext();
        mListView = (ListView) getView().findViewById(R.id.trendingTrakt);
        mListView.setOnItemClickListener(this);
        mProgressBar = (ProgressBar) getView().findViewById(R.id.progressIndicator);
        mNoResults = (TextView) getView().findViewById(R.id.traktNoResults);

        registerForContextMenu(mListView);

        new TrendingMoviesWorker().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_trakt_trending, container, false);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.trending_movies, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        switch (item.getItemId())
        {
            case R.id.add_to_watchlist:
                AddMovieToWatchList(position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void AddMovieToWatchList(int position)
    {
        try
        {
            TraktMovieData movie = mAdapter.getItem(position);
            DbMovies db = new DbMovies(mContext);
            db.AddMovieToWatchList(movie);

            Toast.makeText(mContext, String.format("Added %s to your watchlist", movie.getTitle()), Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex)
        {
            Log.e(getClass().getName(), ex.getMessage());
        }
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
