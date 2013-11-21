package is.contracts.datacontracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arnar on 21.11.2013.
 */
public class TraktMovieData
{
    @SerializedName("title")
    public String title;

    @SerializedName("runtime")
    public int runtime;

    @SerializedName("overview")
    public String overview;

    @SerializedName("poster")
    public String poster;

    public String getTitle() { return this.title; }
    public int getRuntime() { return this.runtime; }
    public String getOverview() { return this.overview; }
    public String getPoster() { return this.poster; }
}
