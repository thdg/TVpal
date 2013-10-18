package is.activites;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import is.datacontracts.EventData;
import is.handlers.EventAdapter;
import is.parsers.Stod2ScheduleParser;
import is.rules.Helpers;
import is.tvpal.R;
import is.handlers.SwipeGestureFilter;

/**
 * Created by Arnar
 *
 * This class handles the activity to show Stöð 2 events.
 * It extends ListActivity to show it as a List.
 * It implements ItemClickListener to handle click events.
 * It implements SwipeGestureFilter.SimpleGestureListener to handle swipe events.
 * @see android.app.ListActivity
 */
public class DisplayStod2Activity extends ListActivity implements AdapterView.OnItemClickListener, SwipeGestureFilter.SimpleGestureListener
{
    public static final String EXTRA_TITLE = "is.activites.TITLE";
    public static final String EXTRA_DESCRIPTION = "is.activites.DESCRIPTION";
    public static final String EXTRA_START = "is.activites.START";
    public static final String EXTRA_DURATION = "is.activites.DURATION";

    private List<EventData> _events;
    private SwipeGestureFilter _detector;
    private ProgressDialog _waitingDialog;
    private String _workingDate;
    private List<EventData> _todaySchedule;
    private EventAdapter _adapterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Initialize();
    }

    private void Initialize()
    {
        _todaySchedule = new ArrayList<EventData>();
        _workingDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        setTitle(String.format("%s: %s", getResources().getString(R.string.stod), Helpers.SetDayFormat(this, _workingDate)));

        Intent intent = getIntent();

        new DownloadStod2Schedules(this).execute(intent.getStringExtra(MainActivity.EXTRA_STOD2));

        ListView lv = getListView();
        lv.setOnItemClickListener(this);

        _detector = new SwipeGestureFilter(this,this);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        EventData selectedEvent = _adapterView.getItem(position);

        Intent intent = new Intent(this, DetailedEventActivity.class);
        intent.putExtra(EXTRA_TITLE, selectedEvent.getTitle());
        intent.putExtra(EXTRA_DESCRIPTION, selectedEvent.getDescription());
        intent.putExtra(EXTRA_START, selectedEvent.getStartTime());
        intent.putExtra(EXTRA_DURATION, selectedEvent.getDuration());

        startActivity(intent);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me)
    {
        // Call onTouchEvent of SwipeGestureFilter class
        this._detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public void onSwipe(int direction)
    {
        switch (direction)
        {
            case SwipeGestureFilter.SWIPE_RIGHT :
                SwipeRightEvent();
                break;

            case SwipeGestureFilter.SWIPE_LEFT :
                SwipeLeftEvent();
                break;
        }

        setTitle(String.format("%s: %s", getResources().getString(R.string.ruv), Helpers.SetDayFormat(this, _workingDate)));
    }

    private void SwipeRightEvent()
    {
        _workingDate = Helpers.MinusOneDayToDate(_workingDate);

        _todaySchedule = new ArrayList<EventData>();

        for (EventData e : _events)
        {
            if (e.getEventDate().equalsIgnoreCase(_workingDate))
                _todaySchedule.add(e);
        }

        if(_todaySchedule.size() == 0)
        {
            Toast.makeText(this, getResources().getString(R.string.scheduleNotAvailable), Toast.LENGTH_SHORT).show();
            _workingDate = Helpers.AddOneDayToDate(_workingDate); //Add one day, so we don't go over the limit
            return;
        }

        _adapterView = new EventAdapter(this, R.layout.listview_item_event, _todaySchedule);
        setListAdapter(_adapterView);
    }

    private void SwipeLeftEvent()
    {
        _workingDate = Helpers.AddOneDayToDate(_workingDate);

        _todaySchedule = new ArrayList<EventData>();

        for (EventData e : _events)
        {
            if (e.getEventDate().equalsIgnoreCase(_workingDate))
                _todaySchedule.add(e);
        }

        if(_todaySchedule.size() == 0)
        {
            Toast.makeText(this, getResources().getString(R.string.scheduleNotAvailable), Toast.LENGTH_SHORT).show();
            _workingDate = Helpers.MinusOneDayToDate(_workingDate); //Minus one day, so we don't go over the limit
            return;
        }

        _adapterView = new EventAdapter(this, R.layout.listview_item_event, _todaySchedule);
        setListAdapter(_adapterView);
    }

    private class DownloadStod2Schedules extends AsyncTask<String, Void, String>
    {
        private Context ctx;

        public DownloadStod2Schedules(Context context)
        {
            this.ctx = context;
        }

        @Override
        protected String doInBackground(String... urls)
        {
            try
            {
                return GetSchedules(urls[0]);
            }
            catch (IOException e)
            {
                return "Unable to retrieve web page. URL may be invalid";
            }
        }

        @Override
        protected void onPreExecute()
        {
            _waitingDialog = new ProgressDialog(ctx);
            _waitingDialog.setMessage(ctx.getResources().getString(R.string.loadingSchedule));
            _waitingDialog.show();
        }

        @Override
        protected void onPostExecute(String result)
        {
            _adapterView = new EventAdapter(ctx, R.layout.listview_item_event, _todaySchedule);
            setListAdapter(_adapterView);
            _waitingDialog.dismiss();
        }

        private String GetSchedules(String myurl) throws IOException
        {
            try
            {
                Stod2ScheduleParser parser = new Stod2ScheduleParser(myurl);
                _events = parser.GetSchedules();

                for (EventData e : _events)
                {
                    if (e.getEventDate().equalsIgnoreCase(_workingDate))
                        _todaySchedule.add(e);
                }
            }
            catch (Exception ex)
            {
                ex.getMessage();
            }

            return "Successful";
        }
    }
}