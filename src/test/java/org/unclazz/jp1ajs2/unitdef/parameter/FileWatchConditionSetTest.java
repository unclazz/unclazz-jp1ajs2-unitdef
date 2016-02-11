package org.unclazz.jp1ajs2.unitdef.parameter;

import static org.junit.Assert.*;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class FileWatchConditionSetTest {
	
	@Rule
	public final ExpectedException expected = ExpectedException.none();

	@Test
	public void valueOfCode_whenInvalidCodeSpecified_throwsException () {
		// Arrange
		expected.expect(IllegalArgumentException.class);
		
		// Act
		FileWatchingCondition.valueOfCode("x");
		
		// Assert
	}

	@Test
	public void valueOfCode_whenNullValueSpecified_throwsException () {
		// Arrange
		expected.expect(IllegalArgumentException.class);
		
		// Act
		FileWatchingCondition.valueOfCode(null);
		
		// Assert
	}

	@Test
	public void valueOfCode_whenValidValueSpecified_returnsInstance () {
		// Arrange
		
		// Act
		final FileWatchingCondition r = FileWatchingCondition.valueOfCode("s");
		
		// Assert
		assertThat(r, equalTo(FileWatchingCondition.SIZE));
	}

	@Test
	public void valueOfCodes_whenEmptyStringSpecified_returnsEmptyList () {
		// Arrange
		
		// Act
		final List<FileWatchingCondition> r = FileWatchingCondition.valueOfCodes("");
		
		// Assert
		assertTrue(r.isEmpty());
	}

	@Test
	public void valueOfCodes_whenNullValueSpecified_throwsException () {
		// Arrange
		expected.expect(NullPointerException.class);
		
		// Act
		FileWatchingCondition.valueOfCodes(null);
		
		// Assert
	}

	@Test
	public void valueOfCodes_whenValidValueSpecified_returnsNotEmptyList () {
		// Arrange
		
		// Act
		final List<FileWatchingCondition> r = FileWatchingCondition.valueOfCodes("c:s");
		
		// Assert
		assertTrue(r.contains(FileWatchingCondition.CREATE));
		assertTrue(r.contains(FileWatchingCondition.SIZE));
	}
}
