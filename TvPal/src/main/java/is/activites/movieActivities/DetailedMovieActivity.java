package is.activites.movieActivities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import is.activites.baseActivities.BaseActivity;
import is.contracts.datacontracts.trakt.TraktMovieDetailedData;
import is.parsers.trakt.TraktParser;
import is.tvpal.R;
import is.utilities.PictureTask;
import is.utilities.StringUtil;

public class DetailedMovieActivity extends BaseActivity
{
    private ProgressBar mProgressBar;
    private TextView mOverview;
    private TextView mTitle;
    private ImageView mPoster;
    private TextView mRuntime;
    private TextView mGenres;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_movie);

        Initialize();
    }

    private void Initialize()
    {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        mProgressBar = (ProgressBar) findViewById(R.id.progressIndicator);
        mOverview = (TextView) findViewById(R.id.movieOverview);
        mTitle = (TextView) findViewById(R.id.movieTitle);
        mPoster = (ImageView) findViewById(R.id.moviePoster);
        mRuntime = (TextView) findViewById(R.id.movieRuntime);
        mGenres = (TextView) findViewById(R.id.movieGenres);

        String movieId = intent.getStringExtra(TrendingMoviesFragment.EXTRA_MOVIEID);
        String moviePoster = intent.getStringExtra(TrendingMoviesFragment.EXTRA_MOVIEPOSTER);

        //TODO: Return some error message
        if(movieId == null)
            return;

        new GetMovieDetailed().execute(movieId);
        new GetPosterShow().execute(moviePoster);
    }

    private class GetMovieDetailed extends AsyncTask<String, Void, TraktMovieDetailedData>
    {
        @Override
        protected TraktMovieDetailedData doInBackground(String... strings)
        {
            return GetMovieDetailed(strings[0]);
        }

        @Override
        protected void onPreExecute()
        {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(TraktMovieDetailedData movie)
        {
            mProgressBar.setVisibility(View.GONE);

            setTitle(movie.getTitle());

            mOverview.setText(movie.getOverview());
            mTitle.setText(movie.getTitle());
            mRuntime.setText(String.format("Runtime: %s min", movie.getRuntime()));
            mGenres.setText(String.format("Genres: %s", StringUtil.JoinArrayToString(movie.getGenres())));
        }

        public TraktMovieDetailedData GetMovieDetailed(String movieId)
        {
            TraktParser parser = new TraktParser();

            return parser.GetMovieDetailed(movieId);
        }
    }

    private class GetPosterShow extends AsyncTask<String, Void, Bitmap>
    {
        @Override
        protected Bitmap doInBackground(String... strings)
        {
            return GetPoster(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap)
        {
            if (bitmap != null)
                mPoster.setImageBitmap(bitmap);
        }

        private Bitmap GetPoster(String posterUrl)
        {
            try
            {
                String formattedPosterUrl = StringUtil.formatTrendingPosterUrl(posterUrl, "-300");
                PictureTask task = new PictureTask();
                return task.getBitmapFromUrl(formattedPosterUrl);
            }
            catch (Exception ex)
            {
                Log.e(getClass().getName(), ex.getMessage());
            }

            return null;
        }
    }

}
