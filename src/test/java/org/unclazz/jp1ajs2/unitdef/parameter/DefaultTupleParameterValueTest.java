package org.unclazz.jp1ajs2.unitdef.parameter;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.unclazz.jp1ajs2.unitdef.Tuple;
import org.unclazz.jp1ajs2.unitdef.builder.Builders;
import org.unclazz.jp1ajs2.unitdef.builder.TupleParameterValueBuilder;

public class DefaultTupleParameterValueTest {

	@Rule
	public final ExpectedException expected = ExpectedException.none();
	
	@Test
	public void isEmpty_whenInstanceMadeOf0Entry_returnsTrue() {
		// Arrange
		final TupleParameterValueBuilder b = Builders.tupleParameterValue();
		
		// Act
		final Tuple t = b.build();
		
		// Assert
		assertTrue(t.isEmpty());
	}
	
	@Test
	public void isEmpty_whenInstanceMadeOf1Entry_returnsFalse() {
		// Arrange
		final TupleParameterValueBuilder b = Builders.tupleParameterValue();
		b.add("foo");
		
		// Act
		final Tuple t = b.build();
		
		// Assert
		assertFalse(t.isEmpty());
	}
	
	@Test
	public void get0_whenInstanceMadeOf0Entry_throwsException() {
		// Arrange
		final TupleParameterValueBuilder b = Builders.tupleParameterValue();
		final Tuple t = b.build();
		expected.expect(IndexOutOfBoundsException.class);
		
		// Act
		t.get(0);
		
		// Assert
	}
	
	@Test
	public void get0_whenInstanceMadeOf1Entry_returnsEntry0Value() {
		// Arrange
		final TupleParameterValueBuilder b = Builders.tupleParameterValue();
		final Tuple t = b.add("foo").build();
		
		// Act
		final CharSequence v = t.get(0);
		
		// Assert
		assertThat(v.toString(), equalTo("foo"));
	}
	
	@Test
	public void get1_whenInstanceMadeOf2Entry_returnsEntry1Value() {
		// Arrange
		final TupleParameterValueBuilder b = Builders.tupleParameterValue();
		final Tuple t = b.add("foo").add("bar").build();
		
		// Act
		final CharSequence v = t.get(1);
		
		// Assert
		assertThat(v.toString(), equalTo("bar"));
	}
	
	@Test
	public void get1_whenInstanceMadeOf1Entry_throwsException() {
		// Arrange
		final TupleParameterValueBuilder b = Builders.tupleParameterValue();
		final Tuple t = b.add("foo").build();
		expected.expect(IndexOutOfBoundsException.class);
		
		// Act
		t.get(1);
		
		// Assert
	}
	
	@Test
	public void getFoo_whenInstanceMadeOfBarEntry_throwsException() {
		// Arrange
		final TupleParameterValueBuilder b = Builders.tupleParameterValue();
		final Tuple t = b.add("bar", "baz").build();
		expected.expect(NoSuchElementException.class);
		
		// Act
		t.get("foo");
		
		// Assert
	}
	
	@Test
	public void getFoo_whenInstanceMadeOfFooEntry_returnsFooEntryValue() {
		// Arrange
		final TupleParameterValueBuilder b = Builders.tupleParameterValue();
		final Tuple t = b.add("foo", "bar").build();
		
		// Act
		final CharSequence v = t.get("foo");
		
		// Assert
		assertThat(v.toString(), equalTo("bar"));
	}
}
