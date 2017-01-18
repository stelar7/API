package math;

public class Vector3f
{
    public static final Vector3f ONE  = new Vector3f(1, 1, 1);
    public static final Vector3f ZERO = new Vector3f(0, 0, 0);
    
    private float x;
    private float y;
    private float z;
    
    public Vector3f(final float v)
    {
        this.x = v;
        this.y = v;
        this.z = v;
    }
    
    public Vector3f(final float x, final float y, final float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3f(final Vector3f position)
    {
        this.x = position.x;
        this.y = position.y;
        this.z = position.z;
    }
    
    public Vector3f abs()
    {
        return new Vector3f(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
    }
    
    public Vector3f add(final float r)
    {
        return new Vector3f(this.x + r, this.y + r, this.z + r);
    }
    
    public Vector3f add(final float x, final float y, final float z)
    {
        return new Vector3f(x + x, y + y, z + z);
    }
    
    public Vector3f add(final Vector3f r)
    {
        return new Vector3f(this.x + r.getX(), this.y + r.getY(), this.z + r.getZ());
    }
    
    public Vector3f cross(final Vector3f r)
    {
        final float x_ = (this.y * r.getZ()) - (this.z * r.getY());
        final float y_ = (this.z * r.getX()) - (this.x * r.getZ());
        final float z_ = (this.x * r.getY()) - (this.y * r.getX());
        
        return new Vector3f(x_, y_, z_);
    }
    
    public Vector3f div(final float r)
    {
        return new Vector3f(this.x / r, this.y / r, this.z / r);
    }
    
    public Vector3f div(final Vector3f r)
    {
        return new Vector3f(this.x / r.getX(), this.y / r.getY(), this.z / r.getZ());
    }
    
    public float dot(final Vector3f r)
    {
        return (this.x * r.getX()) + (this.y * r.getY()) + (this.z * r.getZ());
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
        final Vector3f other = (Vector3f) obj;
        return Float.floatToIntBits(this.x) == Float.floatToIntBits(other.x) && Float.floatToIntBits(this.y) == Float.floatToIntBits(other.y) && Float.floatToIntBits(this.z) == Float.floatToIntBits(other.z);
    }
    
    public float getX()
    {
        return this.x;
    }
    
    public void setX(final float x)
    {
        this.x = x;
    }
    
    public Vector2f getXY()
    {
        return new Vector2f(this.x, this.y);
    }
    
    public Vector2f getXZ()
    {
        return new Vector2f(this.x, this.z);
    }
    
    public float getY()
    {
        return this.y;
    }
    
    public void setY(final float y)
    {
        this.y = y;
    }
    
    public Vector2f getYX()
    {
        return new Vector2f(this.y, this.x);
    }
    
    public Vector2f getYZ()
    {
        return new Vector2f(this.y, this.z);
    }
    
    public float getZ()
    {
        return this.z;
    }
    
    public void setZ(final float z)
    {
        this.z = z;
    }
    
    public Vector2f getZX()
    {
        return new Vector2f(this.z, this.x);
    }
    
    public Vector2f getZY()
    {
        return new Vector2f(this.z, this.y);
    }
    
    @Override
    public int hashCode()
    {
        final int prime  = 31;
        int       result = 1;
        result = (prime * result) + Float.floatToIntBits(this.x);
        result = (prime * result) + Float.floatToIntBits(this.y);
        result = (prime * result) + Float.floatToIntBits(this.z);
        return result;
    }
    
    public float length()
    {
        return (float) Math.sqrt((this.x * this.x) + (this.y * this.y) + (this.z * this.z));
    }
    
    public Vector3f lerp(final Vector3f dest, final float lerpFactor)
    {
        return dest.sub(this).mul(lerpFactor).add(this);
    }
    
    public float max()
    {
        return Math.max(this.x, Math.max(this.y, this.z));
    }
    
    public Vector3f max(final Vector3f dist)
    {
        return new Vector3f(Math.max(this.x, dist.x), Math.max(this.y, dist.y), Math.max(this.z, dist.z));
    }
    
    public Vector3f mul(final float r)
    {
        return new Vector3f(this.x * r, this.y * r, this.z * r);
    }
    
    public Vector3f mul(final Vector3f r)
    {
        return new Vector3f(this.x * r.getX(), this.y * r.getY(), this.z * r.getZ());
    }
    
    public Vector3f normalized()
    {
        final float length = this.length();
        
        return new Vector3f(this.x / length, this.y / length, this.z / length);
    }
    
    public Vector3f reflect(final Vector3f normal)
    {
        return this.sub(normal.mul(this.dot(normal) * 2));
    }
    
    public Vector3f rotate(final Quaternion rotation)
    {
        final Quaternion conjugate = rotation.conjugate();
        
        final Quaternion w = rotation.mul(this).mul(conjugate);
        
        return new Vector3f(w.getX(), w.getY(), w.getZ());
    }
    
    public Vector3f rotate(final Vector3f axis, final float angle)
    {
        final float sinAngle = (float) Math.sin(-angle);
        final float cosAngle = (float) Math.cos(-angle);
        
        return this.cross(axis.mul(sinAngle)).add( // Rotation on local X
                                                   (this.mul(cosAngle)).add( // Rotation on local Z
                                                                             axis.mul(this.dot(axis.mul(1 - cosAngle))))); // Rotation
        // on
        // local
        // Y
    }
    
    public Vector3f set(final float v)
    {
        return this.set(v, v, v);
    }
    
    public Vector3f set(final float x, final float y, final float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }
    
    public Vector3f set(final Vector3f r)
    {
        this.set(r.getX(), r.getY(), r.getZ());
        return this;
    }
    
    public Vector3f sub(final float r)
    {
        return new Vector3f(this.x - r, this.y - r, this.z - r);
    }
    
    public Vector3f sub(final Vector3f r)
    {
        return new Vector3f(this.x - r.getX(), this.y - r.getY(), this.z - r.getZ());
    }
    
    @Override
    public String toString()
    {
        return "(" + this.x + " " + this.y + " " + this.z + ")";
    }
}