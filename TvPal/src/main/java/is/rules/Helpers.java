package is.rules;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import is.tvpal.R;

/**
 * Created by Arnar on 14.9.2013.
 */
public class Helpers
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

    public static String GetCorrectRuvUrlFormat()
    {
        String dateToday = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Now use today date.
        c.add(Calendar.DATE, 7); // Adding 5 days

        String dateAfterWeek = sdf.format(c.getTime());
        return String.format("%s/%s/", dateToday, dateAfterWeek);
    }

    public static String AddOneDayToDate(String workingDate)
    {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;
        try
        {
            date = dt.parse(workingDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(date); // Now use working date.
        c.add(Calendar.DATE, 1); // Add 1 day

        return sdf.format(c.getTime());
    }

    public static String MinusOneDayToDate(String workingDate)
    {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;
        try
        {
            date = dt.parse(workingDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(date); // Now use working date.
        c.add(Calendar.DATE, -1); // Add -1 day

        return sdf.format(c.getTime());
    }

    public static String SetDayFormat(String workingDate)
    {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;
        try
        {
            date = dt.parse(workingDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE d MMM");

        return sdf.format(date);
    }
}
