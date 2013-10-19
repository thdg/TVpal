package is.activites.showActivities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import is.datacontracts.EpisodeData;
import is.handlers.database.DbShowHandler;
import is.tvpal.R;

public class EpisodeActivity extends Activity
{
    private DbShowHandler _db;
    private TextView _episodeTitle;
    private TextView _episodeAired;
    private TextView _episodeSeason;
    private TextView _episodeOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.episode_activity);

        Initialize();
    }

    private void Initialize()
    {
        _db = new DbShowHandler(this);

        Intent intent = getIntent();

        String episodeId = intent.getStringExtra(SingleSeasonActivity.EXTRA_EPISODEID);

        EpisodeData episode = _db.GetEpisodeById(episodeId);

        GenerateLayout(episode);
    }

    private void GenerateLayout(EpisodeData episode)
    {
        _episodeTitle = (TextView) findViewById(R.id.episodeTitle);
        _episodeAired = (TextView) findViewById(R.id.episodeAired);
        _episodeSeason = (TextView) findViewById(R.id.episodeSeason);
        _episodeOverview = (TextView) findViewById(R.id.episodeOverview);

        _episodeTitle.setText(episode.getEpisodeName());
        _episodeAired.setText(String.format("Aired: %s", episode.getAired()));
        _episodeSeason.setText(String.format("Season: %s", episode.getSeasonNumber()));
        _episodeOverview.setText(episode.getOverview());
    }

}
