package is.handlers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.client.methods.HttpPost;

import java.util.List;
import is.datacontracts.EventDataContract;
import is.tvpal.R;

/**
 * Created by Svavar
 *
 * This class implements an Adapter that can be used in both ListView
 * (by implementing the specialized ListAdapter interface}
 * and Spinner (by implementing the specialized SpinnerAdapter interface.
 * It extends BaseAdapter.
 *
 * @see    android.widget.BaseAdapter
 */
public class CustomEventAdapter extends BaseAdapter
{
    private Context context;
    private int layoutResourceId;
    private List<EventDataContract> schedule;

    /**
        @param context This is the current context of the application activity
        @param layoutResourceId The id of the xml layout
        @param schedule List of EventDataContract
     */
    public CustomEventAdapter(Context context, int layoutResourceId, List<EventDataContract> schedule)
    {
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.schedule = schedule;
    }

    static class EventHolder
    {
        ImageView imgIcon;
        TextView title;
        TextView startTime;
        TextView duration;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        EventHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new EventHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.title = (TextView) row.findViewById(R.id.title);
            holder.startTime = (TextView) row.findViewById(R.id.startTime);
            holder.duration = (TextView) row.findViewById(R.id.duration);

            row.setTag(holder);
        }
        else
        {
            holder = (EventHolder)row.getTag();
        }

        EventDataContract dataContract = schedule.get(position);

        String serviceName = dataContract.getServiceName();

        if (serviceName.equalsIgnoreCase("Skjar Einn")) {
            holder.imgIcon.setImageResource(R.drawable.skjareinn_64);
        }
        else if (serviceName.equalsIgnoreCase(context.getResources().getString(R.string.ruv))) {
            holder.imgIcon.setImageResource(R.drawable.ruv_svartur_64);
        }
        else if (serviceName.equalsIgnoreCase("STOD2")) {
            holder.imgIcon.setImageResource(R.drawable.stod2_64);
        }
        else if(serviceName.equalsIgnoreCase("SPORT")) {
            holder.imgIcon.setImageResource(R.drawable.stod2sport_64);
        }
        else if(serviceName.equalsIgnoreCase("STOD3")) {
            holder.imgIcon.setImageResource(R.drawable.stod3_64);
        }
        else if(serviceName.equalsIgnoreCase("BIO")) {
            holder.imgIcon.setImageResource(R.drawable.stod2bio_64);
        }

        holder.title.setText(dataContract.getTitle());
        holder.startTime.setText(String.format("Byrjar: %s",dataContract.getStartTime()));
        holder.duration.setText(String.format("Lengd: %s", dataContract.getDuration()));

        return row;
    }

    @Override
    public int getCount()
    {
        return (schedule == null) ? 0 : schedule.size();
    }

    @Override
    public EventDataContract getItem(int position)
    {
        return schedule.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return schedule.indexOf(getItem(position));
    }
}