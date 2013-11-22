package is.contracts.datacontracts;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by Arnar on 21.11.2013.
 */
public class TraktMovieData
{
    @SerializedName("title")
    public String title;

    @SerializedName("poster")
    public String poster;

    @SerializedName("overview")
    public String overview;

    public String getTitle() { return this.title; }
    public String getPoster() { return this.poster; }
    public String getOverview() { return this.overview; }
}
