package math;

public class Fraction
{

    private final int numerator, denominator;

    public Fraction(final int numerator, final int denominator)
    {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public Fraction add(final Fraction f)
    {
        return new Fraction((numerator * f.denominator) + (f.numerator * denominator), (denominator * f.denominator));
    }

    public Fraction add(final int f)
    {
        return add(new Fraction(f, 1));
    }

    public float asDecimal()
    {
        return numerator / denominator;
    }

    public String asImproperFraction()
    {
        if ((denominator <= 0) || ((numerator / denominator) == 0) || ((numerator % denominator) == 0)) { return this.toString(); }
        return (numerator / denominator) + " " + (numerator % denominator) + "/" + denominator;
    }

    public Fraction divide(final Fraction f)
    {
        return new Fraction(numerator * f.denominator, denominator * f.numerator);
    }

    public Fraction divide(final int f)
    {
        return divide(new Fraction(f, 1));
    }

    public Fraction minimalize()
    {
        final int gcd = Stuff.greatestCommonDivisor(numerator, denominator);
        return new Fraction(numerator / gcd, denominator / gcd);
    }

    public Fraction multiply(final Fraction f)
    {
        return new Fraction(numerator * f.numerator, denominator * f.denominator);
    }

    public Fraction multiply(final int f)
    {
        return multiply(new Fraction(f, 1));
    }

    public Fraction subtract(final Fraction f)
    {
        return new Fraction((numerator * f.denominator) - (f.numerator * denominator), (denominator * f.denominator));
    }

    public Fraction subtract(final int f)
    {
        return subtract(new Fraction(f, 1));
    }

    @Override
    public String toString()
    {
        if ((denominator == 1) && (numerator >= 1)) { return String.valueOf(numerator); }
        return numerator + "/" + denominator;
    }
}