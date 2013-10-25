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
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import is.datacontracts.EpisodeData;
import is.datacontracts.ShowData;

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
                        + KEY_S_THUMBNAIL + " BLOB"
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

        ContentValues values = new ContentValues();
        values.put(KEY_S_SERIESID, series.getSeriesId());
        values.put(KEY_S_NAME, series.getTitle());
        values.put(KEY_S_OVERVIEW, series.getOverview());
        values.put(KEY_S_NETWORK, series.getNetwork());

        db.insert(TABLE_SERIES, null, values);
        db.close();
    }

    public List<ShowData> GetAllSeries()
    {
        List<ShowData> seriesList = new ArrayList<ShowData>();

        String selectQuery = "SELECT * FROM " + TABLE_SERIES + " ORDER BY " + KEY_S_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();//Loop through all entities
        while (!cursor.isAfterLast())
        {
            try
            {
                ShowData series = new ShowData();

                series.setSeriesId(cursor.getString(0));
                series.setTitle(cursor.getString(1));
                series.setOverview(cursor.getString(2));
                series.setNetwork(cursor.getString(3));

                //TODO: Refactor to hashmap in the adapter, this takes an awful lot of memory
                byte[] thumbnailByteStream = cursor.getBlob(4);
                Bitmap bmp = BitmapFactory.decodeByteArray(thumbnailByteStream, 0, thumbnailByteStream.length);
                series.setThumbNail(bmp);

                seriesList.add(series);
                cursor.moveToNext();
            }
            catch (Exception ex)
            {
                ex.getMessage();
            }
        }

        return seriesList;
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

    public Bitmap GetSeriesThumbnail(String seriesId)
    {
        List<EpisodeData> episodeList= new ArrayList<EpisodeData>();

        String selectQuery = String.format("SELECT thumbnail from %s where seriesId = %s", TABLE_SERIES, seriesId);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();//It should exist

        byte[] thumbnailByteStream = cursor.getBlob(0);
        Bitmap bmp = BitmapFactory.decodeByteArray(thumbnailByteStream, 0, thumbnailByteStream.length);
        return bmp;
    }

    public void AddEpisode(EpisodeData episode)
    {
        SQLiteDatabase db = this.getWritableDatabase();

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

    public List<EpisodeData> GetEpisodesBySeason(String seriesId, String seasonNumber)
    {
        List<EpisodeData> episodeList = new ArrayList<EpisodeData>();

        String selectQuery = String.format("select * from episodes where seriesId = %s and season = %s", seriesId, seasonNumber);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            try
            {
                EpisodeData episode = new EpisodeData();

                episode.setEpisodeId(cursor.getString(0));
                episode.setSeriesId(cursor.getString(1));
                episode.setSeasonNumber(cursor.getString(2));
                episode.setEpisodeNumber(cursor.getString(3));
                episode.setEpisodeName(cursor.getString(4));
                episode.setAired(cursor.getString(5));
                episode.setOverview(cursor.getString(6));
                episode.setSeen(cursor.getString(7));
                episode.setDirector(cursor.getString(8));
                episode.setRating(cursor.getString(9));

                episodeList.add(episode);
                cursor.moveToNext();
            }
            catch (Exception ex)
            {
                ex.getMessage();
            }
        }

        return episodeList;
    }

    public List<EpisodeData> GetUpcomingShows(String date)
    {
        List<EpisodeData> episodeList = new ArrayList<EpisodeData>();

        String selectQuery = String.format("select episodeName, aired, seriesId, season, episode " +
                "from episodes " +
                "where aired >= '%s' " +
                "order by aired limit 15", date);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            try
            {
                EpisodeData episode = new EpisodeData();

                episode.setEpisodeName(cursor.getString(0));
                episode.setAired(cursor.getString(1));
                episode.setSeriesId(cursor.getString(2));
                episode.setSeasonNumber(cursor.getString(3));
                episode.setEpisodeNumber(cursor.getString(4));

                episodeList.add(episode);
                cursor.moveToNext();
            }
            catch (Exception ex)
            {
                ex.getMessage();
            }
        }

        return episodeList;
    }

    public List<EpisodeData> GetRecentShows(String date)
    {
        List<EpisodeData> episodeList = new ArrayList<EpisodeData>();

        String selectQuery = String.format("select episodeName, aired, seriesId, season, episode " +
                "from episodes " +
                "where aired < '%s' " +
                "order by aired desc limit 15", date);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            try
            {
                EpisodeData episode = new EpisodeData();

                episode.setEpisodeName(cursor.getString(0));
                episode.setAired(cursor.getString(1));
                episode.setSeriesId(cursor.getString(2));
                episode.setSeasonNumber(cursor.getString(3));
                episode.setEpisodeNumber(cursor.getString(4));

                episodeList.add(episode);
                cursor.moveToNext();
            }
            catch (Exception ex)
            {
                ex.getMessage();
            }
        }

        return episodeList;
    }

    public Cursor GetCursorSeasons(String seriesId)
    {
        String selectQuery = "SELECT distinct season as _id, season FROM " + TABLE_EPISODES + " WHERE seriesId = " + seriesId + " order by season+0 desc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        return cursor;
    }

    public Cursor GetCursorEpisodes(String seriesId, String seasonNumber)
    {
        String selectQuery = String.format("select episodeId as _id, seriesId, season, episode, episodeName, aired " +
                "from episodes where seriesId = %s and season = %s", seriesId, seasonNumber);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        return cursor;
    }
}