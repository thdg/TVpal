package is.activites.shows;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import is.activites.base.BaseFragment;
import is.handlers.adapters.UpcomingRecentAdapter;
import is.handlers.database.DbEpisodes;
import is.tvpal.R;

/**
 * Displays upcoming shows
 * @author Arnar
 */

public class UpcomingShowsFragment extends BaseFragment implements AdapterView.OnItemClickListener
{
    private UpcomingRecentAdapter mAdapter;
    private Context mContext;

    public UpcomingShowsFragment() {}

    public static UpcomingShowsFragment newInstance()
    {
        UpcomingShowsFragment fragment = new UpcomingShowsFragment();

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
        mAdapter = new UpcomingRecentAdapter(mContext, db.GetCursorUpcoming(), 0);
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
        intent.putExtra(SingleSeasonActivity.EXTRA_SELECTED, episode.getInt(5)-1);

        startActivity(intent);
    }
}
