package org.unclazz.jp1ajs2.unitdef.query;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.builder.Builders;
import org.unclazz.jp1ajs2.unitdef.builder.ParameterBuilder;
import org.unclazz.jp1ajs2.unitdef.parameter.LinkedRuleNumber;
import org.unclazz.jp1ajs2.unitdef.query.InternalParameterQueries;

public class InternalParameterQueriesLNTest {
	
	private Parameter makeParameter(String name, String... values) {
		final ParameterBuilder b = Builders.parameter().setName(name);
		for (final String value : values) {
			b.addRawCharSequence(value);
		}
		return b.build();
	}

	@Test
	public void getRuleNumber_returnsInstanceOfRuleNumber() {
		// Arrange
		final Parameter p0 = makeParameter("ln", "0");
		final Parameter p1 = makeParameter("ln", "1");
		final Parameter p2 = makeParameter("ln", "10");
		final Parameter p3 = makeParameter("ln", "0", "1");
		final Parameter p4 = makeParameter("ln", "1", "10");
		final Parameter p5 = makeParameter("ln", "20", "10");
		
		// Act
		final LinkedRuleNumber r0 = p0.query(InternalParameterQueries.LN);
		final LinkedRuleNumber r1 = p1.query(InternalParameterQueries.LN);
		final LinkedRuleNumber r2 = p2.query(InternalParameterQueries.LN);
		final LinkedRuleNumber r3 = p3.query(InternalParameterQueries.LN);
		final LinkedRuleNumber r4 = p4.query(InternalParameterQueries.LN);
		final LinkedRuleNumber r5 = p5.query(InternalParameterQueries.LN);
		
		// Assert
		assertThat(r0.getRuleNumber().intValue(), equalTo(1));
		assertThat(r1.getRuleNumber().intValue(), equalTo(1));
		assertThat(r2.getRuleNumber().intValue(), equalTo(1));
		assertThat(r3.getRuleNumber().intValue(), equalTo(0));
		assertThat(r4.getRuleNumber().intValue(), equalTo(1));
		assertThat(r5.getRuleNumber().intValue(), equalTo(20));
	}
	@Test
	public void getLinkedRuleNumber_returnsInstanceOfRuleNumber() {
		// Arrange
		final Parameter p0 = makeParameter("ln", "0");
		final Parameter p1 = makeParameter("ln", "1");
		final Parameter p2 = makeParameter("ln", "10");
		final Parameter p3 = makeParameter("ln", "0", "1");
		final Parameter p4 = makeParameter("ln", "1", "10");
		final Parameter p5 = makeParameter("ln", "20", "10");
		
		// Act
		final LinkedRuleNumber r0 = p0.query(InternalParameterQueries.LN);
		final LinkedRuleNumber r1 = p1.query(InternalParameterQueries.LN);
		final LinkedRuleNumber r2 = p2.query(InternalParameterQueries.LN);
		final LinkedRuleNumber r3 = p3.query(InternalParameterQueries.LN);
		final LinkedRuleNumber r4 = p4.query(InternalParameterQueries.LN);
		final LinkedRuleNumber r5 = p5.query(InternalParameterQueries.LN);
		
		// Assert
		assertThat(r0.getLinkedRuleNumber().intValue(), equalTo(0));
		assertThat(r1.getLinkedRuleNumber().intValue(), equalTo(1));
		assertThat(r2.getLinkedRuleNumber().intValue(), equalTo(10));
		assertThat(r3.getLinkedRuleNumber().intValue(), equalTo(1));
		assertThat(r4.getLinkedRuleNumber().intValue(), equalTo(10));
		assertThat(r5.getLinkedRuleNumber().intValue(), equalTo(10));
	}
}
