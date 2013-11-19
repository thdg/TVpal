package is.activites.showActivities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import is.contracts.datacontracts.TraktData;
import is.handlers.adapters.TraktAdapter;
import is.parsers.trakt.TraktParser;
import is.tvpal.R;

public class TraktFragment extends Fragment implements AdapterView.OnItemClickListener
{
    private Context mContext;
    private ListView mListView;
    private TraktAdapter mAdapter;
    private ProgressBar mProgessBar;

    public TraktFragment(Context context)
    {
        this.mContext = context;
    }

    public TraktFragment() {}

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mProgessBar = (ProgressBar) getView().findViewById(R.id.progressIndicator);
        mListView = (ListView) getView().findViewById(R.id.trendingTrakt);
        mListView.setOnItemClickListener(this);

        mProgessBar.setVisibility(View.VISIBLE);

        new GetTrendingShows().execute();

        //Execute multiple AsyncTasks parallely
        //new GetTrendingShows().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_trakt, container, false);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
    {
        TraktData show = mAdapter.getItem(position);
        Toast.makeText(mContext, String.format("%d",show.getTvdbId()), Toast.LENGTH_SHORT).show();
    }

    private class GetTrendingShows extends AsyncTask<String, Void, List<TraktData>>
    {
        @Override
        protected List<TraktData> doInBackground(String... strings)
        {
            try {
                return new TraktParser().GetTrendingShows();
            }
            catch (Exception ex)
            {
                return new ArrayList<TraktData>();
            }
        }

        @Override
        protected void onPostExecute(List<TraktData> shows)
        {
            mAdapter = new TraktAdapter(mContext, R.layout.listview_trakt, shows);
            mListView.setAdapter(mAdapter);

            mProgessBar.setVisibility(View.GONE);
        }
    }
}
