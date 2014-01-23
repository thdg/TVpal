package is.contracts.datacontracts.cinema;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 *  Datacontract to store information about movies
 *  @author Arnar
 */
public class CinemaMovie implements Serializable
{
    @SerializedName("title")
    public String title;

    @SerializedName("restricted")
    public String restricted;

    @SerializedName("imdb")
    public String imdb;

    @SerializedName("image")
    public String imageUrl;

    @SerializedName("showtimes")
    public List<CinemaShowtimes> showtimes;

    @SerializedName("imdbLink")
    public String imdbLink;

    public String getTitle() { return this.title; }
    public String getRestricted() { return this.restricted; }
    public String getImdb() { return this.imdb; }
    public String getImageUrl() { return this.imageUrl; }
    public List<CinemaShowtimes> getShowtimes() { return this.showtimes; }
    public String getImdbLink() { return this.imdbLink; }
}
