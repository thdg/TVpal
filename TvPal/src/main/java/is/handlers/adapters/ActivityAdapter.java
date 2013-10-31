package is.handlers.adapters;

/**
 * Created by Arnar on 17.10.2013.
 */

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import is.handlers.database.DbShowHandler;
import is.utilities.DateUtil;
import is.tvpal.R;

public class ActivityAdapter extends CursorAdapter
{
    private static final int LAYOUT = R.layout.listview_activity;

    private LayoutInflater mLayoutInflater;
    private DbShowHandler db;
    private HashMap<String, Bitmap> pictures;

    public ActivityAdapter(Context context, Cursor c, int flags)
    {
        super(context, c, flags);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.pictures = new HashMap<String, Bitmap>();
        this.db = new DbShowHandler(context);
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

        viewHolder.episodeName.setText(mCursor.getString(1));
        viewHolder.episodeNumber.setText(String.format("%sx%s", mCursor.getString(4), mCursor.getString(5)));
        viewHolder.episodeAired.setText(DateUtil.FormatDateEpisode(mCursor.getString(2)));

        String seriesId = mCursor.getString(3);

        //TODO: Maby this takes to much memory, look into that
        if (!pictures.containsKey(seriesId))
        {
            Bitmap bmp = db.GetSeriesThumbnail(seriesId);
            pictures.put(seriesId, bmp);
        }

        viewHolder.episodeImage.setImageBitmap(pictures.get(seriesId));

        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        return mLayoutInflater.inflate(LAYOUT, parent, false);
    }
}
