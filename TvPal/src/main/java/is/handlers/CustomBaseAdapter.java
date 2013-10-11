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
import is.datacontracts.EventDataContract;
import is.tvpal.R;

/**
 * Created by Svavar on 10.10.2013.
 */
public class CustomBaseAdapter extends BaseAdapter {
    Context context;
    int layoutResourceId;
    List<EventDataContract> schedule;

    public CustomBaseAdapter(Context context, int layoutResourceId) {
        this.context = context;
        this.layoutResourceId = layoutResourceId;
    }

    static class EventHolder
    {
        ImageView imgIcon;
        TextView title;
        TextView startTime;
        TextView currentEpisode;
        TextView numberOfEpisodes;
        TextView category;
        TextView subtitles;
        TextView description;
        TextView duration;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        EventHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new EventHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.startTime = (TextView) convertView.findViewById(R.id.startTime);
            holder.duration = (TextView) convertView.findViewById(R.id.duration);


            row.setTag(holder);
        }
        else
        {
            holder = (EventHolder)row.getTag();
        }

        EventDataContract dataContract = (EventDataContract) getItem(position);

        holder.imgIcon.setImageResource(R.drawable.tvshow);
        holder.title.setText(dataContract.getTitle());
        holder.startTime.setText(dataContract.getStartTime());
        holder.currentEpisode.setText(dataContract.getCurrentEpisode());
        holder.numberOfEpisodes.setText(dataContract.getNumberOfEpisodes());
        holder.category.setText(dataContract.getCategory());
        holder.subtitles.setText(dataContract.getSubtitles());
        holder.description.setText(dataContract.getDescription());
        holder.duration.setText(dataContract.getDuration());

        return row;
    }



    @Override
    public int getCount() {
        return (schedule == null) ? 0 : schedule.size();
    }

    @Override
    public EventDataContract getItem(int position) {
        return schedule.get(position);
    }

    @Override
    public long getItemId(int position) {
        return schedule.indexOf(getItem(position));
    }

    public void add(EventDataContract e) {
        schedule.add(e);
    }
}
