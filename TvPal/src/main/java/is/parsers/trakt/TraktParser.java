package is.parsers.trakt;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import is.contracts.datacontracts.TraktData;

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
            HttpUtil http = new HttpUtil();
            String json = http.downloadJSONString(TraktUrl);

            Type listType = new TypeToken<ArrayList<TraktData>>() {}.getType();

            List<TraktData> data = new Gson().fromJson(json, listType);

            return data;
        }
        catch (Exception ex)
        {
            Log.e(getClass().getName(), ex.getMessage());
        }

        return null;
    }
}
