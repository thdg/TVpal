package is.activites.cinemaActivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import is.contracts.datacontracts.Cinema.CinemaMovie;
import is.tvpal.R;

/**
 * Created by Arnar on 21.11.2013.
 */
public class DetailedMovieActivity extends Activity
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
        Intent intent = getIntent();

        CinemaMovie movie = (CinemaMovie) intent.getSerializableExtra(CinemaActivity.EXTRA_MOVIE);
    }
}
