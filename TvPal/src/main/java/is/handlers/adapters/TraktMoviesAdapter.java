package is.handlers.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import is.contracts.datacontracts.TraktEpisodeData;
import is.contracts.datacontracts.TraktMovieData;
import is.handlers.database.DbShowHandler;
import is.thetvdb.TvDbUtil;
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
public class TraktMoviesAdapter extends BaseAdapter
{
    private Context context;
    private int layoutResourceId;
    private List<TraktMovieData> movies;

    public TraktMoviesAdapter(Context context, int layoutResourceId, List<TraktMovieData> movies)
    {
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.movies = movies;
    }

    static class TraktHolder
    {
        TextView title;
        TextView overview;
        TextView runtime;
        TextView genres;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        final TraktHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new TraktHolder();
            holder.title = (TextView) row.findViewById(R.id.traktMovieTitle);
            holder.overview = (TextView) row.findViewById(R.id.traktMovieOverview);
            holder.runtime = (TextView) row.findViewById(R.id.traktRunTime);
            holder.genres = (TextView) row.findViewById(R.id.traktGenres);

            row.setTag(holder);
        }
        else
        {
            holder = (TraktHolder)row.getTag();
        }

        final TraktMovieData movie = getItem(position);

        holder.title.setText(movie.getTitle());
        holder.overview.setText(movie.getOverview());
        holder.runtime.setText(String.format("Runtime: %s minutes", movie.getRuntime()));

        String genres = joinToString(movie.getGenres());
        holder.genres.setText(String.format("Genres: %s", genres));

        return row;
    }

    private String joinToString(List<String> strings)
    {
        StringBuilder sb = new StringBuilder();

        for (String s : strings)
        {
            sb.append(s).append(",");
        }

        return sb.toString();
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
