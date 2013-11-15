package is.parsers.trakt;

import android.os.AsyncTask;
import com.google.gson.Gson;

/**
 * Created by Arnar on 15.11.2013.
 */
public class TraktParser
{
    public static String TraktUrl = "http://api.trakt.tv/shows/trending.json/f0e3af66061e47b3243e25ed7b6443ca";


    private class GetTrendingShows extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            Gson data = new Gson();
            return null;
        }
    }
}
