package is.handlers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import java.util.List;

import is.datacontracts.IDrawerItem;

/**
 * Created by Þorsteinn on 14.10.2013.
 */
public class DrawerListAdapter extends ArrayAdapter<IDrawerItem>
{
    private LayoutInflater _inflater;

    public enum RowType {
        LIST_ITEM, HEADER_ITEM
    }

    /**
     @param context This is the current context of the application activity
     @param items List of items to add to ListView
     */
    public DrawerListAdapter(Context context, List<IDrawerItem> items)
    {
        super(context, 0, items);
        _inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return getItem(position).getView(_inflater, convertView);
    }

    @Override
    public int getViewTypeCount() {
        return RowType.values().length;

    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) == RowType.LIST_ITEM.ordinal();
    }
}
