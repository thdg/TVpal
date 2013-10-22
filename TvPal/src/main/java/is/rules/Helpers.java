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
 * Created by Arnar
 *
 * This class has few static Helper functions.
 * Such as showing alert dialogs and formatting dates.
 *
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
        c.add(Calendar.DATE, 5); // Adding 5 days

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

    public static String SetDayFormat(Context cxt, String workingDate)
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

        SimpleDateFormat sdfDay = new SimpleDateFormat("EEEE");
        String day = GetCorrectDayFormat(cxt, sdfDay.format(date));

        SimpleDateFormat sdfMonth = new SimpleDateFormat("MMM");
        String month = sdfMonth.format(date);

        SimpleDateFormat sdfDayNumber = new SimpleDateFormat("d");
        String dayNumber = sdfDayNumber.format(date);

        return String.format("%s %s %s",day, dayNumber, month);
    }

    private static String GetCorrectDayFormat(Context cxt, String day)
    {
        if(day.equalsIgnoreCase("Monday"))
            return cxt.getResources().getString(R.string.monday);
        if(day.equalsIgnoreCase("Tuesday"))
            return cxt.getResources().getString(R.string.tuesday);
        if(day.equalsIgnoreCase("Wednesday"))
            return cxt.getResources().getString(R.string.wednesday);
        if(day.equalsIgnoreCase("Thursday"))
            return cxt.getResources().getString(R.string.thursday);
        if(day.equalsIgnoreCase("Friday"))
            return cxt.getResources().getString(R.string.friday);
        if(day.equalsIgnoreCase("Saturday"))
            return cxt.getResources().getString(R.string.saturday);
        if(day.equalsIgnoreCase("Sunday"))
            return cxt.getResources().getString(R.string.sunday);

        return null;
    }

    public static String GetDayFormatForEpisodes(String day)
    {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");

        Date date;
        try
        {
            date = dt.parse(day);
        }
        catch (ParseException e)
        {
            return "TBA";
        }

        SimpleDateFormat formatDay = new SimpleDateFormat("yyyy - EEEE d MMM");
        return formatDay.format(date);
    }

    public static String FormatDateEpisode(String day)
    {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");

        Date date;
        try
        {
            date = dt.parse(day);
        }
        catch (ParseException e)
        {
            return "TBA";
        }

        SimpleDateFormat formatDay = new SimpleDateFormat("MMM d - yyyy");
        return formatDay.format(date);
    }
}
