package is.contracts.datacontracts.trakt;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arnar on 23.11.2013.
 */
public class TraktMovieDetailedData
{
    @SerializedName("title")
    public String title;

    @SerializedName("trailer")
    public String trailer;

    @SerializedName("runtime")
    public int runtime;

    @SerializedName("overview")
    public String overview;

    @SerializedName("people")
    public TraktPeople people;

    @SerializedName("genres")
    public List<String> genres;

    @SerializedName("imdb_id")
    public String imdbId;

    @SerializedName("url")
    private String traktUrl;

    public String getTitle() { return this.title; }
    public String getTrailer() { return this.trailer; }
    public int getRuntime() { return this.runtime; }
    public String getOverview() { return this.overview; }
    public TraktPeople getPeople() { return this.people; }
    public List<String> getGenres() { return this.genres; }
    public String getImdbId() { return this.imdbId; }
    public String getTraktUrl() { return this.traktUrl;}
}