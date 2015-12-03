package cn.remex.core.util;

public class Number implements Numeric{
	private double n=0;
	
	public Number(double n) {
		super();
		this.n = n;
	}

	@Override
	public double getValue() {
		return n;
	}

	@Override
	public String toString() {
		return "[n=" + n + "]";
	}
	
}