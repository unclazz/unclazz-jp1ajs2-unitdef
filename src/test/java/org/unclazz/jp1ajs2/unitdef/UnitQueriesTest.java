package org.unclazz.jp1ajs2.unitdef;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;
import org.unclazz.jp1ajs2.unitdef.parameter.AnteroposteriorRelationship;
import org.unclazz.jp1ajs2.unitdef.parameter.CommandLine;
import org.unclazz.jp1ajs2.unitdef.parameter.ElapsedTime;
import org.unclazz.jp1ajs2.unitdef.parameter.Element;
import org.unclazz.jp1ajs2.unitdef.parameter.EndDelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.EndStatusJudgementType;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionCycle;
import org.unclazz.jp1ajs2.unitdef.parameter.MapSize;
import org.unclazz.jp1ajs2.unitdef.parameter.ResultJudgmentType;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitConnectionType;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;
import org.unclazz.jp1ajs2.unitdef.parameter.DelayTime.TimingMethod;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionCycle.CycleUnit;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionTimedOutStatus;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionUserType;
import org.unclazz.jp1ajs2.unitdef.parameter.FileWatchingCondition;
import org.unclazz.jp1ajs2.unitdef.parameter.FileWatchingConditionSet;
import org.unclazz.jp1ajs2.unitdef.parameter.FixedDuration;
import org.unclazz.jp1ajs2.unitdef.parameter.LinkedRuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.MailAddress;
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
	
	@Test
	public void ets_always_returnsUnitQueryForParameterETS() {
		// Arrange
		final Unit unit = sampleJobnetUnit("ets=nr");
		
		// Act
		final ExecutionTimedOutStatus r = unit.query(UnitQueries.ets()).get(0);
		
		// Assert
		assertThat(r, equalTo(ExecutionTimedOutStatus.NORMAL_ENDED));
	}
	
	@Test
	public void eu_always_returnsUnitQueryForParameterEU() {
		// Arrange
		final Unit unit = sampleJobnetUnit("eu=def");
		
		// Act
		final ExecutionUserType r = unit.query(UnitQueries.eu()).get(0);
		
		// Assert
		assertThat(r, equalTo(ExecutionUserType.DEFINITION_USER));
	}
	
	@Test
	public void ey_always_returnsUnitQueryForParameterEY() {
		// Arrange
		final Unit unit = sampleJobnetUnit("ey=01:23", "ey=2,M2879", "ey=3,U2878");
		
		// Act
		final EndDelayTime r = unit.query(UnitQueries.ey()).get(1);
		
		// Assert
		assertThat(r.getRuleNumber().intValue(), equalTo(2));
		assertThat(r.getTimingMethod(), equalTo(TimingMethod.RELATIVE_WITH_ROOT_START_TIME));
		assertThat(r.getTime().getHours(), equalTo(47));
	}
	
	@Test
	public void fd_always_returnsUnitQueryForParameterFD() {
		// Arrange
		final Unit unit = sampleJobnetUnit("fd=1440");
		
		// Act
		final FixedDuration r = unit.query(UnitQueries.fd()).get(0);
		
		// Assert
		assertThat(r.intValue(), equalTo(1440));
	}
	
	@Test
	public void flwc_always_returnsUnitQueryForParameterFLWC() {
		// Arrange
		final Unit unit = sampleJobnetUnit("flwc=d:s");
		
		// Act
		final FileWatchingConditionSet r = unit.query(UnitQueries.flwc()).get(0);
		
		// Assert
		assertThat(r.contains(FileWatchingCondition.CREATE), equalTo(true));
		assertThat(r.contains(FileWatchingCondition.DELETE), equalTo(true));
		assertThat(r.contains(FileWatchingCondition.SIZE), equalTo(true));
		assertThat(r.contains(FileWatchingCondition.MODIFY), equalTo(false));
	}
	
	@Test
	public void jd_always_returnsUnitQueryForParameterJD() {
		// Arrange
		final Unit unit = sampleJobnetUnit("jd=ab");
		
		// Act
		final ResultJudgmentType r = unit.query(UnitQueries.jd()).get(0);
		
		// Assert
		assertThat(r, equalTo(ResultJudgmentType.FORCE_ABNORMAL_END));
	}
	
	@Test
	public void ln_always_returnsUnitQueryForParameterLN() {
		// Arrange
		final Unit unit = sampleJobnetUnit("ln=2","ln=2,1");
		
		// Act
		final LinkedRuleNumber r = unit.query(UnitQueries.ln()).get(0);
		
		// Assert
		assertThat(r.getRuleNumber().intValue(), equalTo(1));
		assertThat(r.getLinkedRuleNumber().intValue(), equalTo(2));
	}
	
	@Test
	public void mladr_always_returnsUnitQueryForParameterMLADR() {
		// Arrange
		final Unit unit = sampleJobnetUnit("mladr=to:\"foo@example.com\"",
				"mladr=bcc:\"bar@example.com\"", "mladr=cc:\"baz@example.com\"");
		
		// Act
		final MailAddress r = unit.query(UnitQueries.mladr()).get(1);
		
		// Assert
		assertThat(r.getType(), equalTo(MailAddress.MailAddressType.BCC));
		assertThat(r.getAddress(), equalTo("bar@example.com"));
	}
	
	@Test
	public void sc_always_returnsUnitQueryForParameterSC() {
		// Arrange
		final Unit unit = sampleJobnetUnit("sc=\"foo.exe bar baz\"");
		
		// Act
		final CommandLine r = unit.query(UnitQueries.sc()).get(0);
		
		// Assert
		assertThat(r.getCommand(), equalTo("foo.exe"));
		assertThat(r.getArguments()[0], equalTo("bar"));
		assertThat(r.getArguments()[1], equalTo("baz"));
	}
}
