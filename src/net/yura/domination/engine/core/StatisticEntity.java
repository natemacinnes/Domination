package net.yura.domination.engine.core;

public class StatisticEntity implements IStatisticEntity {

	private int total = 0;
	
	public void incrementStatistic(final int incrementBy) {
    	total += incrementBy;
    }
	
	public int getStatistic() {
		return total;
	}
	
}
