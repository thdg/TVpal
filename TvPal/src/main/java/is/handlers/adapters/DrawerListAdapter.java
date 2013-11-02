package is.handlers.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import is.datacontracts.DrawerListData;
import is.datacontracts.Item;
import is.tvpal.R;

/**
 * Created by Þorsteinn on 14.10.2013.
 */
public class DrawerListAdapter extends ArrayAdapter<Item>
{
    private LayoutInflater _Inflater;

    public enum RowType {
        LIST_ITEM, HEADER_ITEM
    }

    /**
     @param context This is the current context of the application activity
     @param items List of items to add to ListView
     */
    public DrawerListAdapter(Context context, List<Item> items)
    {
        super(context, 0, items);
        _Inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return getItem(position).getView(_Inflater, convertView);
    }

    @Override
    public int getViewTypeCount() {
        return RowType.values().length;

    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }
}
