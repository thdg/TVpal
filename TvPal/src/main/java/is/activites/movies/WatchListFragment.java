package is.activites.movies;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import is.activites.baseActivities.BaseFragment;
import is.handlers.adapters.WatchListAdapter;
import is.handlers.database.DbMovies;
import is.tvpal.R;

public class WatchListFragment extends BaseFragment
{
    private Context mContext;
    private WatchListAdapter mAdapter;
    private ListView mListView;

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

        mListView = (ListView) getView().findViewById(R.id.trendingTrakt);
        registerForContextMenu(mListView);
        SetAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_watchlist, container, false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.watchlist, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        switch (item.getItemId())
        {
            case R.id.remove_from_watchlist:
                RemoveMovieFromWatchlist(position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void RemoveMovieFromWatchlist(int position)
    {
        Cursor movie = (Cursor) mAdapter.getItem(position);
        new DbMovies(mContext).RemoveMovieFromWatchList(movie.getString(0));
        SetAdapter();
    }

    private void SetAdapter()
    {
        DbMovies db = new DbMovies(mContext);
        mAdapter = new WatchListAdapter(mContext, db.GetWatchlistCursor(), 0);
        mListView.setAdapter(mAdapter);
    }
}
