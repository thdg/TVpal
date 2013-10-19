package is.activites.showActivities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import is.datacontracts.EpisodeData;
import is.handlers.SwipeGestureFilter;
import is.handlers.database.DbShowHandler;
import is.rules.Helpers;
import is.tvpal.R;

public class EpisodeActivity extends Activity implements SwipeGestureFilter.SimpleGestureListener
{
    private DbShowHandler _db;
    private SwipeGestureFilter _detector;
    private List<EpisodeData> _episodes;
    private EpisodeData _episode;
    private String _seriesId;
    private String _seasonNr;
    private String _episodeId;

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
        _detector = new SwipeGestureFilter(this,this);
        _episode = new EpisodeData();

        Intent intent = getIntent();
        //TODO: Find a better way to set title. (Siggi)
        setTitle(String.format("%s",""));
        _seriesId = intent.getStringExtra(SingleSeasonActivity.EXTRA_SERIESID);
        _seasonNr = intent.getStringExtra(SingleSeasonActivity.EXTRA_SEASONNR);
        _episodeId = intent.getStringExtra(SingleSeasonActivity.EXTRA_EPISODEID);

        GenerateLayout();
    }

    private void GenerateLayout()
    {
        _episodes = _db.GetEpisodesBySeason(_seriesId, _seasonNr);

        for (EpisodeData e : _episodes)
        {
            if (e.getEpisodeId().equalsIgnoreCase(_episodeId))
            {
                _episode = e;
                break;
            }
        }
        SetTextInTextViews(_episode);
    }

    private void SetTextInTextViews(EpisodeData episode)
    {
        TextView _episodeTitle = (TextView) findViewById(R.id.episodeTitle);
        TextView _episodeAired = (TextView) findViewById(R.id.episodeAired);
        TextView _episodeSeason = (TextView) findViewById(R.id.episodeSeason);
        TextView _episodeOverview = (TextView) findViewById(R.id.episodeOverview);

        _episodeTitle.setText(episode.getEpisodeName());
        _episodeAired.setText(String.format("Aired: %s", Helpers.GetDayFormatForEpisodes(episode.getAired())));
        _episodeSeason.setText(String.format("Season: %s", episode.getSeasonNumber()));
        _episodeOverview.setText(episode.getOverview());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me)
    {
        // Call onTouchEvent of SwipeGestureFilter class
        this._detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public void onSwipe(int direction)
    {
        switch (direction)
        {
            case SwipeGestureFilter.SWIPE_RIGHT :
                SwipeRightEvent();
                break;

            case SwipeGestureFilter.SWIPE_LEFT :
                SwipeLeftEvent();
                break;
        }
    }

    private void SwipeLeftEvent()
    {
        String selectedEpisodeNumber = _episode.getEpisodeNumber();

        for(EpisodeData e : _episodes)
        {
            if (e.getEpisodeNumber().equalsIgnoreCase(selectedEpisodeNumber))
            {
                int episodeNumber = Integer.parseInt(_episode.getEpisodeNumber());
                String nextEpisode   = Integer.toString(episodeNumber + 1);
                _episode.setEpisodeNumber(nextEpisode);
                GenerateLayout();
            }
        }
    }

    private void SwipeRightEvent()
    {
    }
}
