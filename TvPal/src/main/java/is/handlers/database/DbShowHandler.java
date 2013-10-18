package is.handlers.database;

/**
 * Created by Arnar on 17.10.2013.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
    private static final String KEY_S_SERIESID = "seriesid";
    private static final String KEY_S_NAME = "name";
    private static final String KEY_S_OVERVIEW = "overview";
    private static final String KEY_S_NETWORK = "network";

    //Columns in episodes
    private static final String KEY_E_EPISODEID = "episodeId";
    private static final String KEY_E_SERIESID = "seriesId";
    private static final String KEY_E_SEASON = "season";
    private static final String KEY_E_EPISODE = "episode";
    private static final String KEY_E_EPISODENAME = "episodeName";
    private static final String KEY_E_OVERVIEW = "overview";
    private static final String KEY_E_AIRED = "aired";
    private static final String KEY_E_SEEN = "seen";

    public DbShowHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_SERIES_TABLE =
                "CREATE TABLE " + TABLE_SERIES + "("
                        + KEY_S_SERIESID + " varchar(20) PRIMARY KEY,"
                        + KEY_S_NAME + " varchar(100),"
                        + KEY_S_OVERVIEW + " text,"
                        + KEY_S_NETWORK + " varchar(100)"
                        + ")";

        String CREATE_EPISODE_TABLE =
                "CREATE TABLE " + TABLE_EPISODES + "("
                        + KEY_E_EPISODEID + " varchar(20) PRIMARY KEY,"
                        + KEY_E_SERIESID + " varchar(20),"
                        + KEY_E_SEASON + " varchar(5),"
                        + KEY_E_EPISODE + " varchar(5),"
                        + KEY_E_EPISODENAME  + " varchar(150),"
                        + KEY_E_AIRED + " varchar(15),"
                        + KEY_E_OVERVIEW + " text,"
                        + KEY_E_SEEN + " int"
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
        values.put(KEY_E_SEEN, 0);

        db.insert(TABLE_EPISODES, null, values);
        db.close();
    }

    public List<EpisodeData> GetAllSeasons(String seriesId)
    {
        List<EpisodeData> episodeList= new ArrayList<EpisodeData>();

        String selectQuery = "SELECT distinct season FROM " + TABLE_EPISODES + " WHERE seriesId = " + seriesId + " order by season+0 desc";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();//Loop through all entities
        while (!cursor.isAfterLast())
        {
            try
            {
                EpisodeData episode= new EpisodeData();

                episode.setSeasonNumber(cursor.getString(0));
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
                episode.setEpisodeNumber(cursor.getString(3));
                episode.setEpisodeName(cursor.getString(4));
                episode.setOverview(cursor.getString(6));
                episode.setSeen(Integer.parseInt(cursor.getString(7)));

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

    public void UpdateEpisodeSeen(String episodeId)
    {
        int seen = 1;

        if (GetShowSeen(episodeId))
            seen = 0;

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
}