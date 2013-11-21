package is.parsers.trakt;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import is.contracts.datacontracts.TraktData;
import is.webservices.RestClient;

/**
 * Created by Arnar on 16.11.2013.
 */
public class TraktParser
{
    private String TraktUrl = "http://api.trakt.tv/shows/trending.json/f0e3af66061e47b3243e25ed7b6443ca";

    public List<TraktData> GetTrendingShows()
    {
        try
        {
            RestClient http = new RestClient();
            String json = http.downloadJSONString(TraktUrl);

            Type listType = new TypeToken<ArrayList<TraktData>>() {}.getType();

            return new Gson().fromJson(json, listType);
        }
        catch (Exception ex)
        {
            Log.e(getClass().getName(), "Error downloading trending shows");
        }

        return null;
    }
}
