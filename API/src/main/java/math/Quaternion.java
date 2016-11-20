package math;

public class Quaternion
{
    private float x;
    private float y;
    private float z;
    private float w;

    public Quaternion(final float x, final float y, final float z, final float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    // From Ken Shoemake's "Quaternion Calculus and Fast Animation" article
    public Quaternion(final Matrix4f rot)
    {
        final float trace = rot.get(0, 0) + rot.get(1, 1) + rot.get(2, 2);

        if (trace > 0)
        {
            final float s = 0.5f / (float) Math.sqrt(trace + 1.0f);
            this.w = 0.25f / s;
            this.x = (rot.get(1, 2) - rot.get(2, 1)) * s;
            this.y = (rot.get(2, 0) - rot.get(0, 2)) * s;
            this.z = (rot.get(0, 1) - rot.get(1, 0)) * s;
        } else
        {
            if ((rot.get(0, 0) > rot.get(1, 1)) && (rot.get(0, 0) > rot.get(2, 2)))
            {
                final float s = 2.0f * (float) Math.sqrt((1.0f + rot.get(0, 0)) - rot.get(1, 1) - rot.get(2, 2));
                this.w = (rot.get(1, 2) - rot.get(2, 1)) / s;
                this.x = 0.25f * s;
                this.y = (rot.get(1, 0) + rot.get(0, 1)) / s;
                this.z = (rot.get(2, 0) + rot.get(0, 2)) / s;
            } else if (rot.get(1, 1) > rot.get(2, 2))
            {
                final float s = 2.0f * (float) Math.sqrt((1.0f + rot.get(1, 1)) - rot.get(0, 0) - rot.get(2, 2));
                this.w = (rot.get(2, 0) - rot.get(0, 2)) / s;
                this.x = (rot.get(1, 0) + rot.get(0, 1)) / s;
                this.y = 0.25f * s;
                this.z = (rot.get(2, 1) + rot.get(1, 2)) / s;
            } else
            {
                final float s = 2.0f * (float) Math.sqrt((1.0f + rot.get(2, 2)) - rot.get(0, 0) - rot.get(1, 1));
                this.w = (rot.get(0, 1) - rot.get(1, 0)) / s;
                this.x = (rot.get(2, 0) + rot.get(0, 2)) / s;
                this.y = (rot.get(1, 2) + rot.get(2, 1)) / s;
                this.z = 0.25f * s;
            }
        }

        final float length = (float) Math.sqrt((this.x * this.x) + (this.y * this.y) + (this.z * this.z) + (this.w * this.w));
        this.x /= length;
        this.y /= length;
        this.z /= length;
        this.w /= length;
    }

    public Quaternion(final Vector3f axis, final float angle)
    {
        final float sinHalfAngle = (float) Math.sin(angle / 2);
        final float cosHalfAngle = (float) Math.cos(angle / 2);

        this.x = axis.getX() * sinHalfAngle;
        this.y = axis.getY() * sinHalfAngle;
        this.z = axis.getZ() * sinHalfAngle;
        this.w = cosHalfAngle;
    }

    public Quaternion add(final Quaternion r)
    {
        return new Quaternion(this.x + r.getX(), this.y + r.getY(), this.z + r.getZ(), this.w + r.getW());
    }

    public Quaternion conjugate()
    {
        return new Quaternion(-this.x, -this.y, -this.z, this.w);
    }

    public float dot(final Quaternion r)
    {
        return (this.x * r.getX()) + (this.y * r.getY()) + (this.z * r.getZ()) + (this.w * r.getW());
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
        final Quaternion other = (Quaternion) obj;
        return Float.floatToIntBits(this.w) == Float.floatToIntBits(other.w) && Float.floatToIntBits(this.x) == Float.floatToIntBits(other.x) && Float.floatToIntBits(this.y) == Float.floatToIntBits(other.y) && Float.floatToIntBits(this.z) == Float.floatToIntBits(other.z);
    }

    public Vector3f getBack()
    {
        return new Vector3f(0, 0, -1).rotate(this);
    }

    public Vector3f getDown()
    {
        return new Vector3f(0, -1, 0).rotate(this);
    }

    public Vector3f getForward()
    {
        return new Vector3f(0, 0, 1).rotate(this);
    }

    public Vector3f getLeft()
    {
        return new Vector3f(-1, 0, 0).rotate(this);
    }

    public Vector3f getRight()
    {
        return new Vector3f(1, 0, 0).rotate(this);
    }

    public Vector3f getUp()
    {
        return new Vector3f(0, 1, 0).rotate(this);
    }

    public float getW()
    {
        return this.w;
    }

    public float getX()
    {
        return this.x;
    }

    public float getY()
    {
        return this.y;
    }

    public float getZ()
    {
        return this.z;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + Float.floatToIntBits(this.w);
        result = (prime * result) + Float.floatToIntBits(this.x);
        result = (prime * result) + Float.floatToIntBits(this.y);
        result = (prime * result) + Float.floatToIntBits(this.z);
        return result;
    }

    public float length()
    {
        return (float) Math.sqrt((this.x * this.x) + (this.y * this.y) + (this.z * this.z) + (this.w * this.w));
    }

    public Quaternion mul(final float r)
    {
        return new Quaternion(this.x * r, this.y * r, this.z * r, this.w * r);
    }

    public Quaternion mul(final Quaternion r)
    {
        final float w_ = (this.w * r.getW()) - (this.x * r.getX()) - (this.y * r.getY()) - (this.z * r.getZ());
        final float x_ = ((this.x * r.getW()) + (this.w * r.getX()) + (this.y * r.getZ())) - (this.z * r.getY());
        final float y_ = ((this.y * r.getW()) + (this.w * r.getY()) + (this.z * r.getX())) - (this.x * r.getZ());
        final float z_ = ((this.z * r.getW()) + (this.w * r.getZ()) + (this.x * r.getY())) - (this.y * r.getX());

        return new Quaternion(x_, y_, z_, w_);
    }

    public Quaternion mul(final Vector3f r)
    {
        final float w_ = (-this.x * r.getX()) - (this.y * r.getY()) - (this.z * r.getZ());
        final float x_ = ((this.w * r.getX()) + (this.y * r.getZ())) - (this.z * r.getY());
        final float y_ = ((this.w * r.getY()) + (this.z * r.getX())) - (this.x * r.getZ());
        final float z_ = ((this.w * r.getZ()) + (this.x * r.getY())) - (this.y * r.getX());

        return new Quaternion(x_, y_, z_, w_);
    }

    public Quaternion nlerp(final Quaternion dest, final float lerpFactor, final boolean shortest)
    {
        Quaternion correctedDest = dest;

        if (shortest && (this.dot(dest) < 0))
        {
            correctedDest = new Quaternion(-dest.getX(), -dest.getY(), -dest.getZ(), -dest.getW());
        }

        return correctedDest.sub(this).mul(lerpFactor).add(this).normalized();
    }

    public Quaternion normalized()
    {
        final float length = this.length();

        return new Quaternion(this.x / length, this.y / length, this.z / length, this.w / length);
    }

    public Quaternion set(final float x, final float y, final float z, final float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    public Quaternion set(final Quaternion r)
    {
        this.set(r.getX(), r.getY(), r.getZ(), r.getW());
        return this;
    }

    public void setW(final float w)
    {
        this.w = w;
    }

    public void setX(final float x)
    {
        this.x = x;
    }

    public void setY(final float y)
    {
        this.y = y;
    }

    public void setZ(final float z)
    {
        this.z = z;
    }

    public Quaternion slerp(final Quaternion dest, final float lerpFactor, final boolean shortest)
    {
        final float EPSILON = 1e3f;

        float cos = this.dot(dest);
        Quaternion correctedDest = dest;

        if (shortest && (cos < 0))
        {
            cos = -cos;
            correctedDest = new Quaternion(-dest.getX(), -dest.getY(), -dest.getZ(), -dest.getW());
        }

        if (Math.abs(cos) >= (1 - EPSILON))
        {
            return this.nlerp(correctedDest, lerpFactor, false);
        }

        final float sin = (float) Math.sqrt(1.0f - (cos * cos));
        final float angle = (float) Math.atan2(sin, cos);
        final float invSin = 1.0f / sin;

        final float srcFactor = (float) Math.sin((1.0f - lerpFactor) * angle) * invSin;
        final float destFactor = (float) Math.sin((lerpFactor) * angle) * invSin;

        return this.mul(srcFactor).add(correctedDest.mul(destFactor));
    }

    public Quaternion sub(final Quaternion r)
    {
        return new Quaternion(this.x - r.getX(), this.y - r.getY(), this.z - r.getZ(), this.w - r.getW());
    }

    public Matrix4f toRotationMatrix()
    {
        final Vector3f forward = new Vector3f(2.0f * ((this.x * this.z) - (this.w * this.y)), 2.0f * ((this.y * this.z) + (this.w * this.x)), 1.0f - (2.0f * ((this.x * this.x) + (this.y * this.y))));
        final Vector3f up = new Vector3f(2.0f * ((this.x * this.y) + (this.w * this.z)), 1.0f - (2.0f * ((this.x * this.x) + (this.z * this.z))), 2.0f * ((this.y * this.z) - (this.w * this.x)));
        final Vector3f right = new Vector3f(1.0f - (2.0f * ((this.y * this.y) + (this.z * this.z))), 2.0f * ((this.x * this.y) - (this.w * this.z)), 2.0f * ((this.x * this.z) + (this.w * this.y)));

        return Matrix4f.rotation(forward, up, right);
    }
}