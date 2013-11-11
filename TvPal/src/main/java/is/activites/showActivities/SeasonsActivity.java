package is.activites.showActivities;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import is.handlers.adapters.SeasonAdapter;
import is.handlers.database.DbShowHandler;
import is.tvpal.R;

/**
 * Displays all seasons of some series
 * @author Arnar
 */

public class SeasonsActivity extends ListActivity implements  AdapterView.OnItemClickListener
{
    public static final String EXTRA_SERIESID = "is.activites.SERIESID";
    public static final String EXTRA_SEASON = "is.activites.SEASON";

    private String _seriesId;
    private SeasonAdapter _adapter;
    private DbShowHandler _db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Initialize();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        SetListAdapter();
    }

    private void SetListAdapter()
    {
        _adapter= new SeasonAdapter(this, _db.GetCursorSeasons(_seriesId), 0);
        setListAdapter(_adapter);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void Initialize()
    {
        Intent intent = getIntent();
        _seriesId = intent.getStringExtra(MyShowsActivity.EXTRA_SERIESID);

        String _seriesNumber = intent.getStringExtra(MyShowsActivity.EXTRA_SERIESNUMBER);

        setTitle(_seriesNumber);

        ListView lv = getListView();
        lv.setOnItemClickListener(this);

        registerForContextMenu(getListView());

        _db = new DbShowHandler(this);

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
    {
        Cursor selectedSeason = (Cursor) _adapter.getItem(position);

        Intent intent = new Intent(this, SingleSeasonActivity.class);
        intent.putExtra(EXTRA_SERIESID, _seriesId);
        intent.putExtra(EXTRA_SEASON, selectedSeason.getString(1));
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
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
        Cursor selectedSeason = (Cursor) _adapter.getItem(position);
        _db.UpdateSeasonSeenStatus(selectedSeason.getString(2), selectedSeason.getString(0), seenStatus);
        SetListAdapter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
