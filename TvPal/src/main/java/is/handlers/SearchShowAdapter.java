package is.handlers;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import is.datacontracts.EpisodeDataContract;
import is.datacontracts.ShowDataContract;
import is.parsers.TvDbEpisodeParser;
import is.tvpal.R;

public class SearchShowAdapter extends BaseAdapter
{
    private Context context;
    private int layoutResourceId;
    private List<ShowDataContract> shows;

    public SearchShowAdapter(Context context, int layoutResourceId, List<ShowDataContract> shows)
    {
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.shows = shows;
    }

    static class ShowHolder
    {
        TextView title;
        TextView network;
        TextView overview;
        CheckBox checkShow;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        final ShowHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ShowHolder();
            holder.title = (TextView) row.findViewById(R.id.title);
            holder.network = (TextView) row.findViewById(R.id.network);
            holder.overview = (TextView) row.findViewById(R.id.overview);
            holder.checkShow = (CheckBox) row.findViewById(R.id.checkShow);

            row.setTag(holder);
        }
        else
        {
            holder = (ShowHolder)row.getTag();
        }

        final ShowDataContract dataContract = shows.get(position);

        holder.title.setText(dataContract.getTitle());
        holder.network.setText(String.format("Network: %s",dataContract.getNetwork()));
        holder.overview.setText(String.format("Overview: %s", dataContract.getOverview()));

        holder.checkShow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (((CheckBox) view).isChecked())
                {
                    DbShowHandler db = new DbShowHandler(context);

                    boolean exists = db.CheckIfSeriesExist(dataContract.getSeriesId());

                    if (!exists)
                    {
                        String apiKey = context.getResources().getString(R.string.apiKey);
                        String tvDbUrl = String.format("http://thetvdb.com/api/%s/series/%s/all/", apiKey, dataContract.getSeriesId());
                        new DownloadEpisodes(context).execute(tvDbUrl);
                        db.AddSeries(dataContract);
                    }
                    else
                        Toast.makeText(context, String.format("Series %s exist in your shows", dataContract.getTitle()), Toast.LENGTH_SHORT).show();
                }
                holder.checkShow.setEnabled(false);
            }
        });

        return row;
    }

    private class DownloadEpisodes extends AsyncTask<String, Void, String>
    {
        private Context ctx;

        public DownloadEpisodes(Context context)
        {
            this.ctx = context;
        }

        @Override
        protected String doInBackground(String... urls)
        {
            try
            {
                return GetEpisodes(urls[0]);
            }
            catch (IOException e)
            {
                return "Unable to retrieve web page. URL may be invalid";
            }
        }

        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected void onPostExecute(String result)
        {
        }

        private String GetEpisodes(String myurl) throws IOException
        {
            try
            {
                TvDbEpisodeParser parser = new TvDbEpisodeParser(myurl);
                List<EpisodeDataContract> episodes = parser.GetEpisodes();

            }
            catch (Exception ex)
            {
                ex.getMessage();
            }

            return "Successful";
        }
    }

    @Override
    public int getCount()
    {
        return (shows == null) ? 0 : shows.size();
    }

    @Override
    public ShowDataContract getItem(int position)
    {
        return shows.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return shows.indexOf(getItem(position));
    }
}
