package is.thetvdb;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import java.io.IOException;
import java.util.List;
import is.contracts.datacontracts.EpisodeData;
import is.handlers.database.DbShowHandler;
import is.parsers.tvdb.TvDbUpdateParser;
import is.utilities.ConnectionListener;

/**
 * Created by Arnar on 9.11.2013.
 */
public class TvDbUtil
{
    public static String ApiUrl = "http://thetvdb.com/api/9A96DA217CEB03E7/series/";

    private Context context;

    public TvDbUtil(Context context)
    {
        this.context = context;
    }

    public void UpdateSeries(int seriesId)
    {
        DbShowHandler db = new DbShowHandler(context);
        int lastUpdate = Integer.parseInt(db.GetSeriesLastUpdate(seriesId));
        new UpdateSingleSeriesTask(context, lastUpdate, seriesId).execute(String.format("%s%s/all/en.xml", ApiUrl, seriesId));
    }

    public void UpdateAllSeries()
    {
        new UpdateAllSeriesTask(context).execute();
    }

    public void SetAllEpisodesOfSeriesSeen(int seriesId)
    {
        DbShowHandler db = new DbShowHandler(context);
        db.SetSeriesSeen(seriesId);
    }

    private class UpdateSingleSeriesTask extends AsyncTask<String, Void, String>
    {
        private Context context;
        private int lastUpdate;
        private int seriesId;

        public UpdateSingleSeriesTask(Context context, int lastUpdate, int seriesId)
        {
            this.context = context;
            this.lastUpdate = lastUpdate;
            this.seriesId = seriesId;
        }

        @Override
        protected String doInBackground(String... urls)
        {
            try
            {
                return UpdateEpisodesOfSeries(urls[0]);
            }
            catch (IOException e)
            {
                return "Unable to retrieve web page. URL may be invalid";
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            if (!result.equalsIgnoreCase("error"))
                Toast.makeText(context, "Updated show, " + result, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, "Whoops, something went wrong...", Toast.LENGTH_SHORT).show();
        }

        private String UpdateEpisodesOfSeries(String myurl) throws IOException
        {
            try
            {
                ConnectionListener network = new ConnectionListener(context);
                DbShowHandler db = new DbShowHandler(context);

                if (network.isNetworkAvailable())
                {
                    TvDbUpdateParser parser = new TvDbUpdateParser(myurl, lastUpdate);

                    List<EpisodeData> episodes = parser.GetEpisodes();

                    db.UpdateSingleSeries(episodes, parser.getLatestSeriesUpdate(), seriesId);

                    return Integer.toString(episodes.size());
                }
            }
            catch (Exception ex)
            {
                Log.e(getClass().getName(), ex.getMessage());
            }

            return "error";
        }
    }

    private class UpdateAllSeriesTask extends AsyncTask<String, Void, String>
    {
        private Context context;

        public UpdateAllSeriesTask(Context context)
        {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... urls)
        {
            try
            {
                return UpdateEpisodesOfSeries();
            }
            catch (IOException e)
            {
                return "Unable to retrieve web page. URL may be invalid";
            }
        }

        @Override
        protected void onPreExecute()
        {
            Toast.makeText(context, "Updating shows", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String result)
        {
            if (result.equalsIgnoreCase("error"))
                Toast.makeText(context, "Whoops, something went wrong...", Toast.LENGTH_SHORT).show();
            else if (result.equalsIgnoreCase("noshows"))
                Toast.makeText(context, "No shows to update", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, "Updated all shows, " + result, Toast.LENGTH_SHORT).show();
        }

        private String UpdateEpisodesOfSeries() throws IOException
        {
            try
            {
                DbShowHandler db = new DbShowHandler(context);
                ConnectionListener network = new ConnectionListener(context);
                List<Integer> seriesIds = db.GetAllSeriesIds();

                if (seriesIds.size() == 0)
                    return "noshows";

                for(Integer seriesId : seriesIds)
                {
                    if (network.isNetworkAvailable())
                    {
                        String apiUrl = String.format("%s%s/all/en.xml", ApiUrl, seriesId);
                        String latestUpdate = db.GetSeriesLastUpdate(seriesId);

                        TvDbUpdateParser parser = new TvDbUpdateParser(apiUrl, Integer.parseInt(latestUpdate));

                        List<EpisodeData> episodes = parser.GetEpisodes();

                        db.UpdateSingleSeries(episodes, parser.getLatestSeriesUpdate(), seriesId);
                    }
                }

                db.close();
                return Integer.toString(seriesIds.size());
            }
            catch (Exception ex)
            {
                Log.e(getClass().getName(), ex.getMessage());
            }

            return "error";
        }
    }
}
