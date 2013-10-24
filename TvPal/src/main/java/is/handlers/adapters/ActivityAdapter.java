package is.handlers.adapters;

/**
 * Created by Arnar on 17.10.2013.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import is.datacontracts.EpisodeData;
import is.handlers.database.DbShowHandler;
import is.rules.Helpers;
import is.tvpal.R;

public class ActivityAdapter extends BaseAdapter
{
    private Context context;
    private int layoutResourceId;
    private List<EpisodeData> schedule;
    private DbShowHandler db;
    private HashMap<String, Bitmap> pictures;

    public ActivityAdapter(Context context, int layoutResourceId, List<EpisodeData> schedule)
    {
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.schedule = schedule;
        this.db = new DbShowHandler(context);
        this.pictures = new HashMap<String, Bitmap>();
    }

    static class EventHolder
    {
        ImageView episodeImage;
        TextView episodeName;
        TextView episodeNumber;
        TextView episodeAired;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        final EventHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new EventHolder();
            holder.episodeImage = (ImageView) row.findViewById(R.id.activityImg);
            holder.episodeName = (TextView) row.findViewById(R.id.activityTitle);
            holder.episodeAired = (TextView) row.findViewById(R.id.activityAirDate);
            holder.episodeNumber = (TextView) row.findViewById(R.id.activityEpisode);

            row.setTag(holder);
        }
        else
        {
            holder = (EventHolder)row.getTag();
        }

        final EpisodeData dataContract = schedule.get(position);

        holder.episodeName.setText(dataContract.getEpisodeName());
        holder.episodeNumber.setText(String.format("%sx%s", dataContract.getSeasonNumber(), dataContract.getEpisodeNumber()));
        holder.episodeAired.setText(Helpers.FormatDateEpisode(dataContract.getAired()));

        //TODO: Maby this takes to much memory, look into that
        if (!pictures.containsKey(dataContract.getSeriesId()))
        {
            Bitmap bmp = db.GetSeriesThumbnail(dataContract.getSeriesId());
            pictures.put(dataContract.getSeriesId(), bmp);
        }

        holder.episodeImage.setImageBitmap(pictures.get(dataContract.getSeriesId()));

        return row;
    }

    @Override
    public int getCount()
    {
        return (schedule == null) ? 0 : schedule.size();
    }

    @Override
    public EpisodeData getItem(int position)
    {
        return schedule.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return schedule.indexOf(getItem(position));
    }
}
