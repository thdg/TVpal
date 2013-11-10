package is.thetvdb;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import java.io.IOException;
import java.util.List;
import is.datacontracts.EpisodeData;
import is.handlers.database.DbShowHandler;
import is.parsers.TvDbUpdateParser;

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

    public void UpdateSeries(String seriesId)
    {
        DbShowHandler db = new DbShowHandler(context);
        int lastUpdate = Integer.parseInt(db.GetSeriesLastUpdate(seriesId));
        new DownloadEpisodes(context, lastUpdate, seriesId).execute(String.format("%s%s/all/en.xml", ApiUrl, seriesId));
    }

    public void UpdateAllSeries()
    {
        DbShowHandler db = new DbShowHandler(context);
        db.UpdateAllSeries();
    }

    private class DownloadEpisodes extends AsyncTask<String, Void, String>
    {
        private Context context;
        private int lastUpdate;
        private String seriesId;

        public DownloadEpisodes(Context context, int lastUpdate, String seriesId)
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
                return UpdateEpisodes(urls[0]);
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

        private String UpdateEpisodes(String myurl) throws IOException
        {
            try
            {
                DbShowHandler db = new DbShowHandler(context);
                TvDbUpdateParser parser = new TvDbUpdateParser(myurl, lastUpdate);

                List<EpisodeData> episodes = parser.GetEpisodes();

                db.UpdateSingleSeries(episodes, parser.getLatestSeriesUpdate(), seriesId);

                return Integer.toString(episodes.size());
            }
            catch (Exception ex)
            {
                Log.e(getClass().getName(), ex.getMessage());
            }

            return "error";
        }
    }
}
