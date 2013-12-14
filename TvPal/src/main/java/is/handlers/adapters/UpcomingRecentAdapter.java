package is.handlers.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.widget.CursorAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import is.handlers.database.DbEpisodes;
import is.utilities.DateUtil;
import is.tvpal.R;

/**
 * Created by Arnar on 17.10.2013.
 * An adapter to show recent and upcoming shows.
 * It extends CursorAdapter
 * @see android.support.v4.widget.CursorAdapter
 */

public class UpcomingRecentAdapter extends CursorAdapter
{
    private static final int LAYOUT = R.layout.listview_activity;

    private LayoutInflater mLayoutInflater;
    private DbEpisodes db;
    private SparseArray<Bitmap> pictures;

    public UpcomingRecentAdapter(Context context, Cursor c, int flags)
    {
        super(context, c, flags);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.pictures = new SparseArray<Bitmap>();
        this.db = new DbEpisodes(context);
    }

    static class ViewHolder
    {
        ImageView episodeImage;
        TextView episodeName;
        TextView episodeNumber;
        TextView episodeAired;
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

            viewHolder.episodeImage = (ImageView) convertView.findViewById(R.id.activityImg);
            viewHolder.episodeName = (TextView) convertView.findViewById(R.id.activityTitle);
            viewHolder.episodeAired = (TextView) convertView.findViewById(R.id.activityAirDate);
            viewHolder.episodeNumber = (TextView) convertView.findViewById(R.id.activityEpisode);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.episodeName.setText(mCursor.getString(Episodes.EpisodeName));
        viewHolder.episodeNumber.setText(String.format("%dx%d", mCursor.getInt(Episodes.Season), mCursor.getInt(Episodes.Episode)));
        viewHolder.episodeAired.setText(DateUtil.FormatDateEpisode(mCursor.getString(Episodes.Aired)));

        final int seriesId = mCursor.getInt(Episodes.SeriesId);

        if (pictures.get(seriesId) == null)
        {
            Bitmap bmp = db.GetSeriesPoster(seriesId, true);
            pictures.put(seriesId, bmp);
        }

        viewHolder.episodeImage.setImageBitmap(pictures.get(seriesId));

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
        int EpisodeId = 0;
        int EpisodeName = 1;
        int Aired = 2;
        int SeriesId = 3;
        int Season = 4;
        int Episode = 5;
    }
}
