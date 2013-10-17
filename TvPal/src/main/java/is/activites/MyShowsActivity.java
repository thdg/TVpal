package is.activites;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Toast;

import java.util.List;
import is.datacontracts.ShowDataContract;
import is.handlers.DbShowHandler;
import is.handlers.MyShowsAdapter;
import is.tvpal.R;

public class MyShowsActivity extends ListActivity
{
    private DbShowHandler _db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Initialize();
    }

    private void Initialize()
    {
        _db = new DbShowHandler(this);

        SetListAdapterMyShows();

        registerForContextMenu(getListView());
    }

    private void SetListAdapterMyShows()
    {
        List<ShowDataContract> myShows = _db.GetAllSeries();

        setListAdapter(new MyShowsAdapter(this, R.layout.listview_item_my_shows, myShows));
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
        //Get the selected show, ugly hack to find it in the adapter
        ShowDataContract show = (ShowDataContract) getListAdapter().getItem(selectedShow);

        try
        {
            _db.RemoveShow(show.getSeriesId());
            SetListAdapterMyShows();
            Toast.makeText(this, String.format("Removed %s from your shows", show.getTitle()), Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex)
        {
            ex.getMessage();
        }
    }
}
