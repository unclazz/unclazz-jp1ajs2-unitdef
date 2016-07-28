package org.unclazz.jp1ajs2.unitdef.query;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.Tuple;
import org.unclazz.jp1ajs2.unitdef.builder.Builders;
import org.unclazz.jp1ajs2.unitdef.builder.ParameterBuilder;
import org.unclazz.jp1ajs2.unitdef.parser.TupleParser;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionCycle;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionCycle.CycleUnit;
import org.unclazz.jp1ajs2.unitdef.query.InternalParameterQueries;

public class InternalParameterQueriesCYTest {
	
	private Parameter makeParameter(String name, String... values) {
		final ParameterBuilder b = Builders.parameter().setName(name);
		for (final String value : values) {
			if (value.charAt(0) == '(' && value.charAt(value.length() - 1) == ')') {
				b.addTuple(tuple(value));
			} else {
				b.addRawCharSequence(value);
			}
		}
		return b.build();
	}
	
	private Tuple tuple(CharSequence cs) {
		return new TupleParser().parse(cs).get();
	}
	
	@Test
	public void getCycleUnit_returnsInstanceOfCycleUnitEnum() {
		// Arrange
		final Parameter p0 = makeParameter("ey", "(1,y)");
		final Parameter p1 = makeParameter("ey", "(20,m)");
		final Parameter p2 = makeParameter("ey", "0", "(0,w)");
		final Parameter p3 = makeParameter("ey", "10", "(1,d)");
		
		// Act
		final ExecutionCycle r0 = p0.query(InternalParameterQueries.CY);
		final ExecutionCycle r1 = p1.query(InternalParameterQueries.CY);
		final ExecutionCycle r2 = p2.query(InternalParameterQueries.CY);
		final ExecutionCycle r3 = p3.query(InternalParameterQueries.CY);
		
		// Assert
		assertThat(r0.getCycleUnit(), equalTo(CycleUnit.YEAR));
		assertThat(r1.getCycleUnit(), equalTo(CycleUnit.MONTH));
		assertThat(r2.getCycleUnit(), equalTo(CycleUnit.WEEK));
		assertThat(r3.getCycleUnit(), equalTo(CycleUnit.DAY));
	}
	
	@Test
	public void getInterval_returnsIntAsIntervalInGivenCycleUnit() {
		// Arrange
		final Parameter p0 = makeParameter("ey", "(1,y)");
		final Parameter p1 = makeParameter("ey", "(20,m)");
		final Parameter p2 = makeParameter("ey", "0", "(0,w)");
		final Parameter p3 = makeParameter("ey", "10", "(1,d)");
		
		// Act
		final ExecutionCycle r0 = p0.query(InternalParameterQueries.CY);
		final ExecutionCycle r1 = p1.query(InternalParameterQueries.CY);
		final ExecutionCycle r2 = p2.query(InternalParameterQueries.CY);
		final ExecutionCycle r3 = p3.query(InternalParameterQueries.CY);
		
		// Assert
		assertThat(r0.getInterval(), equalTo(1));
		assertThat(r1.getInterval(), equalTo(20));
		assertThat(r2.getInterval(), equalTo(0));
		assertThat(r3.getInterval(), equalTo(1));
	}
	
	@Test
	public void getRuleNumber_returnsRuleNumber() {
		// Arrange
		final Parameter p0 = makeParameter("ey", "(1,y)");
		final Parameter p1 = makeParameter("ey", "(20,m)");
		final Parameter p2 = makeParameter("ey", "0", "(0,w)");
		final Parameter p3 = makeParameter("ey", "10", "(1,d)");
		
		// Act
		final ExecutionCycle r0 = p0.query(InternalParameterQueries.CY);
		final ExecutionCycle r1 = p1.query(InternalParameterQueries.CY);
		final ExecutionCycle r2 = p2.query(InternalParameterQueries.CY);
		final ExecutionCycle r3 = p3.query(InternalParameterQueries.CY);
		
		// Assert
		assertThat(r0.getRuleNumber().intValue(), equalTo(1));
		assertThat(r1.getRuleNumber().intValue(), equalTo(1));
		assertThat(r2.getRuleNumber().intValue(), equalTo(0));
		assertThat(r3.getRuleNumber().intValue(), equalTo(10));
	}
}
