package is.contracts.datacontracts.trakt;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arnar on 5.12.2013.
 */
public class TraktUser
{
    @SerializedName("username")
    private String username;

    public String getUsername() { return this.username; }
}
