package is.activites.showActivities;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import is.handlers.database.DbShowHandler;
import is.handlers.adapters.EpisodeAdapter;

public class SingleSeasonActivity extends ListActivity implements AdapterView.OnItemClickListener
{
    public static final String EXTRA_SERIESID = "is.activites.showActivities.SERIESID";
    public static final String EXTRA_SEASONNR = "is.activites.showActivities.SEASONNR";
    public static final String EXTRA_EPISODENR = "is.activites.showActivities.EPISODENR";

    private EpisodeAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Initialize();
    }

    private void Initialize()
    {
        DbShowHandler _db = new DbShowHandler(this);

        Intent intent = getIntent();
        String seriesId = intent.getStringExtra(SeasonsActivity.EXTRA_SERIESID);
        String season   = intent.getStringExtra(SeasonsActivity.EXTRA_SEASON);

        setTitle(String.format("Season %s", season));

        ListView lv = getListView();
        lv.setOnItemClickListener(this);

        _adapter = new EpisodeAdapter(this, _db.GetCursorEpisodes(seriesId, season), 0);
        setListAdapter(_adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
    {
        Cursor selectedEpisode = (Cursor) _adapter.getItem(position);

        Intent intent = new Intent(this, EpisodeActivity.class);
        intent.putExtra(EXTRA_SERIESID, selectedEpisode.getString(1));
        intent.putExtra(EXTRA_SEASONNR, selectedEpisode.getString(2));
        intent.putExtra(EXTRA_EPISODENR, selectedEpisode.getString(3));
        startActivity(intent);
    }
}
