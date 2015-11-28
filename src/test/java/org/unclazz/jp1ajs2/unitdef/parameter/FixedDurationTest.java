package org.unclazz.jp1ajs2.unitdef.parameter;

import static org.junit.Assert.*;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class FixedDurationTest {

	@Test
	public void of_int() {
		try {
			FixedDuration.of(0);
			fail();
		} catch (Exception e) {
			// OK
		}
		
		final FixedDuration fd0 = FixedDuration.of(1);
		assertThat(fd0.toString(), CoreMatchers.is("1"));
		assertThat(fd0.toSeconds(), CoreMatchers.is(60));
		assertThat(fd0.toMinutes(), CoreMatchers.is(1));
		assertThat(fd0.intValue(), CoreMatchers.is(1));
		
		final FixedDuration fd1 = FixedDuration.of(1440);
		assertThat(fd1.toString(), CoreMatchers.is("1440"));
		
		final FixedDuration fd2 = FixedDuration.of(1441);
		assertThat(fd2.toString(), CoreMatchers.is("1441"));
	}

}
