package is.activites.showActivities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import is.utilities.BitmapProperties;

public class SearchTvShowActivity extends Activity implements AdapterView.OnItemClickListener
{
    private ListView _lv;
    private EditText _editSearch;
    private List<ShowData> _shows;
    private ProgressDialog _waitingDialog;
    private SearchShowAdapter _adapterView;
    private PopupWindow _popupWindow;
    private ImageView _popupBanner;

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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void Initialize()
    {
        _editSearch = (EditText) findViewById(R.id.editMeh);
        _popupWindow = new PopupWindow();

        _lv = (ListView) findViewById(R.id.lvId);
        _lv.setOnItemClickListener(this);

        InitializeEditTextSearch();
        getActionBar().setDisplayHomeAsUpEnabled(true);
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
        _popupWindow.setContentView(popupView);
        _popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        _popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        TextView showTitle = (TextView) popupView.findViewById(R.id.popupTitle);
        TextView showOverview = (TextView) popupView.findViewById(R.id.popupOverview);
        TextView showNetwork = (TextView) popupView.findViewById(R.id.popupNetwork);
        TextView showFirstAired = (TextView) popupView.findViewById(R.id.popupFirstAired);
        Button showClose = (Button) popupView.findViewById(R.id.popUpClose);
        _popupBanner = (ImageView) popupView.findViewById(R.id.popupBanner);
        if (show.getSeriesId() != null)
            new DownloadBannerBitmap(show.getBanner()).execute();

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
                Log.e(getClass().getName(), ex.getMessage());
            }

            return "Successful";
        }
    }

    private class DownloadBannerBitmap extends AsyncTask<String, Void, String>
    {
        private String apiUrl = "http://thetvdb.com/banners/";
        private String bannerUrl;
        private Bitmap banner;

        public DownloadBannerBitmap(String bannerUrl)
        {
            this.bannerUrl = bannerUrl;
        }

        @Override
        protected String doInBackground(String... urls)
        {
            try
            {
                return GetBanner();
            }
            catch (IOException e)
            {
                return "Unable to retrieve web page. URL may be invalid";
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            if(_popupWindow.isShowing() && result.equalsIgnoreCase("Successful"))
                _popupBanner.setImageBitmap(banner);
        }

        private String GetBanner() throws IOException
        {
            try
            {
                BitmapProperties bmp = new BitmapProperties();
                byte[] bannerByteStream = bmp.getBitmapFromURL(String.format("%s%s", apiUrl, bannerUrl));
                banner = BitmapFactory.decodeByteArray(bannerByteStream, 0, bannerByteStream.length);
                return "Successful";
            }
            catch (Exception ex)
            {
                Log.e(getClass().getName(), ex.getMessage());
            }

            return "Errrrrrrrrrrrrrror";
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}