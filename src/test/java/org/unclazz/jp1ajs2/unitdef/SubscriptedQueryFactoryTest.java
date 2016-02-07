package org.unclazz.jp1ajs2.unitdef;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.unclazz.jp1ajs2.unitdef.builder.Builders;

import static org.unclazz.jp1ajs2.unitdef.UnitQueries.*;

import java.util.List;

public class SubscriptedQueryFactoryTest {

	@Test
	public void integer_returnsUnitQueryInteger() {
		// Arrange
		final Unit u = Builders
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
				.build();
		
		// Act
		final List<Integer> is = u.query(parameter("foo").item(1).integer());
		
		// Assert
		assertThat(is.size(), equalTo(2));
		assertThat(is.get(0), equalTo(456));
		assertThat(is.get(1), equalTo(654));
	}

}
