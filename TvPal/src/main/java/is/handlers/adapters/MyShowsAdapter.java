package is.handlers.adapters;

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

import is.datacontracts.ShowData;
import is.tvpal.R;

public class MyShowsAdapter extends BaseAdapter
{
    private Context context;
    private int layoutResourceId;
    private List<ShowData> schedule;

    public MyShowsAdapter(Context context, int layoutResourceId, List<ShowData> schedule)
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

        final ShowData dataContract = schedule.get(position);

        holder.title.setText(dataContract.getTitle());

        String strNetwork = dataContract.getNetwork();
        if(strNetwork == null) strNetwork = "";
        holder.network.setText(String.format("Network: %s",strNetwork));

        String strOverview = dataContract.getOverview();
        if(strOverview == null) strOverview = "";
        holder.overview.setText(String.format("Overview: %s",strOverview));

        return row;
    }

    @Override
    public int getCount()
    {
        return (schedule == null) ? 0 : schedule.size();
    }

    @Override
    public ShowData getItem(int position)
    {
        return schedule.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return schedule.indexOf(getItem(position));
    }
}
