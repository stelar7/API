package osu;

import java.util.List;

public class Bezier
{
    
    private List<Vector2> points;
    
    public Bezier(List<Vector2> points)
    {
        this.points = points;
    }
    
    public Vector2 valueAt(final float t)
    {
        final int n = points.size();
        if (n == 2)
        {
            return linear(t, points.get(0), points.get(1));
        } else if (n == 3)
        {
            return quadratic(t, points.get(0), points.get(1), points.get(2));
        } else if (n == 4)
        {
            return cubic(t, points.get(0), points.get(1), points.get(2), points.get(3));
        }
        return points.get(0);
    }
    
    private Vector2 cubic(float t, Vector2 p0, Vector2 p1, Vector2 p2, Vector2 p3)
    {
        final float dt  = 1f - t;
        final float dt2 = dt * dt;
        final float t2  = t * t;
        return p0.mul(dt2 * dt).add(p1).mul(3 * dt2 * t).add(p2.mul(3 * dt * t2).add(p3.mul(t2 * t)));
    }
    
    private Vector2 quadratic(float t, Vector2 p0, Vector2 p1, Vector2 p2)
    {
        final float dt = 1f - t;
        return p0.mul(dt * dt).add(p1.mul(2 * dt * t)).add(p2.mul(t * t));
    }
    
    private Vector2 linear(float t, Vector2 p0, Vector2 p1)
    {
        return p0.mul(1 - t).add(p1.mul(t));
        
    }
}