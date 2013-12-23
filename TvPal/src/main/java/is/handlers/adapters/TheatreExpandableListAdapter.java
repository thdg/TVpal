package is.handlers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import java.util.List;
import is.contracts.datacontracts.cinema.CinemaShowtimes;
import is.tvpal.R;

public class TheatreExpandableListAdapter extends BaseExpandableListAdapter {

    private List<CinemaShowtimes> mShowTimes;
    private Context mContext;

    public TheatreExpandableListAdapter(Context context, List<CinemaShowtimes> showtimes)
    {
        this.mContext = context;
        this.mShowTimes = showtimes;
    }

    @Override
    public int getGroupCount()
    {
        return this.mShowTimes.size();
    }

    @Override
    public int getChildrenCount(int groupPos)
    {
        CinemaShowtimes group = (CinemaShowtimes) this.getGroup(groupPos);
        return group.getSchedule().size();
    }

    @Override
    public Object getGroup(int groupPos)
    {
        return this.mShowTimes.get(groupPos);
    }

    @Override
    public Object getChild(int groupPos, int childPos)
    {
        CinemaShowtimes group = (CinemaShowtimes) this.getGroup(groupPos);
        return group.getSchedule().get(childPos);
    }

    @Override
    public long getGroupId(int groupPos)
    {
        return groupPos;
    }

    @Override
    public long getChildId(int groupPos, int childPos)
    {
        return childPos;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup)
    {
        CinemaShowtimes cinema = (CinemaShowtimes) this.getGroup(i);
        String theater = cinema.getTheater();

        if (view == null) 
        {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.listview_cinema_theatre, null);
        }

        ((TextView) view.findViewById(R.id.theatreName)).setText(theater);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup)
    {
        String child = (String) getChild(groupPosition, childPosition);
        String[] childList = child.split(" ");
        String hours = childList[0];

        int index = child.lastIndexOf("(");
        String hall = index != -1 ? child.substring(index) : "";

        if (view == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.listview_cinema_showtime, null);
        }

        ((TextView) view.findViewById(R.id.showtimeShowtime)).setText(hours);
        ((TextView) view.findViewById(R.id.showtimeHall)).setText(hall);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i2)
    {
        return false;
    }
}
