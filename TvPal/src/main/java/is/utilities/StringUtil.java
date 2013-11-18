package is.utilities;

/**
 * Created by Arnar on 18.11.2013.
 */
public class StringUtil
{
    public static String ArrayToString(String guestStars)
    {
        String[] temp = guestStars.split("(?!^)\\|");

        StringBuilder sb = new StringBuilder();

        for (String s : temp)
        {
            sb.append(s.replaceFirst("\\|", "") + ", ");
        }

        String actors = sb.toString();
        int length = actors.lastIndexOf(",");
        return actors.substring(0, length);//Remove last comma
    }
}
