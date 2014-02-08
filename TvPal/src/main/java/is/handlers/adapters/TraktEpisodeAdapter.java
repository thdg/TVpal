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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;
import is.contracts.datacontracts.trakt.TraktEpisodeData;
import is.handlers.database.DatabaseHandler;
import is.handlers.database.DbEpisodes;
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

    private Context mContext;
    private int layoutResourceId;
    private List<TraktEpisodeData> shows;
    private List<Integer> seriesIds;

    public TraktEpisodeAdapter(Context context, int layoutResourceId, List<TraktEpisodeData> shows)
    {
        this.mContext = context;
        this.layoutResourceId = layoutResourceId;
        this.shows = shows;
        this.seriesIds = new DbEpisodes(context).GetAllSeriesIds();
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
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
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
                        TvDbUtil tvdb = new TvDbUtil(mContext);
                        tvdb.GetEpisodesBySeason(tvDbUrl, show.getTitle());

                        seriesIds.add(show.getSeriesId());
                    }
                    else //This should never happen
                        Toast.makeText(mContext, String.format("Series %s exists in your shows", show.getTitle()), Toast.LENGTH_SHORT).show();
                }
                holder.addShow.setVisibility(View.INVISIBLE);
            }
        });

        final String posterUrl = StringUtil.formatTrendingPosterUrl(show.getPoster(), "-138");

        Picasso
                .with(mContext)
                .load(posterUrl)
                .into(holder.poster);

        return row;
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
