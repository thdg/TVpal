package is.utilities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;

/**
 * Created by Arnar on 31.10.2013.
 */
public class DialogUtil
{
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void showNetworkAlertDialog(Context ctx)
    {
        int THEME_HOLO_DARK = 2;

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx, THEME_HOLO_DARK);
        builder
                .setTitle("Network not available")
                .setMessage("You need to turn on Wifi or mobile network")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.show();
        alert.show();
    }
}
