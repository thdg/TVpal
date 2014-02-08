package is.gui.schedules;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;

import is.gui.base.BaseFragment;
import is.contracts.datacontracts.EventData;
import is.handlers.adapters.EventAdapter;
import is.tvpal.R;

public class ScheduleFragment extends BaseFragment implements AdapterView.OnItemClickListener
{
    public static final String EXTRA_EVENT = "is.activites.scheduleActivites.EVENT";
    public static final String EXTRA_SCHEDULE_DAY = "is.activites.scheduleActivites.SCHEDULE_DAY";

    private EventAdapter mAdapter;
    private Context mContext;

    public ScheduleFragment() {}

    public static ScheduleFragment newInstance()
    {
        ScheduleFragment fragment = new ScheduleFragment();

        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mContext = activity.getContext();

        Bundle args = getArguments();
        ArrayList<EventData> _todaySchedule = (ArrayList<EventData>)args.getSerializable(EXTRA_SCHEDULE_DAY);

        if (_todaySchedule != null && _todaySchedule.size() > 0)
        {
            mAdapter = new EventAdapter(mContext, R.layout.listview_event, _todaySchedule);

            ((ListView) getView().findViewById(R.id.schedules)).setAdapter(mAdapter);
            ((ListView) getView().findViewById(R.id.schedules)).setOnItemClickListener(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
    {
        EventData selectedEvent = mAdapter.getItem(position);

        Intent intent = new Intent(mContext, DetailedEventActivity.class);
        intent.putExtra(EXTRA_EVENT, selectedEvent);

        startActivity(intent);
    }
}