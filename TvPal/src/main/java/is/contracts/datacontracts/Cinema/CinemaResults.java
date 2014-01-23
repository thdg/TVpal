package is.contracts.datacontracts.cinema;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Datacontract to store information about movies
 * @author
 */
public class CinemaResults
{
    @SerializedName("results")
    public List<CinemaMovie> movies;

    public List<CinemaMovie> getMovies() { return this.movies; }
}
