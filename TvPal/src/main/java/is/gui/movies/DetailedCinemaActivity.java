package is.gui.movies;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import is.gui.base.BaseActivity;
import is.contracts.datacontracts.cinema.CinemaMovie;
import is.handlers.adapters.TheatreExpandableListAdapter;
import is.tvpal.R;

public class DetailedCinemaActivity extends BaseActivity
{
    private CinemaMovie _movie;

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

        InitializeDetailsButton();

        Intent intent = getIntent();
        _movie = (CinemaMovie) intent.getSerializableExtra(CinemaActivity.EXTRA_MOVIE);

        setTitle(_movie.getTitle());

        ExpandableListView eListView = (ExpandableListView) findViewById(R.id.expandleMovie);
        eListView.setAdapter(new TheatreExpandableListAdapter(this, _movie.getShowtimes()));
    }

    private void InitializeDetailsButton()
    {
        Button detailsBtn = (Button) findViewById(R.id.movieDetailed);

        detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), DetailedMovieActivity.class);
                intent.putExtra(TrendingMoviesFragment.EXTRA_MOVIEID, TrimImdbLink(_movie.getImdbLink()));
                intent.putExtra(TrendingMoviesFragment.EXTRA_MOVIEPOSTER, _movie.getImageUrl());
                startActivity(intent);
            }
        });
    }

    private String TrimImdbLink(String imdbLink)
    {
        int index = imdbLink.lastIndexOf("/");
        imdbLink = imdbLink.substring(index+1);
        return imdbLink;
    }
}
