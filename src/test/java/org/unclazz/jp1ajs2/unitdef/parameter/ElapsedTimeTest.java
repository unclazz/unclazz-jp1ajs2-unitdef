package org.unclazz.jp1ajs2.unitdef.parameter;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ElapsedTimeTest {
	@Rule
	public final ExpectedException expected = ExpectedException.none();

	@Test
	public void ofInt_whenArgIs0_throwsException() {
		// Arrange
		expected.expect(IllegalArgumentException.class);
		
		// Act
		ElapsedTime.of(0);
		try {
			ElapsedTime.of(1441);
			fail();
		} catch (Exception e) {
			// OK
		}
	}

	@Test
	public void ofInt_whenArgIs1441_throwsException() {
		// Arrange
		expected.expect(IllegalArgumentException.class);
		
		// Act
		ElapsedTime.of(1441);
	}

	@Test
	public void ofInt_whenArgIs1_returnsInstanceWrapping1() {
		// Arrange
		
		// Act
		final ElapsedTime mi = ElapsedTime.of(1);
		
		// Asserty
		assertThat(mi.intValue(), equalTo(1));
	}

	@Test
	public void ofInt_whenArgIs1440_returnsInstanceWrapping1440() {
		// Arrange
		
		// Act
		final ElapsedTime mi = ElapsedTime.of(1440);
		
		// Asserty
		assertThat(mi.intValue(), equalTo(1440));
	}

	@Test
	public void ofIntInt_whenBothArgsAre0_throwsException() {
		// Arrange
		expected.expect(IllegalArgumentException.class);
		
		// Act
		ElapsedTime.of(0, 0);
	}

	@Test
	public void ofIntInt_whenArg1GreaterThan59_throwsException() {
		// Arrange
		expected.expect(IllegalArgumentException.class);
		
		// Act
		ElapsedTime.of(1, 60);
	}

	@Test
	public void ofIntInt_whenAre0Is24AndArg1IsGreaterThan0_throwsException() {
		// Arrange
		expected.expect(IllegalArgumentException.class);
		
		// Act
		ElapsedTime.of(24, 1);
	}

	@Test
	public void ofIntInt_whenWrappedValueIsBetween0001And2400_returnsInstance() {
		assertThat(ElapsedTime.of(0, 1).intValue(), equalTo(1));
		assertThat(ElapsedTime.of(0, 59).intValue(), equalTo(59));
		assertThat(ElapsedTime.of(1, 0).intValue(), equalTo(60));
		assertThat(ElapsedTime.of(24, 0).intValue(), equalTo(1440));
	}
	
	@Test
	public void getHours_whenWrappedValueIs1Minutes_returns0() {
		assertThat(ElapsedTime.of(0,  1).getHours(), equalTo(0));
	}
	
	@Test
	public void getHours_whenWrappedValueIs59Minutes_returns0() {
		assertThat(ElapsedTime.of(0,  59).getHours(), equalTo(0));
	}
	
	@Test
	public void getHours_whenWrappedValueIs60Minutes_returns1() {
		assertThat(ElapsedTime.of(60).getHours(), equalTo(1));
	}
	
	@Test
	public void getHours_whenWrappedValueIs119Minutes_returns1() {
		assertThat(ElapsedTime.of(1, 59).getHours(), equalTo(1));
	}
	
	@Test
	public void getHours_whenWrappedValueIs120Minutes_returns2() {
		assertThat(ElapsedTime.of(2, 0).getHours(), equalTo(2));
	}
	
	
	@Test
	public void getMinutes_whenWrappedValueIs1Minutes_returns1() {
		assertThat(ElapsedTime.of(0,  1).getMinutes(), equalTo(1));
	}
	
	@Test
	public void getMinutes_whenWrappedValueIs60Minutes_returns0() {
		assertThat(ElapsedTime.of(1, 0).getMinutes(), equalTo(0));
	}
	
	@Test
	public void getMinutes_whenWrappedValueIs61Minutes_returns1() {
		assertThat(ElapsedTime.of(1, 1).getMinutes(), equalTo(1));
	}
	
	
	@Test
	public void toMinutes_whenWrappedValueIs1Minutes_returns1() {
		assertThat(ElapsedTime.of(0,  1).toMinutes(), equalTo(1));
	}
	
	@Test
	public void toMinutes_whenWrappedValueIs60Minutes_returns60() {
		assertThat(ElapsedTime.of(1, 0).toMinutes(), equalTo(60));
	}
	
	@Test
	public void toMinutes_whenWrappedValueIs61Minutes_returns61() {
		assertThat(ElapsedTime.of(1, 1).toMinutes(), equalTo(61));
	}

	@Test
	public void toDate() {
		assertThat(ElapsedTime.of(1).toDate(), equalTo(dateOfMinutes(1)));
		assertThat(ElapsedTime.of(59).toDate(), equalTo(dateOfMinutes(59)));
		assertThat(ElapsedTime.of(60).toDate(), equalTo(dateOfMinutes(60)));
		assertThat(ElapsedTime.of(1440).toDate(), equalTo(dateOfMinutes(1440)));
	}
	
	private Date dateOfMinutes(int minutes) {
		final Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.add(Calendar.MINUTE, minutes);
		return cal.getTime();
	}
}
