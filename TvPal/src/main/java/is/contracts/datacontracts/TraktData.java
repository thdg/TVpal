package is.contracts.datacontracts;

import com.google.gson.annotations.SerializedName;

/**
 * DataContract to hold information about Trakt shows
 * @author Arnar
 */
public class TraktData
{
    @SerializedName("title")
    public String title;

    @SerializedName("overview")
    public String overview;

    @SerializedName("tvdb_id")
    public String tvdbId;

    @SerializedName("poster")
    public String poster;

    public String getTitle() { return this.title; }
    public void setTitle(String title) { this.title = title; }

    public String getOverview() { return this.overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public String getTvdbId() { return this.tvdbId; }
    public void setTvdbId(String tvdbid) { this.tvdbId = tvdbid; }

    public String getPoster() { return this.poster; }
    public void setPoster(String poster) { this.poster = poster; }

}
