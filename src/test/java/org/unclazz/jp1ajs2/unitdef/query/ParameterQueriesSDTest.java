package org.unclazz.jp1ajs2.unitdef.query;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.Units;
import org.unclazz.jp1ajs2.unitdef.parameter.DayOfWeek;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.ByEntryDate;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.ByYearMonth;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.ByYearMonth.WithDayOfMonth;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.ByYearMonth.WithDayOfWeek;
import org.unclazz.jp1ajs2.unitdef.query.Queries;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.CountingMethod;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.DesignationMethod;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.NumberOfWeek;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.Undefined;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;

public class ParameterQueriesSDTest {
	@Rule
	public final ExpectedException exception = ExpectedException.none();
	
	private static Parameter sampleParameter(String name, String... values) {
		final StringBuilder buff = CharSequenceUtils
				.builder()
				.append("unit=FOO,,,;{ty=n;")
				.append(name).append('=');
		final int baseLen = buff.length();
		
		for (final String value : values) {
			if (baseLen < buff.length()) {
				buff.append(',');
			}
			buff.append(value);
		}
		
		return Units.fromCharSequence(buff.append(";}").toString())
				.get(0).query(Queries.parameters().nameEquals(name).one());
	}
	
	@Test
	public void SD_whenSpecifiedValueIs1en_returnsInstanceOfByEntryDate() {
		// Arrange
		final Parameter p = sampleParameter("sd", "1", " en");
		
		// Act
		final ByEntryDate r = (ByEntryDate)p.query(InternalParameterQueries.SD);
		
		// Assert
		assertThat(r.getDesignationMethod(), equalTo(DesignationMethod.ENTRY_DATE));
		assertThat(r.getRuleNumber().intValue(), equalTo(1));
	}
	
	@Test
	public void SD_whenSpecifiedValueIs0ud_returnsInstanceOfUndefined() {
		// Arrange
		final Parameter p = sampleParameter("sd", "0", " ud");
		
		// Act
		final Undefined r = (Undefined)p.query(InternalParameterQueries.SD);
		
		// Assert
		assertThat(r.getDesignationMethod(), equalTo(DesignationMethod.UNDEFINED));
		assertThat(r.getRuleNumber().intValue(), equalTo(0));
	}
	
	@Test
	public void SD_whenSpecifiedValueIs1ud_throwsException() {
		// Arrange
		final Parameter p = sampleParameter("sd", "1", " ud");
		exception.expect(IllegalArgumentException.class);
		
		// Act
		p.query(InternalParameterQueries.SD);
		
		// Assert
	}
	
	@Test
	public void SD_whenSpecifiedValueIs2YyyyMmDd_returnsInstanceOfWithDayOfMonth() {
		// Arrange
		final Parameter p = sampleParameter("sd", "2", " 2016/02/03");
		
		// Act
		final WithDayOfMonth r = (ByYearMonth.WithDayOfMonth)p.query(InternalParameterQueries.SD);
		
		// Assert
		assertThat(r.getDesignationMethod(), equalTo(DesignationMethod.SCHEDULED_DATE));
		assertThat(r.getRuleNumber().intValue(), equalTo(2));
		assertThat(r.getYearMonth().getYear(), equalTo(2016));
		assertThat(r.getYearMonth().getMonth(), equalTo(2));
		assertThat(r.getDay(), equalTo(3));
		assertThat(r.getCountingMethod(), equalTo(CountingMethod.ABSOLUTE));
	}
	
	@Test
	public void SD_whenSpecifiedValueIs2YyyyMd_returnsInstanceOfWithDayOfMonth() {
		// Arrange
		final Parameter p = sampleParameter("sd", "2", " 2016/2/3");
		
		// Act
		final WithDayOfMonth r = (ByYearMonth.WithDayOfMonth)p.query(InternalParameterQueries.SD);
		
		// Assert
		assertThat(r.getYearMonth().getYear(), equalTo(2016));
		assertThat(r.getYearMonth().getMonth(), equalTo(2));
		assertThat(r.getDay(), equalTo(3));
		assertThat(r.isBackward(), equalTo(false));
	}
	
