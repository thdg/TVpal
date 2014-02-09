package is.gui.schedules;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.slidinglayer.SlidingLayer;

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
    private SlidingLayer mSlidingLayer;
    private ScrollView mDetailedScrollView;
    private TextView mTitle;
    private TextView mEventStartTime;
    private TextView mEventDuration;
    private TextView mEventDescription;

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
            AttachViews();

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
        EventData event = mAdapter.getItem(position);

        if (!mSlidingLayer.isOpened())
        {
            mSlidingLayer.openLayer(true);

            mTitle.setText(event.getTitle());
            mEventDescription.setText(event.getDescription());

            if (!event.getStartTime().isEmpty())
            {
                mEventStartTime.setText(String.format("%s: %s"
                        , getResources().getString(R.string.starting_time)
                        , event.getStartTime()));
            }

            if (!event.getDuration().isEmpty())
            {
                mEventDuration.setText(String.format("%s: %s"
                        , getResources().getString(R.string.duration)
                        , event.getDuration()));
            }

        }
    }

    private void AttachViews()
    {
        mSlidingLayer = (SlidingLayer) getActivity().findViewById(R.id.sliderDetailedInfo);
        mDetailedScrollView = (ScrollView) getActivity().findViewById(R.id.detailedLayout);

        if (mDetailedScrollView != null)
        {
            mTitle = (TextView) mDetailedScrollView.findViewById(R.id.title);
            mEventStartTime = (TextView) mDetailedScrollView.findViewById(R.id.event_starting);
            mEventDuration = (TextView) mDetailedScrollView.findViewById(R.id.event_duration);
            mEventDescription = (TextView) mDetailedScrollView.findViewById(R.id.event_description);
        }
    }
}