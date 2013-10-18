package is.activites.showActivities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import java.util.List;
import is.datacontracts.EpisodeData;
import is.handlers.database.DbShowHandler;
import is.handlers.adapters.EpisodeAdapter;

public class SingleSeasonActivity extends ListActivity
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

        Intent intent = getIntent();
        String seriesId = intent.getStringExtra(SeasonsActivity.EXTRA_SERIESID);
        String season   = intent.getStringExtra(SeasonsActivity.EXTRA_SEASON);

        List<EpisodeData> episodes = _db.GetEpisodesBySeason(seriesId, season);

        setListAdapter(new EpisodeAdapter(this, is.tvpal.R.layout.listview_item_episodes, episodes));
    }
}
