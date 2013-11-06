package is.activites.showActivities;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import is.handlers.adapters.SeasonAdapter;
import is.handlers.database.DbShowHandler;

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
