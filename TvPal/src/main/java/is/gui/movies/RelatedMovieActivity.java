package is.gui.movies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;

import java.util.List;
import is.gui.base.BaseActivity;
import is.contracts.datacontracts.trakt.TraktMovieDetailedData;
import is.handlers.adapters.TraktRelatedMoviesAdapter;
import is.parsers.trakt.TraktParser;
import is.tvpal.R;

public class RelatedMovieActivity extends BaseActivity
{
    private GridView mGridView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_related_movies);

        Initialize();
    }

    private void Initialize()
    {
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String imdbId = intent.getStringExtra(DetailedMovieActivity.EXTRA_MOVIEID);

        mGridView = (GridView) findViewById(R.id.relatedMovies);
        mProgressBar = (ProgressBar) findViewById(R.id.progressIndicator);

        new GetRelatedMoviesWorker(this, imdbId).execute();
    }

    private class GetRelatedMoviesWorker extends AsyncTask<String, Void, List<TraktMovieDetailedData>>
    {
        private String imdbId;
        private Context mContext;

        public GetRelatedMoviesWorker(Context context, String imdbId)
        {
            this.imdbId = imdbId;
            this.mContext = context;
        }

        @Override
        protected List<TraktMovieDetailedData> doInBackground(String... strings)
        {
            try
            {
                return GetReleatedMovies();
            }
            catch (Exception ex)
            {
                Log.e(getClass().getName(), ex.getMessage());
            }

            return null;
        }

        @Override
        protected void onPreExecute()
        {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<TraktMovieDetailedData> movies)
        {
            if (movies == null || movies.size() != 0)
                mGridView.setAdapter(new TraktRelatedMoviesAdapter(mContext, R.layout.listview_related_movie, movies));

            mProgressBar.setVisibility(View.GONE);
        }

        private List<TraktMovieDetailedData> GetReleatedMovies()
        {
            TraktParser parser = new TraktParser();

            return parser.GetReleatedMovies(imdbId);
        }
    }
}
