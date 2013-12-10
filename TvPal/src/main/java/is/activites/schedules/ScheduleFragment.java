package is.activites.schedules;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;

import is.activites.baseActivities.BaseFragment;
import is.contracts.datacontracts.EventData;
import is.handlers.adapters.EventAdapter;
import is.tvpal.R;

public class ScheduleFragment extends BaseFragment implements AdapterView.OnItemClickListener
{
    public static final String EXTRA_EVENT = "is.activites.scheduleActivites.EVENT";
    public static final String EXTRA_SCHEDULE_DAY = "is.activites.scheduleActivites.SCHEDULE_DAY";

    private EventAdapter _adapterView;
    private Context mContext;

    public ScheduleFragment() {}

    public static ScheduleFragment newInstance()
    {
        ScheduleFragment fragment = new ScheduleFragment();

        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mContext = activity.getContext();

        View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        Bundle args = getArguments();
        //Todo: Some type checking
        ArrayList<EventData> _todaySchedule = (ArrayList<EventData>)args.getSerializable(EXTRA_SCHEDULE_DAY);

        _adapterView = new EventAdapter(mContext, R.layout.listview_event, _todaySchedule);

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

        Intent intent = new Intent(mContext, DetailedEventActivity.class);
        intent.putExtra(EXTRA_EVENT, selectedEvent);

        startActivity(intent);
    }
}