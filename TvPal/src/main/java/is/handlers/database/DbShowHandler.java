package is.handlers.database;

/**
 * Created by Arnar on 17.10.2013.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import is.datacontracts.EpisodeData;
import is.datacontracts.ShowData;

/**
 * Class to handle database actions.
 * Create tables, insert/delete/update, get cursor etc.
 * @author Arnar
 */

public class DbShowHandler extends SQLiteOpenHelper
{
    //Database & table info
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "shows";
    private static final String TABLE_SERIES = "series";
    private static final String TABLE_EPISODES = "episodes";

    // Columns in shows
    private static final String KEY_S_SERIESID = "seriesId";
    private static final String KEY_S_NAME = "name";
    private static final String KEY_S_OVERVIEW = "overview";
    private static final String KEY_S_NETWORK = "network";
    private static final String KEY_S_THUMBNAIL = "thumbnail";
    private static final String KEY_S_LASTUPDATED = "lastupdated";

    //Columns in episodes
    private static final String KEY_E_EPISODEID = "episodeId";
    private static final String KEY_E_SERIESID = "seriesId";
    public static final String KEY_E_SEASON = "season";
    private static final String KEY_E_EPISODE = "episode";
    private static final String KEY_E_EPISODENAME = "episodeName";
    private static final String KEY_E_OVERVIEW = "overview";
    private static final String KEY_E_AIRED = "aired";
    private static final String KEY_E_SEEN = "seen";
    private static final String KEY_E_DIRECTOR = "director";
    private static final String KEY_E_RATING = "rating";

    public DbShowHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_SERIES_TABLE =
                "CREATE TABLE " + TABLE_SERIES + "("
                        + KEY_S_SERIESID + " TEXT PRIMARY KEY,"
                        + KEY_S_NAME + " TEXT,"
                        + KEY_S_OVERVIEW + " TEXT,"
                        + KEY_S_NETWORK + " TEXT,"
                        + KEY_S_THUMBNAIL + " BLOB,"
                        + KEY_S_LASTUPDATED + " TEXT"
                        + ")";

        String CREATE_EPISODE_TABLE =
                "CREATE TABLE " + TABLE_EPISODES + "("
                        + KEY_E_EPISODEID + " TEXT PRIMARY KEY,"
                        + KEY_E_SERIESID + " TEXT,"
                        + KEY_E_SEASON + " TEXT,"
                        + KEY_E_EPISODE + " TEXT,"
                        + KEY_E_EPISODENAME  + " TEXT,"
                        + KEY_E_AIRED + " TEXT,"
                        + KEY_E_OVERVIEW + " TEXT,"
                        + KEY_E_SEEN + " TEXT,"
                        + KEY_E_DIRECTOR + " TEXT,"
                        + KEY_E_RATING + " TEXT"
                        + ")";

        db.execSQL(CREATE_SERIES_TABLE);
        db.execSQL(CREATE_EPISODE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //Add functionality when database is updated
    }

    public void AddSeries(ShowData series)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        if (db != null)
        {
            db.beginTransaction();
            try
            {
                ContentValues values = new ContentValues();
                values.put(KEY_S_SERIESID, series.getSeriesId());
                values.put(KEY_S_NAME, series.getTitle());
                values.put(KEY_S_OVERVIEW, series.getOverview());
                values.put(KEY_S_NETWORK, series.getNetwork());

                db.insert(TABLE_SERIES, null, values);
                db.setTransactionSuccessful();
            }
            catch (Exception ex)
            {
                //TODO: Implement error handling
            }
            finally
            {
                db.endTransaction();
            }
        }

