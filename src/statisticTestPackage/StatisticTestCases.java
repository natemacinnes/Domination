package statisticTestPackage;

import net.yura.domination.engine.core.StatType;
import net.yura.domination.engine.core.Statistic;
import org.junit.Test;
import junit.framework.TestCase;

public class StatisticTestCases extends TestCase {
	
	@Test
	public void testAllStatisticsIncrementByOne() {
		Statistic statEngine = new Statistic();
		
		for (StatType stat : StatType.values()) {
			if (stat == StatType.DICE) {
					continue; 
			}
			statEngine.incrementStatistic(stat, 1);
			assertEquals((double) 1, statEngine.get(stat) );
		}
	}
	
	public void testAllStatisticsIncrementByMany() {
		Statistic statEngine = new Statistic();
		final int howMany = 4;
		
		for (StatType stat : StatType.values()) {
			if (stat == StatType.DICE) {
				continue; 
			}
			statEngine.incrementStatistic(stat, howMany);
			assertEquals((double) howMany, statEngine.get(stat) );
		}
	}
	
	public void testOldMethods() {
		Statistic statEngine = new Statistic();
		final int howMany = 4;
		
		statEngine.addReinforcements(howMany);
		assertEquals((double) howMany, statEngine.get(StatType.REINFORCEMENTS) );
		
		statEngine.addKill();
		assertEquals((double) 1, statEngine.get(StatType.KILLS) );
		
		statEngine.addKill();
		assertNotSame((double) 1, statEngine.get(StatType.KILLS) );
	}
}
