package is.handlers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import is.contracts.datacontracts.trakt.TraktMovieData;

/**
 * Created by Arnar on 5.12.2013.
 */
public class DbMovies extends DatabaseHandler
{
    public DbMovies(Context context)
    {
        super(context);
    }

    public Cursor GetWatchlistCursor()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "select imdb_id as _id, title, overview, image_url " +
                             "from movies " +
                             "order by timestamp desc";

        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        return cursor;
    }

    public void AddMovieToWatchList(TraktMovieData movie)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        if (db != null)
        {
            try
            {
                db.beginTransaction();

                ContentValues values = new ContentValues();
                values.put(KEY_M_IMDBID, movie.getImdbId());
                values.put(KEY_M_TITLE, movie.getTitle());
                values.put(KEY_M_IMAGEURL, movie.getImage().getPoster());
                values.put(KEY_M_OVERVIEW, movie.getOverview());
                values.put(KEY_M_TIMESTAMP, new SimpleDateFormat("yyyy-MM-dd kk:mm").format(new Date()));

                db.insert(TABLE_MOVIES, null, values);

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
        }
    }

    public void RemoveMovieFromWatchList(String imdbId)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        try
        {
            db.beginTransaction();

            db.delete(TABLE_MOVIES, String.format("imdb_id = '%s'", imdbId), null);

            db.setTransactionSuccessful();
        }
        catch (Exception ex)
        {
            Log.e(getClass().getName(), ex.getMessage());
            ex.printStackTrace();
        }
        finally
        {
            db.endTransaction();
        }
    }
}
