package is.handlers.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
        ImageView poster;
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
            holder.poster = (ImageView) row.findViewById(R.id.traktPoster);

            row.setTag(holder);
        }
        else
        {
            holder = (TraktHolder)row.getTag();
        }

        TraktData show = this.shows.get(position);

        holder.title.setText(show.getTitle());
        holder.poster.setImageBitmap(null);
        final String posterUrl = show.getPoster();
        new GetPosterShow(posterUrl, holder.poster).execute();

        return row;
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

    private class GetPosterShow extends AsyncTask<Bitmap, Void, Bitmap>
    {
        private String posterUrl;
        private ImageView m;

        public GetPosterShow(String posterUrl, ImageView im)
        {
            this.posterUrl = posterUrl;
            this.m = im;
        }

        @Override

        protected Bitmap doInBackground(Bitmap... view)
        {
            return GetPoster();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            m.setImageBitmap(bitmap);
        }

        private Bitmap GetPoster()
        {
            try
            {
                PictureTask task = new PictureTask();
                byte[] bannerByteStream = task.getBitmapFromURL(posterUrl);
                return BitmapFactory.decodeByteArray(bannerByteStream, 0, bannerByteStream.length);
            }
            catch (Exception ex)
            {
                Log.e(getClass().getName(), ex.getMessage());
            }

            return null;
        }
    }
}
