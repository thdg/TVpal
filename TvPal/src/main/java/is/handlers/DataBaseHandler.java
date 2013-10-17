package is.handlers;

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

import is.datacontracts.ShowDataContract;

public class DataBaseHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "shows";
    private static final String TABLE_SERIES = "series";

    // Series Table Columns names
    private static final String KEY_SERIESID = "seriesid";
    private static final String KEY_NAME = "name";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_NETWORK = "network";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_SERIES_TABLE =
                "CREATE TABLE " + TABLE_SERIES + "("
                        + KEY_SERIESID + " varchar(20) PRIMARY KEY,"
                        + KEY_NAME + " varchar(100),"
                        + KEY_OVERVIEW + " text,"
                        + KEY_NETWORK + " varchar(100)"
                        + ")";

        db.execSQL(CREATE_SERIES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //Add functionality when database is updated
    }

    public void AddSeries(ShowDataContract series)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SERIESID, series.getSeriesId());
        values.put(KEY_NAME, series.getTitle());
        values.put(KEY_OVERVIEW, series.getOverview());
        values.put(KEY_NETWORK, series.getNetwork());

        db.insert(TABLE_SERIES, null, values);
        db.close();
    }

    public List<ShowDataContract> GetAllSeries()
    {
        List<ShowDataContract> seriesList = new ArrayList<ShowDataContract>();

        String selectQuery = "SELECT * FROM " + TABLE_SERIES + " ORDER BY " + KEY_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();//Loop through all entities
        while (!cursor.isAfterLast())
        {
            try {
                ShowDataContract series = new ShowDataContract();

                series.setSeriesId(cursor.getString(0));
                series.setTitle(cursor.getString(1));
                series.setOverview(cursor.getString(2));
                series.setNetwork(cursor.getString(3));

                seriesList.add(series);
                cursor.moveToNext();
            }
            catch (Exception ex) {
                ex.getMessage();
            }
        }

        return seriesList;
    }

    public boolean CheckIfSeriesExist(String seriesId)
    {
        String selectClause = "SELECT * FROM " + TABLE_SERIES;
        String whereClause  = "WHERE " + KEY_SERIESID + " = " + seriesId;
        String selectQuery  = String.format("%s %s", selectClause, whereClause);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //Returns true if series doesn't exist
        return cursor.getCount() != 0;
    }

    public void DeleteComment(ShowDataContract contract)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        String seriesId = contract.getSeriesId();
        database.delete(TABLE_SERIES, KEY_SERIESID + " = " + seriesId , null);
    }
}