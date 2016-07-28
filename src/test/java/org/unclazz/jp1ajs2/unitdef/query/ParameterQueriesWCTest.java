package org.unclazz.jp1ajs2.unitdef.query;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.builder.Builders;
import org.unclazz.jp1ajs2.unitdef.builder.ParameterBuilder;
import org.unclazz.jp1ajs2.unitdef.parameter.RunConditionWatchLimitCount;
import org.unclazz.jp1ajs2.unitdef.parameter.RunConditionWatchLimitCount.LimitationType;
import org.unclazz.jp1ajs2.unitdef.query.InternalParameterQueries;

public class ParameterQueriesWCTest {
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
		final Parameter p0 = makeParameter("wc", "no");
		
		// Act
		final RunConditionWatchLimitCount c0 = p0.query(InternalParameterQueries.WC);
		
		// Assert
		assertThat(c0.getCount(), equalTo(1));
		assertThat(c0.getRuleNumber().intValue(), equalTo(1));
		assertThat(c0.getType(), equalTo(LimitationType.NO_WATCHING));
	}
	
	@Test
	public void values_2_no() {
		// Arrange
		final Parameter p0 = makeParameter("wc", "2", "no");
		
		// Act
		final RunConditionWatchLimitCount c0 = p0.query(InternalParameterQueries.WC);
		
		// Assert
		assertThat(c0.getCount(), equalTo(1));
		assertThat(c0.getRuleNumber().intValue(), equalTo(2));
		assertThat(c0.getType(), equalTo(LimitationType.NO_WATCHING));
	}
	
	@Test
	public void values_un() {
		// Arrange
		final Parameter p0 = makeParameter("wc", "un");
		
		// Act
		final RunConditionWatchLimitCount c0 = p0.query(InternalParameterQueries.WC);
		
		// Assert
		assertThat(c0.getCount(), equalTo(1));
		assertThat(c0.getRuleNumber().intValue(), equalTo(1));
		assertThat(c0.getType(), equalTo(LimitationType.UNLIMITTED));
	}
	
	@Test
	public void values_2_un() {
		// Arrange
		final Parameter p0 = makeParameter("wc", "2", "un");
		
		// Act
		final RunConditionWatchLimitCount c0 = p0.query(InternalParameterQueries.WC);
		
		// Assert
		assertThat(c0.getCount(), equalTo(1));
		assertThat(c0.getRuleNumber().intValue(), equalTo(2));
		assertThat(c0.getType(), equalTo(LimitationType.UNLIMITTED));
	}
	
	@Test
	public void values_2() {
		// Arrange
		final Parameter p0 = makeParameter("wc", "2");
		
		// Act
		final RunConditionWatchLimitCount c0 = p0.query(InternalParameterQueries.WC);
		
		// Assert
		assertThat(c0.getCount(), equalTo(2));
		assertThat(c0.getRuleNumber().intValue(), equalTo(1));
		assertThat(c0.getType(), equalTo(LimitationType.LIMITTED));
	}
	
	@Test
	public void values_2_3() {
		// Arrange
		final Parameter p0 = makeParameter("wc", "2", "3");
		
		// Act
		final RunConditionWatchLimitCount c0 = p0.query(InternalParameterQueries.WC);
		
		// Assert
		assertThat(c0.getCount(), equalTo(3));
		assertThat(c0.getRuleNumber().intValue(), equalTo(2));
		assertThat(c0.getType(), equalTo(LimitationType.LIMITTED));
	}
}
