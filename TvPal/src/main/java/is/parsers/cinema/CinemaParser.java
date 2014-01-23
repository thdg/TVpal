package is.parsers.cinema;

import android.util.Log;
import com.google.gson.Gson;
import java.util.List;

import is.contracts.datacontracts.cinema.CinemaMovie;
import is.contracts.datacontracts.cinema.CinemaResults;
import is.webservices.RestClient;

/**
 *
 */
public class CinemaParser
{
    private String ApisUrl = "http://apis.is/cinema";

    public List<CinemaMovie> GetMovieSchedules()
    {
        try
        {
            RestClient http = new RestClient();
            String json = http.Get(ApisUrl);

            return new Gson().fromJson(json, CinemaResults.class).getMovies();
        }
        catch (Exception ex)
        {
            Log.e(getClass().getName(), "Error downloading movie schedules");
        }

        return null;
    }
}
