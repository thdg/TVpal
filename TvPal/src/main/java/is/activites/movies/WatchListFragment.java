package is.activites.movies;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import is.handlers.adapters.WatchListAdapter;
import is.handlers.database.DbMovies;
import is.tvpal.R;


/**
 * Created by Arnar on 5.12.2013.
 */
public class WatchListFragment extends Fragment
{
    private Context mContext;

    public WatchListFragment(Context context)
    {
        this.mContext = context;
    }

    public WatchListFragment() {}

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        DbMovies db = new DbMovies(mContext);

        ListView mListView = (ListView) getView().findViewById(R.id.trendingTrakt);
        mListView.setAdapter(new WatchListAdapter(mContext, db.GetWatchlistCursor(), 0));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_trakt_movies, container, false);
    }
}
