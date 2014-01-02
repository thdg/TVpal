package is.utilities;

import android.content.Context;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import is.tvpal.R;

/**
 * Created by Arnar
 *
 * This class has few static Helper functions.
 * Such as showing alert dialogs and formatting dates.
 *
 */
public class DateUtil
{
    public static String GetCorrectRuvUrlFormat(int daysToAdd)
    {
        String dateToday = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Now use today date.
        c.add(Calendar.DATE, daysToAdd); // Adding 5 days

        String dateAfterWeek = sdf.format(c.getTime());
        return String.format("%s/%s/", dateToday, dateAfterWeek);
    }

    public static String AddDaysToDate(String workingDate, int daysToAdd)
    {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

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
        c.add(Calendar.DATE, daysToAdd);

        return sdf.format(c.getTime());
    }

    private static String GetCorrectDayFormat(Context cxt, String day)
    {
        if(day.equalsIgnoreCase("Monday") || day.equalsIgnoreCase("mánudagur"))
            return cxt.getResources().getString(R.string.monday);
        if(day.equalsIgnoreCase("Tuesday") || day.equalsIgnoreCase("þriðjudagur"))
            return cxt.getResources().getString(R.string.tuesday);
        if(day.equalsIgnoreCase("Wednesday") || day.equalsIgnoreCase("miðvikudagur"))
            return cxt.getResources().getString(R.string.wednesday);
        if(day.equalsIgnoreCase("Thursday") || day.equalsIgnoreCase("fimmtudagur"))
            return cxt.getResources().getString(R.string.thursday);
        if(day.equalsIgnoreCase("Friday") || day.equalsIgnoreCase("föstudagur"))
            return cxt.getResources().getString(R.string.friday);
        if(day.equalsIgnoreCase("Saturday") || day.equalsIgnoreCase("laugardagur"))
            return cxt.getResources().getString(R.string.saturday);
        if(day.equalsIgnoreCase("Sunday") || day.equalsIgnoreCase("sunnudagur"))
            return cxt.getResources().getString(R.string.sunday);

        return null;
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

    public static String GetDateFormatForTabs(Context cxt, String workingDate)
    {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

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

        SimpleDateFormat sdfMonth = new SimpleDateFormat("d");
        String month = sdfMonth.format(date);

        return String.format("%s %s",day, month);
    }
}