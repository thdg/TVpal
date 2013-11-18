package is.handlers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import is.contracts.datacontracts.EpisodeData;
import is.contracts.datacontracts.SeriesData;

/**
 * Class to handle database actions.
 * Create tables, insert/delete/update data, get cursors etc.
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
    private static final String KEY_S_GENRES = "genres";

    //Columns in episodes
    private static final String KEY_E_EPISODEID = "episodeId";
    private static final String KEY_E_SERIESID = "seriesId";
    private static final String KEY_E_SEASON = "season";
    private static final String KEY_E_EPISODE = "episode";
    private static final String KEY_E_EPISODENAME = "episodeName";
    private static final String KEY_E_OVERVIEW = "overview";
    private static final String KEY_E_AIRED = "aired";
    private static final String KEY_E_SEEN = "seen";
    private static final String KEY_E_DIRECTOR = "director";
    private static final String KEY_E_RATING = "rating";
    private static final String KEY_E_GUESTSTARS = "guestStars";

    public DbShowHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_SERIES_TABLE =
                "CREATE TABLE " + TABLE_SERIES + "("
                        + KEY_S_SERIESID + " INTEGER PRIMARY KEY,"
                        + KEY_S_NAME + " TEXT,"
                        + KEY_S_OVERVIEW + " TEXT,"
                        + KEY_S_NETWORK + " TEXT,"
                        + KEY_S_THUMBNAIL + " BLOB,"
                        + KEY_S_LASTUPDATED + " INTEGER, "
                        + KEY_S_GENRES + " TEXT"
                        + ")";

        String CREATE_EPISODE_TABLE =
                "CREATE TABLE " + TABLE_EPISODES + "("
                        + KEY_E_EPISODEID + " INTEGER PRIMARY KEY,"
                        + KEY_E_SERIESID + " INTEGER,"
                        + KEY_E_SEASON + " INTEGER,"
                        + KEY_E_EPISODE + " INTEGER,"
                        + KEY_E_EPISODENAME  + " TEXT,"
                        + KEY_E_AIRED + " TEXT,"
                        + KEY_E_OVERVIEW + " TEXT,"
                        + KEY_E_SEEN + " INTEGER,"
                        + KEY_E_DIRECTOR + " TEXT,"
                        + KEY_E_RATING + " TEXT, "
                        + KEY_E_GUESTSTARS + " TEXT"
                        + ")";

        String indexEpisodeId = "CREATE UNIQUE INDEX episodeId ON episodes(episodeId ASC)";

        db.execSQL(CREATE_SERIES_TABLE);
        db.execSQL(CREATE_EPISODE_TABLE);
        db.execSQL(indexEpisodeId);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //Add functionality when database is updated
    }

    public void InsertFullSeriesInfo(List<EpisodeData> episodes, SeriesData series)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        if (db != null)
        {
            db.beginTransaction();

            try
            {
                AddSeries(series, db);

                for (EpisodeData e : episodes)
                    AddEpisode(db, e);

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
    }

    public void AddSeries(SeriesData series, SQLiteDatabase db)
    {
        try
        {
            ContentValues values = new ContentValues();
            values.put(KEY_S_SERIESID, series.getSeriesId());
            values.put(KEY_S_NAME, series.getTitle());
            values.put(KEY_S_NETWORK, series.getNetwork());
            values.put(KEY_S_OVERVIEW, series.getOverview());
            values.put(KEY_S_THUMBNAIL, series.getPosterStream());
            values.put(KEY_S_LASTUPDATED, series.getLastUpdated());
            values.put(KEY_S_GENRES, series.getGenres());

            db.insert(TABLE_SERIES, null, values);
        }
        catch (Exception ex)
        {
            Log.e(getClass().getName(), ex.getMessage());
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
            values.put(KEY_E_SEEN, 0);
            values.put(KEY_E_DIRECTOR, episode.getDirector());
            values.put(KEY_E_RATING, episode.getRating());
            values.put(KEY_E_GUESTSTARS, episode.getGuestStars());

            db.insert(TABLE_EPISODES, null, values);
        }
        catch (Exception ex)
        {
            Log.e(getClass().getName(), ex.getMessage());
        }
    }

    public boolean CheckIfSeriesExist(int seriesId)
    {
        String selectQuery  = String.format("select * from series where seriesId = %d", seriesId);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Returns true if series doesn't exist
        return cursor.getCount() != 0;
    }

    public void RemoveShow(int seriesId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SERIES, KEY_S_SERIESID + " = " + seriesId, null);
        db.delete(TABLE_EPISODES, KEY_E_SERIESID + " = " + Integer.toString(seriesId), null);
        db.close();
    }

    public Bitmap GetSeriesThumbnail(int seriesId)
    {
        String selectQuery = String.format("SELECT thumbnail from series where seriesId = %d", seriesId);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();//It should exist

        byte[] thumbnailByteStream = cursor.getBlob(0);
        Bitmap bmp = BitmapFactory.decodeByteArray(thumbnailByteStream, 0, thumbnailByteStream.length);
        db.close();
        return bmp;
    }

    public void UpdateSeasonSeenStatus(int seriesId, int seasonNumber, int seenStatus)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            if (db !=null)
            {
                db.beginTransaction();
                db.execSQL(String.format("UPDATE episodes SET seen = %d WHERE seriesId = %d AND season = %d", seenStatus , seriesId, seasonNumber));
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

    public void SetSeriesSeen(int seriesId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            if (db !=null)
            {
                db.beginTransaction();
                db.execSQL(String.format("UPDATE %s SET seen = 1 WHERE seriesId = %d", TABLE_EPISODES, seriesId));
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

    public void UpdateEpisodeSeen(int episodeId)
    {
        int seen = 1;

        if (GetShowSeen(episodeId))
            seen = 0;

        ContentValues values = new ContentValues();

        values.put(KEY_E_SEEN, seen);

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_EPISODES, values, KEY_E_EPISODEID + " = " + episodeId, null);
        db.close();
    }

    public boolean GetShowSeen(int episodeId)
    {
        String selectQuery = String.format("select * from episodes where episodeId = %d", episodeId);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        return cursor.getInt(7) == 1;
    }

    /*
    *
        Cursors
    *
     */

    public Cursor GetCursorSeasons(int seriesId)
    {
        String selectQuery = "SELECT distinct season as _id, seriesId FROM episodes " +
                             "WHERE seriesId = " + seriesId + " order by season desc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        return cursor;
    }

    public Cursor GetCursorEpisodes(int seriesId, int seasonNumber)
    {
        String selectQuery = String.format("select episodeId as _id, seriesId, season, episode, episodeName, aired, seen " +
                "from episodes where seriesId = %d and season = %d" +
                " order by 0+episode", seriesId, seasonNumber);

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
                "from series order by name");

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        return cursor;
    }

    public Cursor GetCursorEpisodesDetailed(int seriesId, int seasonNumber)
    {
        String selectQuery = String.format("select episodeId as _id, episode, episodeName, season, overview, aired, director, rating, seen, guestStars " +
                "from episodes where seriesId = %d and season = %d" +
                " order by 0+episode", seriesId, seasonNumber);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        return cursor;
    }

    public Cursor GetCursorOverview(int seriesId)
    {
        String selectQuery = String.format("select seriesId as _id, overview, name, network, genres " +
                                           "from series where seriesId = %d", seriesId);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        return cursor;
    }

    /*
        Database queries
     */

    public int GetTotalSeasonCount(int seriesId, int season)
    {
        String selectQuery = String.format("select count(*) from episodes " +
                "where seriesId = %d and season = %d", seriesId, season);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public int GetTotalSeasonSeen(int seriesId, int season)
    {
        String selectQuery = String.format("select count(*) from %s " +
                "where seriesId = %d and season = %d and seen = 1", TABLE_EPISODES, seriesId, season);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public String GetSeriesLastUpdate(int seriesId)
    {
        String selectQuery = String.format("select seriesId as _id, lastupdated from series where seriesId = %d", seriesId);

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery ,null);

        cursor.moveToFirst();
        return cursor.getString(1);
    }

    public boolean DoesEpisodeExist(SQLiteDatabase db, int episodeId)
    {
        String selectQuery = String.format("select episodeId as _id from episodes where episodeId = %d", episodeId);
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor.getCount() == 1;
    }

    /**
     * Update episodes section
     */

    public void UpdateSingleSeries(List<EpisodeData> episodes, int latestUpdate, int seriesId)
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
            values.put(KEY_E_GUESTSTARS, episode.getGuestStars());

            db.update(TABLE_EPISODES, values, KEY_E_EPISODEID + " = " + episode.getEpisodeId(), null);
        }
        catch (Exception ex)
        {
            Log.e(getClass().getName(), ex.getMessage());
        }
    }

    public List<Integer> GetAllSeriesIds()
    {
        List<Integer> seriesIds = new ArrayList<Integer>();

        String selectQuery = "select seriesId as _id from series";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            seriesIds.add(Integer.parseInt(cursor.getString(0)));
            cursor.moveToNext();
        }
        db.close();

        return seriesIds;
    }
}