package is.contracts.datacontracts.trakt;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arnar on 23.11.2013.
 */
public class TraktPerson
{
    @SerializedName("name")
    public String name;

    @SerializedName("character")
    public String character;

    public String getName() { return this.name; }
    public String getCharacter() { return this.character; }
}
