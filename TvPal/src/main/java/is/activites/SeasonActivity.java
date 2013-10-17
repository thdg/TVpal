package is.activites;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import java.util.List;
import is.datacontracts.EpisodeDataContract;
import is.handlers.DbShowHandler;
import is.handlers.SeasonAdapter;
import is.tvpal.R;

public class SeasonActivity extends ListActivity
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

        _db = new DbShowHandler(this);

        List<EpisodeDataContract> episodes = _db.GetAllEpisodes(seriesId);

        setListAdapter(new SeasonAdapter(this, R.layout.listview_item_seasons, episodes));
    }
}
