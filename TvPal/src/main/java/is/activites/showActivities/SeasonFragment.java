package is.activites.showActivities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import is.handlers.adapters.SeasonAdapter;
import is.handlers.database.DbShowHandler;
import is.tvpal.R;

/**
 * A fragment that displays all seasons of some series
 * Created by Arnar on 13.11.2013.
 */
public class SeasonFragment extends Fragment implements AdapterView.OnItemClickListener
{
    public static final String EXTRA_SERIESID = "is.activites.SERIESID";
    public static final String EXTRA_SEASON = "is.activites.SEASON";

    private Context mContext;
    private SeasonAdapter mAdapter;
    private DbShowHandler db;
    private String mSeriesId;
    private ListView listView;

    public SeasonFragment(Context context, String seriesId)
    {
        this.mContext = context;
        this.mSeriesId = seriesId;
        db = new DbShowHandler(context);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        SetListAdapter();
    }

    private void SetListAdapter()
    {
        mAdapter= new SeasonAdapter(mContext, db.GetCursorSeasons(mSeriesId), 0);
        listView.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.seasons_fragment, container, false);

        listView = (ListView) rootView.findViewById(R.id.listviewSeasons);
        listView.setOnItemClickListener(this);
        registerForContextMenu(listView);

        if (rootView != null)
        {
            mAdapter= new SeasonAdapter(mContext, db.GetCursorSeasons(mSeriesId), 0);
            listView.setAdapter(mAdapter);
        }

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
    {
        Cursor selectedSeason = (Cursor) mAdapter.getItem(position);

        Intent intent = new Intent(mContext, SingleSeasonActivity.class);
        intent.putExtra(EXTRA_SERIESID, mSeriesId);
        intent.putExtra(EXTRA_SEASON, selectedSeason.getString(1));
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.season, menu);
    }

    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        switch (item.getItemId())
        {
            case R.id.setSeasonSeen:
                SetSeasonSeenStatus(position, "1");
                return true;
            case R.id.setSeasonNotSeen:
                SetSeasonSeenStatus(position, "0");
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void SetSeasonSeenStatus(int position, String seenStatus)
    {
        Cursor selectedSeason = (Cursor) mAdapter.getItem(position);
        db.UpdateSeasonSeenStatus(selectedSeason.getString(2), selectedSeason.getString(0), seenStatus);
        SetListAdapter();
    }
}