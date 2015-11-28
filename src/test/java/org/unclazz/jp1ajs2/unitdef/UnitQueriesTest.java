package org.unclazz.jp1ajs2.unitdef;

import static org.junit.Assert.*;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.unclazz.jp1ajs2.unitdef.parameter.MapSize;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;

public class UnitQueriesTest {

	private static final String unitdefForSzTest = "unit=FOO,,,;{ty=n;cm=\"jobnet for test UnitQueries\";sz=1×2;}";
	private static final String unitdefForTyTest = "unit=FOO,,,;"
			+ "{ty=n;cm=\"jobnet for test UnitQueries\";sz=1×2;"
			+ "unit=BAR,,,;{ty=j;cm=\"unix job for test UnitQueries\";tho=1;}"
			+ "}";
	
	@Test
	public void szTest() {
		final Unit unit = Units.fromString(unitdefForSzTest).get(0);
		final MapSize mapSize = unit.query(UnitQueries.sz()).get(0);
		assertThat(mapSize.getHeight(), CoreMatchers.is(2));
		assertThat(mapSize.getWidth(), CoreMatchers.is(1));
	}
	
	@Test
	public void tyTest() {
		final Unit unit = Units.fromString(unitdefForTyTest).get(0);
		final UnitType unitType = unit.query(UnitQueries.ty()).get(0);
		assertThat(unitType, CoreMatchers.is(UnitType.JOBNET));
		
		final Unit subUnit = unit.getSubUnit("BAR");
		final UnitType subUnitType = subUnit.query(UnitQueries.ty()).get(0);
		assertThat(subUnitType, CoreMatchers.is(UnitType.UNIX_JOB));
	}

}
