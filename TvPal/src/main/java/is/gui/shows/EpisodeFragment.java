package is.gui.shows;

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
import is.gui.base.BaseFragment;
import is.contracts.datacontracts.EpisodeData;
import is.handlers.database.DbEpisodes;
import is.parsers.tvdb.TvDbPictureParser;
import is.tvpal.R;
import is.utilities.ConnectionListener;

/**
 * A fragment that shows a detailed information for a show
 * @author Arnar
 * @see Fragment
 */
public class EpisodeFragment extends BaseFragment
{
    public static final String EPISODE_FRAGMENT = "is.activites.showActivities.episode_fragment";

    private Context mContext;
    private Bitmap bmp;
    private ImageView poster;
    private ConnectionListener _network;
    private DbEpisodes db;

    public EpisodeFragment() {}

    public static EpisodeFragment newInstance(EpisodeData episode)
    {
        EpisodeFragment fragment = new EpisodeFragment();

        Bundle args = new Bundle();
        args.putSerializable(EPISODE_FRAGMENT, episode);

        fragment.setArguments(args);
        fragment.setRetainInstance(true);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mContext = activity.getContext();
        db = new DbEpisodes(mContext);
        _network = new ConnectionListener(mContext);

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

                    int seen = 0;
                    if(episode.getSeen() == 0)
                        seen = 1;

                    episode.setSeen(seen);
                }
            });

            final String guestStars = episode.getGuestStars().equals("") ? "No Guest Stars" : episode.getGuestStars();

            episodeSeenCbx.setChecked(episode.getSeen() == 1);

            ((TextView) rootView.findViewById(R.id.episodeTitle)).setText(episode.getEpisodeName());
            ((TextView) rootView.findViewById(R.id.episodeAired)).setText(String.format("Aired: %s", episode.getAired()));
            ((TextView) rootView.findViewById(R.id.episodeSeason)).setText(String.format("Season: %s", episode.getSeasonNumber()));
            ((TextView) rootView.findViewById(R.id.episodeOverview)).setText(episode.getOverview());
            ((TextView) rootView.findViewById(R.id.episodeDirector)).setText(episode.getDirector());
            ((TextView) rootView.findViewById(R.id.episodeRating)).setText(episode.getRating());
            ((TextView) rootView.findViewById(R.id.episodeGuestStars)).setText(guestStars);
            poster = (ImageView) rootView.findViewById(R.id.episodePicture);

            //TODO: Implement better bitmap cache, perhaps save the picture on the sd card, also simplify the showing of progressbar
            if (bmp == null && _network.isNetworkAvailable())
            {
                String apiUrl = String.format("http://thetvdb.com/api/9A96DA217CEB03E7/episodes/%d", episode.getEpisodeId());
                new DownloadPicture().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, apiUrl);
            }
            else
            {
                rootView.findViewById(R.id.progressDownloadingPicture).setVisibility(View.INVISIBLE);
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
            {
                poster.setImageBitmap(bmp);
                if (getView() != null)
                    (getView().findViewById(R.id.progressDownloadingPicture)).setVisibility(View.INVISIBLE);
            }
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
