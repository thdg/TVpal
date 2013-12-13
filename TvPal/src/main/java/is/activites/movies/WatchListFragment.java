package is.activites.movies;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import is.activites.baseActivities.BaseFragment;
import is.handlers.adapters.WatchListAdapter;
import is.handlers.database.DbMovies;
import is.tvpal.R;


public class WatchListFragment extends BaseFragment
{
    private Context mContext;

    public WatchListFragment() {}

    public static WatchListFragment newInstance()
    {
        WatchListFragment fragment = new WatchListFragment();

        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mContext = activity.getContext();
        DbMovies db = new DbMovies(mContext);

        ListView mListView = (ListView) getView().findViewById(R.id.trendingTrakt);
        mListView.setAdapter(new WatchListAdapter(mContext, db.GetWatchlistCursor(), 0));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_trakt_trending, container, false);
    }
}
