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
import is.handlers.adapters.EpisodeAdapter;

public class SingleSeasonActivity extends ListActivity implements AdapterView.OnItemClickListener
{
    public static final String EXTRA_SERIESID = "is.activites.showActivities.SERIESID";
    public static final String EXTRA_SEASONNR = "is.activites.showActivities.SEASONNR";
    public static final String EXTRA_EPISODENR = "is.activites.showActivities.EPISODENR";

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

        Intent intent = getIntent();
        String seriesId = intent.getStringExtra(SeasonsActivity.EXTRA_SERIESID);
        String season   = intent.getStringExtra(SeasonsActivity.EXTRA_SEASON);

        setTitle(String.format("Season %s",season));

        List<EpisodeData> episodes = _db.GetEpisodesBySeason(seriesId, season);

        ListView lv = getListView();
        lv.setOnItemClickListener(this);

        setListAdapter(new EpisodeAdapter(this, is.tvpal.R.layout.listview_episodes, episodes));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
    {
        EpisodeData selectedEpisode = (EpisodeData) adapterView.getAdapter().getItem(position);

        Intent intent = new Intent(this, EpisodeActivity.class);
        intent.putExtra(EXTRA_SERIESID, selectedEpisode.getSeriesId());
        intent.putExtra(EXTRA_SEASONNR, selectedEpisode.getSeasonNumber());
        intent.putExtra(EXTRA_EPISODENR, selectedEpisode.getEpisodeNumber());
        startActivity(intent);
    }
}
