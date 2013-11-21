package is.contracts.datacontracts.Cinema;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arnar on 21.11.2013.
 */
public class CinemaShowtimes
{
    @SerializedName("theater")
    public String theater;

    @SerializedName("schedule")
    public List<String> schedule;

    public String getTheater() { return this.theater; }
    public List<String> getSchedule () { return this.schedule; }
}
