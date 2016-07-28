package org.unclazz.jp1ajs2.unitdef.query;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.builder.Builders;
import org.unclazz.jp1ajs2.unitdef.builder.ParameterBuilder;
import org.unclazz.jp1ajs2.unitdef.parameter.StartTime;
import org.unclazz.jp1ajs2.unitdef.query.InternalParameterQueries;

public class InternalParameterQueriesSTTest {
	private Parameter makeParameter(String name, String... values) {
		final ParameterBuilder b = Builders.parameter().setName(name);
		for (final String value : values) {
			b.addRawCharSequence(value);
		}
		return b.build();
	}
	
	@Test
	public void getRuleNumber_returnsRuleNumber() {
		// Arrange
		final Parameter p0 = makeParameter("st", "01:11");
		final Parameter p1 = makeParameter("st", "+11:01");
		final Parameter p2 = makeParameter("st", "2", "11:1");
		final Parameter p3 = makeParameter("st", "20", "+01:11");
		
		// Act
		final StartTime r0 = p0.query(InternalParameterQueries.ST);
		final StartTime r1 = p1.query(InternalParameterQueries.ST);
		final StartTime r2 = p2.query(InternalParameterQueries.ST);
		final StartTime r3 = p3.query(InternalParameterQueries.ST);
		
		// Assert
		assertThat(r0.getRuleNumber().intValue(), equalTo(1));
		assertThat(r1.getRuleNumber().intValue(), equalTo(1));
		assertThat(r2.getRuleNumber().intValue(), equalTo(2));
		assertThat(r3.getRuleNumber().intValue(), equalTo(20));
	}
	@Test
	public void getTime_returnsInstanceOfTime() {
		// Arrange
		final Parameter p0 = makeParameter("st", "01:11");
		final Parameter p1 = makeParameter("st", "+11:01");
		final Parameter p2 = makeParameter("st", "2", "11:1");
		final Parameter p3 = makeParameter("st", "20", "+21:11");
		
		// Act
		final StartTime r0 = p0.query(InternalParameterQueries.ST);
		final StartTime r1 = p1.query(InternalParameterQueries.ST);
		final StartTime r2 = p2.query(InternalParameterQueries.ST);
		final StartTime r3 = p3.query(InternalParameterQueries.ST);
		
		// Assert
		assertThat(r0.getTime().getHours(), equalTo(1));
		assertThat(r0.getTime().getMinutes(), equalTo(11));
		assertThat(r0.isRelative(), equalTo(false));
		assertThat(r1.getTime().getHours(), equalTo(11));
		assertThat(r1.getTime().getMinutes(), equalTo(1));
		assertThat(r1.isRelative(), equalTo(true));
		assertThat(r2.getTime().getHours(), equalTo(11));
		assertThat(r2.getTime().getMinutes(), equalTo(1));
		assertThat(r2.isRelative(), equalTo(false));
		assertThat(r3.getTime().getHours(), equalTo(21));
		assertThat(r3.getTime().getMinutes(), equalTo(11));
		assertThat(r3.isRelative(), equalTo(true));
	}
}
