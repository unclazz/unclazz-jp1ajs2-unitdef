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
		final FileWatchingConditionSet r = FileWatchingConditionSet.of();
		
		// Assert
		assertTrue(r.contains(FileWatchingCondition.CREATE));
		assertThat(r.serialize().toString(), equalTo("c"));
	}

	@Test
	public void of_whenBothSIZEAndMODIFYSpecified_throwsException () {
		// Arrange
		expected.expect(IllegalArgumentException.class);
		
		// Act
		FileWatchingConditionSet.
				of(FileWatchingCondition.SIZE, FileWatchingCondition.MODIFY);
		
		// Assert
	}

	@Test
	public void of_whenDELETEAndMODIFYHaveBeenSet_returnsInstanceContains3Elements () {
		// Arrange
		
		// Act
		final FileWatchingConditionSet r = FileWatchingConditionSet.
				of(FileWatchingCondition.DELETE, FileWatchingCondition.MODIFY);
		
		// Assert
		assertTrue(r.contains(FileWatchingCondition.CREATE));
		assertTrue(r.contains(FileWatchingCondition.DELETE));
		assertTrue(r.contains(FileWatchingCondition.MODIFY));
		assertThat(r.serialize().toString(), equalTo("c:d:m"));
	}

	@Test
	public void add_returnsNewInstanceAndDoesNotModifyOriginalInstance () {
		// Arrange
		final FileWatchingConditionSet r = FileWatchingConditionSet.of();
		
		// Act
		final FileWatchingConditionSet r2 = r.add(FileWatchingCondition.DELETE);
		
		// Assert
		assertTrue(r != r2);
		assertThat(r.serialize().toString(), equalTo("c"));
		assertThat(r2.serialize().toString(), equalTo("c:d"));
	}

	@Test
	public void remove_returnsNewInstanceAndDoesNotModifyOriginalInstance () {
		// Arrange
		final FileWatchingConditionSet r = FileWatchingConditionSet.of(FileWatchingCondition.DELETE);
		
		// Act
		final FileWatchingConditionSet r2 = r.remove(FileWatchingCondition.DELETE);
		
		// Assert
		assertTrue(r != r2);
		assertThat(r.serialize().toString(), equalTo("c:d"));
		assertThat(r2.serialize().toString(), equalTo("c"));
	}
}
