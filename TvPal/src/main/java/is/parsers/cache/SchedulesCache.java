package is.parsers.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import is.utilities.ConnectionListener;
import is.webservices.RestClient;

public class SchedulesCache
{
    public static String RuvSchedules = "is.parsers.schedules.RUVCACHE";
    public static String RuvLatestUpdate = "is.parsers.schedules.RUVLATESTUPDATE";

    public static String SkjarinnSchedules = "is.parsers.schedules.SKJARINNCACHE";
    public static String SkjarinnLatestUpdate = "is.parsers.schedules.SKJARINNLATESTUPDATE";

    public static String Stod2Schedules = "is.parsers.schedules.STOD2CACHE";
    public static String Stod2LatestUpdate = "is.parsers.schedules.STOD2LATESTUPDATE";

    public static String Stod2SportSchedules = "is.parsers.schedules.STOD2SPORTCACHE";
    public static String Stod2SportLatestUpdate = "is.parsers.schedules.STOD2SPORTLATESTUPDATE";

    public static String Stod2Sport2Schedules = "is.parsers.schedules.STOD2SPORT2CACHE";
    public static String Stod2Sport2LatestUpdate = "is.parsers.schedules.STOD2SPORT2LATESTUPDATE";

    public static String Stod2BioSchedules = "is.parsers.schedules.STOD2BIOCACHE";
    public static String Stod2BioLatestUpdate = "is.parsers.schedules.STOD2BIOLATESTUPDATE";

    public static String Stod2GullSchedules = "is.parsers.schedules.STOD2GULLCACHE";
    public static String Stod2GullLatestUpdate = "is.parsers.schedules.STOD2GULLLATESTUPDATE";

    public static String Stod3Schedules = "is.parsers.schedules.STOD3CACHE";
    public static String Stod3LatestUpdate = "is.parsers.schedules.STOD3LATESTUPDATE";

    public Context mContext;

    public SchedulesCache(Context mContext)
    {
        this.mContext = mContext;
    }

    public String GetSchedules(String url, String schedulesCache, String latestUpdateCache)
    {
        try
        {
            String latestUpdate = GetLatestUpdateFromCache(latestUpdateCache);
            String todaysDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            String schedules;
            boolean networkAvailable = new ConnectionListener(mContext).isNetworkAvailable();

            if ((!todaysDate.equalsIgnoreCase(latestUpdate)) && networkAvailable)
            {
                RestClient client = new RestClient();
                schedules = client.Get(url);
                WriteSchedulesToCache(schedules, schedulesCache, latestUpdateCache);
            }
            else
            {
                schedules = GetSchedulesFromCache(schedulesCache);
            }

            return schedules;
        }
        catch (Exception ex)
        {
            Log.e(getClass().getName(), ex.getMessage());
        }

        return null;
    }

    private void WriteSchedulesToCache(String schedules, String schedulesCache, String latestUpdateCache)
    {
        String latestUpdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(schedulesCache, schedules);
        editor.putString(latestUpdateCache, latestUpdate);
        editor.commit();
    }

    private String GetSchedulesFromCache(String schedulesCache)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getString(schedulesCache, null);
    }

    private String GetLatestUpdateFromCache(String latestUpdate)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getString(latestUpdate, null);
    }
}
