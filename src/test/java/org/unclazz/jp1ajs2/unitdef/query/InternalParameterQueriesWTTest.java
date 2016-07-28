package org.unclazz.jp1ajs2.unitdef.query;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.builder.Builders;
import org.unclazz.jp1ajs2.unitdef.builder.ParameterBuilder;
import org.unclazz.jp1ajs2.unitdef.parameter.RunConditionWatchLimitTime.LimitationType;
import org.unclazz.jp1ajs2.unitdef.parameter.RunConditionWatchLimitTime;
import org.unclazz.jp1ajs2.unitdef.query.InternalParameterQueries;

public class InternalParameterQueriesWTTest {
	private Parameter makeParameter(String name, String... values) {
		final ParameterBuilder b = Builders.parameter().setName(name);
		for (final String value : values) {
			b.addRawCharSequence(value);
		}
		return b.build();
	}
	
	@Test
	public void values_no() {
		// Arrange
		final Parameter p0 = makeParameter("wt", "no");
		
		// Act
		final RunConditionWatchLimitTime t0 = p0.query(InternalParameterQueries.WT);
		
		// Assert
		assertThat(t0.getTime(), nullValue());
		assertThat(t0.getRuleNumber().intValue(), equalTo(1));
		assertThat(t0.getType(), equalTo(LimitationType.NO_WATCHING));
	}
	
	@Test
	public void values_2_no() {
		// Arrange
		final Parameter p0 = makeParameter("wt", "2", "no");
		
		// Act
		final RunConditionWatchLimitTime t0 = p0.query(InternalParameterQueries.WT);
		
		// Assert
		assertThat(t0.getTime(), nullValue());
		assertThat(t0.getRuleNumber().intValue(), equalTo(2));
		assertThat(t0.getType(), equalTo(LimitationType.NO_WATCHING));
	}
	
	@Test
	public void values_un() {
		// Arrange
		final Parameter p0 = makeParameter("wt", "un");
		
		// Act
		final RunConditionWatchLimitTime t0 = p0.query(InternalParameterQueries.WT);
		
		// Assert
		assertThat(t0.getTime(), nullValue());
		assertThat(t0.getRuleNumber().intValue(), equalTo(1));
		assertThat(t0.getType(), equalTo(LimitationType.UNLIMITTED));
	}
	
	@Test
	public void values_2_un() {
		// Arrange
		final Parameter p0 = makeParameter("wt", "2", "un");
		
		// Act
		final RunConditionWatchLimitTime t0 = p0.query(InternalParameterQueries.WT);
		
		// Assert
		assertThat(t0.getTime(), nullValue());
		assertThat(t0.getRuleNumber().intValue(), equalTo(2));
		assertThat(t0.getType(), equalTo(LimitationType.UNLIMITTED));
	}
	
	@Test
	public void values_hhmm() {
		// Arrange
		final Parameter p0 = makeParameter("wt", "12:34");
		
		// Act
		final RunConditionWatchLimitTime t0 = p0.query(InternalParameterQueries.WT);
		
		// Assert
		assertThat(t0.getTime().getHours(), equalTo(12));
		assertThat(t0.getTime().getMinutes(), equalTo(34));
		assertThat(t0.getRuleNumber().intValue(), equalTo(1));
		assertThat(t0.getType(), equalTo(LimitationType.ABSOLUTE_TIME));
	}
	
	@Test
	public void values_2_hhmm() {
		// Arrange
		final Parameter p0 = makeParameter("wt", "3", "12:34");
		
		// Act
		final RunConditionWatchLimitTime t0 = p0.query(InternalParameterQueries.WT);
		
		// Assert
		assertThat(t0.getTime().getHours(), equalTo(12));
		assertThat(t0.getTime().getMinutes(), equalTo(34));
		assertThat(t0.getRuleNumber().intValue(), equalTo(3));
		assertThat(t0.getType(), equalTo(LimitationType.ABSOLUTE_TIME));
	}
	
	@Test
	public void values_mmmm() {
		// Arrange
		final Parameter p0 = makeParameter("wt", "1234");
		
		// Act
		final RunConditionWatchLimitTime t0 = p0.query(InternalParameterQueries.WT);
		
		// Assert
		assertThat(t0.getTime().getHours(), equalTo(20));
		assertThat(t0.getTime().getMinutes(), equalTo(34));
		assertThat(t0.getRuleNumber().intValue(), equalTo(1));
		assertThat(t0.getType(), equalTo(LimitationType.RELATIVE_TIME));
	}
	
	@Test
	public void values_2_mmmm() {
		// Arrange
		final Parameter p0 = makeParameter("wt", "3", "1234");
		
		// Act
		final RunConditionWatchLimitTime t0 = p0.query(InternalParameterQueries.WT);
		
		// Assert
		assertThat(t0.getTime().getHours(), equalTo(20));
		assertThat(t0.getTime().getMinutes(), equalTo(34));
		assertThat(t0.getRuleNumber().intValue(), equalTo(3));
		assertThat(t0.getType(), equalTo(LimitationType.RELATIVE_TIME));
	}
}
