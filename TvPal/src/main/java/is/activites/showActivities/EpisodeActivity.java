package is.activites.showActivities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import is.datacontracts.EpisodeData;
import is.handlers.SwipeGestureFilter;
import is.handlers.database.DbShowHandler;
import is.parsers.TvDbPictureParser;
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
    private String _episodeNr;
    private ImageView _episodePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.episode_activity);

        Initialize();
    }

    private void Initialize()
    {
        _episodePicture = (ImageView) findViewById(R.id.episodePicture);

        _db = new DbShowHandler(this);
        _detector = new SwipeGestureFilter(this,this);
        _episode = new EpisodeData();

        Intent intent = getIntent();

        //TODO: Find a better way to set title. (Siggi)
        setTitle(String.format("%s",""));

        _seriesId = intent.getStringExtra(SingleSeasonActivity.EXTRA_SERIESID);
        _seasonNr = intent.getStringExtra(SingleSeasonActivity.EXTRA_SEASONNR);
        _episodeNr = intent.getStringExtra(SingleSeasonActivity.EXTRA_EPISODENR);

        _episodes = _db.GetEpisodesBySeason(_seriesId, _seasonNr);

        GenerateLayout();
    }

    private void GenerateLayout()
    {
        for (EpisodeData e : _episodes)
        {
            if (e.getEpisodeNumber().equalsIgnoreCase(_episodeNr))
            {
                _episode = e;
                break;
            }
        }
        SetTextInTextViews();
    }

    private void SetTextInTextViews()
    {
        if(_episode.getPicture() != null)
        {
            _episodePicture.setImageBitmap(_episode.getPicture());
        }
        else
        {
            _episodePicture.setImageBitmap(null);
            String apiUrl = String.format("http://thetvdb.com/api/9A96DA217CEB03E7/episodes/%s", _episode.getEpisodeId());
            new DownloadPicture(_episode).execute(apiUrl);
        }

        TextView _episodeTitle = (TextView) findViewById(R.id.episodeTitle);
        TextView _episodeAired = (TextView) findViewById(R.id.episodeAired);
        TextView _episodeSeason = (TextView) findViewById(R.id.episodeSeason);
        TextView _episodeOverview = (TextView) findViewById(R.id.episodeOverview);

        _episodeTitle.setText(_episode.getEpisodeName());
        _episodeAired.setText(String.format("Aired: %s", Helpers.GetDayFormatForEpisodes(_episode.getAired())));
        _episodeSeason.setText(String.format("Season %s \nEpisode: %s", _episode.getSeasonNumber(), _episode.getEpisodeNumber()));
        _episodeOverview.setText(_episode.getOverview());
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
                _episodeNr = Integer.toString(episodeNumber + 1);
                GenerateLayout();
                return;
            }
        }
    }

    private void SwipeRightEvent()
    {
        String selectedEpisodeNumber = _episode.getEpisodeNumber();

        for(EpisodeData e : _episodes)
        {
            if (e.getEpisodeNumber().equalsIgnoreCase(selectedEpisodeNumber))
            {
                int episodeNumber = Integer.parseInt(_episode.getEpisodeNumber());
                _episodeNr = Integer.toString(episodeNumber - 1);
                GenerateLayout();
                return;
            }
        }
    }

    private class DownloadPicture extends AsyncTask<String, Void, String>
    {
        private final EpisodeData episode;

        public DownloadPicture(EpisodeData episode)
        {
            this.episode = episode;
        }

        @Override
        protected String doInBackground(String... urls)
        {
            try
            {
                return GetPicture(urls[0]);
            }
            catch (IOException e)
            {
                return "Unable to retrieve web page. URL may be invalid";
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            if(_episodeNr.equalsIgnoreCase(episode.getEpisodeNumber()))
            {
                _episodePicture.setImageBitmap(_episode.getPicture());
            }
        }

        private String GetPicture(String myurl) throws IOException
        {
            try
            {
                TvDbPictureParser parser = new TvDbPictureParser(myurl);
                Bitmap picture = parser.GetEpisodePicture();

                episode.setPicture(picture);
            }
            catch (Exception ex)
            {
                ex.getMessage();
            }

            return "Successful";
        }
    }
}
