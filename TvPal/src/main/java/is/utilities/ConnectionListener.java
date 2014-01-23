package is.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Arnar
 *
 * Class to check if a device is connected to a Network.
 */
public class ConnectionListener
{
    private Context context;

    public ConnectionListener(Context context)
    {
        this.context = context;
    }

    public boolean isNetworkAvailable()
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiStatus   = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileStatus = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return ((wifiStatus != null && wifiStatus.isConnected()) || ((mobileStatus != null) && mobileStatus.isConnected()));
    }
}
