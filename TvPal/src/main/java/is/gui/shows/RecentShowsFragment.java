package is.gui.shows;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import is.gui.base.BaseFragment;
import is.handlers.adapters.UpcomingRecentAdapter;
import is.handlers.database.DbEpisodes;
import is.tvpal.R;

/**
 * Displays all recent shows
 * @author Arnar
 */

public class RecentShowsFragment extends BaseFragment implements AdapterView.OnItemClickListener
{
    private UpcomingRecentAdapter mAdapter;
    private Context mContext;

    public RecentShowsFragment() {}

    public static RecentShowsFragment newInstance()
    {
        RecentShowsFragment fragment = new RecentShowsFragment();

        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mContext = activity.getContext();

        View rootView = inflater.inflate(R.layout.fragment_upcoming_recent, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.overviewShows);
        listView.setOnItemClickListener(this);

        DbEpisodes db = new DbEpisodes(mContext);
        mAdapter = new UpcomingRecentAdapter(mContext, db.GetCursorRecent(), 0);
        listView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
    {
        mContext = activity.getContext();

        Cursor episode = (Cursor) mAdapter.getItem(position);

        Intent intent = new Intent(mContext, EpisodeActivity.class);
        intent.putExtra(SingleSeasonActivity.EXTRA_SERIESID, episode.getInt(3));
        intent.putExtra(SingleSeasonActivity.EXTRA_SEASONNR, episode.getInt(4));
        intent.putExtra(SingleSeasonActivity.EXTRA_SELECTED, episode.getInt(5)-1);  //Fuck ugly hack to set correct episode

        startActivity(intent);
    }
}
