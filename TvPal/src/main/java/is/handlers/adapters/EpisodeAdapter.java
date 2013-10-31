package is.handlers.adapters;

/**
 * Created by Arnar on 18.10.2013.
 */

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

import is.handlers.database.DbShowHandler;
import is.utilities.DateUtil;
import is.tvpal.R;

public class EpisodeAdapter extends CursorAdapter {

    private static final int LAYOUT = R.layout.listview_episodes;

    private LayoutInflater mLayoutInflater;
    private DbShowHandler db;
    private List<Boolean> mCheckedShows = new ArrayList<Boolean>();
    private String seriesId;
    private String season;

    public EpisodeAdapter(Context context, Cursor c, int flags, String seriesId, String season)
    {
        super(context, c, flags);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.db = new DbShowHandler(context);
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

        final String episodeId = mCursor.getString(0);

        viewHolder.checkShowSeen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                db.UpdateEpisodeSeen(episodeId);
                changeCursor(db.GetCursorEpisodes(seriesId, season));
            }
        });

        String showSeen = mCursor.getString(6);
        if (showSeen.equalsIgnoreCase("1"))
        {
            mCheckedShows.set(position, true);
        }
        else
        {
            mCheckedShows.set(position, false);
        }

        viewHolder.checkShowSeen.setChecked(mCheckedShows.get(position));

        viewHolder.numberOfEpisode.setText(String.format("%s:", mCursor.getString(3)));
        viewHolder.name.setText(mCursor.getString(4));
        viewHolder.aired.setText(DateUtil.FormatDateEpisode(mCursor.getString(5)));

        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        return mLayoutInflater.inflate(LAYOUT, parent, false);
    }
}