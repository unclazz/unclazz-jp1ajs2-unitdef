package org.unclazz.jp1ajs2.unitdef.builder;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.unclazz.jp1ajs2.unitdef.Attributes;
import org.unclazz.jp1ajs2.unitdef.parameter.AnteroposteriorRelationship;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitConnectionType;

public class DefaultAnteroposteriorRelationshipTest {

	@Rule
	public final ExpectedException expected = ExpectedException.none();
	
	@Test
	public void build_whenUnitNameNoSpecified_throwsException() {
		// Arrange
		final AnteroposteriorRelationshipBuilder b = Builders.parameterAR();
		b.setConnectionType(UnitConnectionType.SEQUENTIAL);
		expected.expect(NullPointerException.class);
		
		// Act
		b.build();
		
		// Assert
	}
	
	@Test
	public void build_whenUnitNameIsEmpty_throwsException() {
		// Arrange
		final AnteroposteriorRelationshipBuilder b = Builders.parameterAR();
		b.setFromUnitName("").setToUnitName("")
		.setConnectionType(UnitConnectionType.SEQUENTIAL);
		expected.expect(IllegalArgumentException.class);
		
		// Act
		b.build();
		
		// Assert
	}
	
	@Test
	public void build_whenBothUnitNameHaveBeenSet_returnsParameterInstance() {
		// Arrange
		final AnteroposteriorRelationshipBuilder b = Builders.parameterAR();
		b.setFromUnitName("foo").setToUnitName("bar")
		.setConnectionType(UnitConnectionType.CONDITIONAL);
		
		// Act
		final AnteroposteriorRelationship r = b.build();
		
		// Assert
		assertThat(r.getConnectionType(), equalTo(UnitConnectionType.CONDITIONAL));
		assertThat(r.getFromUnitName(), equalTo("foo"));
		assertThat(r.getToUnitName(), equalTo("bar"));
	}
	
	@Test
	public void build_whenConnectionTypeNoSpecified_returnsSequentialTypedInstance() {
		// Arrange
		final AnteroposteriorRelationshipBuilder b = Builders.parameterAR();
		b.setFromUnitName("foo").setToUnitName("bar");
		
		// Act
		final AnteroposteriorRelationship r = b.build();
		
		// Assert
		assertThat(r.getConnectionType(), equalTo(UnitConnectionType.SEQUENTIAL));
		assertThat(r.getFromUnitName(), equalTo("foo"));
		assertThat(r.getToUnitName(), equalTo("bar"));
	}
}
