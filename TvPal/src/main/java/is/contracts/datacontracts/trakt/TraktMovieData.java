package is.contracts.datacontracts.trakt;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arnar on 21.11.2013.
 */
public class TraktMovieData
{
    @SerializedName("title")
    private String title;

    @SerializedName("overview")
    private String overview;

    @SerializedName("images")
    private TraktImage image;

    @SerializedName("imdb_id")
    private String imdbId;

    public String getTitle() { return this.title; }
    public String getOverview() { return this.overview; }
    public TraktImage getImage() { return this.image; }
    public String getImdbId() { return this.imdbId; }

    public void setTitle(String title) { this.title = title;}
    public void setOverview(String overview) { this.overview = overview;}
    public void setImage(TraktImage image) { this.image = image; }
    public void setImdbId(String imdbId) { this.imdbId = imdbId;};
}