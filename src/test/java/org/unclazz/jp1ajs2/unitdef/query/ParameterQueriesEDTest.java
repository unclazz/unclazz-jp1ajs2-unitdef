package org.unclazz.jp1ajs2.unitdef.query;

import static org.junit.Assert.*;

import java.util.Calendar;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.builder.Builders;
import org.unclazz.jp1ajs2.unitdef.builder.ParameterBuilder;
import org.unclazz.jp1ajs2.unitdef.parameter.EndDate;
import org.unclazz.jp1ajs2.unitdef.query.InternalParameterQueries;

public class ParameterQueriesEDTest {
	private Parameter makeParameter(String name, String... values) {
		final ParameterBuilder b = Builders.parameter().setName(name);
		for (final String value : values) {
			b.addRawCharSequence(value);
		}
		return b.build();
	}
	
	@Test
	public void values_yyyyMd() {
		// Arrange
		final Parameter p0 = makeParameter("wd", "2016/7/1");
		
		// Act
		final EndDate r0 = p0.query(InternalParameterQueries.ED);
		final Calendar c0 = Calendar.getInstance();
		c0.setTime(r0.toDate());
		
		// Assert
		assertThat(r0.getYear(), equalTo(2016));
		assertThat(r0.getMonth(), equalTo(7));
		assertThat(r0.getDayOfMonth(), equalTo(1));
		assertThat(c0.get(Calendar.YEAR), equalTo(2016));
		assertThat(c0.get(Calendar.MONTH), equalTo(6));
		assertThat(c0.get(Calendar.DAY_OF_MONTH), equalTo(1));
		assertThat(c0.get(Calendar.HOUR_OF_DAY), equalTo(0));
		assertThat(c0.get(Calendar.MINUTE), equalTo(0));
		assertThat(c0.get(Calendar.SECOND), equalTo(0));
		assertThat(c0.get(Calendar.MILLISECOND), equalTo(0));
	}
	
	@Test
	public void values_yyyyMMdd() {
		// Arrange
		final Parameter p0 = makeParameter("wd", "2016/10/11");
		
		// Act
		final EndDate r0 = p0.query(InternalParameterQueries.ED);
		final Calendar c0 = Calendar.getInstance();
		c0.setTime(r0.toDate());
		
		// Assert
		assertThat(r0.getYear(), equalTo(2016));
		assertThat(r0.getMonth(), equalTo(10));
		assertThat(r0.getDayOfMonth(), equalTo(11));
		assertThat(c0.get(Calendar.YEAR), equalTo(2016));
		assertThat(c0.get(Calendar.MONTH), equalTo(9));
		assertThat(c0.get(Calendar.DAY_OF_MONTH), equalTo(11));
		assertThat(c0.get(Calendar.HOUR_OF_DAY), equalTo(0));
		assertThat(c0.get(Calendar.MINUTE), equalTo(0));
		assertThat(c0.get(Calendar.SECOND), equalTo(0));
		assertThat(c0.get(Calendar.MILLISECOND), equalTo(0));
	}
}
