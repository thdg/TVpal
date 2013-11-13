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

import is.handlers.adapters.SingleSeasonAdapter;
import is.handlers.database.DbShowHandler;

/**
 * Displays all episodes of a single season of some series
 * @author Arnar
 */

public class SingleSeasonActivity extends ListActivity implements AdapterView.OnItemClickListener
{
    public static final String EXTRA_SERIESID = "is.activites.showActivities.SERIESID";
    public static final String EXTRA_SEASONNR = "is.activites.showActivities.SEASONNR";
    public static final String EXTRA_EPISODENR = "is.activites.showActivities.EPISODENR";
    public static final String EXTRA_SELECTED = "is.activites.showActivities.SELECTED";

    private SingleSeasonAdapter _adapter;
    private DbShowHandler _db;
    private String _seriesId;
    private String _season;

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

        _adapter = new SingleSeasonAdapter(this, _db.GetCursorEpisodes(_seriesId, _season), 0, _seriesId, _season);
        setListAdapter(_adapter);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void Initialize()
    {
        _db = new DbShowHandler(this);

        Intent intent = getIntent();
        _seriesId = intent.getStringExtra(SeasonFragment.EXTRA_SERIESID);
        _season   = intent.getStringExtra(SeasonFragment.EXTRA_SEASON);

        setTitle(String.format("Season %s", _season));

        ListView lv = getListView();
        lv.setOnItemClickListener(this);

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
    {
        Cursor selectedEpisode = (Cursor) _adapter.getItem(position);

        Intent intent = new Intent(this, EpisodeActivity.class);
        intent.putExtra(EXTRA_SERIESID, selectedEpisode.getString(1));
        intent.putExtra(EXTRA_SEASONNR, selectedEpisode.getString(2));
        intent.putExtra(EXTRA_EPISODENR, selectedEpisode.getString(3));
        intent.putExtra(EXTRA_SELECTED, position);
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
