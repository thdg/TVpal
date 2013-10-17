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
import is.datacontracts.ShowDataContract;
import is.tvpal.R;

public class CustomMyShowsAdapter extends BaseAdapter
{
    private Context context;
    private int layoutResourceId;
    private List<ShowDataContract> schedule;

    public CustomMyShowsAdapter(Context context, int layoutResourceId, List<ShowDataContract> schedule)
    {
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.schedule = schedule;
    }

    static class EventHolder
    {
        TextView title;
        TextView network;
        TextView overview;
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
            holder.title = (TextView) row.findViewById(R.id.title);
            holder.network = (TextView) row.findViewById(R.id.network);
            holder.overview = (TextView) row.findViewById(R.id.overview);

            row.setTag(holder);
        }
        else
        {
            holder = (EventHolder)row.getTag();
        }

        final ShowDataContract dataContract = schedule.get(position);

        holder.title.setText(dataContract.getTitle());
        holder.network.setText(String.format("Network: %s",dataContract.getNetwork()));
        holder.overview.setText(String.format("Overview: %s", dataContract.getOverview()));

        return row;
    }

    @Override
    public int getCount()
    {
        return (schedule == null) ? 0 : schedule.size();
    }

    @Override
    public ShowDataContract getItem(int position)
    {
        return schedule.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return schedule.indexOf(getItem(position));
    }
}
