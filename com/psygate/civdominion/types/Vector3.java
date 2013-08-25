package com.psygate.civdominion.types;

public class Vector3<T, V, W> {
	private T t;
	private V v;
	private W w;
	
	public Vector3(T t, V v, W w) {
		this.t = t;
		this.v = v;
		this.w = w;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}

	public V getV() {
		return v;
	}

	public void setV(V v) {
		this.v = v;
	}

	public W getW() {
		return w;
	}

	public void setW(W w) {
		this.w = w;
	}
}
