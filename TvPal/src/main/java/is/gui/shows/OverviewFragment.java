package is.gui.shows;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import is.gui.base.BaseFragment;
import is.gui.movies.DetailedMovieActivity;
import is.gui.movies.TraktCommentsActivity;
import is.handlers.database.DbEpisodes;
import is.tvpal.R;
import is.utilities.ExternalIntents;

/**
 * A fragment to display basic information about a series
 * @author Arnar
 */
public class OverviewFragment extends BaseFragment
{
    public static final String ARG_SeriesId = "series_id";

    public static OverviewFragment newInstance(int seriesId)
    {
        OverviewFragment fragment = new OverviewFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SeriesId, seriesId);
        fragment.setRetainInstance(true);
        fragment.setArguments(bundle);

        return fragment;
    }

    public OverviewFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_overview, container, false);

        final Context mContext = activity.getContext();
        DbEpisodes db = new DbEpisodes(mContext);

        Bundle args = getArguments();
        final Cursor mCursor = db.GetCursorOverview(args.getInt(ARG_SeriesId));

        if (rootView != null)
        {
            Bitmap poster = db.GetSeriesPoster(mCursor.getInt(Series.SeriesId), false);
            ((ImageView) rootView.findViewById(R.id.overviewPicture)).setImageBitmap(poster);
            ((TextView) rootView.findViewById(R.id.overviewTitle)).setText(mCursor.getString(Series.Name));
            ((TextView) rootView.findViewById(R.id.overviewText)).setText(mCursor.getString(Series.Overview));
            ((TextView) rootView.findViewById(R.id.overviewNetwork)).setText(mCursor.getString(Series.Network));
            ((TextView) rootView.findViewById(R.id.overviewGenres)).setText(mCursor.getString(Series.Genres));
            ((TextView) rootView.findViewById(R.id.overviewActors)).setText(mCursor.getString(Series.Actors));

            rootView.findViewById(R.id.startImdbIntent).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    ExternalIntents.StartIMDBIntent(mContext, mCursor.getString(Series.ImdbId));
                }
            });

            rootView.findViewById(R.id.startTraktComments).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    final String TraktCommentUrl = "http://api.trakt.tv/show/comments.json/f0e3af66061e47b3243e25ed7b6443ca/";
                    Intent intent = new Intent(mContext, TraktCommentsActivity.class);
                    intent.putExtra(DetailedMovieActivity.EXTRA_MOVIE, mCursor.getString(Series.Name));
                    intent.putExtra(DetailedMovieActivity.EXTRA_MOVIEID, TraktCommentUrl + mCursor.getString(Series.ImdbId));
                    startActivity(intent);
                }
            });

            rootView.findViewById(R.id.startTvdbIntent).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ExternalIntents.StartTvdbIntent(mContext, mCursor.getInt(Series.SeriesId));
                }
            });

            rootView.findViewById(R.id.startTraktIntent).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ExternalIntents.StartTraktIntent(mContext, mCursor.getString(Series.Name));
                }
            });
        }

        return rootView;
    }

    private interface Series
    {
        int SeriesId = 0;
        int Overview = 1;
        int Name = 2;
        int Network = 3;
        int Genres = 4;
        int Actors = 5;
        int ImdbId = 6;
    }
}
