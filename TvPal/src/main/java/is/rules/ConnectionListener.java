package is.rules;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Arnar on 14.9.2013.
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
