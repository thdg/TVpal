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

    @SerializedName("runtime")
    public int runtime;

    @SerializedName("overview")
    public String overview;

    @SerializedName("genres")
    public List<String> genres;

    public String getTitle() { return this.title; }
    public int getRuntime() { return this.runtime; }
    public String getOverview() { return this.overview; }
    public List<String> getGenres() { return this.genres; }
}
