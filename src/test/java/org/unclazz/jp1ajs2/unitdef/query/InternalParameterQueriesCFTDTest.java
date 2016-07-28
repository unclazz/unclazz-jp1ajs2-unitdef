package org.unclazz.jp1ajs2.unitdef.query;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.builder.Builders;
import org.unclazz.jp1ajs2.unitdef.builder.ParameterBuilder;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateAdjustment;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateAdjustment.AdjustmentType;
import org.unclazz.jp1ajs2.unitdef.query.InternalParameterQueries;

public class InternalParameterQueriesCFTDTest {
	private Parameter makeParameter(String name, String... values) {
		final ParameterBuilder b = Builders.parameter().setName(name);
		for (final String value : values) {
			b.addRawCharSequence(value);
		}
		return b.build();
	}
	
	@Test
	public void values_no() {
		// Arrange
		final Parameter p0 = makeParameter("cftd", "no");
		
		// Act
		final StartDateAdjustment a0 = p0.query(InternalParameterQueries.CFTD);
		
		// Assert
		assertThat(a0.getBusinessDays(), equalTo(1));
		assertThat(a0.getDeadlineDays(), equalTo(10));
		assertThat(a0.getRuleNumber().intValue(), equalTo(1));
		assertThat(a0.getType(), equalTo(AdjustmentType.NOT_ADJUST));
	}
	
	@Test
	public void values_1_no() {
		// Arrange
		final Parameter p0 = makeParameter("cftd", "1", "no");
		
		// Act
		final StartDateAdjustment a0 = p0.query(InternalParameterQueries.CFTD);
		
		// Assert
		assertThat(a0.getBusinessDays(), equalTo(1));
		assertThat(a0.getDeadlineDays(), equalTo(10));
		assertThat(a0.getRuleNumber().intValue(), equalTo(1));
		assertThat(a0.getType(), equalTo(AdjustmentType.NOT_ADJUST));
	}
	
	@Test
	public void values_2_no_3() {
		// Arrange
		final Parameter p0 = makeParameter("cftd", "2", "no", "3");
		
		// Act
		final StartDateAdjustment a0 = p0.query(InternalParameterQueries.CFTD);
		
		// Assert
		assertThat(a0.getBusinessDays(), equalTo(3));
		assertThat(a0.getDeadlineDays(), equalTo(10));
		assertThat(a0.getRuleNumber().intValue(), equalTo(2));
		assertThat(a0.getType(), equalTo(AdjustmentType.NOT_ADJUST));
	}
	
	@Test
	public void values_2_no_3_4() {
		// Arrange
		final Parameter p0 = makeParameter("cftd", "2", "no", "3", "4");
		
		// Act
		final StartDateAdjustment a0 = p0.query(InternalParameterQueries.CFTD);
		
		// Assert
		assertThat(a0.getBusinessDays(), equalTo(3));
		assertThat(a0.getDeadlineDays(), equalTo(4));
		assertThat(a0.getRuleNumber().intValue(), equalTo(2));
		assertThat(a0.getType(), equalTo(AdjustmentType.NOT_ADJUST));
	}
	
	@Test
	public void values_af() {
		// Arrange
		final Parameter p0 = makeParameter("cftd", "af");
		
		// Act
		final StartDateAdjustment a0 = p0.query(InternalParameterQueries.CFTD);
		
		// Assert
		assertThat(a0.getBusinessDays(), equalTo(1));
		assertThat(a0.getDeadlineDays(), equalTo(10));
		assertThat(a0.getRuleNumber().intValue(), equalTo(1));
		assertThat(a0.getType(), equalTo(AdjustmentType.AFTER));
	}
	
	@Test
	public void values_af_1_2() {
		// Arrange
		final Parameter p0 = makeParameter("cftd", "af", "1", "2");
		
		// Act
		final StartDateAdjustment a0 = p0.query(InternalParameterQueries.CFTD);
		
		// Assert
		assertThat(a0.getBusinessDays(), equalTo(1));
		assertThat(a0.getDeadlineDays(), equalTo(2));
		assertThat(a0.getRuleNumber().intValue(), equalTo(1));
		assertThat(a0.getType(), equalTo(AdjustmentType.AFTER));
	}
	
	@Test
	public void values_2_af() {
		// Arrange
		final Parameter p0 = makeParameter("cftd", "2", "af");
		
		// Act
		final StartDateAdjustment a0 = p0.query(InternalParameterQueries.CFTD);
		
		// Assert
		assertThat(a0.getBusinessDays(), equalTo(1));
		assertThat(a0.getDeadlineDays(), equalTo(10));
		assertThat(a0.getRuleNumber().intValue(), equalTo(2));
		assertThat(a0.getType(), equalTo(AdjustmentType.AFTER));
	}
	
	@Test
	public void values_2_af_3() {
		// Arrange
		final Parameter p0 = makeParameter("cftd", "2", "af", "3");
		
		// Act
		final StartDateAdjustment a0 = p0.query(InternalParameterQueries.CFTD);
		
		// Assert
		assertThat(a0.getBusinessDays(), equalTo(3));
		assertThat(a0.getDeadlineDays(), equalTo(10));
		assertThat(a0.getRuleNumber().intValue(), equalTo(2));
		assertThat(a0.getType(), equalTo(AdjustmentType.AFTER));
	}
	
	@Test
	public void values_2_af_3_4() {
		// Arrange
		final Parameter p0 = makeParameter("cftd", "2", "af", "3", "4");
		
		// Act
		final StartDateAdjustment a0 = p0.query(InternalParameterQueries.CFTD);
		
		// Assert
		assertThat(a0.getBusinessDays(), equalTo(3));
		assertThat(a0.getDeadlineDays(), equalTo(4));
		assertThat(a0.getRuleNumber().intValue(), equalTo(2));
		assertThat(a0.getType(), equalTo(AdjustmentType.AFTER));
	}
}
