package math;

public class Matrix4f
{
	public static Matrix4f identity()
	{
		final Matrix4f a = new Matrix4f();
		
		a.set(0, 0, 1);
		a.set(1, 1, 1);
		a.set(2, 2, 1);
		a.set(3, 3, 1);
		
		return a;
	}
	
	public static Matrix4f orthographic(final float left, final float right, final float bottom, final float top, final float far, final float near)
	{
		final float rl = right - left;
		final float tb = top - bottom;
		final float fn = far - near;
		
		final Matrix4f a = Matrix4f.identity();
		
		a.set(0, 0, 2 / rl);
		a.set(0, 3, -(right + left) / rl);
		a.set(1, 1, 2 / tb);
		a.set(1, 3, -(top + bottom) / tb);
		a.set(2, 2, -2 / fn);
		a.set(2, 3, -(far + near) / fn);
		
		return a;
	}
	
	public static Matrix4f perspective(final float fov, final float aspectRatio, final float zNear, final float zFar)
	{
		final float tanHalfFOV = (float) Math.tan(0.5 * fov);
		final float fdepth     = zFar - zNear;
		final float odepth     = 1 / fdepth;
		
		final Matrix4f a = Matrix4f.identity();
		
		a.set(0, 0, (1.0f / tanHalfFOV) / aspectRatio);
		a.set(1, 1, 1.0f / tanHalfFOV);
		a.set(2, 2, zFar * odepth);
		a.set(2, 3, 1);
		a.set(3, 2, (-zFar * zNear) * odepth);
		a.set(3, 3, 0);
		
		return a;
	}
	
	public static Matrix4f rotation(final float _x, final float _y, final float _z)
	{
		
		final float x = (float) Math.toRadians(_x);
		final float y = (float) Math.toRadians(_y);
		final float z = (float) Math.toRadians(_z);
		
		final Matrix4f rx = Matrix4f.identity();
		final Matrix4f ry = Matrix4f.identity();
		final Matrix4f rz = Matrix4f.identity();
		
		rx.set(1, 1, (float) Math.cos(x));
		rx.set(1, 2, (float) -Math.sin(x));
		rx.set(2, 1, (float) Math.sin(x));
		rx.set(2, 2, (float) Math.cos(x));
		
		ry.set(0, 0, (float) Math.cos(y));
		ry.set(0, 2, (float) -Math.sin(y));
		ry.set(2, 0, (float) Math.sin(y));
		ry.set(2, 2, (float) Math.cos(y));
		
		rz.set(0, 0, (float) Math.cos(z));
		rz.set(0, 1, (float) -Math.sin(z));
		rz.set(1, 0, (float) Math.sin(z));
		rz.set(1, 1, (float) Math.cos(z));
		
		return rz.mul(rx.mul(ry));
	}
	
	public static Matrix4f rotation(final Vector3f forward, final Vector3f up)
	{
		final Vector3f f = forward.normalized();
		Vector3f       r = up.normalized();
		r = r.cross(f);
		
		final Vector3f u = f.cross(r);
		
		return Matrix4f.rotation(f, u, r);
	}
	
	public static Matrix4f rotation(final Vector3f forward, final Vector3f up, final Vector3f right)
	{
		
		final Matrix4f a = Matrix4f.identity();
		
		a.set(0, 0, right.getX());
		a.set(0, 1, right.getY());
		a.set(0, 2, right.getZ());
		
		a.set(1, 0, up.getX());
		a.set(1, 1, up.getY());
		a.set(1, 2, up.getZ());
		
		a.set(2, 0, forward.getX());
		a.set(2, 1, forward.getY());
		a.set(2, 2, forward.getZ());
		
		return a;
	}
	
	public static Matrix4f scale(final float x, final float y, final float z)
	{
		final Matrix4f a = Matrix4f.identity();
		
		a.set(0, 0, x);
		a.set(1, 1, y);
		a.set(2, 2, z);
		
		return a;
	}
	
