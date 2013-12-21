package is.activites.movies;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;
import is.activites.base.BaseActivity;
import is.contracts.datacontracts.cinema.CinemaMovie;
import is.handlers.adapters.TheatreExpandableListAdapter;
import is.tvpal.R;

/**
 * Created by Arnar on 21.11.2013.
 */
public class DetailedCinemaActivity extends BaseActivity
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
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        CinemaMovie movie = (CinemaMovie) intent.getSerializableExtra(CinemaActivity.EXTRA_MOVIE);

        setTitle(movie.getTitle());

        ExpandableListView eListView = (ExpandableListView) findViewById(R.id.expandleMovie);
        eListView.setAdapter(new TheatreExpandableListAdapter(this, movie.getShowtimes()));
    }
}
