package math;

public class Fraction
{
    
    private final int numerator;
    private final int denominator;
    
    public Fraction(final int f, final int g)
    {
        this.numerator = f;
        this.denominator = g;
    }
    
    public Fraction add(final Fraction f)
    {
        return new Fraction((this.numerator * f.denominator) + (f.numerator * this.denominator), this.denominator * f.denominator);
    }
    
    public Fraction add(final int f)
    {
        return this.add(new Fraction(f, 1));
    }
    
    public float asDecimal()
    {
        return this.numerator / this.denominator;
    }
    
    public String asImproperFraction()
    {
        if ((this.denominator <= 0) || ((this.numerator / this.denominator) == 0) || ((this.numerator % this.denominator) == 0))
        {
            return this.toString();
        }
        return (this.numerator / this.denominator) + " " + (this.numerator % this.denominator) + "/" + this.denominator;
    }
    
    public Fraction divide(final Fraction f)
    {
        return new Fraction(this.numerator * f.denominator, this.denominator * f.numerator);
    }
    
    public Fraction divide(final int f)
    {
        return this.divide(new Fraction(f, 1));
    }
    
    public Fraction minimalize()
    {
        final int gcd = Stuff.greatestCommonDivisor(this.numerator, this.denominator);
        return new Fraction(this.numerator / gcd, this.denominator / gcd);
    }
    
    public Fraction multiply(final Fraction f)
    {
        return new Fraction(this.numerator * f.numerator, this.denominator * f.denominator);
    }
    
    public Fraction multiply(final int f)
    {
        return this.multiply(new Fraction(f, 1));
    }
    
    public Fraction subtract(final Fraction f)
    {
        return new Fraction((this.numerator * f.denominator) - (f.numerator * this.denominator), this.denominator * f.denominator);
    }
    
    public Fraction subtract(final int f)
    {
        return this.subtract(new Fraction(f, 1));
    }
    
    @Override
    public String toString()
    {
        if ((this.denominator == 1) && (this.numerator >= 1))
        {
            return String.valueOf(this.numerator);
        }
        return this.numerator + "/" + this.denominator;
    }
}