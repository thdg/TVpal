package is.handlers.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.nfc.NfcAdapter;
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

import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

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

        final String posterUrl = StringUtil.formatTrendingPosterUrl(movie.getImage().getPoster(), "-138");

        Picasso
                .with(mContext)
                .load(posterUrl)
                .into(holder.poster);

        return row;
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