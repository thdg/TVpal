package is.handlers.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

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
    private HashMap<String, Bitmap> mPosters;

    public MyShowsAdapter(Context context, Cursor c, int flags)
    {
        super(context, c, flags);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPosters = new HashMap<String, Bitmap>();
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

        final String seriesId = mCursor.getString(0);
        viewHolder.title.setText(mCursor.getString(1));

        if(mPosters.get(seriesId) != null)
        {
            viewHolder.thumbnail.setImageBitmap(mPosters.get(seriesId));
        }
        else
        {
            byte[] thumbnailByteStream = mCursor.getBlob(2);
            Bitmap bmp = BitmapFactory.decodeByteArray(thumbnailByteStream, 0, thumbnailByteStream.length);
            mPosters.put(seriesId, bmp);
            viewHolder.thumbnail.setImageBitmap(bmp);
        }

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