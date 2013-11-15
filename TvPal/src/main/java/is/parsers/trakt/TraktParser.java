package is.parsers.trakt;

import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import is.utilities.JsonImport;

/**
 * Created by Arnar on 15.11.2013.
 */
public class TraktParser
{
    public static String TraktUrl = "http://api.trakt.tv/shows/trending.json/f0e3af66061e47b3243e25ed7b6443ca";

    public void dostuff()
    {
        new GetTrendingShows().execute();
    }

    private class GetTrendingShows extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            try
            {
                JsonImport jsonHelper = new JsonImport();
                String json = jsonHelper.downloadJSONString(TraktUrl);

                if (json != null)
                    return null;
            }
            catch (IOException e)
            {
                Log.e(getClass().getName(), e.getMessage());
            }
            return null;
        }




    }
}
