package com.psygate.math.euclidian.polygon;

import com.psygate.math.euclidian.point.Point2D;
import com.psygate.math.euclidian.vector.Vector2D;

public class Triangle2D {
	private final Point2D a, b, c;
	private final Vector2D ab, ac, bc;

	public Triangle2D(Point2D a, Point2D b, Point2D c) {
		this.a = a;
		this.b = b;
		this.c = c;
		ab = b.sub(a);
		ac = c.sub(a);
		bc = c.sub(b);
	}

	public Triangle2D(double xa, double ya, double xb, double yb, double xc, double yc) {
		this.a = new Point2D(xa, ya);
		this.b = new Point2D(xb, yb);
		this.c = new Point2D(xc, yc);
		ab = b.sub(a);
		ac = c.sub(a);
		bc = c.sub(b);
	}

	public Point2D getA() {
		return a;
	}

	public Point2D getB() {
		return b;
	}

	public Point2D getC() {
		return c;
	}

	public boolean contains(Point2D p) {
		// Compute vectors
		Vector2D v0 = ac;
		Vector2D v1 = ab;
		Vector2D v2 = p.sub(a);

		// Compute dot products
		double dot00 = v0.dot(v0); // dot(v0, v0);
		double dot01 = v0.dot(v1); // dot(v0, v1);
		double dot02 = v0.dot(v2); // dot(v0, v2);
		double dot11 = v1.dot(v1); // dot(v1, v1);
		double dot12 = v1.dot(v2); // dot(v1, v2);

		// Compute barycentric coordinates
		double invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
		double u = (dot11 * dot02 - dot01 * dot12) * invDenom;
		double v = (dot00 * dot12 - dot01 * dot02) * invDenom;

		// Check if point is in triangle
		return (u >= 0) && (v >= 0) && (u + v < 1);
	}
	
	public Point2D projectOnAB(Point2D q) {
		Vector2D r = a.sub(b);
		Vector2D s = r.rightNormal();
		double t = ((q.sub(b)).cross(r) / (r.cross(s)));
//		System.out.println(t);
		
		return q.add(s.scale(t));
	}
	
	public Point2D projectOnAC(Point2D q) {
		Vector2D r = a.sub(c);
		Vector2D s = r.rightNormal();
		double t = ((q.sub(c)).cross(r) / (r.cross(s)));
		
		return q.add(s.scale(t));
	}
	
	@Override
	public String toString() {
		return "Triangle{"+a+","+b+","+c+"}";
	}
}
