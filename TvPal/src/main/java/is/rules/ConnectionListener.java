package is.rules;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Sigurður
 *
 * Class to check if a device is connected to a Network.
 */
public class ConnectionListener
{
    public boolean isNetworkAvailable(Context ctx)
    {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiStatus   = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileStatus = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return ((wifiStatus != null && wifiStatus.isConnected()) || ((mobileStatus != null) && mobileStatus.isConnected()));
    }
}
