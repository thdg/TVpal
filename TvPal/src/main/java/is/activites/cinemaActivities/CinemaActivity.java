package is.activites.cinemaActivities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import java.util.List;
import is.contracts.datacontracts.Cinema.CinemaMovies;
import is.parsers.cinema.CinemaParser;
import is.tvpal.R;

public class CinemaActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema);

        Initialize();
    }

    private void Initialize()
    {
        new GetMovieScedules().execute();
    }

    private class GetMovieScedules extends AsyncTask<String, Void, List<CinemaMovies>>
    {
        @Override
        protected List<CinemaMovies> doInBackground(String... strings)
        {
            return getMovies();
        }

        @Override
        protected void onPreExecute()
        {
            //TODO:Initialize the progress bar
        }

        @Override
        protected void onPostExecute(List<CinemaMovies> movies)
        {
            //TODO:set movies adapter & close progress bar
        }

        public List<CinemaMovies> getMovies()
        {
            CinemaParser parser = new CinemaParser();
            return parser.GetMovieSchedules();
        }
    }
}
