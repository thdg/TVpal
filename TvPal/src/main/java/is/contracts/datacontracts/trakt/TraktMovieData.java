package is.contracts.datacontracts.trakt;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arnar on 21.11.2013.
 */
public class TraktMovieData
{
    @SerializedName("title")
    public String title;

    @SerializedName("overview")
    public String overview;

    @SerializedName("images")
    public TraktImage image;

    @SerializedName("imdb_id")
    public String imdbId;

    public String getTitle() { return this.title; }
    public String getOverview() { return this.overview; }
    public TraktImage getImage() { return this.image; }
    public String getImdbId() { return this.imdbId; }
}