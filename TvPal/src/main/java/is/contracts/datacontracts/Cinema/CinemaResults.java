package is.contracts.datacontracts.Cinema;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Datacontract to store information about movies
 * @author
 */
public class CinemaResults
{
    @SerializedName("results")
    public List<CinemaMovies> movies;

    public List<CinemaMovies> getMovies() { return this.movies; }
}
