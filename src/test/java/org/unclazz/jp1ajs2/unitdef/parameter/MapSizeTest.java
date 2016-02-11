package org.unclazz.jp1ajs2.unitdef.parameter;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class MapSizeTest {
	
	@Rule
	public final ExpectedException expected = ExpectedException.none();

	@Test
	public void of_whenArg0Is0_throwsException () {
		// Arrange
		expected.expect(IllegalArgumentException.class);
		
		// Act
		MapSize.of(0, 1);
		
		// Assert
	}

	@Test
	public void of_whenArg0Is101_throwsException () {
		// Arrange
		expected.expect(IllegalArgumentException.class);
		
		// Act
		MapSize.of(101, 1);
		
		// Assert
	}

	@Test
	public void of_whenArg1Is0_throwsException () {
		// Arrange
		expected.expect(IllegalArgumentException.class);
		
		// Act
		MapSize.of(1, 0);
		
		// Assert
	}

	@Test
	public void of_whenArg1Is101_throwsException () {
		// Arrange
		expected.expect(IllegalArgumentException.class);
		
		// Act
		MapSize.of(1, 101);
		
		// Assert
	}

	@Test
	public void ofWidth_whenArg0Is0_throwsException () {
		// Arrange
		expected.expect(IllegalArgumentException.class);
		
		// Act
		MapSize.ofWidth(0);
		
		// Assert
	}

	@Test
	public void ofWidth_whenArg0Is101_throwsException () {
		// Arrange
		expected.expect(IllegalArgumentException.class);
		
		// Act
		MapSize.ofWidth(101);
		
		// Assert
	}

	@Test
	public void ofHeight_whenArg0Is0_throwsException () {
		// Arrange
		expected.expect(IllegalArgumentException.class);
		
		// Act
		MapSize.ofHeight(0);
		
		// Assert
	}

	@Test
	public void ofHeight_whenArg0Is101_throwsException () {
		// Arrange
		expected.expect(IllegalArgumentException.class);
		
		// Act
		MapSize.ofHeight(101);
		
		// Assert
	}

	@Test
	public void ofWidth_returnsInstanceHasDefaultHeight () {
		// Arrange
		
		// Act
		final MapSize sz = MapSize.ofWidth(100);
		
		// Assert
		assertThat(sz.getWidth(), equalTo(100));
		assertThat(sz.getHeight(), equalTo(MapSize.DEFAULT_HEIGHT));
	}

	@Test
	public void ofHeight_returnsInstanceHasDefaultWidth () {
		// Arrange
		
		// Act
		final MapSize sz = MapSize.ofHeight(100);
		
		// Assert
		assertThat(sz.getWidth(), equalTo(MapSize.DEFAULT_WIDTH));
		assertThat(sz.getHeight(), equalTo(100));
	}

	@Test
	public void DEFAULT_isInstanceOf10x8 () {
		// Arrange
		
		// Act
		
		// Assert
		assertThat(MapSize.DEFAULT.getWidth(), equalTo(10));
		assertThat(MapSize.DEFAULT.getHeight(), equalTo(8));
	}
}
