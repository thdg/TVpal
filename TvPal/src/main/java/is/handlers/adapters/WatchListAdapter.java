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
        viewHolder.poster.setImageBitmap(null);
        new PosterWorker(mCursor.getString(Movie.ImageUrl), position).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, viewHolder);

        return convertView;
    }

    //Todo: extract worker to a class so we can use this worker both here and for trending movies
    private class PosterWorker extends AsyncTask<ViewHolder, Void, Bitmap>
    {
        private String posterUrl;
        private ViewHolder holder;
        private int position;

        /**
         * @param posterUrl The url of a picture to download
         * @param position The position of the ViewHolder
         */
        public PosterWorker(String posterUrl, int position)
        {
            this.posterUrl = posterUrl;
            this.position = position;
        }

        @Override

        protected Bitmap doInBackground(ViewHolder... view)
        {
            holder = view[0];
            return GetPoster();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap)
        {
            if (holder.position == position)
            {
                holder.poster.setImageBitmap(bitmap);
            }
        }

        private Bitmap GetPoster()
        {
            try
            {
                String formattedPosterUrl = StringUtil.formatTrendingPosterUrl(posterUrl, "-138");
                return PictureTask.getBitmapFromUrl(formattedPosterUrl);
            }
            catch (Exception ex)
            {
                Log.e(getClass().getName(), ex.getMessage());
            }

            return null;
        }
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