package math;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Notation
{

    private static final List<Character> operators = Arrays.asList('+', '-', '*', '/');

    public static String fromPostfix(String input, boolean verbose)
    {
        Stack<Float> stack = new Stack<Float>();
        char[] items = input.toCharArray();
        for (int i = 0; i < items.length; i++)
        {
            char c = items[i];
            if (!isValidChar(c)) return "INVALID INPUT " + c;
            if (verbose) System.out.println("REMAINING INPUT:" + input.substring(i) + "\tSTACK: " + stack + "\tNEXT ACTION: " + (operators.contains(c) ? stack.get(0) + Character.toString(c) + stack.get(1) : "PUSH " + (char) c));
            if (operators.contains(c))
            {
                if (stack.size() < 2) return "Can't pop when less than 2 items!";
                float f1 = stack.pop();
                float f2 = stack.pop();
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
        if (stack.size() > 1) return "More elements left on stack!";
        return stack.pop().toString();
    }

    private static boolean isValidChar(char c)
    {
        return operators.contains(c) || Character.toString(c).matches("[0-9]");
    }

    public static String fromPrefix(String input, boolean verbose)
    {
        StringBuilder sb = new StringBuilder(input).reverse();
        return fromPostfix(sb.toString(), verbose);
    }

}
