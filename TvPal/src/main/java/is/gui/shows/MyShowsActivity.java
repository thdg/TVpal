package is.gui.shows;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import is.gui.base.BaseActivity;
import is.handlers.database.DbEpisodes;
import is.handlers.adapters.MyShowsAdapter;
import is.thetvdb.TvDbUtil;
import is.tvpal.R;

/**
 * Displays all series that a user has added to his shows
 * @author Arnar
 */

public class MyShowsActivity extends BaseActivity implements AdapterView.OnItemClickListener
{
    public static final String EXTRA_SERIESID = "is.activities.showActivities.SERIESID";
    public static final String EXTRA_NAME = "is.actvities.showActivities.SERIESNAME";

    private DbEpisodes mDB;
    private GridView mGridView;
    private MyShowsAdapter mAdapter;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myshows);

        Initialize();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void Initialize()
    {
        mDB = new DbEpisodes(this);

        mProgressBar = (ProgressBar) findViewById(R.id.progressUpdateShows);
        mGridView = (GridView) findViewById(R.id.myshows_series);
        mGridView.setOnItemClickListener(this);

        SetListAdapterMyShows();
        registerForContextMenu(mGridView);

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void SetListAdapterMyShows()
    {
        mAdapter = new MyShowsAdapter(this, mDB.GetCursorMyShows(), 0);
        mGridView.setAdapter(mAdapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_shows_context_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        switch (item.getItemId())
        {
            case R.id.removeShow:
                RemoveShow(position);
                return true;
            case R.id.updateShow:
                UpdateShow(position);
                return true;
            case R.id.seenAllEpisodes:
                SeenAllEpisodes(position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void RemoveShow(int selectedShow)
    {
        Cursor show = (Cursor) mAdapter.getItem(selectedShow);

        try
        {
            mDB.RemoveShow(show.getInt(0));
            SetListAdapterMyShows();
            Toast.makeText(this, String.format("Removed %s from your shows", show.getString(1)), Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex)
        {
            Log.e(getClass().getName(), ex.getMessage());
        }
    }

    private void UpdateShow(int position)
    {
        Cursor show = (Cursor) mAdapter.getItem(position);

        TvDbUtil update = new TvDbUtil(this);
        update.UpdateSeries(show.getInt(0), mProgressBar);
    }

    private void SeenAllEpisodes(int position)
    {
        Cursor cursor = (Cursor) mAdapter.getItem(position);

        TvDbUtil tvdb = new TvDbUtil(this);
        tvdb.SetAllEpisodesOfSeriesSeen(cursor.getInt(0));
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
    {
        Cursor show = (Cursor) mAdapter.getItem(position);

        Intent intent = new Intent(this, SeriesActivity.class);
        intent.putExtra(EXTRA_SERIESID, show.getInt(0));
        intent.putExtra(EXTRA_NAME, show.getString(1));
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
            case R.id.updateAllShows:
                UpdateAllShows();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void UpdateAllShows()
    {
        TvDbUtil tv = new TvDbUtil(this);
        tv.UpdateAllSeries(mProgressBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_shows_menu, menu);
        return true;
    }
}