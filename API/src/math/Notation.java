package math;

import java.util.*;

public final class Notation
{
    private Notation()
    {
    }
    
    private static final List<Character> operators = Arrays.asList('+', '-', '*', '/');
    
    public static String fromPostfix(final String input)
    {
        final ArrayDeque<Float> stack = new ArrayDeque<>();
        final char[]            items = input.toCharArray();
        for (int i = 0; i < items.length; i++)
        {
            final char c = items[i];
            if (!Notation.isValidChar(c))
            {
                return "INVALID INPUT " + c;
            }
            if (Notation.operators.contains(c))
            {
                if (stack.size() < 2)
                {
                    return "Can't pop when less than 2 items!";
                }
                final float f1 = stack.pop();
                final float f2 = stack.pop();
                if (c == '+')
                {
                    stack.push(f2 + f1);
                    continue;
                }
                if (c == '-')
                {
                    stack.push(f2 - f1);
                    continue;
                }
                if (c == '*')
                {
                    stack.push(f2 * f1);
                    continue;
                }
                if (c == '/')
                {
                    stack.push(f2 / f1);
                    continue;
                }
            }
            stack.push(Float.parseFloat(Character.toString(c)));
        }
        if (stack.size() > 1)
        {
            return "ERROR!! More elements left on stack!";
        }
        return stack.pop().toString();
    }
    
    public static String fromPrefix(final String input)
    {
        final StringBuilder sb = new StringBuilder(input).reverse();
        return Notation.fromPostfix(sb.toString());
    }
    
    private static boolean isValidChar(final char c)
    {
        return Notation.operators.contains(c) || Character.toString(c).matches("[0-9]");
    }
    
}
