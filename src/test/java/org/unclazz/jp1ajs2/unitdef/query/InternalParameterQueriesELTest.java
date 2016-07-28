package org.unclazz.jp1ajs2.unitdef.query;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.builder.Builders;
import org.unclazz.jp1ajs2.unitdef.builder.ParameterBuilder;
import org.unclazz.jp1ajs2.unitdef.parameter.Element;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;
import org.unclazz.jp1ajs2.unitdef.query.InternalParameterQueries;

public class InternalParameterQueriesELTest {
	private Parameter makeParameter(String name, String... values) {
		final ParameterBuilder b = Builders.parameter().setName(name);
		for (final String value : values) {
			b.addRawCharSequence(value);
		}
		return b.build();
	}
	
	@Test
	public void getUnitName_returnsParameterValueAt0() {
		// Arrange
		final Parameter p0 = makeParameter("el", "XXXX0001", "g", "+80 +48");
		
		// Act
		final Element r0 = p0.query(InternalParameterQueries.EL);
		
		// Assert
		assertThat(r0.getUnitName(), equalTo("XXXX0001"));
	}
	
	@Test
	public void getUnitType_returnsParameterValueAt1AsUnitType() {
		// Arrange
		final Parameter p0 = makeParameter("el", "XXXX0001", "g", "+80 +48");
		final Parameter p1 = makeParameter("el", "XXXX0001", "pj", "+80 +48");
		
		// Act
		final Element r0 = p0.query(InternalParameterQueries.EL);
		final Element r1 = p1.query(InternalParameterQueries.EL);
		
		// Assert
		assertThat(r0.getUnitType(), equalTo(UnitType.JOB_GROUP));
		assertThat(r1.getUnitType(), equalTo(UnitType.PC_JOB));
	}
	
	@Test
	public void getHPixel_returnsNumberOfLeftSideInParameterValueAt2() {
		// Arrange
		final Parameter p0 = makeParameter("el", "XXXX0001", "g", "+80 +48");
		
		// Act
		final Element r0 = p0.query(InternalParameterQueries.EL);
		
		// Assert
		assertThat(r0.getHPixel(), equalTo(80));
	}
	
	@Test
	public void getVPixel_returnsNumberOfRightSideInParameterValueAt2() {
		// Arrange
		final Parameter p0 = makeParameter("el", "XXXX0001", "g", "+80 +48");
		
		// Act
		final Element r0 = p0.query(InternalParameterQueries.EL);
		
		// Assert
		assertThat(r0.getVPixel(), equalTo(48));
	}
	
	@Test
	public void getXCoord_returnsNumberOfLeftSideInParameterValueAt2AsCoord() {
		// Arrange
		final Parameter p0 = makeParameter("el", "XXXX0001", "g", "+80 +48");
		final Parameter p1 = makeParameter("el", "XXXX0001", "g", "+240 +240");
		
		// Act
		final Element r0 = p0.query(InternalParameterQueries.EL);
		final Element r1 = p1.query(InternalParameterQueries.EL);
		
		// Assert
		assertThat(r0.getXCoord(), equalTo(0));
		assertThat(r1.getXCoord(), equalTo(1));
	}
	
	@Test
	public void getYCoord_returnsNumberOfRightSideInParameterValueAt2AsCoord() {
		// Arrange
		final Parameter p0 = makeParameter("el", "XXXX0001", "g", "+80 +48");
		final Parameter p1 = makeParameter("el", "XXXX0001", "g", "+240 +240");
		
		// Act
		final Element r0 = p0.query(InternalParameterQueries.EL);
		final Element r1 = p1.query(InternalParameterQueries.EL);
		
		// Assert
		assertThat(r0.getYCoord(), equalTo(0));
		assertThat(r1.getYCoord(), equalTo(2));
	}
	
}
