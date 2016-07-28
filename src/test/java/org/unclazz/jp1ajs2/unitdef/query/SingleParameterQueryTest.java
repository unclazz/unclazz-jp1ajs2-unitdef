package org.unclazz.jp1ajs2.unitdef.query;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.builder.Builders;
import org.unclazz.jp1ajs2.unitdef.builder.ParameterBuilder;

public class SingleParameterQueryTest {
	
	private Parameter makeParameter(String name, String... values) {
		final ParameterBuilder b = Builders.parameter().setName(name);
		for (final String value : values) {
			b.addRawCharSequence(value);
		}
		return b.build();
	}
	
	@Test
	public void whenValueAt_specifiesPositionOfCheckTarget() {
		// Arrange
		final Parameter p0 = makeParameter("foo", "bar", "baz");
		final Query<Parameter, Parameter> q0 = Queries.parameter()
				.whenValueAt(0).contentEquals("bar").thenAppend("hello");
		final Query<Parameter, Parameter> q1 = Queries.parameter()
				.whenValueAt(0).contentEquals("baz").thenAppend("hello");
		final Query<Parameter, Parameter> q2 = Queries.parameter()
				.whenValueAt(1).contentEquals("baz").thenAppend("hello");
		
		// Act
		final Parameter r0 = p0.query(q0);
		final Parameter r1 = p0.query(q1);
		final Parameter r2 = p0.query(q2);
		
		// Assert
		assertThat(r0.getValues().size(), equalTo(3));
		assertThat(r0.getValues().get(2).getStringValue(), equalTo("hello"));
		assertThat(r1.getValues().size(), equalTo(2));
		assertThat(r2.getValues().size(), equalTo(3));
		assertThat(r2.getValues().get(2).getStringValue(), equalTo("hello"));
	}
	
	@Test
	public void contentEquals_specifiesExpectedValueForCheckTarget() {
		// Arrange
		final Parameter p0 = makeParameter("foo", "bar", "baz");
		final Query<Parameter, Parameter> q0 = Queries.parameter()
				.whenValueAt(0).contentEquals("bar").thenAppend("hello");
		final Query<Parameter, Parameter> q1 = Queries.parameter()
				.whenValueAt(0).contentEquals("baz").contentEquals("bar").thenAppend("hello");
		final Query<Parameter, Parameter> q2 = Queries.parameter()
				.whenValueAt(0).contentEquals("bar").contentEquals("baz").thenAppend("hello");
		
		// Act
		final Parameter r0 = p0.query(q0);
		final Parameter r1 = p0.query(q1);
		final Parameter r2 = p0.query(q2);
		
		// Assert
		assertThat(r0.getValues().size(), equalTo(3));
		assertThat(r0.getValues().get(2).getStringValue(), equalTo("hello"));
		assertThat(r1.getValues().size(), equalTo(2));
		assertThat(r2.getValues().size(), equalTo(2));
	}
	
	@Test
	public void contains_specifiesExpectedValuesSubsequence() {
		// Arrange
		final Parameter p0 = makeParameter("foo", "bar", "baz");
		final Query<Parameter, Parameter> q0 = Queries.parameter()
				.whenValueAt(0).contains("ba").thenAppend("hello");
		final Query<Parameter, Parameter> q1 = Queries.parameter()
				.whenValueAt(0).contains("az").thenAppend("hello");
		final Query<Parameter, Parameter> q2 = Queries.parameter()
				.whenValueAt(1).contains("az").thenAppend("hello");
		
		// Act
		final Parameter r0 = p0.query(q0);
		final Parameter r1 = p0.query(q1);
		final Parameter r2 = p0.query(q2);
		
		// Assert
		assertThat(r0.getValues().size(), equalTo(3));
		assertThat(r0.getValues().get(2).getStringValue(), equalTo("hello"));
		assertThat(r1.getValues().size(), equalTo(2));
		assertThat(r2.getValues().size(), equalTo(3));
		assertThat(r2.getValues().get(2).getStringValue(), equalTo("hello"));
	}
	
