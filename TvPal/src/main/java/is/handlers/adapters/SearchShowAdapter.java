package is.handlers.adapters;

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

import is.datacontracts.EpisodeData;
import is.datacontracts.ShowData;
import is.handlers.database.DbShowHandler;
import is.parsers.TvDbEpisodeParser;
import is.tvpal.R;

public class SearchShowAdapter extends BaseAdapter
{
    private Context context;
    private int layoutResourceId;
    private List<ShowData> shows;

    public SearchShowAdapter(Context context, int layoutResourceId, List<ShowData> shows)
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

        final ShowData dataContract = shows.get(position);

        holder.title.setText(dataContract.getTitle());

        String strNetwork = dataContract.getNetwork();
        if(strNetwork == null) strNetwork = "";
        holder.network.setText(String.format("Network: %s",strNetwork));

        String strOverview = dataContract.getOverview();
        if(strOverview == null) strOverview = "";
        holder.overview.setText(String.format("Overview: %s",strOverview));

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
                        String tvDbUrl = String.format("http://thetvdb.com/api/%s/series/%s/all/en.xml", apiKey, dataContract.getSeriesId());
                        new DownloadEpisodes(context, dataContract).execute(tvDbUrl);
                        Toast.makeText(context, String.format("%s will be added to your shows", dataContract.getTitle()), Toast.LENGTH_SHORT).show();
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
        private ShowData show;

        public DownloadEpisodes(Context context, ShowData show)
        {
            this.ctx = context;
            this.show = show;
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
        protected void onPostExecute(String result)
        {
            if (result.equalsIgnoreCase("Successful"))
                Toast.makeText(ctx, String.format("Added %s your shows", show.getTitle()), Toast.LENGTH_SHORT).show();
            else if (result.equalsIgnoreCase("Problem"))
                Toast.makeText(ctx, "Whoops, something went wrong", Toast.LENGTH_SHORT).show();
        }

        private String GetEpisodes(String myurl) throws IOException
        {
            DbShowHandler db = new DbShowHandler(ctx);

            try
            {
                db.AddSeries(show);

                TvDbEpisodeParser parser = new TvDbEpisodeParser(myurl, context, show.getSeriesId());
                List<EpisodeData> episodes = parser.GetEpisodes();

                db.AddSeries(show);
                db.AddEpisodes(episodes);
            }
            catch (Exception ex)
            {
                db.RemoveShow(show.getSeriesId());
                return "Problem";
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
    public ShowData getItem(int position)
    {
        return shows.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return shows.indexOf(getItem(position));
    }
}
