package org.unclazz.jp1ajs2.unitdef.builder;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.unclazz.jp1ajs2.unitdef.Attributes;

public class AttributesBuilderTest {

	@Rule
	public final ExpectedException expected = ExpectedException.none();
	
	@Test
	public void build_whenUnitNameNoSpecified_throwsException() {
		// Arrange
		final AttributesBuilder b = Builders.attributes();
		b.setJP1UserName("foo").setPermissionMode("bar").setResourceGroupName("baz");
		expected.expect(IllegalArgumentException.class);
		
		// Act
		b.build();
		
		// Assert
	}
	
	@Test
	public void build_whenUnitNameSpecified_returnsAttributesInstance() {
		// Arrange
		final AttributesBuilder b = Builders.attributes();
		b.setJP1UserName("foo").setPermissionMode("bar").setResourceGroupName("baz")
		.setName("x");
		
		// Act
		final Attributes as = b.build();

		// Assert
		assertThat(as.getUnitName(), equalTo("x"));
		assertThat(as.getJP1UserName(), equalTo("foo"));
		assertThat(as.getPermissionMode(), equalTo("bar"));
		assertThat(as.getResourceGroupName(), equalTo("baz"));
	}
}
