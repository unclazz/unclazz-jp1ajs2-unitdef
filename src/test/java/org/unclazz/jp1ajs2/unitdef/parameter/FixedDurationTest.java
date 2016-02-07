package org.unclazz.jp1ajs2.unitdef.parameter;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class FixedDurationTest {
	
	@Rule
	public final ExpectedException expected = ExpectedException.none();

	@Test
	public void of_whenArgIs0_throwsException () {
		// Arrange
		expected.expect(IllegalArgumentException.class);
		
		// Act
		FixedDuration.of(0);
		
		// Assert
	}

	@Test
	public void of_whenArgIsGreaterThan0_returnsInstance() {
		// Arrange
		
		// Act
		final FixedDuration fd0 = FixedDuration.of(1);
		
		// Assert
		assertThat(fd0.toString(), equalTo("1"));
	}

	@Test
	public void of_whenArgIs1441_returnsInstance() {
		// Arrange
		
		// Act
		final FixedDuration fd0 = FixedDuration.of(1441);
		
		// Assert
		assertThat(fd0.toString(), equalTo("1441"));
	}

	@Test
	public void toSeconds_whenWrappedValueIs1_returns60() {
		// Arrange
		
		// Act
		final FixedDuration fd0 = FixedDuration.of(1);
		
		// Assert
		assertThat(fd0.toSeconds(), equalTo(60));
	}

	@Test
	public void toSeconds_whenWrappedValueIs2_returns120() {
		// Arrange
		
		// Act
		final FixedDuration fd0 = FixedDuration.of(2);
		
		// Assert
		assertThat(fd0.toSeconds(), equalTo(120));
	}

	@Test
	public void toMinutes_whenWrappedValueIs2_returns2() {
		// Arrange
		
		// Act
		final FixedDuration fd0 = FixedDuration.of(2);
		
		// Assert
		assertThat(fd0.toMinutes(), equalTo(2));
	}

	@Test
	public void intValue_whenWrappedValueIs2_returns2() {
		// Arrange
		
		// Act
		final FixedDuration fd0 = FixedDuration.of(2);
		
		// Assert
		assertThat(fd0.intValue(), equalTo(2));
	}
}
