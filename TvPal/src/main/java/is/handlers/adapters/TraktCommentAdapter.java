package is.handlers.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import is.contracts.datacontracts.trakt.TraktComment;
import is.tvpal.R;

public class TraktCommentAdapter extends BaseAdapter
{
    private Context context;
    private int layoutResourceId;
    private List<TraktComment> comments;

    public TraktCommentAdapter(Context context, int layoutResourceId, List<TraktComment> comments)
    {
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.comments = comments;
    }

    static class TraktHolder
    {
        TextView username;
        TextView text;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        final TraktHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new TraktHolder();
            holder.username = (TextView) row.findViewById(R.id.trakt_user);
            holder.text = (TextView) row.findViewById(R.id.trakt_commentbyuser);
            row.setTag(holder);
        }
        else
        {
            holder = (TraktHolder)row.getTag();
        }

        final TraktComment comment = getItem(position);

        holder.username.setText(comment.getUser().getUsername() + " shouted");
        holder.text.setText(comment.getComment());

        return row;
    }

    @Override
    public int getCount()
    {
        return (comments == null) ? 0 : comments.size();
    }

    @Override
    public TraktComment getItem(int position)
    {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return comments.indexOf(getItem(position));
    }
}