package org.unclazz.jp1ajs2.unitdef.builder;

import static org.junit.Assert.*;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.Unit;
import static org.unclazz.jp1ajs2.unitdef.builder.Builders.*;

public class UnitBuilderTest {

	@Rule
	public final ExpectedException expected = ExpectedException.none();
	
	@Test
	public void setFullQualifiedName_whenArgIsNull_throwsException() {
		// Arrange
		final CharSequence cs = null;
		expected.expect(NullPointerException.class);
		
		// Act
		unit().setFullQualifiedName(cs);
		
		// Assert
	}
	
	@Test
	public void setFullQualifiedName_whenArgIsEmptyString_throwsException() {
		// Arrange
		expected.expect(IllegalArgumentException.class);
		
		// Act
		unit().setFullQualifiedName("");
		
		// Assert
	}
	
	@Test
	public void setFullQualifiedName_whenArgIsEmptyArray_throwsException() {
		// Arrange
		expected.expect(IllegalArgumentException.class);
		
		// Act
		unit().setFullQualifiedName(new String[0]);
		
		// Assert
	}
	
	@Test
	public void setFullQualifiedName_whenArgIsEmptyList_throwsException() {
		// Arrange
		expected.expect(IllegalArgumentException.class);
		
		// Act
		unit().setFullQualifiedName(Collections.<CharSequence>emptyList());
		
		// Assert
	}
	
	@Test
	public void setAttributes_whenArgIsNull_throwsException() {
		// Arrange
		expected.expect(NullPointerException.class);
		
		// Act
		unit().setAttributes(null);
		
		// Assert
	}
	
	@Test
	public void addParameter_whenArgIsNull_throwsException() {
		// Arrange
		expected.expect(NullPointerException.class);
		
		// Act
		unit().addParameter(null);
		
		// Assert
	}
	
	@Test
	public void addParameters_whenArgIsNull_throwsException() {
		// Arrange
		expected.expect(NullPointerException.class);
		
		// Act
		unit().addParameters(new Parameter[]{null});
		
		// Assert
	}
	
	@Test
	public void addSubUnit_whenArgIsNull_throwsException() {
		// Arrange
		expected.expect(NullPointerException.class);
		
		// Act
		unit().addSubUnit(null);
		// Assert
	}
	
	@Test
	public void addSubUnits_whenArgIsNull_throwsException() {
		// Arrange
		expected.expect(NullPointerException.class);
		
		// Act
		unit().addSubUnits(new Unit[]{null});
		// Assert
	}
	
	@Test
	public void build_whenAttributesIsNotSpecified_throwsException() {
		// Arrange
		final UnitBuilder b = unit()
		.setFullQualifiedName("FOO")
		.addParameter(parameter()
				.setName("ty")
				.addRawCharSequence("g")
				.build());
		expected.expect(NullPointerException.class);
		
		// Act
		b.build();
		
		// Assert
	}
	
	@Test
	public void build_whenNoParameterSpecified_throwsException() {
		// Arrange
		final UnitBuilder b = unit()
		.setFullQualifiedName("FOO")
		.setAttributes(attributes().setName("FOO").build());
		expected.expect(IllegalArgumentException.class);
		
		// Act
		b.build();
		
		// Assert
	}
	
	@Test
	public void build_whenParameterTYIsNotSpecified_throwsException() {
		// Arrange
		final UnitBuilder b = unit()
		.setFullQualifiedName("FOO")
		.setAttributes(attributes().setName("FOO").build())
		.addParameter(parameter().setName("cm").addRawCharSequence("comment").build());
		expected.expect(IllegalArgumentException.class);
		
		// Act
		b.build();
		
		// Assert
	}
	
	@Test
	public void build_whenNoConsistencyOfUnitNames_throwsException() {
		// Arrange
		final UnitBuilder b = unit()
		.setFullQualifiedName("FOO")
		.setAttributes(attributes().setName("BAR").build())
		.addParameter(parameter().setName("ty").addRawCharSequence("g").build());
		expected.expect(IllegalArgumentException.class);
		
		// Act
		b.build();
		
		// Assert
	}
	
	@Test
	public void build_whenOtherwise_returnsInstance() {
		// Arrange
		final UnitBuilder b = unit()
		.setFullQualifiedName("FOO")
		.setAttributes(attributes().setName("FOO").build())
		.addParameter(parameter().setName("ty").addRawCharSequence("g").build());
		
		// Act
		final Unit r = b.build();
		
		// Assert
		assertThat(r.getName(), equalTo("FOO"));
	}
}
