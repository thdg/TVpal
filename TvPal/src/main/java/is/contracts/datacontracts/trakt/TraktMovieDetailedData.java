package is.contracts.datacontracts.trakt;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arnar on 23.11.2013.
 */
public class TraktMovieDetailedData
{
    @SerializedName("title")
    private String title;

    @SerializedName("trailer")
    private String trailer;

    @SerializedName("runtime")
    private int runtime;

    @SerializedName("overview")
    private String overview;

    @SerializedName("people")
    private TraktPeople people;

    @SerializedName("genres")
    private List<String> genres;

    @SerializedName("imdb_id")
    private String imdbId;

    @SerializedName("url")
    private String traktUrl;

    @SerializedName("ratings")
    private TraktRating rating;

    @SerializedName("year")
    private int releaseYear;

    @SerializedName("images")
    private TraktImage image;

    public String getTitle() { return this.title; }
    public String getTrailer() { return this.trailer; }
    public int getRuntime() { return this.runtime; }
    public String getOverview() { return this.overview; }
    public TraktPeople getPeople() { return this.people; }
    public List<String> getGenres() { return this.genres; }
    public String getImdbId() { return this.imdbId; }
    public String getTraktUrl() { return this.traktUrl;}
    public TraktRating getRating() { return this.rating; }
    public int getReleaseYear() { return this.releaseYear; }
    public TraktImage getImage() { return this.image;}
}