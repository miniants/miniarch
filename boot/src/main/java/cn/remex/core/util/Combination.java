package cn.remex.core.util;

import java.util.List;


public class Combination<T extends Numeric>{
	private double deviation=0;
	private List<T> combinationItems;
	public Combination(double deviation, List<T> combinationItems) {
		super();
		this.deviation = deviation;
		this.combinationItems = combinationItems;
	}
	public double getDeviation() {
		return deviation;
	}
	public List<T> getCombinationItems() {
		return combinationItems;
	}
	@Override
	public String toString() {
		return "Combination [deviation=" + deviation + ", combinationItems="
				+ combinationItems + "]";
	}
	
}