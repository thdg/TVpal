package is.activites.showActivities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.List;

import is.datacontracts.EpisodeData;
import is.handlers.database.DbShowHandler;
import is.handlers.adapters.SeasonAdapter;
import is.tvpal.R;

public class SeasonsActivity extends ListActivity implements  AdapterView.OnItemClickListener
{
    public static final String EXTRA_SERIESID = "is.activites.SERIESID";
    public static final String EXTRA_SEASON = "is.activites.SEASON";

    private DbShowHandler _db;
    private String _seriesId;
    private String _seriesNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Initialize();
    }

    private void Initialize()
    {
        Intent intent = getIntent();
        _seriesId = intent.getStringExtra(MyShowsActivity.EXTRA_SERIESID);

        _seriesNumber = intent.getStringExtra(MyShowsActivity.EXTRA_SERIESNUMBER);

        setTitle(_seriesNumber);

        ListView lv = getListView();
        lv.setOnItemClickListener(this);

        _db = new DbShowHandler(this);

        List<EpisodeData> episodes = _db.GetAllSeasons(_seriesId);

        setListAdapter(new SeasonAdapter(this, R.layout.listview_item_season, episodes));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
    {
        EpisodeData selectedSeason = (EpisodeData) adapterView.getAdapter().getItem(position);

        Intent intent = new Intent(this, SingleSeasonActivity.class);
        intent.putExtra(EXTRA_SERIESID, _seriesId);
        intent.putExtra(EXTRA_SEASON, selectedSeason.getSeasonNumber());
        startActivity(intent);
    }
}
