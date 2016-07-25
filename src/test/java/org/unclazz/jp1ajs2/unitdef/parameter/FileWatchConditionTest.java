package org.unclazz.jp1ajs2.unitdef.parameter;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class FileWatchConditionTest {
	
	@Rule
	public final ExpectedException expected = ExpectedException.none();

	@Test
	public void of_whenEmptyArraySpecified_returnsInstanceConstainsCREATE () {
		// Arrange
		
		// Act
		final FileWatchCondition r = FileWatchCondition.of();
		
		// Assert
		assertTrue(r.contains(FileWatchConditionFlag.CREATE));
		assertThat(r.serialize().toString(), equalTo("c"));
	}

	@Test
	public void of_whenBothSIZEAndMODIFYSpecified_throwsException () {
		// Arrange
		expected.expect(IllegalArgumentException.class);
		
		// Act
		FileWatchCondition.
				of(FileWatchConditionFlag.SIZE, FileWatchConditionFlag.MODIFY);
		
		// Assert
	}

	@Test
	public void of_whenDELETEAndMODIFYHaveBeenSet_returnsInstanceContains3Elements () {
		// Arrange
		
		// Act
		final FileWatchCondition r = FileWatchCondition.
				of(FileWatchConditionFlag.DELETE, FileWatchConditionFlag.MODIFY);
		
		// Assert
		assertTrue(r.contains(FileWatchConditionFlag.CREATE));
		assertTrue(r.contains(FileWatchConditionFlag.DELETE));
		assertTrue(r.contains(FileWatchConditionFlag.MODIFY));
		assertThat(r.serialize().toString(), equalTo("c:d:m"));
	}

	@Test
	public void add_returnsNewInstanceAndDoesNotModifyOriginalInstance () {
		// Arrange
		final FileWatchCondition r = FileWatchCondition.of();
		
		// Act
		final FileWatchCondition r2 = r.add(FileWatchConditionFlag.DELETE);
		
		// Assert
		assertTrue(r != r2);
		assertThat(r.serialize().toString(), equalTo("c"));
		assertThat(r2.serialize().toString(), equalTo("c:d"));
	}

	@Test
	public void remove_returnsNewInstanceAndDoesNotModifyOriginalInstance () {
		// Arrange
		final FileWatchCondition r = FileWatchCondition.of(FileWatchConditionFlag.DELETE);
		
		// Act
		final FileWatchCondition r2 = r.remove(FileWatchConditionFlag.DELETE);
		
		// Assert
		assertTrue(r != r2);
		assertThat(r.serialize().toString(), equalTo("c:d"));
		assertThat(r2.serialize().toString(), equalTo("c"));
	}
}
