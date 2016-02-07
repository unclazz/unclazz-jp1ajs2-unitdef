package org.unclazz.jp1ajs2.unitdef.builder;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.unclazz.jp1ajs2.unitdef.Parameter;

public class ParameterBuilderTest {

	@Rule
	public final ExpectedException expected = ExpectedException.none();
	
	@Test
	public void build_whenParamNameNoSpecified_throwsException() {
		// Arrange
		final ParameterBuilder b = Builders.parameter();
		b.addRawCharSequence("foo");
		expected.expect(NullPointerException.class);
		
		// Act
		b.build();
		
		// Assert
	}
	
	@Test
	public void build_whenParamNameIsEmpty_throwsException() {
		// Arrange
		final ParameterBuilder b = Builders.parameter();
		b.setName("").addRawCharSequence("foo");
		expected.expect(IllegalArgumentException.class);
		
		// Act
		b.build();
		
		// Assert
	}
	
	@Test
	public void build_whenParamNameIsNull_throwsException() {
		// Arrange
		final ParameterBuilder b = Builders.parameter();
		b.setName(null).addRawCharSequence("foo");
		expected.expect(NullPointerException.class);
		
		// Act
		b.build();
		
		// Assert
	}
	
	@Test
	public void build_whenParamValuesIsEmpty_throwsException() {
		// Arrange
		final ParameterBuilder b = Builders.parameter();
		b.setName("foo");
		expected.expect(IllegalArgumentException.class);
		
		// Act
		b.build();
		
		// Assert
	}
	
	@Test
	public void build_whenParamNameAndRawCharSequenceValueHaveBeenSet_returnParamInstance() {
		// Arrange
		final ParameterBuilder b = Builders.parameter();
		b.setName("foo").addRawCharSequence("bar");
		
		// Act
		final Parameter p = b.build();
		
		// Assert
		assertThat(p.toString(), equalTo("foo=bar"));
	}
	
	@Test
	public void build_whenParamNameAndQuotedValueHaveBeenSet_returnParamInstance() {
		// Arrange
		final ParameterBuilder b = Builders.parameter();
		b.setName("foo").addQuoted("bar \"baz\"");
		
		// Act
		final Parameter p = b.build();
		
		// Assert
		assertThat(p.toString(), equalTo("foo=\"bar #\"baz#\"\""));
	}
}
