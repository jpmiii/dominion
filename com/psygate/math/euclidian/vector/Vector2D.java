package com.psygate.math.euclidian.vector;

public class Vector2D {
	private final double x, y;

	public Vector2D(double d, double e) {
		this.x = d;
		this.y = e;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public Vector2D normalize() {
		double sl = length();
		return new Vector2D(x / sl, y / sl);
	}
	
	public double length() {
		return Math.sqrt(x * x + y * y);
	}

	public Vector2D scale(double scale) {
		return new Vector2D(x * scale, y * scale);
	}

	public String toString() {
		return "Vector2D(" + x + "," + y + ")";
	}
	
	public Vector2D add(Vector2D vec) {
		return new Vector2D(vec.x + x, vec.y +y);
	}
	
	public Vector2D sub(Vector2D vec) {
		return new Vector2D(x - vec.x, y - vec.y);
	}
	
	public Vector2D inv() {
		return new Vector2D(-x, -y);
	}
	
	public double dot(Vector2D two) {
		return x * two.x + y * two.y;
	}
	
	public double cross(Vector2D two) {
		return x * two.y - y * two.x;
	}
	
	public Vector2D rightNormal() {
		return new Vector2D(y, -x);
	}
	
	public Vector2D leftNormal() {
		return new Vector2D(-y, x);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof Vector2D)) {
			return false;
		} else {
			Vector2D cast = (Vector2D) obj;
			return cast.x == x && cast.y == y;
		}
	}
	
	@Override
	public int hashCode() {
		return (int) (x * 104729 + y);
	}
}
