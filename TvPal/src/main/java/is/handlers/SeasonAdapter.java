package is.handlers;

/**
 * Created by Arnar on 17.10.2013.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

import is.datacontracts.EpisodeData;
import is.tvpal.R;

public class SeasonAdapter extends BaseAdapter
{
    private Context context;
    private int layoutResourceId;
    private List<EpisodeData> schedule;

    public SeasonAdapter(Context context, int layoutResourceId, List<EpisodeData> schedule)
    {
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.schedule = schedule;
    }

    static class EventHolder
    {
        TextView title;
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
            holder.title = (TextView) row.findViewById(R.id.season);

            row.setTag(holder);
        }
        else
        {
            holder = (EventHolder)row.getTag();
        }

        final EpisodeData dataContract = schedule.get(position);

        holder.title.setText(String.format("Season %s", dataContract.getSeasonNumber()));

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