        db.close();
    }

    public boolean CheckIfSeriesExist(String seriesId)
    {
        String selectClause = "SELECT * FROM " + TABLE_SERIES;
        String whereClause  = "WHERE " + KEY_S_SERIESID + " = " + seriesId;
        String selectQuery  = String.format("%s %s", selectClause, whereClause);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //Returns true if series doesn't exist
        return cursor.getCount() != 0;
    }

    public void RemoveShow(String seriesId)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_SERIES, KEY_S_SERIESID + " = " + seriesId , null);
        database.delete(TABLE_EPISODES, KEY_E_SERIESID + " = " + seriesId, null);
    }

    public void AddThumbnailToSeries(byte[] byteStream, String seriesId)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_S_THUMBNAIL, byteStream);

        db.update(TABLE_SERIES, values, KEY_S_SERIESID + " = " + seriesId, null);
        db.close();
    }

    public void AddLastUpdatedToSeries(String lastUpdated, String seriesId)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_S_LASTUPDATED, lastUpdated);

        if(db != null)
        {
            db.update(TABLE_SERIES, values, KEY_S_SERIESID + " = " + seriesId, null);
            db.close();
        }
    }

    public Bitmap GetSeriesThumbnail(String seriesId)
    {
        String selectQuery = String.format("SELECT thumbnail from %s where seriesId = %s", TABLE_SERIES, seriesId);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();//It should exist

        byte[] thumbnailByteStream = cursor.getBlob(0);
        Bitmap bmp = BitmapFactory.decodeByteArray(thumbnailByteStream, 0, thumbnailByteStream.length);
        return bmp;
    }

    public void AddEpisodes(List<EpisodeData> episodes)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db != null)
        {
            db.beginTransaction();
            try
            {
                for (EpisodeData episode : episodes)
                {
                    ContentValues values = new ContentValues();
                    values.put(KEY_E_EPISODEID, episode.getEpisodeId());
                    values.put(KEY_S_SERIESID, episode.getSeriesId());
                    values.put(KEY_E_SEASON, episode.getSeasonNumber());
                    values.put(KEY_E_EPISODE, episode.getEpisodeNumber());
                    values.put(KEY_E_EPISODENAME, episode.getEpisodeName());
                    values.put(KEY_E_AIRED, episode.getAired());
                    values.put(KEY_S_OVERVIEW, episode.getOverview());
                    values.put(KEY_E_SEEN, "0");
                    values.put(KEY_E_DIRECTOR, episode.getDirector());
                    values.put(KEY_E_RATING, episode.getRating());

                    db.insert(TABLE_EPISODES, null, values);
                }

                db.setTransactionSuccessful();
            }
            catch (Exception ex)
            {
                //TODO: Implement error handling
            }
            finally
            {
                db.endTransaction();
            }

            db.close();
        }
    }

    public void AddEpisode(SQLiteDatabase db, EpisodeData episode)
    {
        try
        {
            ContentValues values = new ContentValues();
            values.put(KEY_E_EPISODEID, episode.getEpisodeId());
            values.put(KEY_S_SERIESID, episode.getSeriesId());
            values.put(KEY_E_SEASON, episode.getSeasonNumber());
            values.put(KEY_E_EPISODE, episode.getEpisodeNumber());
            values.put(KEY_E_EPISODENAME, episode.getEpisodeName());
            values.put(KEY_E_AIRED, episode.getAired());
            values.put(KEY_S_OVERVIEW, episode.getOverview());
            values.put(KEY_E_SEEN, "0");
            values.put(KEY_E_DIRECTOR, episode.getDirector());
            values.put(KEY_E_RATING, episode.getRating());

            db.insert(TABLE_EPISODES, null, values);
        }
        catch (Exception ex)
        {
            Log.e(getClass().getName(), ex.getMessage());
        }
    }

    public void SetSeasonSeen(String seriesId, String seasonNumber)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            if (db !=null)
            {
                db.beginTransaction();
                db.execSQL(String.format("UPDATE %s SET seen = 1 WHERE seriesId = '%s' AND season = '%s'", TABLE_EPISODES, seriesId, seasonNumber));
                db.setTransactionSuccessful();
            }
        }
        catch (Exception ex)
        {
            Log.e(getClass().getName(), ex.getMessage());
        }
        finally
        {
            db.endTransaction();
        }

        db.close();
    }

    public void UpdateEpisodeSeen(String episodeId)
    {
        String seen = "1";

        if (GetShowSeen(episodeId))
            seen = "0";

        ContentValues values = new ContentValues();

        values.put(KEY_E_SEEN, seen);

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_EPISODES, values, KEY_E_EPISODEID + " = " + episodeId, null);
    }

    public boolean GetShowSeen(String episodeId)
    {
        String selectQuery = String.format("select * from episodes where episodeId = " + episodeId);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        return Integer.parseInt(cursor.getString(7)) == 1;
    }

    /*
    *
        Cursors
    *
     */

    public Cursor GetCursorSeasons(String seriesId)
    {
        String selectQuery = "SELECT distinct season as _id, season, seriesId FROM " + TABLE_EPISODES + " WHERE seriesId = " + seriesId + " order by season+0 desc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        return cursor;
    }

    public Cursor GetCursorEpisodes(String seriesId, String seasonNumber)
    {
        String selectQuery = String.format("select episodeId as _id, seriesId, season, episode, episodeName, aired, seen " +
                "from episodes where seriesId = %s and season = %s", seriesId, seasonNumber);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        return cursor;
    }

    public Cursor GetCursorUpcoming()
    {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String selectQuery = String.format("select episodeId as _id, episodeName, aired, seriesId, season, episode " +
                "from episodes " +
                "where aired >= '%s' " +
                "order by aired limit 15", date);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        return cursor;
    }

    public Cursor GetCursorRecent()
    {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String selectQuery = String.format("select episodeId as _id, episodeName, aired, seriesId, season, episode " +
                "from episodes " +
                "where aired < '%s' " +
                "order by aired desc limit 15", date);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        return cursor;
    }

    public Cursor GetCursorMyShows()
    {
        String selectQuery = String.format("select seriesId as _id, name, thumbnail " +
                "from %s order by %s", TABLE_SERIES, KEY_S_NAME);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        return cursor;
    }

    public Cursor GetCursorEpisodesDetailed(String seriesId, String seasonNumber)
    {
        String selectQuery = String.format("select episodeId as _id, episode, episodeName, season, overview, aired, director, rating, seen " +
                "from episodes where seriesId = %s and season = %s", seriesId, seasonNumber);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        return cursor;
    }

    /*
        Database queries
     */

    public int GetTotalSeasonCount(String seriesId, String season)
    {
        String selectQuery = String.format("select count(*) from %s " +
                "where seriesId = '%s' and season = '%s'", TABLE_EPISODES, seriesId, season);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public int GetTotalSeasonSeen(String seriesId, String season)
    {
        String selectQuery = String.format("select count(*) from %s " +
                "where seriesId = '%s' and season = '%s' and seen = '1'", TABLE_EPISODES, seriesId, season);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public String GetSeriesLastUpdate(String seriesId)
    {
        String selectQuery = String.format("select seriesId as _id, lastupdated from series where seriesId = '%s'", seriesId);

        SQLiteDatabase db = this.getWritableDatabase();
        assert db != null;
        Cursor cursor = db.rawQuery(selectQuery ,null);

        cursor.moveToFirst();
        return cursor.getString(1);
    }

    public boolean DoesEpisodeExist(SQLiteDatabase db, String episodeId)
    {
        String selectQuery = String.format("select episodeId as _id from episodes where episodeId = '%s'", episodeId);
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor.getCount() == 1;
    }

    /**
     * Update episodes section
     */

    public void UpdateSingleSeries(List<EpisodeData> episodes, String latestUpdate, String seriesId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try
        {
            for (EpisodeData e : episodes)
            {
                if (!DoesEpisodeExist(db, e.getEpisodeId())) {
                    AddEpisode(db, e);
                    Log.d(getClass().getName(), "Updating episode: " + e.getEpisodeName());
                }
                else {
                    UpdateEpisode(db, e);
                    Log.d(getClass().getName(), "Updating episode: " + e.getEpisodeName());
                }
            }

            //Only update latestUpdate column if we have episodes to update
            if (episodes.size() > 0)
            {
                //Finally update latest update column in seriesTable
                ContentValues values = new ContentValues();
                values.put(KEY_S_LASTUPDATED, latestUpdate);
                db.update(TABLE_SERIES, values, KEY_S_SERIESID + " = " + seriesId, null);
            }

            db.setTransactionSuccessful();
        }
        catch (Exception ex)
        {
            Log.e(getClass().getName(), ex.getMessage());
        }
        finally
        {
            db.endTransaction();
        }

        db.close();
    }

    public void UpdateAllSeries()
    {
        List<String> seriesIds = GetAllSeriesIds();

        for (String series : seriesIds)
        {
            //TODO: Call parser to get the episodes to update & getLatestUpdate & call UpdateSingleSeries
        }
    }

    public void UpdateEpisode(SQLiteDatabase db, EpisodeData episode)
    {
        try
        {
            ContentValues values = new ContentValues();
            values.put(KEY_E_EPISODEID, episode.getEpisodeId());
            values.put(KEY_S_SERIESID, episode.getSeriesId());
            values.put(KEY_E_SEASON, episode.getSeasonNumber());
            values.put(KEY_E_EPISODE, episode.getEpisodeNumber());
            values.put(KEY_E_EPISODENAME, episode.getEpisodeName());
            values.put(KEY_E_AIRED, episode.getAired());
            values.put(KEY_S_OVERVIEW, episode.getOverview());
            values.put(KEY_E_DIRECTOR, episode.getDirector());
            values.put(KEY_E_RATING, episode.getRating());

            db.update(TABLE_EPISODES, values, KEY_E_EPISODEID + " = " + episode.getEpisodeId(), null);
        }
        catch (Exception ex)
        {
            Log.e(getClass().getName(), ex.getMessage());
        }
    }

    public List<String> GetAllSeriesIds()
    {
        List<String> seriesIds = new ArrayList<String>();

        String selectQuery = "select seriesId as _id from series";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            seriesIds.add(cursor.getString(0));
            cursor.moveToNext();
        }
        db.close();

        return seriesIds;
    }
}