	public static Matrix4f translation(final float x, final float y, final float z)
	{
		final Matrix4f a = Matrix4f.identity();
		
		a.set(0, 3, x);
		a.set(1, 3, y);
		a.set(2, 3, z);
		
		return a;
	}
	
	private final float[][] m;
	
	public Matrix4f()
	{
		this.m = new float[4][4];
	}
	
	public float determinant()
	{
		return (((((((((((((this.m[3][0] * this.m[2][1] * this.m[1][2] * this.m[0][3]) - (this.m[2][0] * this.m[3][1] * this.m[1][2] * this.m[0][3]) - (this.m[3][0] * this.m[1][1] * this.m[2][2] * this.m[0][3])) + (this.m[1][0] * this.m[3][1] * this.m[2][2] * this.m[0][3]) + (this.m[2][0] * this.m[1][1] * this.m[3][2] * this.m[0][3])) - (this.m[1][0] * this.m[2][1] * this.m[3][2] * this.m[0][3]) - (this.m[3][0] * this.m[2][1] * this.m[0][2] * this.m[1][3])) + (this.m[2][0] * this.m[3][1] * this.m[0][2] * this.m[1][3]) + (this.m[3][0] * this.m[0][1] * this.m[2][2] * this.m[1][3])) - (this.m[0][0] * this.m[3][1] * this.m[2][2] * this.m[1][3]) - (this.m[2][0] * this.m[0][1] * this.m[3][2] * this.m[1][3])) + (this.m[0][0] * this.m[2][1] * this.m[3][2] * this.m[1][3]) + (this.m[3][0] * this.m[1][1] * this.m[0][2] * this.m[2][3])) - (this.m[1][0] * this.m[3][1] * this.m[0][2] * this.m[2][3]) - (this.m[3][0] * this.m[0][1] * this.m[1][2] * this.m[2][3])) + (this.m[0][0] * this.m[3][1] * this.m[1][2] * this.m[2][3]) + (this.m[1][0] * this.m[0][1] * this.m[3][2] * this.m[2][3])) - (this.m[0][0] * this.m[1][1] * this.m[3][2] * this.m[2][3]) - (this.m[2][0] * this.m[1][1] * this.m[0][2] * this.m[3][3])) + (this.m[1][0] * this.m[2][1] * this.m[0][2] * this.m[3][3]) + (this.m[2][0] * this.m[0][1] * this.m[1][2] * this.m[3][3])) - (this.m[0][0] * this.m[2][1] * this.m[1][2] * this.m[3][3]) - (this.m[1][0] * this.m[0][1] * this.m[2][2] * this.m[3][3])) + (this.m[0][0] * this.m[1][1] * this.m[2][2] * this.m[3][3]));
	}
	
	public float get(final int x, final int y)
	{
		return this.m[x][y];
	}
	
	public float[][] getM()
	{
		final float[][] res = new float[4][4];
		
		for (int i = 0; i < 4; i++)
		{
			System.arraycopy(this.m[i], 0, res[i], 0, 4);
		}
		
		return res;
	}
	
