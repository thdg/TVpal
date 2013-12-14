package is.handlers.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.CursorAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import is.handlers.database.DbEpisodes;
import is.tvpal.R;

/**
 * Created by Arnar on 18.10.2013.
 * An adapter to show what shows a user is watching
 * It extends CursorAdapter
 * @see android.support.v4.widget.CursorAdapter
 */

public class MyShowsAdapter extends CursorAdapter
{
    private static final int LAYOUT = R.layout.listview_my_shows;

    private LayoutInflater mLayoutInflater;
    private SparseArray<Bitmap> mPosters;

    public MyShowsAdapter(Context context, Cursor c, int flags)
    {
        super(context, c, flags);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPosters = new SparseArray<Bitmap>();
    }

    static class ViewHolder
    {
        TextView title;
        ImageView thumbnail;
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
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.imgIcon);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final int seriesId = mCursor.getInt(Series.SeriesId);
        viewHolder.title.setText(mCursor.getString(Series.Name));

        if(mPosters.get(seriesId) != null)
        {
            viewHolder.thumbnail.setImageBitmap(mPosters.get(seriesId));
        }
        else
        {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;

            Bitmap bmp = new DbEpisodes(mContext).GetSeriesPoster(mCursor.getInt(Series.SeriesId), true);
            mPosters.put(seriesId, bmp);
            viewHolder.thumbnail.setImageBitmap(bmp);
        }

        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {}

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        return mLayoutInflater.inflate(LAYOUT, parent, false);
    }

    private interface Series
    {
        int SeriesId = 0;
        int Name = 1;
    }
}