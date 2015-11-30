package org.unclazz.jp1ajs2.unitdef.parameter;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class MinutesIntervalTest {

	@Test
	public void ofMinutes_int() {
		assertThat(MinutesInterval.ofMinutes(1).intValue(), CoreMatchers.is(1));
		assertThat(MinutesInterval.ofMinutes(1440).intValue(), CoreMatchers.is(1440));
		
		try {
			MinutesInterval.ofMinutes(0);
			fail();
		} catch (Exception e) {
			// OK
		}
		try {
			MinutesInterval.ofMinutes(1441);
			fail();
		} catch (Exception e) {
			// OK
		}
	}

	@Test
	public void of_int_int() {
		assertThat(MinutesInterval.of(0, 1).intValue(), CoreMatchers.is(1));
		assertThat(MinutesInterval.of(0, 59).intValue(), CoreMatchers.is(59));
		assertThat(MinutesInterval.of(1, 0).intValue(), CoreMatchers.is(60));
		assertThat(MinutesInterval.of(24, 0).intValue(), CoreMatchers.is(1440));
		
		try {
			MinutesInterval.of(0, 0);
			fail();
		} catch (Exception e) {
			// OK
		}
		try {
			MinutesInterval.of(0, 60);
			fail();
		} catch (Exception e) {
			// OK
		}
		try {
			MinutesInterval.of(24, 1);
			fail();
		} catch (Exception e) {
			// OK
		}
	}
	
	@Test
	public void getHours() {
		assertThat(MinutesInterval.of(0,  1).getHours(), CoreMatchers.is(0));
		assertThat(MinutesInterval.of(0,  59).getHours(), CoreMatchers.is(0));
		assertThat(MinutesInterval.of(1, 0).getHours(), CoreMatchers.is(1));
		assertThat(MinutesInterval.of(1, 1).getHours(), CoreMatchers.is(1));
		assertThat(MinutesInterval.of(1, 59).getHours(), CoreMatchers.is(1));
		assertThat(MinutesInterval.of(2, 0).getHours(), CoreMatchers.is(2));
	}
	
	@Test
	public void getMinutes() {
		assertThat(MinutesInterval.of(0,  1).getMinutes(), CoreMatchers.is(1));
		assertThat(MinutesInterval.of(0,  59).getMinutes(), CoreMatchers.is(59));
		assertThat(MinutesInterval.of(1, 0).getMinutes(), CoreMatchers.is(0));
		assertThat(MinutesInterval.of(1, 1).getMinutes(), CoreMatchers.is(1));
		assertThat(MinutesInterval.of(1, 59).getMinutes(), CoreMatchers.is(59));
		assertThat(MinutesInterval.of(2, 0).getMinutes(), CoreMatchers.is(0));
	}

	@Test
	public void toDate() {
		assertThat(MinutesInterval.ofMinutes(1).toDate(), CoreMatchers.is(dateOf(1)));
		assertThat(MinutesInterval.ofMinutes(59).toDate(), CoreMatchers.is(dateOf(59)));
		assertThat(MinutesInterval.ofMinutes(60).toDate(), CoreMatchers.is(dateOf(60)));
		assertThat(MinutesInterval.ofMinutes(1440).toDate(), CoreMatchers.is(dateOf(1440)));
	}
	
	private Date dateOf(int minutes) {
		final Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.add(Calendar.MINUTE, minutes);
		return cal.getTime();
	}
}
