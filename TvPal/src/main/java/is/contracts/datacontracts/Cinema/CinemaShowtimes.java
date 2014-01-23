package is.contracts.datacontracts.cinema;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Arnar on 21.11.2013.
 */
public class CinemaShowtimes implements Serializable
{
    @SerializedName("theater")
    public String theater;

    @SerializedName("schedule")
    public List<String> schedule;

    public String getTheater() { return this.theater; }
    public List<String> getSchedule () { return this.schedule; }
}
