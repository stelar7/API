package div;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public final class Miscellaneous
{
    private Miscellaneous()
    {
    }
    
    /**
     * Changes an String[] into a String from start to stop
     *
     * @param array the array to transform
     * @param start the starting point in the array (Arrays start at 0)
     * @param stop  the place to stop in the array
     * @return the array as a string
     **/
    public static String arrayToString(final String[] array, final int start, final int stop)
    {
        
        final StringBuilder sb = new StringBuilder();
        for (int i = start; i < stop; i++)
        {
            sb.append(array[i] + " ");
        }
        return sb.toString().trim();
    }
    
    /**
     * Capitalizes the first letter in a string
     *
     * @param string the string to capitalize
     * @return the string with the first letter capitalized
     **/
    public static String capitalize(final String string)
    {
        return string.substring(0, 1).toUpperCase(Locale.ENGLISH) + string.substring(1, string.length()).toLowerCase(Locale.ENGLISH);
    }
    
    /**
     * Gets the current date in the format Jan 21, 1990 22:48
     *
     * @return Date the current date
     **/
    public static String getDate()
    {
        final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        final Date             d   = new Date();
        return sdf.format(d);
    }
    
    /**
     * Gets the current date in the given format
     *
     * @param sdf the format to get the date in
     * @return Date the current date
     **/
    
    public static String getDate(final SimpleDateFormat sdf)
    {
        final Date d = new Date();
        return sdf.format(d);
    }
    
    /**
     * Checks if a string can be converted to a int
     *
     * @param string the string to check
     * @return if the string can be an int
     **/
    public static boolean isInt(final String string)
    {
        return string.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+");
    }
    
    /**
     * Finds the longest common subsequence of the arrays
     *
     * @param stringA first string
     * @param stringB second string
     * @return the longest common subsequence of the two strings
     */
    public static List<Character> longestCommonSubsequence(final char[] sequenceOne, final char[] sequenceTwo)
    {
        final int[][] num = new int[sequenceOne.length + 1][sequenceTwo.length + 1];
        for (int i = 1; i <= sequenceOne.length; i++)
        {
            for (int j = 1; j <= sequenceTwo.length; j++)
            {
                if (sequenceOne[i - 1] == sequenceTwo[j - 1])
                {
                    num[i][j] = 1 + num[i - 1][j - 1];
                } else
                {
                    num[i][j] = Math.max(num[i - 1][j], num[i][j - 1]);
                }
            }
        }
        int                   s1position = sequenceOne.length;
        int                   s2position = sequenceTwo.length;
        final List<Character> result     = new LinkedList<>();
        while ((s1position != 0) && (s2position != 0))
        {
            if (sequenceOne[s1position - 1] == sequenceTwo[s2position - 1])
            {
                result.add(sequenceOne[s1position - 1]);
                s1position--;
                s2position--;
            } else if (num[s1position][s2position - 1] >= num[s1position][s2position])
            {
                s2position--;
            } else
            {
                s1position--;
            }
        }
        Collections.reverse(result);
        return result;
    }
    
    /**
     * Takes a array, returns the array in reversed order
     *
     * @param input the array to reverse
     * @return the input array in reverse order
     */
    public static int[] reverseArray(final int[] input)
    {
        final int[] array = input.clone();
        for (int i = 0; i < (array.length / 2); i++)
        {
            final int temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
        return array;
    }
}