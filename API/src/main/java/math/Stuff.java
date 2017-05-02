package math;

public final class Stuff
{
    
    private Stuff()
    {
    }
    
    public static int binaryToDecimal(final String s)
    {
        int i = 0;
        for (final char c : s.toCharArray())
        {
            i = (i * 2) + (c == '1' ? 1 : 0);
        }
        return i;
    }
    
    public static double constrain(final double x, final double min, final double max)
    {
        if (x < min)
        {
            return min;
        }
        if (x > max)
        {
            return max;
        }
        return x;
    }
    
    public static String decimalToBinary(int i)
    {
        int                 local = i;
        final StringBuilder sb    = new StringBuilder();
        while (local > 0)
        {
            sb.append(local % 2 == 0 ? "0" : "1");
            local = local / 2;
        }
        return sb.reverse().toString();
    }
    
    public static double getLastXOfNumber(final int number, final int x)
    {
        int modulo = 1;
        int tempx  = x;
        while (tempx != 0)
        {
            modulo *= 10;
            tempx--;
        }
        return number % modulo;
    }
    
    public static int greatestCommonDivisor(final float numerator, final float denominator)
    {
        return (int) ((denominator == 0) ? numerator : Stuff.greatestCommonDivisor(denominator, numerator % denominator));
    }
    
    public static double mapToRange(final double x, final double inMin, final double inMax, final double outMin, final double outMax)
    {
        return (((x - inMin) * (outMax - outMin)) / (inMax - inMin)) + outMin;
    }
}
