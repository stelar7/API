package osu;

public class Vector2
{
    public float x;
    public float y;
    
    public Vector2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public String toString()
    {
        return "Vector2{" +
               "x=" + x +
               ", y=" + y +
               '}';
    }
    
    public Vector2 mul(float v)
    {
        return new Vector2(x * v, y * v);
    }
    
    public Vector2 add(Vector2 mul)
    {
        return new Vector2(x + mul.x, y + mul.y);
    }
}
