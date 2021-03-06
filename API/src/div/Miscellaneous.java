package div;

import java.text.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.*;


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
            sb.append(array[i]).append(" ");
        }
        return sb.toString().trim();
    }
    
    /**
     * Transforms a byte[] into a String of "bytes"
     *
     * @param bytes the bytes to transform
     * @return the string with the "bytes"
     */
    public static String bytesToHexString(final byte[] bytes)
    {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        final char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++)
        {
            final int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[(j * 2) + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
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
     * Transforms a string of "bytes" into an array of bytes
     *
     * @param s the string
     * @return a byte[] of the "bytes" in s
     */
    public static byte[] hexStringToByteArray(final String s)
    {
        final int    len  = s.length();
        final byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2)
        {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
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
     * Computes the Levenshtein distance between two strings
     *
     * @param s String A
     * @param t String b
     * @return the amount of operations needed to turn A into B
     */
    public static int itterativeLevenshtein(final String s, final String t)
    {
        if (s.equals(t))
        {
            return 0;
        }
        
        if (s.length() == 0)
        {
            return t.length();
        }
        
        if (t.length() == 0)
        {
            return s.length();
        }
        
        final int[] v0 = new int[t.length() + 1];
        final int[] v1 = new int[t.length() + 1];
        
        for (int i = 0; i < v0.length; i++)
        {
            v0[i] = i;
        }
        
        for (int i = 0; i < s.length(); i++)
        {
            v1[0] = i + 1;
            
            for (int j = 0; j < t.length(); j++)
            {
                final int cost = s.charAt(i) == t.charAt(j) ? 0 : 1;
                
                v1[j + 1] = Math.min(Math.min(v1[j] + 1, v0[j + 1] + 1), v0[j] + cost);
            }
            
            System.arraycopy(v1, 0, v0, 0, v0.length);
        }
        
        return v1[t.length()];
        
    }
    
    /**
     * Finds the longest common subsequence of the arrays
     *
     * @param sequenceOne first string
     * @param sequenceTwo second string
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
        for (int i = 0; i < (input.length / 2); i++)
        {
            final int temp = input[i];
            input[i] = input[input.length - 1 - i];
            input[input.length - 1 - i] = temp;
        }
        return input;
    }
    
    public static int gcd(int a, int b)
    {
        if (a == b && b == 0)
        {
            return 0;
        }
        
        if (a == 0)
        {
            return b;
        }
        
        if (b == 0)
        {
            return a;
        }
        
        return 1;
        
        
        /*
        int shift = (a | b).trailingZeros();
        u >>= a.trailingZeros();
        do {
            a >>= a.trailingZeros();
            if(a>b) {
                int t = b;
                b = a;
                a = t;
            }
            b = b - a;
        } while(b != 0);
        
        return u << shift;
        */
    }
    
    public static int lcm(int a, int b)
    {
        return Math.abs(a * b) / gcd(a, b);
    }
    
    static class VoseSampler<T>
    {
        Map<T, Float> distribution = new HashMap<>();
        
        Map<T, Float> probabilityTable    = new HashMap<>();
        Map<T, T>     aliasTable          = new HashMap<>();
        Map<T, Float> scaledProbabilities = new HashMap<>();
        
        Deque<T> smallProbabilities = new ArrayDeque<>();
        Deque<T> largeProbabilities = new ArrayDeque<>();
        
        List<T> distributionList = new ArrayList<>();
        
        public VoseSampler(Map<T, Float> distribution)
        {
            this.distribution = distribution;
            distributionList = new ArrayList<>(this.distribution.keySet());
            
            int n = distribution.size();
            this.distribution.forEach((o, p) -> {
                float scaled = scaledProbabilities.compute(o, (k, v) -> p * n);
                
                if (scaled < 1)
                {
                    smallProbabilities.add(o);
                } else
                {
                    largeProbabilities.add(o);
                }
            });
            
            while (!smallProbabilities.isEmpty() && !largeProbabilities.isEmpty())
            {
                T small = smallProbabilities.poll();
                T large = largeProbabilities.poll();
                
                probabilityTable.put(small, scaledProbabilities.get(small));
                aliasTable.put(small, large);
                
                float scaled = scaledProbabilities.compute(large, (k, v) -> (scaledProbabilities.get(large) + scaledProbabilities.get(small)) - 1);
                if (scaled < 1)
                {
                    smallProbabilities.add(large);
                } else
                {
                    largeProbabilities.add(large);
                }
            }
            
            while (!largeProbabilities.isEmpty())
            {
                probabilityTable.compute(largeProbabilities.poll(), (k, v) -> 1f);
            }
            
            while (!smallProbabilities.isEmpty())
            {
                probabilityTable.compute(smallProbabilities.poll(), (k, v) -> 1f);
            }
        }
        
        public T generateAlias()
        {
            int   pos  = ThreadLocalRandom.current().nextInt(distributionList.size());
            float bias = ThreadLocalRandom.current().nextFloat();
            
            T     col   = distributionList.get(pos);
            float prob  = probabilityTable.get(col);
            T     alias = aliasTable.get(col);
            
            boolean toss = bias < prob;
            return toss ? col : alias;
        }
        
        public List<T> sample(int count)
        {
            return IntStream.range(0, count).mapToObj(i -> generateAlias()).collect(Collectors.toList());
        }
        
        public static void main(String[] args)
        {
            VoseSampler<String> sampler = new VoseSampler<>(new HashMap<>()
            {{
                // should ideally sum to 1f
                put("a", 0.10f);
                put("b", 0.80f);
                put("c", 0.05f);
                put("d", 0.03f);
                put("e", 0.02f);
                put("x", 0.001f);
            }});
            
            System.out.println(sampler.sample(1_000).stream().collect(Collectors.groupingBy(a -> a, Collectors.counting())));
        }
    }
}
