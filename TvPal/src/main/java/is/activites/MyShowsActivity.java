package is.activites;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;
import java.util.List;

import is.datacontracts.EpisodeData;
import is.datacontracts.ShowData;
import is.handlers.DbShowHandler;
import is.handlers.MyShowsAdapter;
import is.tvpal.R;

public class MyShowsActivity extends ListActivity implements AdapterView.OnItemClickListener
{
    public static final String EXTRA_SERIESID = "is.activities.SERIESID";

    private DbShowHandler _dbShow;
    private ListView _lv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Initialize();
    }

    private void Initialize()
    {
        _dbShow = new DbShowHandler(this);

        _lv = getListView();
        _lv.setOnItemClickListener(this);

        SetListAdapterMyShows();

        registerForContextMenu(getListView());
    }

    private void SetListAdapterMyShows()
    {
        List<ShowData> myShows = _dbShow.GetAllSeries();

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
        ShowData show = (ShowData) getListAdapter().getItem(selectedShow);

        try
        {
            _dbShow.RemoveShow(show.getSeriesId());
            SetListAdapterMyShows();
            Toast.makeText(this, String.format("Removed %s from your shows", show.getTitle()), Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        ShowData show = (ShowData) getListAdapter().getItem(i);

        Intent intent = new Intent(this, SeasonsActivity.class);
        intent.putExtra(EXTRA_SERIESID, show.getSeriesId());
        startActivity(intent);
    }
}