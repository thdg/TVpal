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

    @SerializedName("overview")
    public String overview;

    @SerializedName("images")
    public TraktImage image;

    public String getTitle() { return this.title; }
    public String getOverview() { return this.overview; }
    public TraktImage getImage() { return this.image; }
}