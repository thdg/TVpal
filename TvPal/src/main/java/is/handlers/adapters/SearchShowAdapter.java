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
import is.contracts.datacontracts.EpisodeData;
import is.contracts.datacontracts.SeriesData;
import is.handlers.database.DbShowHandler;
import is.parsers.tvdb.TvDbEpisodeParser;
import is.tvpal.R;

/**
 * Created by Arnar on 12.10.2013.
 * An adapter to shows from the TvDb database
 * A user can check a show to add the show under "MyShows"
 * It extends BaseAdapter
 * @see android.widget.BaseAdapter
 */

public class SearchShowAdapter extends BaseAdapter
{
    public static final String ApiKey = "9A96DA217CEB03E7";

    private Context context;
    private int layoutResourceId;
    private List<SeriesData> shows;
    private List<Integer> seriesIds;

    public SearchShowAdapter(Context context, int layoutResourceId, List<SeriesData> shows)
    {
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.shows = shows;
        this.seriesIds = new DbShowHandler(context).GetAllSeriesIds();
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

        final SeriesData series = getItem(position);

        holder.title.setText(series.getTitle());

        String network = series.getNetwork() == null ? "" : String.format("Network: %s",series.getNetwork());
        holder.network.setText(network);

        String overview = series.getOverview();
        overview = overview == null ? "" : String.format("%s...",overview.substring(0, Math.min(overview.length(), 60)));
        holder.overview.setText(overview);

        holder.checkShow.setChecked(false);
        holder.checkShow.setVisibility(seriesIds.contains(series.getSeriesId()) ? View.INVISIBLE : View.VISIBLE);

        holder.checkShow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (((CheckBox) view).isChecked())
                {
                    if (!seriesIds.contains(series.getSeriesId()))                    {
                        String tvDbUrl = String.format("http://thetvdb.com/api/%s/series/%d/all/en.xml", ApiKey, series.getSeriesId());
                        new DownloadEpisodes(context, series.getTitle()).execute(tvDbUrl);
                        Toast.makeText(context, String.format("%s will be added to your shows", series.getTitle()), Toast.LENGTH_SHORT).show();
                        seriesIds.add(series.getSeriesId());
                    }
                    else
                        Toast.makeText(context, String.format("Series %s exists in your shows", series.getTitle()), Toast.LENGTH_SHORT).show();
                }
                holder.checkShow.setVisibility(View.INVISIBLE);
            }
        });

        return row;
    }

    private class DownloadEpisodes extends AsyncTask<String, Void, String>
    {
        private Context ctx;
        private String seriesTitle;

        public DownloadEpisodes(Context context, String seriesTitle)
        {
            this.ctx = context;
            this.seriesTitle = seriesTitle;
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
                Toast.makeText(ctx, String.format("Added %s your shows", seriesTitle), Toast.LENGTH_SHORT).show();
            else if (result.equalsIgnoreCase("Problem"))
                Toast.makeText(ctx, "Whoops, something went wrong", Toast.LENGTH_SHORT).show();
        }

        private String GetEpisodes(String myurl) throws IOException
        {
            DbShowHandler db = new DbShowHandler(ctx);

            try
            {
                TvDbEpisodeParser parser = new TvDbEpisodeParser(myurl);
                List<EpisodeData> episodes = parser.GetEpisodes();
                SeriesData show = parser.getSeries();

                db.InsertFullSeriesInfo(episodes, show);
            }
            catch (Exception ex)
            {
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
    public SeriesData getItem(int position)
    {
        return shows.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return shows.indexOf(getItem(position));
    }
}
