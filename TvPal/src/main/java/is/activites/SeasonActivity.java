package is.activites;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import is.datacontracts.EpisodeData;
import is.handlers.DbShowHandler;
import is.handlers.SeasonAdapter;
import is.tvpal.R;

public class SeasonActivity extends ListActivity implements  AdapterView.OnItemClickListener
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
        Intent intent = getIntent();
        String seriesId = intent.getStringExtra(MyShowsActivity.EXTRA_SERIESID);

        ListView lv = getListView();
        lv.setOnItemClickListener(this);

        _db = new DbShowHandler(this);

        List<EpisodeData> episodes = _db.GetAllSeasons(seriesId);

        setListAdapter(new SeasonAdapter(this, R.layout.listview_item_seasons, episodes));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        DbShowHandler db = new DbShowHandler(this);
        List<EpisodeData> data = db.GetAllEpisodes("d");
    }
}
