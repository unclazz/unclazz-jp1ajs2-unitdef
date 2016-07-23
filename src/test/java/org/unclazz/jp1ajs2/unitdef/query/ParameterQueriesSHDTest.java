package org.unclazz.jp1ajs2.unitdef.query;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.builder.Builders;
import org.unclazz.jp1ajs2.unitdef.builder.ParameterBuilder;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateCompensationDeadline;
import org.unclazz.jp1ajs2.unitdef.query.ParameterQueries;

public class ParameterQueriesSHDTest {
	private Parameter makeParameter(String name, String... values) {
		final ParameterBuilder b = Builders.parameter().setName(name);
		for (final String value : values) {
			b.addRawCharSequence(value);
		}
		return b.build();
	}
	
	@Test
	public void values_5() {
		// Arrange
		final Parameter p0 = makeParameter("shd", "5");
		
		// Act
		final StartDateCompensationDeadline r0 = p0.query(ParameterQueries.SHD);
		
		// Assert
		assertThat(r0.getDeadlineDays(), equalTo(5));
		assertThat(r0.getRuleNumber().intValue(), equalTo(1));
	}
	
	@Test
	public void values_5_6() {
		// Arrange
		final Parameter p0 = makeParameter("shd", "5", "6");
		
		// Act
		final StartDateCompensationDeadline r0 = p0.query(ParameterQueries.SHD);
		
		// Assert
		assertThat(r0.getDeadlineDays(), equalTo(6));
		assertThat(r0.getRuleNumber().intValue(), equalTo(5));
	}
}
