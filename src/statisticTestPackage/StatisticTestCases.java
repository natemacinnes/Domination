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
		assertEquals("Test for addReinforcements() failed", (double) howMany, statEngine.get(StatType.REINFORCEMENTS), 0 );
		
		statEngine.addKill();
		assertEquals("Test for addKill() failed", (double) 1, statEngine.get(StatType.KILLS), 0 );
		
		statEngine.addKill();
		assertNotSame((double) 1, statEngine.get(StatType.KILLS));
		
		
		statEngine.addCasualty();
		assertEquals("Test for addCasualty() failed", (double) 1, statEngine.get(StatType.CASUALTIES), 0 );
		statEngine.addAttack();
		assertEquals("Test for addAttack() failed", (double) 1, statEngine.get(StatType.ATTACKS), 0 );
		statEngine.addAttacked();
		assertEquals("Test for addAttacked() failed", (double) 1, statEngine.get(StatType.ATTACKED), 0 );
		statEngine.addRetreat();
		assertEquals("Test for addRetreat() failed", (double) 1, statEngine.get(StatType.RETREATS), 0 );
		statEngine.addCountriesWon();
		assertEquals("Test for addCountriesWon() failed", (double) 1, statEngine.get(StatType.COUNTRIES_WON), 0 );
		statEngine.addCountriesLost();
		assertEquals("Test for addCountriesLost() failed", (double) 1, statEngine.get(StatType.COUNTRIES_LOST), 0 );
	}
}
