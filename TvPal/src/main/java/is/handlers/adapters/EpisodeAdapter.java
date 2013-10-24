package is.handlers.adapters;

/**
 * Created by Arnar on 18.10.2013.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.List;
import is.datacontracts.EpisodeData;
import is.handlers.database.DbShowHandler;
import is.rules.Helpers;
import is.tvpal.R;

/**
 * Created by Arnar on 17.10.2013.
 */

public class EpisodeAdapter extends BaseAdapter
{
    private Context context;
    private int layoutResourceId;
    private List<EpisodeData> schedule;

    public EpisodeAdapter(Context context, int layoutResourceId, List<EpisodeData> schedule)
    {
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.schedule = schedule;
    }

    static class EventHolder
    {
        TextView numberOfEpisode;
        TextView name;
        TextView aired;
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
            holder.numberOfEpisode = (TextView) row.findViewById(R.id.numberOfEpisode);
            holder.name = (TextView) row.findViewById(R.id.episodeName);
            holder.aired = (TextView) row.findViewById(R.id.aired);

            row.setTag(holder);
        }
        else
        {
            holder = (EventHolder)row.getTag();
        }

        final EpisodeData dataContract = schedule.get(position);

        holder.numberOfEpisode.setText(String.format("%s:", dataContract.getEpisodeNumber()));
        holder.name.setText(dataContract.getEpisodeName());
        holder.aired.setText(Helpers.FormatDateEpisode(dataContract.getAired()));

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
