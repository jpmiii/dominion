package com.psygate.math.euclidian.point;

import com.psygate.math.euclidian.vector.Vector2D;

public class Point2D {
	private final double x;
	private final double y;
	
	public Point2D(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public Vector2D add(Point2D p2) {
		return new Vector2D(x + p2.x, y + p2.y);
	}
	
	public Vector2D sub(Point2D p2) {
		return new Vector2D(x - p2.x, y - p2.y);
	}
	
	public Point2D add(Vector2D p2) {
		return new Point2D(x + p2.getX(), y + p2.getY());
	}
	
	public Point2D sub(Vector2D p2) {
		return new Point2D(x - p2.getX(), y - p2.getY());
	}
	
	@Override
	public String toString() {
		return "Point2D("+x+","+y+")";
	}
}
