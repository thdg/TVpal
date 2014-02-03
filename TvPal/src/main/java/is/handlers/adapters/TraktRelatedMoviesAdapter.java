package is.handlers.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import is.contracts.datacontracts.trakt.TraktMovieDetailedData;
import is.tvpal.R;
import is.utilities.PictureTask;
import is.utilities.StringUtil;

public class TraktRelatedMoviesAdapter extends BaseAdapter
{
    private Context mContext;
    private int layoutResourceId;
    private List<TraktMovieDetailedData> mMovies;
    private SparseArray<Bitmap> mPosters;

    public TraktRelatedMoviesAdapter(Context context, int layoutResourceId, List<TraktMovieDetailedData> movies)
    {
        this.mContext = context;
        this.layoutResourceId = layoutResourceId;
        this.mMovies = movies;
        this.mPosters = new SparseArray<Bitmap>();
    }

    static class RelatedMovieHolder
    {
        TextView title;
        TextView overview;
        ImageView poster;
        TextView runtime;
        TextView rating;
        int position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        final RelatedMovieHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RelatedMovieHolder();

            holder.title = (TextView) row.findViewById(R.id.relatedMovieTitle);
            holder.overview = (TextView) row.findViewById(R.id.relatedMovieOverview);
            holder.poster = (ImageView) row.findViewById(R.id.relatedMoviePoster);
            holder.rating = (TextView) row.findViewById(R.id.relatedMovieRating);
            holder.runtime = (TextView) row.findViewById(R.id.relatedMovieRuntime);

            row.setTag(holder);
        }
        else
        {
            holder = (RelatedMovieHolder)row.getTag();
        }

        final TraktMovieDetailedData movie = getItem(position);

        holder.position = position;

        holder.overview.setText(String.format("%s (%s)", movie.getOverview(), movie.getReleaseYear()));
        holder.title.setText(movie.getTitle());

        holder.rating.setText(movie.getRating().getPercentage() + "%");
        holder.runtime.setText(movie.getRuntime() + " min");

        if (mPosters.indexOfKey(position) < 0)
        {
            holder.poster.setVisibility(View.INVISIBLE);
            new GetPosterWorker(movie.getImage().getPoster(), position).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, holder);
        }
        else
        {
            holder.poster.setImageBitmap(mPosters.get(position, null));
        }

        return row;
    }

    private class GetPosterWorker extends AsyncTask<RelatedMovieHolder, Void, Bitmap>
    {
        private String posterUrl;
        private RelatedMovieHolder viewHolder;
        private int position;

        public GetPosterWorker(String posterUrl, int position)
        {
            this.posterUrl = posterUrl;
            this.position = position;
        }

        @Override
        protected Bitmap doInBackground(RelatedMovieHolder... viewHolders)
        {
            try
            {
                this.viewHolder = viewHolders[0];
                return GetPoster();
            }
            catch (Exception ex)
            {
                Log.e(getClass().getName(), ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap)
        {
            if(viewHolder.position == position)
            {
                viewHolder.poster.setImageBitmap(bitmap);

                Animation fadeInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.abc_fade_in);
                viewHolder.poster.startAnimation(fadeInAnimation);

                mPosters.put(position, bitmap);
                viewHolder.poster.setVisibility(View.VISIBLE);
            }
        }

        private Bitmap GetPoster()
        {
            String formattedPosterUrl = StringUtil.formatTrendingPosterUrl(posterUrl, "-138");
            return PictureTask.getBitmapFromUrl(formattedPosterUrl);
        }
    }

    @Override
    public int getCount()
    {
        return (mMovies == null) ? 0 : mMovies.size();
    }

    @Override
    public TraktMovieDetailedData getItem(int position)
    {
        return mMovies.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return mMovies.indexOf(getItem(position));
    }
}

