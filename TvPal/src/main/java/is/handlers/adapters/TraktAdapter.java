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
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import is.contracts.datacontracts.TraktData;
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
 * @see    android.widget.BaseAdapter
 */
public class TraktAdapter extends BaseAdapter
{
    private Context context;
    private int layoutResourceId;
    private List<TraktData> shows;

    public TraktAdapter(Context context, int layoutResourceId, List<TraktData> shows)
    {
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.shows = shows;
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
        TraktHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new TraktHolder();
            holder.title = (TextView) row.findViewById(R.id.traktTitle);
            holder.overview = (TextView) row.findViewById(R.id.traktOverview);
            holder.poster = (ImageView) row.findViewById(R.id.traktPoster);

            row.setTag(holder);
        }
        else
        {
            holder = (TraktHolder)row.getTag();
        }

        TraktData show = getItem(position);

        holder.position = position;
        holder.title.setText(show.getTitle());
        holder.overview.setText(show.getOverview());
        //TODO:
        //Look into using GridView and not listview to show TraktShows, will perform better with
        //loading bitmaps to the view.
        holder.poster.setImageBitmap(null);
        final String posterUrl = show.getPoster();
        new GetPosterShow(posterUrl, position).execute(holder);

        return row;
    }

    private class GetPosterShow extends AsyncTask<TraktHolder, Void, Bitmap>
    {
        private String posterUrl;
        private TraktHolder holder;
        private int position;

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
        return (shows == null) ? 0 : shows.size();
    }

    @Override
    public TraktData getItem(int position)
    {
        return shows.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return shows.indexOf(getItem(position));
    }
}
