package is.contracts.datacontracts.Cinema;

import com.google.gson.annotations.SerializedName;

/**
 *  Datacontract to store information about movies
 *  @author Arnar
 */
public class CinemaMovies
{
    @SerializedName("title")
    public String title;

    public String getTitle() { return this.title; }
}
