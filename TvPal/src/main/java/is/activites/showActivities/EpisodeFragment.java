package is.activites.showActivities;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import is.datacontracts.EpisodeData;
import is.handlers.database.DbShowHandler;
import is.parsers.TvDbPictureParser;
import is.tvpal.R;
import is.utilities.ConnectionListener;

/**
 * A fragment that shows a detailed information for a show
 * @author Arnar
 * @see Fragment
 */
public class EpisodeFragment extends Fragment
{
    public static final String EPISODE_FRAGMENT = "episode_fragment";

    private Context cxt;
    private Bitmap bmp;
    private ImageView poster;
    private ConnectionListener _network;
    private DbShowHandler db;

    public EpisodeFragment(Context cxt)
    {
        this.cxt = cxt;
        this._network = new ConnectionListener(cxt);
        this.db = new DbShowHandler(cxt);
    }

    public EpisodeFragment() {}

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.activity_episode, container, false);
        Bundle args = getArguments();
        final EpisodeData episode = (EpisodeData) args.getSerializable(EPISODE_FRAGMENT);

        if (rootView != null)
        {
            CheckBox episodeSeenCbx = (CheckBox) rootView.findViewById(R.id.episodeSeen);

            episodeSeenCbx.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    db.UpdateEpisodeSeen(episode.getEpisodeId());

                    String seen = "0";
                    if(episode.getSeen().equalsIgnoreCase("0"))
                        seen = "1";

                    episode.setSeen(seen);
                }
            });

            final String guestStars = episode.getGuestStars().equals("") ? "Guest Stars: None" : String.format("Guest Stars: %s", episode.getGuestStars());

            episodeSeenCbx.setChecked(episode.getSeen().equalsIgnoreCase("1"));

            ((TextView) rootView.findViewById(R.id.episodeTitle)).setText(episode.getEpisodeName());
            ((TextView) rootView.findViewById(R.id.episodeAired)).setText(String.format("Aired: %s", episode.getAired()));
            ((TextView) rootView.findViewById(R.id.episodeSeason)).setText(String.format("Season: %s", episode.getSeasonNumber()));
            ((TextView) rootView.findViewById(R.id.episodeOverview)).setText(episode.getOverview());
            ((TextView) rootView.findViewById(R.id.episodeDirector)).setText(String.format("Director: %s", episode.getDirector()));
            ((TextView) rootView.findViewById(R.id.episodeRating)).setText(String.format("Rating: %s", episode.getRating()));
            ((TextView) rootView.findViewById(R.id.episodeGuestStars)).setText(guestStars);
            poster = (ImageView) rootView.findViewById(R.id.episodePicture);

            //TODO: Implement better bitmap cache, perhaps save the picture on the sd card
            if (bmp == null && _network.isNetworkAvailable())
            {
                String apiUrl = String.format("http://thetvdb.com/api/9A96DA217CEB03E7/episodes/%s", episode.getEpisodeId());
                new DownloadPicture().execute(apiUrl);
            }
        }

        return rootView;
    }

    private class DownloadPicture extends AsyncTask<String, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(String... urls)
        {
            try
            {
                return GetPicture(urls[0]);
            }
            catch (IOException e)
            {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean successful)
        {
            if (successful)
                poster.setImageBitmap(bmp);
        }

        private Boolean GetPicture(String myurl) throws IOException
        {
            try
            {
                TvDbPictureParser parser = new TvDbPictureParser(myurl);
                bmp = parser.GetEpisodePicture();
            }
            catch (Exception ex)
            {
                Log.e(getClass().getName(), ex.getMessage());
            }

            return bmp != null;
        }
    }
}
