package is.activites.showActivities;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import is.handlers.database.DbShowHandler;
import is.tvpal.R;

/**
 * A fragment to display basic information about a series
 * @author Arnar
 */
public class OverviewFragment extends Fragment
{
    private Context context;
    private Cursor mCursor;

    public OverviewFragment(Context context, Cursor cursor)
    {
        this.context = context;
        this.mCursor = cursor;
    }

    public OverviewFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_overview, container, false);

        DbShowHandler db = new DbShowHandler(context);

        if (rootView != null)
        {
            Bitmap poster = db.GetSeriesThumbnail(mCursor.getInt(Series.SeriesId));
            ((ImageView) rootView.findViewById(R.id.overviewPicture)).setImageBitmap(poster);
            ((TextView) rootView.findViewById(R.id.overviewTitle)).setText(mCursor.getString(Series.Name));
            ((TextView) rootView.findViewById(R.id.overviewText)).setText(mCursor.getString(Series.Overview));
            ((TextView) rootView.findViewById(R.id.overviewNetwork)).setText(String.format("Network: %s", mCursor.getString(Series.Network)));
            ((TextView) rootView.findViewById(R.id.overviewGenres)).setText(String.format("Genres: %s", mCursor.getString(Series.Genres)));
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
    }
}
