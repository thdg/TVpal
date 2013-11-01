package is.activites.showActivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.List;

import is.datacontracts.ShowData;
import is.handlers.adapters.SearchShowAdapter;
import is.parsers.TvDbShowParser;
import is.tvpal.R;

public class SearchTvShowActivity extends Activity implements AdapterView.OnItemClickListener
{
    private ListView _lv;
    private EditText _editSearch;
    private List<ShowData> _shows;
    private ProgressDialog _waitingDialog;
    private SearchShowAdapter _adapterView;
    private PopupWindow _popupWindow;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_tvshows_activity);

        Initialize();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (_popupWindow.isShowing())
            {
                _popupWindow.dismiss();
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    private void Initialize()
    {
        _editSearch = (EditText) findViewById(R.id.editMeh);

        _lv = (ListView) findViewById(R.id.lvId);
        _lv.setOnItemClickListener(this);

        InitializeEditTextSearch();
    }

    private void InitializeEditTextSearch()
    {
        _editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); //Close the keyboard

                    return true;
                }
                return false;
            }
        });
    }

    private void performSearch()
    {
        String userEntry = null;

        try {
            userEntry = _editSearch.getText().toString();
            userEntry = userEntry.replace(" ", "%20"); //Delete whitespaces and instert %20 to set correct urlFormat for the API
        }
        catch(Exception ex) {
            ex.getMessage();
        }

        String searchUrl = String.format("%s%s", getResources().getString(R.string.tvdbBaseUrl), userEntry);

        new DownloadShows(this).execute(searchUrl);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        ShowData show = _adapterView.getItem(i);

        CreatePopupWindow(show);
    }

    private void CreatePopupWindow(ShowData show)
    {
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_show, null);
        _popupWindow = new PopupWindow(popupView,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        TextView showTitle = (TextView) popupView.findViewById(R.id.popupTitle);
        TextView showOverview = (TextView) popupView.findViewById(R.id.popupOverview);
        TextView showNetwork = (TextView) popupView.findViewById(R.id.popupNetwork);
        TextView showFirstAired = (TextView) popupView.findViewById(R.id.popupFirstAired);
        Button showClose = (Button) popupView.findViewById(R.id.popUpClose);

        showTitle.setText(show.getTitle());
        showOverview.setText(String.format("Overview: %s", show.getOverview()));
        showNetwork.setText(String.format("Network: %s", show.getNetwork()));
        showFirstAired.setText(String.format("First aired: %s", show.getFirstAired()));

        _popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        showClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _popupWindow.dismiss();
            }
        });
    }

    private class DownloadShows extends AsyncTask<String, Void, String>
    {
        private Context ctx;

        public DownloadShows(Context context)
        {
            this.ctx = context;
        }

        @Override
        protected String doInBackground(String... urls)
        {
            try
            {
                return GetShows(urls[0]);
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
            _waitingDialog.setMessage(getString(R.string.loadingShows));
            _waitingDialog.show();
        }

        @Override
        protected void onPostExecute(String result)
        {
            if (result.equalsIgnoreCase("empty"))
                Toast.makeText(ctx, "No shows found", Toast.LENGTH_SHORT).show();

            _adapterView = new SearchShowAdapter(ctx, R.layout.listview_search_show, _shows);
            _lv.setAdapter(_adapterView);

            _waitingDialog.dismiss();
        }

        private String GetShows(String myurl) throws IOException
        {
            try
            {
                TvDbShowParser parser = new TvDbShowParser(myurl);
                _shows = parser.GetShows();

                if(_shows.size() == 0)
                {
                    return "Empty";
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