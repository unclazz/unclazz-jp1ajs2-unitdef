package org.unclazz.jp1ajs2.unitdef.query;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.builder.Builders;
import org.unclazz.jp1ajs2.unitdef.builder.ParameterBuilder;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateCompensation;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateCompensation.CompensationMethod;
import org.unclazz.jp1ajs2.unitdef.query.InternalParameterQueries;

public class InternalParameterQueriesSHTest {
	private Parameter makeParameter(String name, String... values) {
		final ParameterBuilder b = Builders.parameter().setName(name);
		for (final String value : values) {
			b.addRawCharSequence(value);
		}
		return b.build();
	}
	
	@Test
	public void values_be() {
		// Arrange
		final Parameter p0 = makeParameter("sh", "be");
		
		// Act
		final StartDateCompensation r0 = p0.query(InternalParameterQueries.SH);
		
		// Assert
		assertThat(r0.getMethod(), equalTo(CompensationMethod.BEFORE));
		assertThat(r0.getRuleNumber().intValue(), equalTo(1));
	}
	
	@Test
	public void values_2_be() {
		// Arrange
		final Parameter p0 = makeParameter("sh", "2", "be");
		
		// Act
		final StartDateCompensation r0 = p0.query(InternalParameterQueries.SH);
		
		// Assert
		assertThat(r0.getMethod(), equalTo(CompensationMethod.BEFORE));
		assertThat(r0.getRuleNumber().intValue(), equalTo(2));
	}
	
	@Test
	public void values_af() {
		// Arrange
		final Parameter p0 = makeParameter("sh", "af");
		
		// Act
		final StartDateCompensation r0 = p0.query(InternalParameterQueries.SH);
		
		// Assert
		assertThat(r0.getMethod(), equalTo(CompensationMethod.AFTER));
		assertThat(r0.getRuleNumber().intValue(), equalTo(1));
	}
	
	@Test
	public void values_2_af() {
		// Arrange
		final Parameter p0 = makeParameter("sh", "2", "af");
		
		// Act
		final StartDateCompensation r0 = p0.query(InternalParameterQueries.SH);
		
		// Assert
		assertThat(r0.getMethod(), equalTo(CompensationMethod.AFTER));
		assertThat(r0.getRuleNumber().intValue(), equalTo(2));
	}
	
	@Test
	public void values_ca() {
		// Arrange
		final Parameter p0 = makeParameter("sh", "ca");
		
		// Act
		final StartDateCompensation r0 = p0.query(InternalParameterQueries.SH);
		
		// Assert
		assertThat(r0.getMethod(), equalTo(CompensationMethod.CANCEL));
		assertThat(r0.getRuleNumber().intValue(), equalTo(1));
	}
	
	@Test
	public void values_2_ca() {
		// Arrange
		final Parameter p0 = makeParameter("sh", "2", "ca");
		
		// Act
		final StartDateCompensation r0 = p0.query(InternalParameterQueries.SH);
		
		// Assert
		assertThat(r0.getMethod(), equalTo(CompensationMethod.CANCEL));
		assertThat(r0.getRuleNumber().intValue(), equalTo(2));
	}
	
	@Test
	public void values_no() {
		// Arrange
		final Parameter p0 = makeParameter("sh", "no");
		
		// Act
		final StartDateCompensation r0 = p0.query(InternalParameterQueries.SH);
		
		// Assert
		assertThat(r0.getMethod(), equalTo(CompensationMethod.NOT_CONSIDER));
		assertThat(r0.getRuleNumber().intValue(), equalTo(1));
	}
	
	@Test
	public void values_2_no() {
		// Arrange
		final Parameter p0 = makeParameter("sh", "2", "no");
		
		// Act
		final StartDateCompensation r0 = p0.query(InternalParameterQueries.SH);
		
		// Assert
		assertThat(r0.getMethod(), equalTo(CompensationMethod.NOT_CONSIDER));
		assertThat(r0.getRuleNumber().intValue(), equalTo(2));
	}
}
