package is.contracts.datacontracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arnar on 22.11.2013.
 */
public class TraktImage
{
    @SerializedName("poster")
    public String poster;

    public String getPoster() { return this.poster; }
}
