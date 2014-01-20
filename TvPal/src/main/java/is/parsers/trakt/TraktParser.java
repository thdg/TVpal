package is.parsers.trakt;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import is.contracts.datacontracts.trakt.TraktComment;
import is.contracts.datacontracts.trakt.TraktMovieDetailedData;
import is.contracts.datacontracts.trakt.TraktEpisodeData;
import is.contracts.datacontracts.trakt.TraktMovieData;
import is.webservices.RestClient;

/**
 * Created by Arnar on 16.11.2013.
 */
public class TraktParser
{
    private static final String TraktEpisodeUrl = "http://api.trakt.tv/shows/trending.json/f0e3af66061e47b3243e25ed7b6443ca";
    private static final String TraktMoviesUrl = "http://api.trakt.tv/movies/trending.json/f0e3af66061e47b3243e25ed7b6443ca";
    private static final String TraktSearchUrl = "http://api.trakt.tv/search/movies.json/f0e3af66061e47b3243e25ed7b6443ca/";
    private static final String TraktSummaryUrl = "http://api.trakt.tv/movie/summary.json/f0e3af66061e47b3243e25ed7b6443ca/";
    private static final String TraktRelatedUrl = "http://api.trakt.tv/movie/related.json/f0e3af66061e47b3243e25ed7b6443ca/";



    public List<TraktEpisodeData> GetTrendingShows()
    {
        try
        {
            RestClient http = new RestClient();
            String json = http.Get(TraktEpisodeUrl);

            Type listType = new TypeToken<ArrayList<TraktEpisodeData>>() {}.getType();

            return new Gson().fromJson(json, listType);
        }
        catch (Exception ex)
        {
            Log.e(getClass().getName(), "Error downloading trending shows");
        }

        return null;
    }

    public List<TraktMovieData> GetTrendingMovies()
    {
        try
        {
            RestClient http = new RestClient();
            String json = http.Get(TraktMoviesUrl);

            Type listType = new TypeToken<ArrayList<TraktMovieData>>() {}.getType();

            return new Gson().fromJson(json, listType);
        }
        catch (Exception ex)
        {
            Log.e(getClass().getName(), "Error downloading trending movies");
        }

        return null;
    }

    public List<TraktMovieData> SearchMovie(String movie)
    {
        try
        {
            RestClient client = new RestClient();
            String json = client.Get(String.format("%s%s", TraktSearchUrl, movie));

            Type listType = new TypeToken<ArrayList<TraktMovieData>>() {}.getType();

            return new Gson().fromJson(json, listType);
        }
        catch (Exception ex)
        {
            Log.e(getClass().getName(), "Error searching for movies");
        }

        return null;
    }

    public TraktMovieDetailedData GetMovieDetailed(String movieId)
    {
        try
        {
            RestClient client = new RestClient();
            String json = client.Get(String.format("%s%s", TraktSummaryUrl, movieId));

            return new Gson().fromJson(json, TraktMovieDetailedData.class);
        }
        catch (Exception ex)
        {
            Log.e(getClass().getName(), "Error getting detailed movie");
        }

        return null;
    }

    public List<TraktComment> GetCommentsForMovie(String commentUrl)
    {
        try
        {
            RestClient client = new RestClient();
            String json = client.Get(commentUrl);

            Type listType = new TypeToken<ArrayList<TraktComment>>() {}.getType();

            return new Gson().fromJson(json, listType);
        }
        catch (Exception ex)
        {
            Log.e(getClass().getName(), "Error searching for comments");
        }

        return null;
    }

    public List<TraktMovieDetailedData> GetReleatedMovies(String imdbId)
    {
        try
        {
            RestClient client = new RestClient();
            String json = client.Get(TraktRelatedUrl + imdbId);

            Type listType = new TypeToken<ArrayList<TraktMovieDetailedData>>() {}.getType();

            return new Gson().fromJson(json, listType);
        }
        catch (Exception ex)
        {
            Log.e(getClass().getName(), "Error searching related movies");
        }

        return null;
    }
}
