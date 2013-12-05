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

/**
 * Created by thorsteinn on 11/21/13.
 */
public class TheatreExpandableListAdapter extends BaseExpandableListAdapter {

    private List<CinemaShowtimes> _showtimes;
    private Context _context;

    public TheatreExpandableListAdapter(Context context, List<CinemaShowtimes> showtimes) {
        this._context = context;
        this._showtimes = showtimes;
    }

    @Override
    public int getGroupCount() {
        return this._showtimes.size();
    }

    @Override
    public int getChildrenCount(int groupPos) {
        CinemaShowtimes group = (CinemaShowtimes) this.getGroup(groupPos);
        return group.getSchedule().size();
    }

    @Override
    public Object getGroup(int groupPos) {
        return this._showtimes.get(groupPos);
    }

    @Override
    public Object getChild(int groupPos, int childPos) {
        CinemaShowtimes group = (CinemaShowtimes) this.getGroup(groupPos);
        return group.getSchedule().get(childPos);
    }

    @Override
    public long getGroupId(int groupPos) {
        return groupPos;
    }

    @Override
    public long getChildId(int groupPos, int childPos) {
        return childPos;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        CinemaShowtimes cinema = (CinemaShowtimes) this.getGroup(i);
        String theater = cinema.getTheater();

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.listview_cinema_theatre, null);
        }

        TextView name = (TextView) view.findViewById(R.id.theatreName);

        name.setText(theater);

        return view;
    }

    @Override
    public View getChildView(int i, int i2, boolean b, View view, ViewGroup viewGroup) {
        String child = (String) getChild(i, i2);
        String[] childList = child.split(" ");
        String hours = childList[0];
        String hall = childList.length>1 ? childList[1] : "";

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.listview_cinema_showtime, null);
        }

        TextView hoursView = (TextView) view.findViewById(R.id.showtimeShowtime);
        TextView hallView = (TextView) view.findViewById(R.id.showtimeHall);

        hoursView.setText(hours);
        hallView.setText(hall);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return false;
    }
}
