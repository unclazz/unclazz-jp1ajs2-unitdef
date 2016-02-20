package org.unclazz.jp1ajs2.unitdef;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;
import org.unclazz.jp1ajs2.unitdef.parameter.AnteroposteriorRelationship;
import org.unclazz.jp1ajs2.unitdef.parameter.ElapsedTime;
import org.unclazz.jp1ajs2.unitdef.parameter.Element;
import org.unclazz.jp1ajs2.unitdef.parameter.EndStatusJudgementType;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionCycle;
import org.unclazz.jp1ajs2.unitdef.parameter.MapSize;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitConnectionType;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionCycle.CycleUnit;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;
import org.unclazz.jp1ajs2.unitdef.util.UnsignedIntegral;

public class UnitQueriesTest {
	
	private static Unit sampleJobnetUnit(String... params) {
		final StringBuilder buff = CharSequenceUtils
				.builder()
				.append("unit=FOO,,,;{ty=n;");
		
		for (final String param : params) {
			buff.append(param);
			if (!param.endsWith(";")) {
				buff.append(';');
			}
		}
		
		return Units.fromCharSequence(buff.append("}").toString()).get(0);
	}
	
	@Test
	public void sz_always_returnsUnitQueryForParameterSZ() {
		// Arrange
		final Unit unit = sampleJobnetUnit("sz=1×2");
		
		// Act
		final MapSize r = unit.query(UnitQueries.sz()).get(0);
		
		// Assert
		assertThat(r.getHeight(), equalTo(2));
		assertThat(r.getWidth(), equalTo(1));
	}
	
	@Test
	public void ty_always_returnsUnitQueryForParameterTY() {
		// Arrange
		final Unit unit = sampleJobnetUnit("sz=1×2");
		
		// Act
		final UnitType r = unit.query(UnitQueries.ty()).get(0);
		
		// Assert
		assertThat(r, equalTo(UnitType.JOBNET));
	}
	
	@Test
	public void ar_always_returnsUnitQueryForParameterAR() {
		// Arrange
		final Unit unit = sampleJobnetUnit("ar=(f=BAR0,t=BAR1)",
				"ar=(f=BAR0,t=BAR2,seq)", "ar=(f=BAR1,t=BAR3,con)");
		
		// Act
		final AnteroposteriorRelationship r = unit.query(UnitQueries.ar()).get(2);
		
		// Assert
		assertThat(r.getToUnitName(), equalTo("BAR3"));
		assertThat(r.getConnectionType(), equalTo(UnitConnectionType.CONDITIONAL));
	}
	
	@Test
	public void cm_always_returnsUnitQueryForParameterCM() {
		// Arrange
		final Unit unit = sampleJobnetUnit("cm=\"Sample Unit Comment\"");
		
		// Act
		final CharSequence r = unit.query(UnitQueries.cm()).get(0);
		
		// Assert
		assertThat(r.toString(), equalTo("Sample Unit Comment"));
	}
	
	@Test
	public void cy_always_returnsUnitQueryForParameterCY() {
		// Arrange
		final Unit unit = sampleJobnetUnit("cy=(4,y)",
				"cy=2,(3,m)", "cy=3,(2,d)", "cy=4,(1,w)");
		
		// Act
		final ExecutionCycle r = unit.query(UnitQueries.cy()).get(0);
		
		// Assert
		assertThat(r.getRuleNumber().intValue(), equalTo(1));
		assertThat(r.getInterval(), equalTo(4));
		assertThat(r.getCycleUnit(), equalTo(CycleUnit.YEAR));
	}
	
	@Test
	public void ej_always_returnsUnitQueryForParameterEJ() {
		// Arrange
		final Unit unit = sampleJobnetUnit("ej=gt");
		
		// Act
		final EndStatusJudgementType r = unit.query(UnitQueries.ej()).get(0);
		
		// Assert
		assertThat(r, equalTo(EndStatusJudgementType.EXIT_CODE_GT));
	}
	
	@Test
	public void ejc_always_returnsUnitQueryForParameterEJC() {
		// Arrange
		final Unit unit = sampleJobnetUnit("ejc=4294967295");
		
		// Act
		final UnsignedIntegral r = unit.query(UnitQueries.ejc()).get(0);
		
		// Assert
		assertThat(r.longValue(), equalTo(4294967295L));
	}
	
	@Test
	public void el_always_returnsUnitQueryForParameterEL() {
		// Arrange
		final Unit unit = sampleJobnetUnit("el=BAR0,n,+80 +48", 
				"el=BAR1,n,+240 +48", "el=BAR2,n,+80 +144");
		
		// Act
		final Element r = unit.query(UnitQueries.el()).get(2);
		
		// Assert
		assertThat(r.getUnitName(), equalTo("BAR2"));
		assertThat(r.getUnitType(), equalTo(UnitType.JOBNET));
		assertThat(r.getVPixel(), equalTo(144));
		assertThat(r.getYCoord(), equalTo(1));
	}
	
	@Test
	public void etm_always_returnsUnitQueryForParameterETM() {
		// Arrange
		final Unit unit = sampleJobnetUnit("etm=1440");
		
		// Act
		final ElapsedTime r = unit.query(UnitQueries.etm()).get(0);
		
		// Assert
		assertThat(r.intValue(), equalTo(1440));
		assertThat(r.getHours(), equalTo(24));
		assertThat(r.getMinutes(), equalTo(0));
	}

}
