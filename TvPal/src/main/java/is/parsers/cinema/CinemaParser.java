package is.parsers.cinema;

import android.util.Log;
import com.google.gson.Gson;
import java.util.List;
import is.contracts.datacontracts.Cinema.CinemaMovies;
import is.contracts.datacontracts.Cinema.CinemaResults;
import is.webservices.RestClient;

/**
 * Created by Arnar on 21.11.2013.
 */
public class CinemaParser
{
    private String ApisUrl = "http://apis.is/cinema";

    public List<CinemaMovies> GetMovieSchedules()
    {
        try
        {
            RestClient http = new RestClient();
            String json = http.downloadJSONString(ApisUrl);

            return new Gson().fromJson(json, CinemaResults.class).getMovies();
        }
        catch (Exception ex)
        {
            Log.e(getClass().getName(), "Error downloading trending shows");
        }

        return null;
    }
}
