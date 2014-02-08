package is.gui.movies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.List;
import is.gui.base.BaseActivity;
import is.contracts.datacontracts.trakt.TraktComment;
import is.handlers.adapters.TraktCommentAdapter;
import is.parsers.trakt.TraktParser;
import is.tvpal.R;

/**
 * Created by Arnar on 5.12.2013.
 */
public class TraktCommentsActivity extends BaseActivity
{
    public static final String EXTRA_Title = "is.activites.movieActivities.EXTRA_MOVIE";
    public static final String EXTRA_ImdbId = "is.activites.movieActivities.EXTRA_MOVIEID";

    private ListView mListView;
    private ProgressBar mProgressBar;
    private TextView mNoResults;
    private TextView mMovieTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trakt_comments);

        Initialize();
    }

    private void Initialize()
    {
        mListView = (ListView) findViewById(R.id.trakt_comments);
        mProgressBar = (ProgressBar) findViewById(R.id.progressIndicator);
        mNoResults = (TextView) findViewById(R.id.noResults);
        mMovieTitle = (TextView) findViewById(R.id.comment_movieid);

        Intent intent = getIntent();

        String imdbId = intent.getStringExtra(DetailedMovieActivity.EXTRA_MOVIEID);
        String title = intent.getStringExtra(DetailedMovieActivity.EXTRA_MOVIE);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        new TraktCommentsWorker(this, title).execute(imdbId);
    }

    private class TraktCommentsWorker extends AsyncTask<String, Void, List<TraktComment>>
    {
        private Context mContext;
        private String title;

        public TraktCommentsWorker(Context context, String title)
        {
            this.mContext = context;
            this.title = title;
        }

        @Override
        protected List<TraktComment> doInBackground(String... urls)
        {
            return GetCommentsForMovie(urls[0]);
        }

        @Override
        protected void onPreExecute()
        {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<TraktComment> comments)
        {
            if(comments == null || comments.size() == 0)
            {
                mProgressBar.setVisibility(View.INVISIBLE);
                mNoResults.setVisibility(View.VISIBLE);
            }
            else
            {
                mMovieTitle.setText(title);

                mListView.setAdapter(new TraktCommentAdapter(mContext, R.layout.listview_trakt_comments, comments));

                mProgressBar.setVisibility(View.INVISIBLE);
            }
        }

        private List<TraktComment> GetCommentsForMovie(String movie)
        {
            TraktParser parser = new TraktParser();

            return parser.GetCommentsForMovie(movie);
        }
    }
}
