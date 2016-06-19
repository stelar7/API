package math;

public class Vector2f
{
    private float x;
    private float y;

    public Vector2f(final float x, final float y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2f abs()
    {
        return new Vector2f(Math.abs(this.x), Math.abs(this.y));
    }

    public Vector2f add(final float r)
    {
        return new Vector2f(this.x + r, this.y + r);
    }

    public Vector2f add(final Vector2f r)
    {
        return new Vector2f(this.x + r.getX(), this.y + r.getY());
    }

    public float cross(final Vector2f r)
    {
        return (this.x * r.getY()) - (this.y * r.getX());
    }

    public Vector2f div(final float r)
    {
        return new Vector2f(this.x / r, this.y / r);
    }

    public Vector2f div(final Vector2f r)
    {
        return new Vector2f(this.x / r.getX(), this.y / r.getY());
    }

    public float dot(final Vector2f r)
    {
        return (this.x * r.getX()) + (this.y * r.getY());
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (this.getClass() != obj.getClass())
        {
            return false;
        }
        final Vector2f other = (Vector2f) obj;
        if (Float.floatToIntBits(this.x) != Float.floatToIntBits(other.x))
        {
            return false;
        }
        if (Float.floatToIntBits(this.y) != Float.floatToIntBits(other.y))
        {
            return false;
        }
        return true;
    }

    public float getX()
    {
        return this.x;
    }

    public float getY()
    {
        return this.y;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + Float.floatToIntBits(this.x);
        result = (prime * result) + Float.floatToIntBits(this.y);
        return result;
    }

    public float length()
    {
        return (float) Math.sqrt((this.x * this.x) + (this.y * this.y));
    }

    public Vector2f lerp(final Vector2f dest, final float lerpFactor)
    {
        return dest.sub(this).mul(lerpFactor).add(this);
    }

    public float max()
    {
        return Math.max(this.x, this.y);
    }

    public Vector2f mul(final float r)
    {
        return new Vector2f(this.x * r, this.y * r);
    }

    public Vector2f mul(final Vector2f r)
    {
        return new Vector2f(this.x * r.getX(), this.y * r.getY());
    }

    public Vector2f normalized()
    {
        final float length = this.length();

        return new Vector2f(this.x / length, this.y / length);
    }

    public Vector2f reflect(final Vector2f normal)
    {
        return this.sub(normal.mul(this.dot(normal) * 2));
    }

    public Vector2f rotate(final float angle)
    {
        final double rad = Math.toRadians(angle);
        final double cos = Math.cos(rad);
        final double sin = Math.sin(rad);

        return new Vector2f((float) ((this.x * cos) - (this.y * sin)), (float) ((this.x * sin) + (this.y * cos)));
    }

    public void set(final float x, final float y)
    {
        this.x = x;
        this.y = y;
    }

    public void set(final Vector2f r)
    {
        this.set(r.getX(), r.getY());
    }

    public void setX(final float x)
    {
        this.x = x;
    }

    public void setY(final float y)
    {
        this.y = y;
    }

    public Vector2f sub(final float r)
    {
        return new Vector2f(this.x - r, this.y - r);
    }

    public Vector2f sub(final Vector2f r)
    {
        return new Vector2f(this.x - r.getX(), this.y - r.getY());
    }

    @Override
    public String toString()
    {
        return "(" + this.x + " " + this.y + ")";
    }
}