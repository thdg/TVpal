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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import is.contracts.datacontracts.Cinema.CinemaMovie;
import is.tvpal.R;
import is.utilities.PictureTask;

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
public class CinemaAdapter extends BaseAdapter
{
    private Context context;
    private int layoutResourceId;
    private List<CinemaMovie> movies;
    private SparseArray<Bitmap> images;

    public CinemaAdapter(Context context, int layoutResourceId, List<CinemaMovie> movies)
    {
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.movies = movies;
        this.images = new SparseArray<Bitmap>();
    }

    static class CinemaHolder
    {
        TextView title;
        ImageView image;
        TextView imdb;
        TextView restricted;
        int position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        final CinemaHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new CinemaHolder();
            holder.title = (TextView) row.findViewById(R.id.cinemaTitle);
            holder.image = (ImageView) row.findViewById(R.id.cinemaPicture);
            holder.imdb = (TextView) row.findViewById(R.id.cinemaImdbScore);
            holder.restricted = (TextView) row.findViewById(R.id.cinemaRestricted);

            row.setTag(holder);
        }
        else
        {
            holder = (CinemaHolder)row.getTag();
        }

        final CinemaMovie movie = getItem(position);

        holder.position = position;
        holder.title.setText(movie.getTitle());
        holder.imdb.setText(movie.getImdb());
        holder.restricted.setText(movie.getRestricted());

        if (images.get(position) == null)
        {
            holder.image.setVisibility(View.INVISIBLE);
            new GetMoviePoster(movie.getImageUrl(), position).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, holder);
        }
        else
        {
            holder.image.setImageBitmap(images.get(position));
        }

        return row;
    }

    private class GetMoviePoster extends AsyncTask<CinemaHolder, Void, Bitmap>
    {
        private String posterUrl;
        private CinemaHolder holder;
        private int position;

        /**
         * @param posterUrl The url of a picture to download
         */
        public GetMoviePoster(String posterUrl, int position)
        {
            this.posterUrl = posterUrl;
            this.position = position;
        }

        @Override
        protected Bitmap doInBackground(CinemaHolder... view)
        {
            holder = view[0];
            return GetPoster();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap)
        {
            if (bitmap != null)
            {
                images.put(position, bitmap);
                if (holder.position == position)
                {
                    holder.image.setImageBitmap(bitmap);
                    holder.image.setVisibility(View.VISIBLE);
                }
            }
        }

        private Bitmap GetPoster()
        {
            try
            {
                PictureTask task = new PictureTask();
                return task.getBitmapFromUrl(posterUrl);
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
    public CinemaMovie getItem(int position)
    {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return movies.indexOf(getItem(position));
    }
}
