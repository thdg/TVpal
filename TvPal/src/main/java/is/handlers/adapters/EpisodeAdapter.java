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
import android.widget.TextView;

import is.rules.Helpers;
import is.tvpal.R;

public class EpisodeAdapter extends CursorAdapter {

    private static final int LAYOUT = R.layout.listview_episodes;

    private LayoutInflater mLayoutInflater;

    public EpisodeAdapter(Context context, Cursor c, int flags)
    {
        super(context, c, flags);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    static class ViewHolder
    {
        TextView numberOfEpisode;
        TextView name;
        TextView aired;
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

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.numberOfEpisode.setText(String.format("%s:", mCursor.getString(3)));
        viewHolder.name.setText(mCursor.getString(4));
        viewHolder.aired.setText(Helpers.FormatDateEpisode(mCursor.getString(5)));

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