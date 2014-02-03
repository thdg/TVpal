package is.handlers.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import is.contracts.datacontracts.trakt.TraktMovieData;
import is.tvpal.R;
import is.utilities.PictureTask;
import is.utilities.StringUtil;

/**
 * Created by Arnar
 *
 * This class implements an Adapter that can be used in both ListView
 * (by implementing the specialized ListAdapter interface}
 * and Spinner (by implementing the specialized SpinnerAdapter interface.
 * It extends BaseAdapter.
 *
 * @see android.widget.BaseAdapter
 */
public class TraktMoviesAdapter extends BaseAdapter
{
    private Context mContext;
    private int layoutResourceId;
    private List<TraktMovieData> movies;

    public TraktMoviesAdapter(Context context, int layoutResourceId, List<TraktMovieData> movies)
    {
        this.mContext = context;
        this.layoutResourceId = layoutResourceId;
        this.movies = movies;
    }

    static class TraktHolder
    {
        TextView title;
        TextView overview;
        ImageView poster;
        int position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        final TraktHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new TraktHolder();
            holder.title = (TextView) row.findViewById(R.id.traktMovieTitle);
            holder.overview = (TextView) row.findViewById(R.id.traktMovieOverview);
            holder.poster = (ImageView) row.findViewById(R.id.traktMoviePoster);
            row.setTag(holder);
        }
        else
        {
            holder = (TraktHolder)row.getTag();
        }

        final TraktMovieData movie = getItem(position);

        holder.position = position;
        holder.title.setText(movie.getTitle());
        holder.overview.setText(movie.getOverview());

        holder.poster.setImageBitmap(null);
        final String posterUrl = movie.getImage().getPoster();

        //Execute Async Tasks parallely to improve bitmap download.
        new GetPosterShow(posterUrl, position).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, holder);

        return row;
    }

    private class GetPosterShow extends AsyncTask<TraktHolder, Void, Bitmap>
    {
        private String posterUrl;
        private TraktHolder holder;
        private int position;

        /**
         * @param posterUrl The url of a picture to download
         * @param position The position of the ViewHolder
         */
        public GetPosterShow(String posterUrl, int position)
        {
            this.posterUrl = posterUrl;
            this.position = position;
        }

        @Override

        protected Bitmap doInBackground(TraktHolder... view)
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
    public int getCount()
    {
        return (movies == null) ? 0 : movies.size();
    }

    @Override
    public TraktMovieData getItem(int position)
    {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return movies.indexOf(getItem(position));
    }
}