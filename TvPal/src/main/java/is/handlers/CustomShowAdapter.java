package is.handlers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import is.datacontracts.ShowDataContract;
import is.tvpal.R;

public class CustomShowAdapter extends BaseAdapter
{
    private Context context;
    private int layoutResourceId;
    private List<ShowDataContract> shows;

    public CustomShowAdapter(Context context, int layoutResourceId, List<ShowDataContract> shows)
    {
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.shows = shows;
    }

    static class ShowHolder
    {
        ImageView imgIcon;
        TextView title;
        TextView network;
        TextView overview;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ShowHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ShowHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.title = (TextView) row.findViewById(R.id.title);
            holder.network = (TextView) row.findViewById(R.id.network);
            holder.overview = (TextView) row.findViewById(R.id.overview);

            row.setTag(holder);
        }
        else
        {
            holder = (ShowHolder)row.getTag();
        }

        ShowDataContract dataContract = shows.get(position);

        holder.title.setText(dataContract.getTitle());
        holder.network.setText(String.format("Network: %s",dataContract.getNetwork()));
        holder.overview.setText(String.format("Overview: %s", dataContract.getOverview()));

        return row;
    }

    @Override
    public int getCount()
    {
        return (shows == null) ? 0 : shows.size();
    }

    @Override
    public ShowDataContract getItem(int position)
    {
        return shows.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return shows.indexOf(getItem(position));
    }
}
