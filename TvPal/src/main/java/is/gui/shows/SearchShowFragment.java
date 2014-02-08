package is.gui.shows;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.List;
import is.gui.base.BaseFragment;
import is.contracts.datacontracts.SeriesData;
import is.handlers.adapters.SearchShowAdapter;
import is.parsers.tvdb.TvDbShowParser;
import is.tvpal.R;

/**
 * An activity to search for episodes and add them to "MyShows"
 * @author Arnar
 * @see is.gui.shows.MyShowsActivity
 */

public class SearchShowFragment extends BaseFragment
{
    private ListView mListView;
    private EditText mEditSearch;
    private Context mContext;
    private ProgressBar mProgressBar;

    public SearchShowFragment() {}

    public static SearchShowFragment newInstance()
    {
        SearchShowFragment fragment = new SearchShowFragment();

        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mContext = activity.getContext();
        mEditSearch = (EditText) getView().findViewById(R.id.searchShow);
        mProgressBar = (ProgressBar) getView().findViewById(R.id.progressIndicator);
        mListView = (ListView) getView().findViewById(R.id.lvId);

        InitializeEditTextSearch();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_search_shows, container, false);
    }

    private void InitializeEditTextSearch()
    {
        mEditSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();

                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); //Close the keyboard

                    return true;
                }
                return false;
            }
        });
    }

    private void performSearch()
    {
        String userEntry = null;

        try
        {
            userEntry = mEditSearch.getText().toString();
            userEntry = userEntry.replace(" ", "%20"); //Delete whitespaces and insert %20 to set correct urlFormat for the API
        }
        catch(Exception ex)
        {
            Log.e(getClass().getName(), ex.getMessage());
        }

        String searchUrl = String.format("%s%s", getResources().getString(R.string.tvdbBaseUrl), userEntry);

        new DownloadShows(mContext).execute(searchUrl);
    }

    private class DownloadShows extends AsyncTask<String, Void, List<SeriesData>>
    {
        private Context ctx;

        public DownloadShows(Context context)
        {
            this.ctx = context;
        }

        @Override
        protected List<SeriesData> doInBackground(String... urls)
        {
            try
            {
                return GetShows(urls[0]);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute()
        {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<SeriesData> shows)
        {
            if (shows == null)
            {
                Toast.makeText(ctx, "No shows found", Toast.LENGTH_SHORT).show();
            }
            else
            {
                SearchShowAdapter mAdapter = new SearchShowAdapter(ctx, R.layout.listview_search_show, shows);
                mListView.setAdapter(mAdapter);
            }

            mProgressBar.setVisibility(View.INVISIBLE);
        }

        private List<SeriesData> GetShows(String myurl) throws IOException
        {
            try
            {
                TvDbShowParser parser = new TvDbShowParser(myurl);
                return parser.GetShows();
            }
            catch (Exception ex)
            {
                Log.e(getClass().getName(), ex.getMessage());
            }

            return null;
        }
    }
}