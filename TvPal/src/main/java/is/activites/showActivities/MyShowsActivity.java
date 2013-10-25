package is.activites.showActivities;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Currency;
import java.util.List;

import is.datacontracts.ShowData;
import is.handlers.database.DbShowHandler;
import is.handlers.adapters.MyShowsAdapter;
import is.tvpal.R;

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

    private void Initialize()
    {
        _db = new DbShowHandler(this);

        _lv = getListView();
        _lv.setOnItemClickListener(this);

        SetListAdapterMyShows();
        registerForContextMenu(getListView());
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
            ex.getMessage();
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
    {
        Cursor show = (Cursor) _adapter.getItem(position);

        Intent intent = new Intent(this, SeasonsActivity.class);
        intent.putExtra(EXTRA_SERIESID, show.getString(0));
        intent.putExtra(EXTRA_SERIESNUMBER, show.getString(1));

        startActivity(intent);
    }
}