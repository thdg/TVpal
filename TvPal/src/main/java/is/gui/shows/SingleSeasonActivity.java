package is.gui.shows;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import is.gui.base.BaseActivity;
import is.handlers.adapters.SingleSeasonAdapter;
import is.handlers.database.DbEpisodes;
import is.tvpal.R;

/**
 * Displays all episodes of a single season of some series
 * @author Arnar
 */

public class SingleSeasonActivity extends BaseActivity implements AdapterView.OnItemClickListener
{
    public static final String EXTRA_SERIESID = "is.activites.showActivities.SERIESID";
    public static final String EXTRA_SEASONNR = "is.activites.showActivities.SEASONNR";
    public static final String EXTRA_EPISODENR = "is.activites.showActivities.EPISODENR";
    public static final String EXTRA_SELECTED = "is.activites.showActivities.SELECTED";

    private SingleSeasonAdapter mAdapter;
    private ListView mListView;
    private DbEpisodes mDB;
    private int _seriesId;
    private int _season;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_season);

        Initialize();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        mAdapter = new SingleSeasonAdapter(this, mDB.GetCursorEpisodes(_seriesId, _season), 0, _seriesId, _season);
        mListView.setAdapter(mAdapter);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void Initialize()
    {
        mDB = new DbEpisodes(this);

        Intent intent = getIntent();
        _seriesId = intent.getIntExtra(SeasonFragment.EXTRA_SERIESID, 0);
        _season   = intent.getIntExtra(SeasonFragment.EXTRA_SEASON, 0);

        setTitle(String.format("Season %s", _season));

        mListView = (ListView) findViewById(R.id.episode_single_season);
        mListView.setOnItemClickListener(this);

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
    {
        Cursor selectedEpisode = (Cursor) mAdapter.getItem(position);

        Intent intent = new Intent(this, EpisodeActivity.class);
        intent.putExtra(EXTRA_SERIESID, selectedEpisode.getInt(1));
        intent.putExtra(EXTRA_SEASONNR, selectedEpisode.getInt(2));
        intent.putExtra(EXTRA_EPISODENR, selectedEpisode.getInt(3));
        intent.putExtra(EXTRA_SELECTED, position);
        startActivity(intent);
    }
}
