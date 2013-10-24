package is.activites.showActivities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import is.datacontracts.EpisodeData;
import is.handlers.adapters.ActivityAdapter;
import is.handlers.database.DbShowHandler;
import is.tvpal.R;

public class UpcomingShowsActivity extends Activity
{
    private ListView _listView;
    private DbShowHandler _db;
    private List<EpisodeData> _upcomingShows;
    private ActivityAdapter _adapterView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shows_activity);

        Initialize();
    }

    private void Initialize()
    {
        _db = new DbShowHandler(this);
        TextView tv = (TextView) findViewById(R.id.overviewTitle);
        tv.setText("Upcoming shows");

        _listView = (ListView) findViewById(R.id.overviewShows);

        new GetUpcomingShows(this).execute();
    }

    private class GetUpcomingShows extends AsyncTask<String, Void, String>
    {
        private Context context;

        public GetUpcomingShows(Context context)
        {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... urls)
        {
            try
            {
                return GetShows();
            }
            catch (Exception e)
            {
                return "Unable to retrieve Upcoming shows";
            }
        }

        @Override
        public void onPostExecute(String result)
        {
            if (result.equalsIgnoreCase("Successful"))
            {
                _adapterView = new ActivityAdapter(context, R.layout.listview_activity, _upcomingShows);
                _listView.setAdapter(_adapterView);
            }
        }

        private String GetShows()
        {
            try
            {
                _upcomingShows = _db.GetUpcomingShows(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            }
            catch (Exception ex)
            {
                return "Error";
            }

            return "Successful";
        }
    }
}