	@Test
	public void SD_whenSpecifiedValueIs2MmDd_returnsInstanceOfWithDayOfMonth() {
		// Arrange
		final Parameter p = sampleParameter("sd", "2", " 02/03");
		
		// Act
		final WithDayOfMonth r = (ByYearMonth.WithDayOfMonth)p.query(InternalParameterQueries.SD);
		
		// Assert
		assertThat(r.getDesignationMethod(), equalTo(DesignationMethod.SCHEDULED_DATE));
		assertThat(r.getRuleNumber().intValue(), equalTo(2));
		assertThat(r.getYearMonth().getYear(), equalTo(-1));
		assertThat(r.getYearMonth().getMonth(), equalTo(2));
		assertThat(r.getDay(), equalTo(3));
		assertThat(r.getCountingMethod(), equalTo(CountingMethod.ABSOLUTE));
	}
	
	@Test
	public void SD_whenSpecifiedValueIs2YyyyMm_throwsException() {
		// Arrange
		final Parameter p = sampleParameter("sd", "2", " 2016/02/");
		exception.expect(RuntimeException.class);
		
		// Act
		p.query(InternalParameterQueries.SD);
		
		// Assert
	}
	
	@Test
	public void SD_whenSpecifiedValueIs2Mm_throwsException() {
		// Arrange
		final Parameter p = sampleParameter("sd", "2", " 02/");
		exception.expect(RuntimeException.class);
		
		// Act
		p.query(InternalParameterQueries.SD);
		
		// Assert
	}
	
	@Test
	public void SD_whenSpecifiedValueIs2YyyyMmPlusDd_returnsInstanceOfWithDayOfMonth() {
		// Arrange
		final Parameter p = sampleParameter("sd", "2", " 2016/02/+03");
		
		// Act
		final WithDayOfMonth r = (ByYearMonth.WithDayOfMonth)p.query(InternalParameterQueries.SD);
		
		// Assert
		assertThat(r.getCountingMethod(), equalTo(CountingMethod.RELATIVE));
	}
	
	@Test
	public void SD_whenSpecifiedValueIs2YyyyMmAsterDd_returnsInstanceOfWithDayOfMonth() {
		// Arrange
		final Parameter p = sampleParameter("sd", "2", " 2016/02/*03");
		
		// Act
		final WithDayOfMonth r = (ByYearMonth.WithDayOfMonth)p.query(InternalParameterQueries.SD);
		
		// Assert
		assertThat(r.getCountingMethod(), equalTo(CountingMethod.BUSINESS_DAY));
	}
	
	@Test
	public void SD_whenSpecifiedValueIs2YyyyMmAtDd_returnsInstanceOfWithDayOfMonth() {
		// Arrange
		final Parameter p = sampleParameter("sd", "2", " 2016/02/@03");
		
		// Act
		final WithDayOfMonth r = (ByYearMonth.WithDayOfMonth)p.query(InternalParameterQueries.SD);
		
		// Assert
		assertThat(r.getCountingMethod(), equalTo(CountingMethod.NON_BUSINESS_DAY));
	}
	
	@Test
	public void SD_whenSpecifiedValueIs2YyyyMmBMinusDd_returnsInstanceOfWithDayOfMonth() {
		// Arrange
		final Parameter p = sampleParameter("sd", "2", " 2016/02/b-03");
		
		// Act
		final WithDayOfMonth r = (ByYearMonth.WithDayOfMonth)p.query(InternalParameterQueries.SD);
		
		// Assert
		assertThat(r.getCountingMethod(), equalTo(CountingMethod.ABSOLUTE));
		assertThat(r.getDay(), equalTo(3));
		assertThat(r.isBackward(), equalTo(true));
	}
	
	@Test
	public void SD_whenSpecifiedValueIs2MmBMinusDd_returnsInstanceOfWithDayOfMonth() {
		// Arrange
		final Parameter p = sampleParameter("sd", "2", " 02/b-03");
		
		// Act
		final WithDayOfMonth r = (ByYearMonth.WithDayOfMonth)p.query(InternalParameterQueries.SD);
		
		// Assert
		assertThat(r.getCountingMethod(), equalTo(CountingMethod.ABSOLUTE));
		assertThat(r.getDay(), equalTo(3));
		assertThat(r.isBackward(), equalTo(true));
	}
	
