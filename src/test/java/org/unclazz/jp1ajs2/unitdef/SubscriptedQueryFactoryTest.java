package org.unclazz.jp1ajs2.unitdef;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.unclazz.jp1ajs2.unitdef.builder.Builders;

import static org.unclazz.jp1ajs2.unitdef.UnitQueries.*;

import java.util.List;

public class SubscriptedQueryFactoryTest {

	private Unit sampleUnitHasIntAndYesNo() {
		return Builders
				.unit()
				.setAttributes(Builders.attributes().setName("foo").build())
				.setFullQualifiedName(Builders.fullQualifiedName().addFragment("foo").build())
				.addParameter(Builders.parameter().setName("ty").addRawCharSequence("g").build())
				.addParameter(Builders.parameter()
						.setName("foo").addRawCharSequence("123")
						.addRawCharSequence("456").build())
				.addParameter(Builders.parameter()
						.setName("foo").addRawCharSequence("321")
						.addRawCharSequence("654").build())
				.addParameter(Builders.parameter()
						.setName("bar").addRawCharSequence("y")
						.addRawCharSequence("n").build())
				.build();
	}
	
	private Unit sampleUnitDoesNotHaveIntAndYesNo() {
		return Builders
				.unit()
				.setAttributes(Builders.attributes().setName("foo").build())
				.setFullQualifiedName(Builders.fullQualifiedName().addFragment("foo").build())
				.addParameter(Builders.parameter().setName("ty").addRawCharSequence("g").build())
				.addParameter(Builders.parameter()
						.setName("foo").addRawCharSequence("abc")
						.addRawCharSequence("def").build())
				.addParameter(Builders.parameter()
						.setName("foo").addRawCharSequence("cba")
						.addRawCharSequence("fed").build())
				.addParameter(Builders.parameter()
						.setName("bar").addRawCharSequence("a")
						.addRawCharSequence("b").build())
				.build();
	}
	
	@Test
	public void integer_returnsUnitQueryForIntValue() {
		// Arrange
		final Unit u = sampleUnitHasIntAndYesNo();
		
		// Act
		final List<Integer> is = u.query(parameter("foo").item(1).integer());
		
		// Assert
		assertThat(is.size(), equalTo(2));
		assertThat(is.get(0), equalTo(456));
		assertThat(is.get(1), equalTo(654));
	}
	
	@Test
	public void integerInt_returnsUnitQueryForIntValueWithDefault() {
		// Arrange
		final Unit u = sampleUnitDoesNotHaveIntAndYesNo();
		
		// Act
		final List<Integer> is = u.query(parameter("foo").item(1).integer(123456));
		
		// Assert
		assertThat(is.size(), equalTo(2));
		assertThat(is.get(0), equalTo(123456));
		assertThat(is.get(1), equalTo(123456));
	}
	
	@Test
	public void contains_returnsUnitQueryForBoolean() {
		// Arrange
		final Unit u = sampleUnitHasIntAndYesNo();
		
		// Act
		final List<Boolean> bs = u.query(parameter("bar").item(1).contentEquals("n"));
		
		// Assert
		assertThat(bs.size(), equalTo(1));
		assertThat(bs.get(0), equalTo(true));
	}

}
