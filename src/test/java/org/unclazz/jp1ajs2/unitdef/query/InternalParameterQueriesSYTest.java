package org.unclazz.jp1ajs2.unitdef.query;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.builder.Builders;
import org.unclazz.jp1ajs2.unitdef.builder.ParameterBuilder;
import org.unclazz.jp1ajs2.unitdef.parameter.DelayTime.TimingMethod;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDelayTime;
import org.unclazz.jp1ajs2.unitdef.query.InternalParameterQueries;

public class InternalParameterQueriesSYTest {
	private Parameter makeParameter(String name, String... values) {
		final ParameterBuilder b = Builders.parameter().setName(name);
		for (final String value : values) {
			b.addRawCharSequence(value);
		}
		return b.build();
	}
	
	@Test
	public void getTimingMethod_returnsInstanceOfTimingMethodEnum() {
		// Arrange
		final Parameter p0 = makeParameter("sy", "1:01");
		final Parameter p1 = makeParameter("sy", "M1");
		final Parameter p2 = makeParameter("sy", "U2");
		final Parameter p3 = makeParameter("sy", "C20");
		
		// Act
		final StartDelayTime r0 = p0.query(InternalParameterQueries.SY);
		final StartDelayTime r1 = p1.query(InternalParameterQueries.SY);
		final StartDelayTime r2 = p2.query(InternalParameterQueries.SY);
		final StartDelayTime r3 = p3.query(InternalParameterQueries.SY);
		
		// Assert
		assertThat(r0.getTimingMethod(), equalTo(TimingMethod.ABSOLUTE));
		assertThat(r1.getTimingMethod(), equalTo(TimingMethod.RELATIVE_WITH_ROOT_START_TIME));
		assertThat(r2.getTimingMethod(), equalTo(TimingMethod.RELATIVE_WITH_SUPER_START_TIME));
		assertThat(r3.getTimingMethod(), equalTo(TimingMethod.RELATIVE_WITH_THEMSELF_START_TIME));
	}
	
	@Test
	public void getTime_returnsInstanceOfTime() {
		// Arrange
		final Parameter p0 = makeParameter("sy", "1:23");
		final Parameter p1 = makeParameter("sy", "M1");
		final Parameter p2 = makeParameter("sy", "U2");
		final Parameter p3 = makeParameter("sy", "C20");
		
		// Act
		final StartDelayTime r0 = p0.query(InternalParameterQueries.SY);
		final StartDelayTime r1 = p1.query(InternalParameterQueries.SY);
		final StartDelayTime r2 = p2.query(InternalParameterQueries.SY);
		final StartDelayTime r3 = p3.query(InternalParameterQueries.SY);
		
		// Assert
		assertThat(r0.getTime().getHours(), equalTo(1));
		assertThat(r0.getTime().getMinutes(), equalTo(23));
		assertThat(r1.getTime().getHours(), equalTo(0));
		assertThat(r1.getTime().getMinutes(), equalTo(1));
		assertThat(r2.getTime().getHours(), equalTo(0));
		assertThat(r2.getTime().getMinutes(), equalTo(2));
		assertThat(r3.getTime().getHours(), equalTo(0));
		assertThat(r3.getTime().getMinutes(), equalTo(20));
	}
	
	@Test
	public void getRuleNumber_whenCorrespondentValueIsOmitted_returnsRuleNumberRepresents1() {
		// Arrange
		final Parameter p0 = makeParameter("sy", "1:23");
		final Parameter p1 = makeParameter("sy", "M1");
		final Parameter p2 = makeParameter("sy", "U2");
		final Parameter p3 = makeParameter("sy", "C20");
		
		// Act
		final StartDelayTime r0 = p0.query(InternalParameterQueries.SY);
		final StartDelayTime r1 = p1.query(InternalParameterQueries.SY);
		final StartDelayTime r2 = p2.query(InternalParameterQueries.SY);
		final StartDelayTime r3 = p3.query(InternalParameterQueries.SY);
		
		// Assert
		assertThat(r0.getRuleNumber().intValue(), equalTo(1));
		assertThat(r1.getRuleNumber().intValue(), equalTo(1));
		assertThat(r2.getRuleNumber().intValue(), equalTo(1));
		assertThat(r3.getRuleNumber().intValue(), equalTo(1));
	}
	
	@Test
	public void getRuleNumber_returnsInstanceOfRuleNumber() {
		// Arrange
		final Parameter p0 = makeParameter("sy", "1","1:23");
		final Parameter p1 = makeParameter("sy", "2","M1");
		final Parameter p2 = makeParameter("sy", "3","U2");
		final Parameter p3 = makeParameter("sy", "4","C20");
		
		// Act
		final StartDelayTime r0 = p0.query(InternalParameterQueries.SY);
		final StartDelayTime r1 = p1.query(InternalParameterQueries.SY);
		final StartDelayTime r2 = p2.query(InternalParameterQueries.SY);
		final StartDelayTime r3 = p3.query(InternalParameterQueries.SY);
		
		// Assert
		assertThat(r0.getRuleNumber().intValue(), equalTo(1));
		assertThat(r1.getRuleNumber().intValue(), equalTo(2));
		assertThat(r2.getRuleNumber().intValue(), equalTo(3));
		assertThat(r3.getRuleNumber().intValue(), equalTo(4));
	}
}