	@Test
	public void SD_whenSpecifiedValueIs2YyyyMmPlusBMinusDd_returnsInstanceOfWithDayOfMonth() {
		// Arrange
		final Parameter p = sampleParameter("sd", "2", " 2016/02/+b-03");
		
		// Act
		final WithDayOfMonth r = (ByYearMonth.WithDayOfMonth)p.query(InternalParameterQueries.SD);
		
		// Assert
		assertThat(r.getCountingMethod(), equalTo(CountingMethod.RELATIVE));
		assertThat(r.getDay(), equalTo(3));
		assertThat(r.isBackward(), equalTo(true));
	}
	
	@Test
	public void SD_whenSpecifiedValueIs2YyyyMmDow_returnsInstanceOfWithDayOfWeek() {
		// Arrange
		final Parameter p = sampleParameter("sd", "2", " 2016/02/sa");
		
		// Act
		final WithDayOfWeek r = (ByYearMonth.WithDayOfWeek)p.query(InternalParameterQueries.SD);
		
		// Assert
		assertThat(r.getDayOfWeek(), equalTo(DayOfWeek.SATURDAY));
		assertThat(r.getNumberOfWeek(), equalTo(NumberOfWeek.NOT_SPECIFIED));
		assertThat(r.isRelative(), equalTo(false));
	}
	
	@Test
	public void SD_whenSpecifiedValueIs2MmDow_returnsInstanceOfWithDayOfWeek() {
		// Arrange
		final Parameter p = sampleParameter("sd", "2", " 02/sa");
		
		// Act
		final WithDayOfWeek r = (ByYearMonth.WithDayOfWeek)p.query(InternalParameterQueries.SD);
		
		// Assert
		assertThat(r.getDayOfWeek(), equalTo(DayOfWeek.SATURDAY));
		assertThat(r.getNumberOfWeek(), equalTo(NumberOfWeek.NOT_SPECIFIED));
		assertThat(r.isRelative(), equalTo(false));
	}
	
	@Test
	public void SD_whenSpecifiedValueIs2YyyyMmDowColon2_returnsInstanceOfWithDayOfWeek() {
		// Arrange
		final Parameter p = sampleParameter("sd", "2", " 2016/02/sa :2");
		
		// Act
		final WithDayOfWeek r = (ByYearMonth.WithDayOfWeek)p.query(InternalParameterQueries.SD);
		
		// Assert
		assertThat(r.getDayOfWeek(), equalTo(DayOfWeek.SATURDAY));
		assertThat(r.getNumberOfWeek(), equalTo(NumberOfWeek.of(2)));
	}
	
	@Test
	public void SD_whenSpecifiedValueIs2YyyyMmDowColonB_returnsInstanceOfWithDayOfWeek() {
		// Arrange
		final Parameter p = sampleParameter("sd", "2", " 2016/02/sa :b");
		
		// Act
		final WithDayOfWeek r = (ByYearMonth.WithDayOfWeek)p.query(InternalParameterQueries.SD);
		
		// Assert
		assertThat(r.getDayOfWeek(), equalTo(DayOfWeek.SATURDAY));
		assertThat(r.getNumberOfWeek(), equalTo(NumberOfWeek.LAST_WEEK));
	}
	
	@Test
	public void SD_whenSpecifiedValueIs2YyyyMmPlusDowColon2_returnsInstanceOfWithDayOfWeek() {
		// Arrange
		final Parameter p = sampleParameter("sd", "2", " 2016/02/+sa :2");
		
		// Act
		final WithDayOfWeek r = (ByYearMonth.WithDayOfWeek)p.query(InternalParameterQueries.SD);
		
		// Assert
		assertThat(r.getDayOfWeek(), equalTo(DayOfWeek.SATURDAY));
		assertThat(r.getNumberOfWeek(), equalTo(NumberOfWeek.of(2)));
		assertThat(r.isRelative(), equalTo(true));
	}
}
