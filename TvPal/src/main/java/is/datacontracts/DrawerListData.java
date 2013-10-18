package is.datacontracts;

/**
 * Created by thdg9_000 on 14.10.2013.
 */
public class DrawerListData
{
    private String name;
    private int iconId;

    public DrawerListData(String name, int iconId)
    {
        this.name = name;
        this.iconId = iconId;
    }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public int getIcon() { return this.iconId; }
    public void setIcon(int iconId) { this.iconId = iconId; }

    @Override
    public String toString()
    {
        return String.format("%s", this.name);
    }
}