	@Test
	public void endsWith_specifiesExpectedValuesSuffix() {
		// Arrange
		final Parameter p0 = makeParameter("foo", "bar", "baz");
		final Query<Parameter, Parameter> q0 = Queries.parameter()
				.whenValueAt(0).endsWith("ar").thenAppend("hello");
		final Query<Parameter, Parameter> q1 = Queries.parameter()
				.whenValueAt(0).endsWith("bar").thenAppend("hello");
		final Query<Parameter, Parameter> q2 = Queries.parameter()
				.whenValueAt(0).endsWith("a").thenAppend("hello");
		
		// Act
		final Parameter r0 = p0.query(q0);
		final Parameter r1 = p0.query(q1);
		final Parameter r2 = p0.query(q2);
		
		// Assert
		assertThat(r0.getValues().size(), equalTo(3));
		assertThat(r0.getValues().get(2).getStringValue(), equalTo("hello"));
		assertThat(r1.getValues().size(), equalTo(3));
		assertThat(r2.getValues().size(), equalTo(2));
	}
	
	@Test
	public void startsWith_specifiesExpectedValuesSuffix() {
		// Arrange
		final Parameter p0 = makeParameter("foo", "bar", "baz");
		final Query<Parameter, Parameter> q0 = Queries.parameter()
				.whenValueAt(0).startsWith("ba").thenAppend("hello");
		final Query<Parameter, Parameter> q1 = Queries.parameter()
				.whenValueAt(0).startsWith("bar").thenAppend("hello");
		final Query<Parameter, Parameter> q2 = Queries.parameter()
				.whenValueAt(0).startsWith("a").thenAppend("hello");
		
		// Act
		final Parameter r0 = p0.query(q0);
		final Parameter r1 = p0.query(q1);
		final Parameter r2 = p0.query(q2);
		
		// Assert
		assertThat(r0.getValues().size(), equalTo(3));
		assertThat(r0.getValues().get(2).getStringValue(), equalTo("hello"));
		assertThat(r1.getValues().size(), equalTo(3));
		assertThat(r2.getValues().size(), equalTo(2));
	}
	
	@Test
	public void equalsAnyOf_specifiesOptionsOfExpectedValue() {
		// Arrange
		final Parameter p0 = makeParameter("foo", "bar", "baz");
		final Query<Parameter, Parameter> q0 = Queries.parameter()
				.whenValueAt(0).equalsAnyOf("ba", "bar").thenAppend("hello");
		final Query<Parameter, Parameter> q1 = Queries.parameter()
				.whenValueAt(0).equalsAnyOf("ba", "baz").thenAppend("hello");
		
		// Act
		final Parameter r0 = p0.query(q0);
		final Parameter r1 = p0.query(q1);
		
		// Assert
		assertThat(r0.getValues().size(), equalTo(3));
		assertThat(r0.getValues().get(2).getStringValue(), equalTo("hello"));
		assertThat(r1.getValues().size(), equalTo(2));
	}
	
	@Test
	public void consistsOfAlphabets_specifiesExpectedValuesContent() {
		// Arrange
		final Parameter p0 = makeParameter("foo", "bar", "123", "baz123");
		final Query<Parameter, Parameter> q0 = Queries.parameter()
				.whenValueAt(0).consistsOfAlphabets().thenAppend("hello");
		final Query<Parameter, Parameter> q1 = Queries.parameter()
				.whenValueAt(1).consistsOfAlphabets().thenAppend("hello");
		final Query<Parameter, Parameter> q2 = Queries.parameter()
				.whenValueAt(2).consistsOfAlphabets().thenAppend("hello");
		
		// Act
		final Parameter r0 = p0.query(q0);
		final Parameter r1 = p0.query(q1);
		final Parameter r2 = p0.query(q2);
		
		// Assert
		assertThat(r0.getValues().size(), equalTo(4));
		assertThat(r0.getValues().get(3).getStringValue(), equalTo("hello"));
		assertThat(r1.getValues().size(), equalTo(3));
		assertThat(r2.getValues().size(), equalTo(3));
	}
	
