package org.unclazz.jp1ajs2.unitdef.query;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.builder.Builders;
import org.unclazz.jp1ajs2.unitdef.builder.ParameterBuilder;
import org.unclazz.jp1ajs2.unitdef.parameter.FixedDuration;
import org.unclazz.jp1ajs2.unitdef.query.InternalParameterQueries;

public class InternalParameterQueriesFDTest {
	private Parameter makeParameter(String name, String... values) {
		final ParameterBuilder b = Builders.parameter().setName(name);
		for (final String value : values) {
			b.addRawCharSequence(value);
		}
		return b.build();
	}
	
	@Test
	public void intValue_returnsIntAsDuration() {
		// Arrange
		final Parameter p0 = makeParameter("fd", "1");
		final Parameter p1 = makeParameter("fd", "2879");
		
		// Act
		final FixedDuration r0 = p0.query(InternalParameterQueries.FD);
		final FixedDuration r1 = p1.query(InternalParameterQueries.FD);
		
		// Assert
		assertThat(r0.intValue(), equalTo(1));
		assertThat(r1.intValue(), equalTo(2879));
	}
}