	public Matrix4f inverse()
	{
		final float determinant = this.determinant();
		
		if (determinant == 0f)
		{
			return null;
		}
		
		final float inv_det = 1.0f / determinant;
		
		final Matrix4f a = new Matrix4f();
		
		a.set(0, 0, ((((this.m[1][2] * this.m[2][3] * this.m[3][1]) - (this.m[1][3] * this.m[2][2] * this.m[3][1])) + (this.m[1][3] * this.m[2][1] * this.m[3][2])) - (this.m[1][1] * this.m[2][3] * this.m[3][2]) - (this.m[1][2] * this.m[2][1] * this.m[3][3])) + (this.m[1][1] * this.m[2][2] * this.m[3][3]));
		a.set(0, 1, (((this.m[0][3] * this.m[2][2] * this.m[3][1]) - (this.m[0][2] * this.m[2][3] * this.m[3][1]) - (this.m[0][3] * this.m[2][1] * this.m[3][2])) + (this.m[0][1] * this.m[2][3] * this.m[3][2]) + (this.m[0][2] * this.m[2][1] * this.m[3][3])) - (this.m[0][1] * this.m[2][2] * this.m[3][3]));
		a.set(0, 2, ((((this.m[0][2] * this.m[1][3] * this.m[3][1]) - (this.m[0][3] * this.m[1][2] * this.m[3][1])) + (this.m[0][3] * this.m[1][1] * this.m[3][2])) - (this.m[0][1] * this.m[1][3] * this.m[3][2]) - (this.m[0][2] * this.m[1][1] * this.m[3][3])) + (this.m[0][1] * this.m[1][2] * this.m[3][3]));
		a.set(0, 3, (((this.m[0][3] * this.m[1][2] * this.m[2][1]) - (this.m[0][2] * this.m[1][3] * this.m[2][1]) - (this.m[0][3] * this.m[1][1] * this.m[2][2])) + (this.m[0][1] * this.m[1][3] * this.m[2][2]) + (this.m[0][2] * this.m[1][1] * this.m[2][3])) - (this.m[0][1] * this.m[1][2] * this.m[2][3]));
		a.set(1, 0, (((this.m[1][3] * this.m[2][2] * this.m[3][0]) - (this.m[1][2] * this.m[2][3] * this.m[3][0]) - (this.m[1][3] * this.m[2][0] * this.m[3][2])) + (this.m[1][0] * this.m[2][3] * this.m[3][2]) + (this.m[1][2] * this.m[2][0] * this.m[3][3])) - (this.m[1][0] * this.m[2][2] * this.m[3][3]));
		a.set(1, 1, ((((this.m[0][2] * this.m[2][3] * this.m[3][0]) - (this.m[0][3] * this.m[2][2] * this.m[3][0])) + (this.m[0][3] * this.m[2][0] * this.m[3][2])) - (this.m[0][0] * this.m[2][3] * this.m[3][2]) - (this.m[0][2] * this.m[2][0] * this.m[3][3])) + (this.m[0][0] * this.m[2][2] * this.m[3][3]));
		a.set(1, 2, (((this.m[0][3] * this.m[1][2] * this.m[3][0]) - (this.m[0][2] * this.m[1][3] * this.m[3][0]) - (this.m[0][3] * this.m[1][0] * this.m[3][2])) + (this.m[0][0] * this.m[1][3] * this.m[3][2]) + (this.m[0][2] * this.m[1][0] * this.m[3][3])) - (this.m[0][0] * this.m[1][2] * this.m[3][3]));
		a.set(1, 3, ((((this.m[0][2] * this.m[1][3] * this.m[2][0]) - (this.m[0][3] * this.m[1][2] * this.m[2][0])) + (this.m[0][3] * this.m[1][0] * this.m[2][2])) - (this.m[0][0] * this.m[1][3] * this.m[2][2]) - (this.m[0][2] * this.m[1][0] * this.m[2][3])) + (this.m[0][0] * this.m[1][2] * this.m[2][3]));
		a.set(2, 0, ((((this.m[1][1] * this.m[2][3] * this.m[3][0]) - (this.m[1][3] * this.m[2][1] * this.m[3][0])) + (this.m[1][3] * this.m[2][0] * this.m[3][1])) - (this.m[1][0] * this.m[2][3] * this.m[3][1]) - (this.m[1][1] * this.m[2][0] * this.m[3][3])) + (this.m[1][0] * this.m[2][1] * this.m[3][3]));
		a.set(2, 1, (((this.m[0][3] * this.m[2][1] * this.m[3][0]) - (this.m[0][1] * this.m[2][3] * this.m[3][0]) - (this.m[0][3] * this.m[2][0] * this.m[3][1])) + (this.m[0][0] * this.m[2][3] * this.m[3][1]) + (this.m[0][1] * this.m[2][0] * this.m[3][3])) - (this.m[0][0] * this.m[2][1] * this.m[3][3]));
		a.set(2, 2, ((((this.m[0][1] * this.m[1][3] * this.m[3][0]) - (this.m[0][3] * this.m[1][1] * this.m[3][0])) + (this.m[0][3] * this.m[1][0] * this.m[3][1])) - (this.m[0][0] * this.m[1][3] * this.m[3][1]) - (this.m[0][1] * this.m[1][0] * this.m[3][3])) + (this.m[0][0] * this.m[1][1] * this.m[3][3]));
		a.set(2, 3, (((this.m[0][3] * this.m[1][1] * this.m[2][0]) - (this.m[0][1] * this.m[1][3] * this.m[2][0]) - (this.m[0][3] * this.m[1][0] * this.m[2][1])) + (this.m[0][0] * this.m[1][3] * this.m[2][1]) + (this.m[0][1] * this.m[1][0] * this.m[2][3])) - (this.m[0][0] * this.m[1][1] * this.m[2][3]));
		a.set(3, 0, (((this.m[1][2] * this.m[2][1] * this.m[3][0]) - (this.m[1][1] * this.m[2][2] * this.m[3][0]) - (this.m[1][2] * this.m[2][0] * this.m[3][1])) + (this.m[1][0] * this.m[2][2] * this.m[3][1]) + (this.m[1][1] * this.m[2][0] * this.m[3][2])) - (this.m[1][0] * this.m[2][1] * this.m[3][2]));
		a.set(3, 1, ((((this.m[0][1] * this.m[2][2] * this.m[3][0]) - (this.m[0][2] * this.m[2][1] * this.m[3][0])) + (this.m[0][2] * this.m[2][0] * this.m[3][1])) - (this.m[0][0] * this.m[2][2] * this.m[3][1]) - (this.m[0][1] * this.m[2][0] * this.m[3][2])) + (this.m[0][0] * this.m[2][1] * this.m[3][2]));
		a.set(3, 2, (((this.m[0][2] * this.m[1][1] * this.m[3][0]) - (this.m[0][1] * this.m[1][2] * this.m[3][0]) - (this.m[0][2] * this.m[1][0] * this.m[3][1])) + (this.m[0][0] * this.m[1][2] * this.m[3][1]) + (this.m[0][1] * this.m[1][0] * this.m[3][2])) - (this.m[0][0] * this.m[1][1] * this.m[3][2]));
		a.set(3, 3, ((((this.m[0][1] * this.m[1][2] * this.m[2][0]) - (this.m[0][2] * this.m[1][1] * this.m[2][0])) + (this.m[0][2] * this.m[1][0] * this.m[2][1])) - (this.m[0][0] * this.m[1][2] * this.m[2][1]) - (this.m[0][1] * this.m[1][0] * this.m[2][2])) + (this.m[0][0] * this.m[1][1] * this.m[2][2]));
		
		return a.mul(inv_det);
	}
	
	public Matrix4f mul(final float f)
	{
		final Matrix4f res = new Matrix4f();
		
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				res.set(i, j, this.m[i][j] * f);
			}
		}
		
		return res;
	}
	
	public Matrix4f mul(final Matrix4f r)
	{
		final Matrix4f res = new Matrix4f();
		
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				res.set(i, j, (this.m[i][0] * r.get(0, j)) + (this.m[i][1] * r.get(1, j)) + (this.m[i][2] * r.get(2, j)) + (this.m[i][3] * r.get(3, j)));
			}
		}
		
		return res;
	}
	
	public void set(final int x, final int y, final float mue)
	{
		this.m[x][y] = mue;
	}
	
	public Vector3f transform(final Vector3f r)
	{
		return new Vector3f((this.m[0][0] * r.getX()) + (this.m[0][1] * r.getY()) + (this.m[0][2] * r.getZ()) + this.m[0][3], (this.m[1][0] * r.getX()) + (this.m[1][1] * r.getY()) + (this.m[1][2] * r.getZ()) + this.m[1][3], (this.m[2][0] * r.getX()) + (this.m[2][1] * r.getY()) + (this.m[2][2] * r.getZ()) + this.m[2][3]);
	}
}