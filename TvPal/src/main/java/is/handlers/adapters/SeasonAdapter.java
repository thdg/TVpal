package is.handlers.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import is.handlers.database.DatabaseHandler;
import is.handlers.database.DbEpisodes;
import is.tvpal.R;

/**
 * Created by Arnar on 12.10.2013.
 * An adapter to shows all seasons of a given Series
 * @see android.support.v4.widget.CursorAdapter
 */

public class SeasonAdapter extends CursorAdapter
{
    private static final int LAYOUT = R.layout.listview_seasons;

    private LayoutInflater mLayoutInflater;
    private DbEpisodes db;

    public SeasonAdapter(Context context, Cursor c, int flags)
    {
        super(context, c, flags);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.db = new DbEpisodes(context);
    }

    static class ViewHolder
    {
        TextView seasonTitle;
        TextView progress;
        ProgressBar progressBar;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (!mDataValid)
        {
            throw new IllegalStateException("Only call when cursor is valid");
        }
        if (!mCursor.moveToPosition(position))
        {
            throw new IllegalStateException("Failed to move cursor to position  " + position);
        }

        final ViewHolder viewHolder;

        if (convertView == null)
        {
            convertView = newView(mContext, mCursor, parent);

            viewHolder = new ViewHolder();
            viewHolder.seasonTitle = (TextView) convertView.findViewById(R.id.season);
            viewHolder.progress = (TextView) convertView.findViewById(R.id.seasonProgressTxt);
            viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.seasonProgressBar);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final int season = mCursor.getInt(Episodes.Season);
        final int seriesId = mCursor.getInt(Episodes.SeriesId);

        if (season != 0)
            viewHolder.seasonTitle.setText(String.format("Season %d" , season));
        else
            viewHolder.seasonTitle.setText("Specials");

        int totalShows = db.GetTotalSeasonCount(seriesId, season);
        int totalShowsSeen = db.GetTotalSeasonSeen(seriesId, season);

        viewHolder.progress.setText(String.format("%d/%d", totalShowsSeen, totalShows));
        viewHolder.progressBar.setMax(totalShows);
        viewHolder.progressBar.setProgress(totalShowsSeen);

        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {}

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        return mLayoutInflater.inflate(LAYOUT, parent, false);
    }

    private interface Episodes
    {
        int Season = 0;
        int SeriesId = 1;
    }
}