package is.activites.showActivities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import is.contracts.datacontracts.TraktData;
import is.handlers.adapters.TraktAdapter;
import is.parsers.trakt.HttpUtil;
import is.tvpal.R;
import is.utilities.PictureTask;

public class TraktFragment extends Fragment implements AdapterView.OnItemClickListener
{
    private Context mContext;
    private ListView mListView;
    private TraktAdapter mAdapter;

    public TraktFragment(Context context)
    {
        this.mContext = context;
    }

    public TraktFragment() {}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView = (ListView) getView().findViewById(R.id.trendingTrakt);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_trakt, container, false);

        new GetTrendingShows().execute();

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
    {
        TraktData show = mAdapter.getItem(position);

        Toast.makeText(mContext, show.getTvdbId(), Toast.LENGTH_SHORT).show();
    }

    private class GetTrendingShows extends AsyncTask<String, Void, List<TraktData>>
    {
        private String TraktUrl = "http://api.trakt.tv/shows/trending.json/f0e3af66061e47b3243e25ed7b6443ca";

        @Override
        protected List<TraktData> doInBackground(String... strings)
        {
            return GetTraktShows();
        }

        @Override
        protected void onPostExecute(List<TraktData> shows)
        {
            mAdapter = new TraktAdapter(mContext, R.layout.listview_trakt, shows);
            mListView.setAdapter(mAdapter);
        }

        private List<TraktData> GetTraktShows()
        {
            try
            {
                HttpUtil http = new HttpUtil();
                String json = http.downloadJSONString(TraktUrl);

                Type listType = new TypeToken<ArrayList<TraktData>>() {}.getType();

                List<TraktData> data = new Gson().fromJson(json, listType);

                return data;
            }
            catch (Exception ex)
            {
                Log.e(getClass().getName(), ex.getMessage());
            }

            return null;
        }
    }
}
