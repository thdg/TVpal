package is.utilities;

import java.util.List;

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

    public static String formatTrendingPosterUrl(String poster, String imgSize)
    {
        int posterLength = poster.length();
        int index = poster.lastIndexOf(".");

        String firstPart = poster.substring(0, index);
        String secondPart = poster.substring(index, posterLength);

        return String.format("%s%s%s", firstPart, imgSize, secondPart);
    }

    public static String JoinArrayToString(List<String> strings)
    {
        StringBuilder sb = new StringBuilder();

        for (String s : strings)
            sb.append(s).append(", ");

        return sb.toString();
    }
}
