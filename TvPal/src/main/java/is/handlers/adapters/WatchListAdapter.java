package is.handlers.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import is.tvpal.R;
import is.utilities.PictureTask;
import is.utilities.StringUtil;

public class WatchListAdapter extends CursorAdapter
{
    private static final int LAYOUT = R.layout.listview_trakt_movies;

    private LayoutInflater mLayoutInflater;

    public WatchListAdapter(Context context, Cursor c, int flags)
    {
        super(context, c, flags);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    static class ViewHolder
    {
        TextView title;
        TextView overview;
        ImageView poster;
        int position;
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
            viewHolder.title = (TextView) convertView.findViewById(R.id.traktMovieTitle);
            viewHolder.overview = (TextView) convertView.findViewById(R.id.traktMovieOverview);
            viewHolder.poster = (ImageView) convertView.findViewById(R.id.traktMoviePoster);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.position = position;
        viewHolder.title.setText(mCursor.getString(Movie.Title));
        viewHolder.overview.setText(mCursor.getString(Movie.Overview));

        final String posterUrl = StringUtil.formatTrendingPosterUrl(mCursor.getString(Movie.ImageUrl), "-138");

        Picasso
                .with(mContext)
                .load(posterUrl)
                .into(viewHolder.poster);

        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {}

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        return mLayoutInflater.inflate(LAYOUT, parent, false);
    }

    private interface Movie
    {
        int ImdbId = 0;
        int Title = 1;
        int Overview = 2;
        int ImageUrl = 3;
    }
}