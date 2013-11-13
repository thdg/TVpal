package is.activites.scheduleActivites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import is.datacontracts.EventData;
import is.handlers.adapters.EventAdapter;
import is.tvpal.R;

public class ScheduleFragment extends Fragment implements AdapterView.OnItemClickListener
{
    public static final String EXTRA_EVENT = "is.activites.scheduleActivites.EVENT";
    public static final String ARG_SCHEDULE_DAY = "schedule_day";

    private EventAdapter _adapterView;
    private Context cxt;

    public ScheduleFragment(Context cxt)
    {
        this.cxt = cxt;
    }

    public ScheduleFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        Bundle args = getArguments();
        //Todo: Some type checking
        ArrayList<EventData> _todaySchedule = (ArrayList<EventData>)args.getSerializable(ARG_SCHEDULE_DAY);

        _adapterView = new EventAdapter(inflater.getContext(), R.layout.listview_event, _todaySchedule);

        if (rootView != null)
        {
            ((ListView) rootView.findViewById(R.id.schedules)).setAdapter(_adapterView);
            ((ListView) rootView.findViewById(R.id.schedules)).setOnItemClickListener(this);
        }

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        EventData selectedEvent = _adapterView.getItem(i);

        Intent intent = new Intent(cxt, DetailedEventActivity.class);
        intent.putExtra(EXTRA_EVENT, selectedEvent);

        startActivity(intent);
    }
}