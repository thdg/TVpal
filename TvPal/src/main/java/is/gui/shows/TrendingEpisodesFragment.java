package is.gui.shows;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.haarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;

import java.util.ArrayList;
import java.util.List;

import is.gui.base.BaseFragment;
import is.contracts.datacontracts.trakt.TraktEpisodeData;
import is.handlers.adapters.TraktEpisodeAdapter;
import is.parsers.trakt.TraktParser;
import is.tvpal.R;

/**
 * A class which displays Trending Shows from Trakt Web Service
 * @author Arnar
 * @see android.support.v4.app.Fragment
 */
public class TrendingEpisodesFragment extends BaseFragment
{
    private Context mContext;
    private GridView mGridView;
    private ProgressBar mProgessBar;
    private TextView mNoResults;

    public TrendingEpisodesFragment() {}

    public static TrendingEpisodesFragment newInstance()
    {
        TrendingEpisodesFragment fragment = new TrendingEpisodesFragment();

        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mContext = activity.getContext();
        mProgessBar = (ProgressBar) getView().findViewById(R.id.progressIndicator);
        mNoResults = (TextView) getView().findViewById(R.id.traktNoResults);
        mGridView = (GridView) getView().findViewById(R.id.trendingTrakt);

        new GetTrendingShows().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_trakt_trending, container, false);
    }

    private class GetTrendingShows extends AsyncTask<String, Void, List<TraktEpisodeData>>
    {
        @Override
        protected List<TraktEpisodeData> doInBackground(String... strings)
        {
            try {
                return new TraktParser().GetTrendingShows();
            }
            catch (Exception ex)
            {
                return new ArrayList<TraktEpisodeData>();
            }
        }

        @Override
        protected void onPreExecute()
        {
            mProgessBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<TraktEpisodeData> shows)
        {
            if (shows == null)
            {
                mProgessBar.setVisibility(View.GONE);
                mNoResults.setVisibility(View.VISIBLE);
            }
            else
            {
                TraktEpisodeAdapter mAdapter = new TraktEpisodeAdapter(mContext, R.layout.listview_trakt_episodes, shows);

                mGridView.setAdapter(mAdapter);

                mProgessBar.setVisibility(View.GONE);
                mNoResults.setVisibility(View.GONE);
            }

        }
    }
}
