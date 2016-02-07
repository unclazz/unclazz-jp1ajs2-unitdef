package org.unclazz.jp1ajs2.unitdef.util;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CharSequenceUtilsTest {

	@Rule
	public final ExpectedException expected = ExpectedException.none();
	
	private CharSequence charSequence(final char... cs) {
		return new StringBuilder().append(cs);
	}
	
	@Test
	public void contentsAreEqual_whenArg0IsNull_throwsException() {
		// Arrange
		expected.expect(NullPointerException.class);
		
		// Act
		CharSequenceUtils.contentsAreEqual(null, "foo");
		
		// Assert
	}
	
	@Test
	public void contentsAreEqual_whenArg1IsNull_throwsException() {
		// Arrange
		expected.expect(NullPointerException.class);
		
		// Act
		CharSequenceUtils.contentsAreEqual("foo", null);
		
		// Assert
	}
	
	@Test
	public void contentsAreEqual_whenComparingStringWithCharSequenceHasSameContents_returnsTrue() {
		// Arrange
		
		// Act
		final boolean r = CharSequenceUtils.contentsAreEqual("foo", charSequence('f', 'o', 'o'));
		
		// Assert
		assertTrue(r);
	}
	
	@Test
	public void contentsAreEqual_whenComparingCharSequenceWithStringHasSameContents_returnsTrue() {
		// Arrange
		
		// Act
		final boolean r = CharSequenceUtils.contentsAreEqual(charSequence('f', 'o', 'o'), "foo");
		
		// Assert
		assertTrue(r);
	}
	
	@Test
	public void contentsAreEqual_whenComparingDifferentContents_returnsFalse() {
		// Arrange
		
		// Act
		final boolean r = CharSequenceUtils.contentsAreEqual("foo", "fox");
		
		// Assert
		assertFalse(r);
	}
	
	@Test
	public void startsWith_whenArg0IsNull_throwsException() {
		// Arrange
		expected.expect(NullPointerException.class);
		
		// Act
		CharSequenceUtils.startsWith(null, "foo");
		
		// Assert
	}
	
	@Test
	public void startsWith_whenArg1IsNull_throwsException() {
		// Arrange
		expected.expect(NullPointerException.class);
		
		// Act
		CharSequenceUtils.startsWith("foo", null);
		
		// Assert
	}
	
	@Test
	public void startsWith_whenArg0ShorterThanArg1_returnsFalse() {
		// Arrange
		
		// Act
		final boolean r = CharSequenceUtils.startsWith("fo", "foo");
		
		// Assert
		assertFalse(r);
	}
	
	@Test
	public void startsWith_whenDifferentContents_returnsFalse() {
		// Arrange
		
		// Act
		final boolean r = CharSequenceUtils.startsWith("foo", "fox");
		
		// Assert
		assertFalse(r);
	}
	
	@Test
	public void startsWith_whenSameContents_returnsTrue() {
		// Arrange
		
		// Act
		final boolean r = CharSequenceUtils.startsWith("foo", "foo");
		
		// Assert
		assertTrue(r);
	}
	
	@Test
	public void startsWith_whenArg0StartsWithArg1_returnsTrue() {
		// Arrange
		
		// Act
		final boolean r = CharSequenceUtils.startsWith("foox", "foo");
		
		// Assert
		assertTrue(r);
	}
	
	@Test
	public void contains_whenArg0StartsWithArg1_returnsTrue() {
		// Arrange
		
		// Act
		final boolean r = CharSequenceUtils.contains("foox", "foo");
		
		// Assert
		assertTrue(r);
	}
	
	@Test
	public void contains_whenArg0EndsWithArg1_returnsTrue() {
		// Arrange
		
		// Act
		final boolean r = CharSequenceUtils.contains("xfoo", "foo");
		
		// Assert
		assertTrue(r);
	}
	
	@Test
	public void contains_whenArg0ContainsArg1_returnsTrue() {
		// Arrange
		
		// Act
		final boolean r = CharSequenceUtils.contains("xfoox", "foo");
		
		// Assert
		assertTrue(r);
	}
	
	@Test
	public void contains_whenArg0DoesNotContainArg1_returnsTrue() {
		// Arrange
		
		// Act
		final boolean r = CharSequenceUtils.contains("xfxoox", "foo");
		
		// Assert
		assertFalse(r);
	}
}
