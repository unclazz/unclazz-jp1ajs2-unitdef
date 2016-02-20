package org.unclazz.jp1ajs2.unitdef.builder;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.unclazz.jp1ajs2.unitdef.Attributes;
import static org.unclazz.jp1ajs2.unitdef.builder.Builders.*;

public class AttributesBuilderTest {

	@Rule
	public final ExpectedException expected = ExpectedException.none();
	
	@Test
	public void setName_whenUnitNameContainsSlash_throwsException() {
		// Arrange
		final AttributesBuilder b = attributes()
				.setJP1UserName("foo")
				.setPermissionMode(permissionMode("0000"))
				.setResourceGroupName("baz");
		expected.expect(IllegalArgumentException.class);
		
		// Act
		b.setName("foo/bar");
		
		// Assert
	}
	
	@Test
	public void build_whenUnitNameNoSpecified_throwsException() {
		// Arrange
		final AttributesBuilder b = attributes();
		b.setJP1UserName("foo").setPermissionMode(permissionMode("0000")).setResourceGroupName("baz");
		expected.expect(NullPointerException.class);
		
		// Act
		b.build();
		
		// Assert
	}
	
	@Test
	public void build_whenUnitNameIsNull_throwsException() {
		// Arrange
		final AttributesBuilder b = attributes();
		b.setJP1UserName("foo").setPermissionMode(permissionMode("0004")).setResourceGroupName("baz");
		expected.expect(NullPointerException.class);
		
		// Act
		b.build();
		
		// Assert
	}
	
	@Test
	public void build_whenUnitNameIsEmpty_throwsException() {
		// Arrange
		final AttributesBuilder b = attributes();
		b.setName("").setJP1UserName("foo").setPermissionMode(permissionMode("0007")).setResourceGroupName("baz");
		expected.expect(IllegalArgumentException.class);
		
		// Act
		b.build();
		
		// Assert
	}
	
	@Test
	public void build_whenUnitNameSpecified_returnsAttributesInstance() {
		// Arrange
		final AttributesBuilder b = attributes();
		b.setJP1UserName("foo").setPermissionMode(permissionMode("0003")).setResourceGroupName("baz")
		.setName("x");
		
		// Act
		final Attributes as = b.build();

		// Assert
		assertThat(as.getUnitName(), equalTo("x"));
		assertThat(as.getJP1UserName(), equalTo("foo"));
		assertThat(as.getPermissionMode().toString(), equalTo("0003"));
		assertThat(as.getResourceGroupName(), equalTo("baz"));
	}
}
