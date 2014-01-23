package is.handlers.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import is.handlers.database.DbEpisodes;
import is.utilities.DateUtil;
import is.tvpal.R;

/**
 * Created by Arnar on 18.10.2013.
 * An adapter to show all episodes of a single season of some series
 * It extends CursorAdapter
 * @see android.support.v4.widget.CursorAdapter
 */

public class SingleSeasonAdapter extends CursorAdapter {

    private static final int LAYOUT = R.layout.listview_episodes;

    private LayoutInflater mLayoutInflater;
    private DbEpisodes db;
    private List<Boolean> mCheckedShows = new ArrayList<Boolean>();
    private int seriesId;
    private int season;

    public SingleSeasonAdapter(Context context, Cursor c, int flags, int seriesId, int season)
    {
        super(context, c, flags);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.db = new DbEpisodes(context);
        this.season = season;
        this.seriesId = seriesId;

        for(int i = 0; i < this.getCount(); i++) {
            mCheckedShows.add(i, false);
        }
    }

    static class ViewHolder
    {
        TextView numberOfEpisode;
        TextView name;
        TextView aired;
        CheckBox checkShowSeen;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (!mDataValid)
        {
            throw new IllegalStateException("Only call when cursor is valid");
        }
        if (!mCursor.moveToPosition(position))
        {
            throw new IllegalStateException("Failed to move cursor to position  " + position);
        }

        final ViewHolder viewHolder;

        if (convertView == null)
        {
            convertView = newView(mContext, mCursor, parent);

            viewHolder = new ViewHolder();
            viewHolder.numberOfEpisode = (TextView) convertView.findViewById(R.id.numberOfEpisode);
            viewHolder.name = (TextView) convertView.findViewById(R.id.episodeName);
            viewHolder.aired = (TextView) convertView.findViewById(R.id.aired);
            viewHolder.checkShowSeen = (CheckBox) convertView.findViewById(R.id.setEpisodeSeen);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final int episodeId = mCursor.getInt(Episodes.EpisodeId);

        viewHolder.checkShowSeen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                db.UpdateEpisodeSeen(episodeId);
                changeCursor(db.GetCursorEpisodes(seriesId, season));
            }
        });

        int showSeen = mCursor.getInt(Episodes.Seen);
        if (showSeen == 1)
        {
            mCheckedShows.set(position, true);
        }
        else
        {
            mCheckedShows.set(position, false);
        }

        viewHolder.checkShowSeen.setChecked(mCheckedShows.get(position));

        viewHolder.numberOfEpisode.setText(String.format("%d:", mCursor.getInt(Episodes.Episode)));
        viewHolder.name.setText(mCursor.getString(Episodes.EpisodeName));
        viewHolder.aired.setText(DateUtil.FormatDateEpisode(mCursor.getString(Episodes.Aired)));

        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {}

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        return mLayoutInflater.inflate(LAYOUT, parent, false);
    }

    private interface Episodes
    {
        int EpisodeId = 0;
        int SeriesId = 1;
        int Season = 2;
        int Episode = 3;
        int EpisodeName = 4;
        int Aired = 5;
        int Seen = 6;
    }
}