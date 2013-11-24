package is.contracts.datacontracts.trakt;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arnar on 24.11.2013.
 */
public class TraktRating
{
    @SerializedName("percentage")
    private int percentage;

    public int getPercentage() { return this.percentage; }
}