	@Test
	public void consistsOfDigits_specifiesExpectedValuesContent() {
		// Arrange
		final Parameter p0 = makeParameter("foo", "bar", "123", "baz123");
		final Query<Parameter, Parameter> q0 = Queries.parameter()
				.whenValueAt(0).consistsOfDigits().thenAppend("hello");
		final Query<Parameter, Parameter> q1 = Queries.parameter()
				.whenValueAt(1).consistsOfDigits().thenAppend("hello");
		final Query<Parameter, Parameter> q2 = Queries.parameter()
				.whenValueAt(2).consistsOfDigits().thenAppend("hello");
		
		// Act
		final Parameter r0 = p0.query(q0);
		final Parameter r1 = p0.query(q1);
		final Parameter r2 = p0.query(q2);
		
		// Assert
		assertThat(r0.getValues().size(), equalTo(3));
		assertThat(r1.getValues().size(), equalTo(4));
		assertThat(r1.getValues().get(3).getStringValue(), equalTo("hello"));
		assertThat(r2.getValues().size(), equalTo(3));
	}
	
	@Test
	public void whenValueCount_specifiesNumberOfParameterValues() {
		// Arrange
		final Parameter p0 = makeParameter("foo", "bar", "baz");
		final Query<Parameter, Parameter> q0 = Queries.parameter()
				.whenValueCount(2).thenAppend("hello");
		final Query<Parameter, Parameter> q1 = Queries.parameter()
				.whenValueCount(3).thenAppend("hello");
		
		// Act
		final Parameter r0 = p0.query(q0);
		final Parameter r1 = p0.query(q1);
		
		// Assert
		assertThat(r0.getValues().size(), equalTo(3));
		assertThat(r0.getValues().get(2).getStringValue(), equalTo("hello"));
		assertThat(r1.getValues().size(), equalTo(2));
	}
	
	@Test
	public void valueAt_specifiesPositionOfCheckTarget() {
		// Arrange
		final Parameter p0 = makeParameter("foo", "bar", "baz");
		final Query<Parameter, Parameter> q0 = Queries.parameter()
				.whenValueCount(2).valueAt(0).contentEquals("bar").thenAppend("hello");
		final Query<Parameter, Parameter> q1 = Queries.parameter()
				.whenValueCount(2).valueAt(0).contentEquals("baz").thenAppend("hello");
		final Query<Parameter, Parameter> q2 = Queries.parameter()
				.whenValueCount(2).valueAt(1).contentEquals("baz").thenAppend("hello");
		
		// Act
		final Parameter r0 = p0.query(q0);
		final Parameter r1 = p0.query(q1);
		final Parameter r2 = p0.query(q2);
		
		// Assert
		assertThat(r0.getValues().size(), equalTo(3));
		assertThat(r0.getValues().get(2).getStringValue(), equalTo("hello"));
		assertThat(r1.getValues().size(), equalTo(2));
		assertThat(r2.getValues().size(), equalTo(3));
		assertThat(r2.getValues().get(2).getStringValue(), equalTo("hello"));
	}
	
	@Test
	public void thenInsert_specifiesInsert() {
		// Arrange
		final Parameter p0 = makeParameter("foo", "bar", "baz");
		final Query<Parameter, Parameter> q0 = Queries.parameter()
				.whenValueAt(0).contentEquals("bar").thenInsert(1, "hello");
		
		// Act
		final Parameter r0 = p0.query(q0);
		
		// Assert
		assertThat(r0.getValues().size(), equalTo(3));
		assertThat(r0.getValues().get(1).getStringValue(), equalTo("hello"));
	}
	
	@Test
	public void thenPrepend_specifiesPrependOperation() {
		// Arrange
		final Parameter p0 = makeParameter("foo", "bar", "baz");
		final Query<Parameter, Parameter> q0 = Queries.parameter()
				.whenValueAt(0).contentEquals("bar").thenPrepend("hello");
		
		// Act
		final Parameter r0 = p0.query(q0);
		
		// Assert
		assertThat(r0.getValues().size(), equalTo(3));
		assertThat(r0.getValues().get(0).getStringValue(), equalTo("hello"));
	}
	
	@Test
	public void thenReplace_specifiesReplaceOperation() {
		// Arrange
		final Parameter p0 = makeParameter("foo", "bar", "baz");
		final Query<Parameter, Parameter> q0 = Queries.parameter()
				.whenValueAt(0).contentEquals("bar").thenReplace(1, "hello");
		
		// Act
		final Parameter r0 = p0.query(q0);
		
		// Assert
		assertThat(r0.getValues().size(), equalTo(2));
		assertThat(r0.getValues().get(1).getStringValue(), equalTo("hello"));
	}
}
