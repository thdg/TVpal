package is.activites.cinemaActivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;

import is.contracts.datacontracts.Cinema.CinemaMovie;
import is.handlers.adapters.TheatreExpandableListAdapter;
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
        setContentView(R.layout.activity_cinema_theatres);

        Initialize();
    }

    private void Initialize()
    {
        Intent intent = getIntent();

        CinemaMovie movie = (CinemaMovie) intent.getSerializableExtra(CinemaActivity.EXTRA_MOVIE);

        ExpandableListView adapter = (ExpandableListView) findViewById(R.id.expandleMovie);

        adapter.setAdapter(new TheatreExpandableListAdapter(this, movie.getShowtimes()));
    }
}
