package is.activites.movies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import is.activites.baseActivities.BaseActivity;
import is.contracts.datacontracts.trakt.TraktMovieDetailedData;
import is.parsers.trakt.TraktParser;
import is.tvpal.R;
import is.utilities.ExternalIntents;
import is.utilities.PictureTask;
import is.utilities.StringUtil;

public class DetailedMovieActivity extends BaseActivity
{
    public static final String EXTRA_MOVIE = "is.activites.movieActivities.EXTRA_MOVIE";
    public static final String EXTRA_MOVIEID = "is.activites.movieActivities.EXTRA_MOVIEID";

    private ProgressBar mProgressBar;
    private TextView mOverview;
    private TextView mTitle;
    private ImageView mPoster;
    private TextView mRuntime;
    private TextView mGenres;
    private Button mYoutubeIntent;
    private Button mImdbIntent;
    private LinearLayout mLayout;
    private TextView mActors;
    private TextView mDirectors;
    private Button mTraktIntent;
    private TextView mRating;
    private TextView mReleaseYear;
    private Button mTraktCommentsActivity;

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

        mLayout = (LinearLayout) findViewById(R.id.layoutShowMore);
        mProgressBar = (ProgressBar) findViewById(R.id.progressIndicator);
        mOverview = (TextView) findViewById(R.id.movieOverview);
        mTitle = (TextView) findViewById(R.id.movieTitle);
        mPoster = (ImageView) findViewById(R.id.moviePoster);
        mRuntime = (TextView) findViewById(R.id.movieRuntime);
        mGenres = (TextView) findViewById(R.id.movieGenres);
        mYoutubeIntent = (Button) findViewById(R.id.startYoutubeIntent);
        mImdbIntent = (Button) findViewById(R.id.startImdbIntent);
        mActors = (TextView) findViewById(R.id.movieActors);
        mDirectors = (TextView) findViewById(R.id.movieDirectors);
        mTraktIntent = (Button) findViewById(R.id.startTraktIntent);
        mRating = (TextView) findViewById(R.id.movieRating);
        mReleaseYear = (TextView) findViewById(R.id.movieReleaseYear);
        mTraktCommentsActivity = (Button) findViewById(R.id.startTraktComments);

        String movieId = intent.getStringExtra(TrendingMoviesFragment.EXTRA_MOVIEID);
        String moviePoster = intent.getStringExtra(TrendingMoviesFragment.EXTRA_MOVIEPOSTER);

        new GetMovieDetailedWorker(this).execute(movieId);
        new PosterTask().execute(moviePoster);
    }

    private class GetMovieDetailedWorker extends AsyncTask<String, Void, TraktMovieDetailedData>
    {
        private Context mContext;
        private static final String IMDB_URL = "http://www.imdb.com/title/";

        private GetMovieDetailedWorker(Context context)
        {
            this.mContext = context;
        }

        @Override
        protected TraktMovieDetailedData doInBackground(String... strings)
        {
            return GetMovie(strings[0]);
        }

        @Override
        protected void onPreExecute()
        {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(final TraktMovieDetailedData movie)
        {
            if (movie != null)
            {
                setTitle(movie.getTitle());
                mLayout.setVisibility(View.VISIBLE);

                mOverview.setText(movie.getOverview());
                mTitle.setText(movie.getTitle());
                mRuntime.setText(String.format("%d min", movie.getRuntime()));
                mGenres.setText(StringUtil.JoinArrayToString(movie.getGenres()));
                mActors.setText(StringUtil.GetTraktPeople(movie.getPeople().getActors()));
                mDirectors.setText(StringUtil.GetTraktPeople(movie.getPeople().getDirectors()));
                mRating.setText(movie.getRating().getPercentage() + " %");
                mReleaseYear.setText(String.format("%d", movie.getReleaseYear()));

                mYoutubeIntent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTrailer()));
                            startActivity(intent);
                        } catch (Exception ex) {
                            Toast.makeText(mContext, "Couldn't open trailer", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                mImdbIntent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ExternalIntents.StartIMDBIntent(mContext, movie.getImdbId());
                    }
                });

                mTraktIntent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            StartUriIntent(movie.getTraktUrl());
                        }
                        catch (Exception ex) {
                            Toast.makeText(mContext, "Couldn't open IMDB", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                mTraktCommentsActivity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        Intent intent = new Intent(mContext, MovieCommentsActivity.class);
                        intent.putExtra(EXTRA_MOVIE, movie.getTitle());
                        intent.putExtra(EXTRA_MOVIEID, movie.getImdbId());
                        startActivity(intent);
                    }
                });
            }

            mProgressBar.setVisibility(View.GONE);
        }

        public TraktMovieDetailedData GetMovie(String movieId)
        {
            TraktParser parser = new TraktParser();

            return parser.GetMovieDetailed(movieId);
        }
    }

    private class PosterTask extends AsyncTask<String, Void, Bitmap>
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

    private void StartUriIntent(String uri)
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(uri));
        startActivity(i);
    }
}
