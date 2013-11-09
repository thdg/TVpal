package is.activites.showActivities;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;
import is.handlers.database.DbShowHandler;
import is.handlers.adapters.MyShowsAdapter;
import is.thetvdb.TvDbUtil;
import is.tvpal.R;

/**
 * Displays all series that a user has added to his shows
 * @author Arnar
 */

public class MyShowsActivity extends ListActivity implements AdapterView.OnItemClickListener
{
    public static final String EXTRA_SERIESID = "is.activities.SERIESID";
    public static final String EXTRA_SERIESNUMBER = "is.activities.SERIESNUMBER";

    private DbShowHandler _db;
    private ListView _lv;
    private MyShowsAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Initialize();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void Initialize()
    {
        _db = new DbShowHandler(this);

        _lv = getListView();
        _lv.setOnItemClickListener(this);

        SetListAdapterMyShows();
        registerForContextMenu(getListView());

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void SetListAdapterMyShows()
    {
        _adapter = new MyShowsAdapter(this, _db.GetCursorMyShows(), 0);
        setListAdapter(_adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_shows, menu);
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
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void RemoveShow(int selectedShow)
    {
        Cursor show = (Cursor) _adapter.getItem(selectedShow);

        try
        {
            _db.RemoveShow(show.getString(0));
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
        Cursor show = (Cursor) _adapter.getItem(position);

        TvDbUtil update = new TvDbUtil(this);
        update.UpdateSeries(show.getString(0));
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
    {
        Cursor show = (Cursor) _adapter.getItem(position);

        Intent intent = new Intent(this, SeasonsActivity.class);
        intent.putExtra(EXTRA_SERIESID, show.getString(0));
        intent.putExtra(EXTRA_SERIESNUMBER, show.getString(1));

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