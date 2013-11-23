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
import is.contracts.datacontracts.trakt.TraktEpisodeData;
import is.handlers.database.DbShowHandler;
import is.thetvdb.TvDbUtil;
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
public class TraktEpisodeAdapter extends BaseAdapter
{
    public static final String ApiKey = "9A96DA217CEB03E7";

    private Context context;
    private int layoutResourceId;
    private List<TraktEpisodeData> shows;
    private List<Integer> seriesIds;

    public TraktEpisodeAdapter(Context context, int layoutResourceId, List<TraktEpisodeData> shows)
    {
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.shows = shows;
        this.seriesIds = new DbShowHandler(context).GetAllSeriesIds();
    }

    static class TraktHolder
    {
        TextView title;
        TextView overview;
        ImageView poster;
        CheckBox addShow;
        int position;
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
            holder.title = (TextView) row.findViewById(R.id.traktTitle);
            holder.overview = (TextView) row.findViewById(R.id.traktOverview);
            holder.poster = (ImageView) row.findViewById(R.id.traktPoster);
            holder.addShow = (CheckBox) row.findViewById(R.id.traktAddShow);

            row.setTag(holder);
        }
        else
        {
            holder = (TraktHolder)row.getTag();
        }

        final TraktEpisodeData show = getItem(position);

        holder.position = position;
        holder.title.setText(show.getTitle());
        holder.overview.setText(show.getOverview());

        holder.addShow.setChecked(false);
        holder.addShow.setVisibility(seriesIds.contains(show.getSeriesId()) ? View.INVISIBLE : View.VISIBLE);

        holder.addShow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (((CheckBox) view).isChecked())
                {
                    if (!seriesIds.contains(show.getSeriesId()))
                    {
                        String tvDbUrl = String.format("http://thetvdb.com/api/%s/series/%d/all/en.xml", ApiKey, show.getSeriesId());
                        TvDbUtil tvdb = new TvDbUtil(context);
                        tvdb.GetEpisodesBySeason(tvDbUrl, show.getTitle());

                        seriesIds.add(show.getSeriesId());
                    }
                    else //This should never happen
                        Toast.makeText(context, String.format("Series %s exists in your shows", show.getTitle()), Toast.LENGTH_SHORT).show();
                }
                holder.addShow.setVisibility(View.INVISIBLE);
            }
        });

        holder.poster.setImageBitmap(null);
        final String posterUrl = show.getPoster();

        //Execute Async Tasks parallely to improve bitmap download.
        new GetPosterShow(posterUrl, position).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, holder);

        return row;
    }

    /**
     * A class to download bitmaps and load them in ViewHolders
     * @author Arnar
     * @see android.os.AsyncTask
     */
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
                PictureTask task = new PictureTask();
                return task.getBitmapFromUrl(formattedPosterUrl);
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
    public TraktEpisodeData getItem(int position)
    {
        return shows.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return shows.indexOf(getItem(position));
    }
}
