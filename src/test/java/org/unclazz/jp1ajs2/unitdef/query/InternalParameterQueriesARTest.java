package org.unclazz.jp1ajs2.unitdef.query;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.Tuple;
import org.unclazz.jp1ajs2.unitdef.builder.Builders;
import org.unclazz.jp1ajs2.unitdef.builder.ParameterBuilder;
import org.unclazz.jp1ajs2.unitdef.parameter.AnteroposteriorRelationship;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitConnectionType;
import org.unclazz.jp1ajs2.unitdef.parser.TupleParser;
import org.unclazz.jp1ajs2.unitdef.query.InternalParameterQueries;

public class InternalParameterQueriesARTest {
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
	public void getConnectionType_returnsInstanceOfUnitConnectionTypeEnum() {
		// Arrange
		final Parameter p0 = makeParameter("ar", "(f=XXXX0001,t=XXXX0002)");
		final Parameter p1 = makeParameter("ar", "(f=XXXX0002,t=XXXX0001,con)");
		final Parameter p2 = makeParameter("ar", "(f=XXXX0003,t=XXXX0002,seq)");
		
		// Act
		final AnteroposteriorRelationship r0 = p0.query(InternalParameterQueries.AR);
		final AnteroposteriorRelationship r1 = p1.query(InternalParameterQueries.AR);
		final AnteroposteriorRelationship r2 = p2.query(InternalParameterQueries.AR);
		
		// Assert
		assertThat(r0.getConnectionType(), equalTo(UnitConnectionType.SEQUENTIAL));
		assertThat(r1.getConnectionType(), equalTo(UnitConnectionType.CONDITIONAL));
		assertThat(r2.getConnectionType(), equalTo(UnitConnectionType.SEQUENTIAL));
	}
	
	@Test
	public void getFromUnitName_returnsUnitNameOfFrom() {
		// Arrange
		final Parameter p0 = makeParameter("ar", "(f=XXXX0001,t=XXXX0002)");
		final Parameter p1 = makeParameter("ar", "(f=XXXX0002,t=XXXX0001,con)");
		final Parameter p2 = makeParameter("ar", "(f=XXXX0003,t=XXXX0002,seq)");
		
		// Act
		final AnteroposteriorRelationship r0 = p0.query(InternalParameterQueries.AR);
		final AnteroposteriorRelationship r1 = p1.query(InternalParameterQueries.AR);
		final AnteroposteriorRelationship r2 = p2.query(InternalParameterQueries.AR);
		
		// Assert
		assertThat(r0.getFromUnitName(), equalTo("XXXX0001"));
		assertThat(r1.getFromUnitName(), equalTo("XXXX0002"));
		assertThat(r2.getFromUnitName(), equalTo("XXXX0003"));
	}
	
	@Test
	public void getToUnitName_returnsUnitNameOfTo() {
		// Arrange
		final Parameter p0 = makeParameter("ar", "(f=XXXX0001,t=XXXX0002)");
		final Parameter p1 = makeParameter("ar", "(f=XXXX0002,t=XXXX0001,con)");
		final Parameter p2 = makeParameter("ar", "(f=XXXX0003,t=XXXX0002,seq)");
		
		// Act
		final AnteroposteriorRelationship r0 = p0.query(InternalParameterQueries.AR);
		final AnteroposteriorRelationship r1 = p1.query(InternalParameterQueries.AR);
		final AnteroposteriorRelationship r2 = p2.query(InternalParameterQueries.AR);
		
		// Assert
		assertThat(r0.getToUnitName(), equalTo("XXXX0002"));
		assertThat(r1.getToUnitName(), equalTo("XXXX0001"));
		assertThat(r2.getToUnitName(), equalTo("XXXX0002"));
	}
}
