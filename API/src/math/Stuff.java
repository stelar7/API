package math;

public class Stuff
{

    public static double mapToRange(double x, double in_min, double in_max, double out_min, double out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public static double constrain(double x, double min, double max)
    {
        if (x < min) return min;
        if (x > max) return max;
        return x;
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

    public static String decimalToBinary(int i)
    {
        final StringBuilder sb = new StringBuilder();
        while (i > 0)
        {
            sb.append((((i % 2) == 0) ? "0" : "1"));
            i = i / 2;
        }
        return sb.reverse().toString();
    }

    public static double getLastXOfNumber(final int number, final int x)
    {
        int modulo = 1;
        int tempx = x;
        while (tempx != 0)
        {
            modulo *= 10;
            tempx--;
        }
        return number % modulo;
    }

    public static int greatestCommonDivisor(final int a, final int b)
    {
        return (b == 0) ? a : greatestCommonDivisor(b, a % b);
    }
}
