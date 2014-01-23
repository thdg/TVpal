package is.contracts.datacontracts.trakt;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arnar on 22.11.2013.
 */
public class TraktImage
{
    @SerializedName("poster")
    private String poster;

    public String getPoster() { return this.poster; }
}
