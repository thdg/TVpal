package is.handlers.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import is.contracts.datacontracts.SeriesData;
import is.handlers.database.DatabaseHandler;
import is.handlers.database.DbEpisodes;
import is.thetvdb.TvDbUtil;
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
        this.seriesIds = new DbEpisodes(context).GetAllSeriesIds();
    }

    static class ShowHolder
    {
        TextView title;
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

        holder.overview.setText(series.getOverview());

        holder.checkShow.setChecked(false);
        holder.checkShow.setVisibility(seriesIds.contains(series.getSeriesId()) ? View.INVISIBLE : View.VISIBLE);

        holder.checkShow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (((CheckBox) view).isChecked())
                {
                    if (!seriesIds.contains(series.getSeriesId()))
                    {
                        String tvDbUrl = String.format("http://thetvdb.com/api/%s/series/%d/all/en.xml", ApiKey, series.getSeriesId());
                        TvDbUtil tvdb = new TvDbUtil(context);
                        tvdb.GetEpisodesBySeason(tvDbUrl, series.getTitle());

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
