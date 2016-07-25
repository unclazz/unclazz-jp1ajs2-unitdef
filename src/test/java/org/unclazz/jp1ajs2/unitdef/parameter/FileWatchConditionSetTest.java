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
		FileWatchConditionFlag.valueOfCode("x");
		
		// Assert
	}

	@Test
	public void valueOfCode_whenNullValueSpecified_throwsException () {
		// Arrange
		expected.expect(IllegalArgumentException.class);
		
		// Act
		FileWatchConditionFlag.valueOfCode(null);
		
		// Assert
	}

	@Test
	public void valueOfCode_whenValidValueSpecified_returnsInstance () {
		// Arrange
		
		// Act
		final FileWatchConditionFlag r = FileWatchConditionFlag.valueOfCode("s");
		
		// Assert
		assertThat(r, equalTo(FileWatchConditionFlag.SIZE));
	}

	@Test
	public void valueOfCodes_whenEmptyStringSpecified_returnsEmptyList () {
		// Arrange
		
		// Act
		final List<FileWatchConditionFlag> r = FileWatchConditionFlag.valueOfCodes("");
		
		// Assert
		assertTrue(r.isEmpty());
	}

	@Test
	public void valueOfCodes_whenNullValueSpecified_throwsException () {
		// Arrange
		expected.expect(NullPointerException.class);
		
		// Act
		FileWatchConditionFlag.valueOfCodes(null);
		
		// Assert
	}

	@Test
	public void valueOfCodes_whenValidValueSpecified_returnsNotEmptyList () {
		// Arrange
		
		// Act
		final List<FileWatchConditionFlag> r = FileWatchConditionFlag.valueOfCodes("c:s");
		
		// Assert
		assertTrue(r.contains(FileWatchConditionFlag.CREATE));
		assertTrue(r.contains(FileWatchConditionFlag.SIZE));
	}
}
