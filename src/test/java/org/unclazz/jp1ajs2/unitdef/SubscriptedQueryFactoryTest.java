package org.unclazz.jp1ajs2.unitdef;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.unclazz.jp1ajs2.unitdef.builder.Builders;
import org.unclazz.jp1ajs2.unitdef.query.Queries;

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
	
	private Unit sampleUnitHasTuple() {
		return Units.fromCharSequence(""
				+ "unit=FOO,,,;{"
				+ "ty=n;"
				+ "cm=\"Sample\";"
				+ "ar=(f=BAR0,t=BAR1,seq);"
				+ "ar=(f=BAR0,t=BAR2,seq);"
				+ "ar=(f=BAR1,t=BAR3,cond);"
				+ "unit=BAR0,,,;{ty=n;}"
				+ "unit=BAR1,,,;{ty=jdj;}"
				+ "unit=BAR2,,,;{ty=n;}"
				+ "unit=BAR3,,,;{ty=n;}"
				+ "}").get(0);
	}
	
	@Test
	public void integer_returnsUnitQueryForIntValue() {
		// Arrange
		final Unit u = sampleUnitHasIntAndYesNo();
		
		// Act
		final List<Integer> is = u.query(Queries
				.parameters().nameEquals("foo")
				.theirValues().at(1).asInteger().list());
		
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
		final List<Integer> is = u.query(Queries
				.parameters().nameEquals("foo")
				.theirValues().at(1)
				.asInteger(123456).list());
		
		// Assert
		assertThat(is.size(), equalTo(2));
		assertThat(is.get(0), equalTo(123456));
		assertThat(is.get(1), equalTo(123456));
	}
	
	@Test
	public void tuple_returnsUnitQueryForTuple() {
		// Arrange
		final Unit u = sampleUnitHasTuple();
		
		// Act
		final List<Tuple> rs = u.query(Queries
				.parameters().nameEquals("ar")
				.theirValues().at(0).typeIsTuple().list());
		
		// Assert
		assertThat(rs.size(), equalTo(3));
		assertThat(rs.get(0).get("t"), equalTo((CharSequence)"BAR1"));
	}

}
