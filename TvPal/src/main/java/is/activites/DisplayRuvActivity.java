package is.activites;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.IOException;
import java.util.List;

import is.datacontracts.EventDataContract;
import is.parsers.RuvScheduleParser;
import is.tvpal.R;

public class DisplayRuvActivity extends ListActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener
{
    public static final String EXTRA_TITLE = "online.activites.TITLE";
    public static final String EXTRA_DESCRIPTION = "online.activites.DESCRIPTION";

    private ArrayAdapter<EventDataContract> _schedulesAdapter;
    private ProgressDialog _waitingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Initialize();
    }

    private void Initialize()
    {
        new DownloadRuvSchedules(this).execute(getResources().getString(R.string.ruvBaseUrl));

        ListView lv = getListView();
        lv.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        EventDataContract selectedEvent = _schedulesAdapter.getItem(position);

        Intent intent = new Intent(this, DetailedEventActivity.class);
        intent.putExtra(EXTRA_DESCRIPTION, selectedEvent.getDescription());
        intent.putExtra(EXTRA_TITLE, selectedEvent.getTitle());

        startActivity(intent);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}
    public void onNothingSelected(AdapterView<?> parent) {}

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
            setListAdapter(_schedulesAdapter);

            _waitingDialog.dismiss();
        }

        private String GetSchedules(String myurl) throws IOException
        {
            try
            {
                RuvScheduleParser parser = new RuvScheduleParser(myurl);
                List<EventDataContract> events = parser.GetSchedules();

                _schedulesAdapter = new ArrayAdapter<EventDataContract>(ctx, R.layout.row);

                for (EventDataContract e : events)
                {
                    _schedulesAdapter.add(e);
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
