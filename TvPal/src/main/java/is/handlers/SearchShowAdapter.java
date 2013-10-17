package is.handlers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import is.datacontracts.ShowDataContract;
import is.tvpal.R;

public class SearchShowAdapter extends BaseAdapter
{
    private Context context;
    private int layoutResourceId;
    private List<ShowDataContract> shows;

    public SearchShowAdapter(Context context, int layoutResourceId, List<ShowDataContract> shows)
    {
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.shows = shows;
    }

    static class ShowHolder
    {
        TextView title;
        TextView network;
        TextView overview;
        CheckBox checkShow;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        final ShowHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ShowHolder();
            holder.title = (TextView) row.findViewById(R.id.title);
            holder.network = (TextView) row.findViewById(R.id.network);
            holder.overview = (TextView) row.findViewById(R.id.overview);
            holder.checkShow = (CheckBox) row.findViewById(R.id.checkShow);

            row.setTag(holder);
        }
        else
        {
            holder = (ShowHolder)row.getTag();
        }

        final ShowDataContract dataContract = shows.get(position);

        holder.title.setText(dataContract.getTitle());
        holder.network.setText(String.format("Network: %s",dataContract.getNetwork()));
        holder.overview.setText(String.format("Overview: %s", dataContract.getOverview()));

        holder.checkShow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (((CheckBox) view).isChecked())
                {
                    DataBaseHandler db = new DataBaseHandler(context);

                    boolean exists = db.CheckIfSeriesExist(dataContract.getSeriesId());

                    if (!exists)
                    {
                        db.AddSeries(dataContract);
                        Toast.makeText(context, String.format("%s has been added to your shows", dataContract.getTitle()) ,Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(context, String.format("Series %s exist in your shows", dataContract.getTitle()), Toast.LENGTH_SHORT).show();
                }
                holder.checkShow.setEnabled(false);
            }
        });

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
