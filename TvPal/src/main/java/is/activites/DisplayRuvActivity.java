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
import is.datacontracts.EventDataContract;
import is.handlers.CustomBaseAdapter;
import is.handlers.SwipeGestureFilter;
import is.parsers.RuvScheduleParser;
import is.rules.Helpers;
import is.tvpal.R;

public class DisplayRuvActivity extends ListActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, SwipeGestureFilter.SimpleGestureListener
{
    public static final String EXTRA_TITLE = "is.activites.TITLE";
    public static final String EXTRA_DESCRIPTION = "is.activites.DESCRIPTION";
    public static final String EXTRA_START = "is.activites.START";
    public static final String EXTRA_DURATION = "is.activites.DURATION";

    private List<EventDataContract> _events;
    private ProgressDialog _waitingDialog;
    private SwipeGestureFilter _detector;
    private String _workingDate;
    private List<EventDataContract> _todaySchedule;
    private CustomBaseAdapter _adapterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Initialize();
    }

    private void Initialize()
    {
        _todaySchedule = new ArrayList<EventDataContract>();
        _workingDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        setTitle(String.format("%s: %s", getResources().getString(R.string.ruv), Helpers.SetDayFormat(_workingDate)));

        String ruvBaseUrl = getResources().getString(R.string.ruvBaseUrl);
        String ruvUrl = String.format("%s%s", ruvBaseUrl, Helpers.GetCorrectRuvUrlFormat());

        new DownloadRuvSchedules(this).execute(ruvUrl);

        ListView lv = getListView();
        lv.setOnItemClickListener(this);

        _detector = new SwipeGestureFilter(this,this);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        EventDataContract selectedEvent = _adapterView.getItem(position);

        Intent intent = new Intent(this, DetailedEventActivity.class);
        intent.putExtra(EXTRA_DESCRIPTION, selectedEvent.getDescription());
        intent.putExtra(EXTRA_TITLE, selectedEvent.getTitle());
        intent.putExtra(EXTRA_START, selectedEvent.getStartTime());
        intent.putExtra(EXTRA_DURATION, selectedEvent.getDuration());

        startActivity(intent);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}
    public void onNothingSelected(AdapterView<?> parent) {}

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

        setTitle(String.format("%s: %s", getResources().getString(R.string.ruv), Helpers.SetDayFormat(_workingDate)));
    }

    private void SwipeRightEvent()
    {
        _workingDate = Helpers.MinusOneDayToDate(_workingDate);

        _todaySchedule = new ArrayList<EventDataContract>();

        for (EventDataContract e : _events)
        {
            if (e.getEventDate().equalsIgnoreCase(_workingDate))
                _todaySchedule.add(e);
        }

        if(_todaySchedule.size() == 0)
        {
            Toast.makeText(this, "Schedule not available: " + _workingDate, Toast.LENGTH_SHORT).show();
            _workingDate = Helpers.AddOneDayToDate(_workingDate); //Add one day, so we don't go over the limit
            return;
        }

        _adapterView = new CustomBaseAdapter(this, R.layout.listview_item_row, _todaySchedule);
        setListAdapter(_adapterView);
        Toast.makeText(this, _workingDate, Toast.LENGTH_SHORT).show();
    }

    private void SwipeLeftEvent()
    {
        _workingDate = Helpers.AddOneDayToDate(_workingDate);

        _todaySchedule = new ArrayList<EventDataContract>();

        for (EventDataContract e : _events)
        {
            if (e.getEventDate().equalsIgnoreCase(_workingDate))
                _todaySchedule.add(e);
        }

        if(_todaySchedule.size() == 0)
        {
            Toast.makeText(this, "Schedule not available: " + _workingDate, Toast.LENGTH_SHORT).show();
            _workingDate = Helpers.MinusOneDayToDate(_workingDate); //Minus one day, so we don't go over the limit
            return;
        }

        _adapterView = new CustomBaseAdapter(this, R.layout.listview_item_row, _todaySchedule);
        setListAdapter(_adapterView);
        Toast.makeText(this, _workingDate, Toast.LENGTH_SHORT).show();
    }

    private class DownloadRuvSchedules extends AsyncTask<String, Void, String>
    {
        private Context ctx;

        public DownloadRuvSchedules(Context context)
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
            _waitingDialog.setMessage("Loading schedules");
            _waitingDialog.show();
        }

        @Override
        protected void onPostExecute(String result)
        {
            _adapterView = new CustomBaseAdapter(ctx, R.layout.listview_item_row, _todaySchedule);
            setListAdapter(_adapterView);
            _waitingDialog.dismiss();
        }

        private String GetSchedules(String myurl) throws IOException
        {
            try
            {
                RuvScheduleParser parser = new RuvScheduleParser(myurl);
                _events = parser.GetSchedules();

                for (EventDataContract e : _events)
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
