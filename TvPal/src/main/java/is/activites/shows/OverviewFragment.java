package is.activites.shows;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import is.activites.baseActivities.BaseFragment;
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
            Bitmap poster = db.GetSeriesThumbnail(mCursor.getInt(Series.SeriesId));
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
