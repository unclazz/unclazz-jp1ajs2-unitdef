package org.unclazz.jp1ajs2.unitdef.builder;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.unclazz.jp1ajs2.unitdef.FullQualifiedName;

public class FullQualifiedNameBuilderTest {

	@Rule
	public final ExpectedException expected = ExpectedException.none();

	@Test
	public void build_whenFragmentsNoSpecified_throwsException() {
		// Arrange
		final FullQualifiedNameBuilder b = Builders.fullQualifiedName();
		expected.expect(IllegalArgumentException.class);
		
		// Act
		b.build();
			
		// Assert
	}

	@Test
	public void addFragment_whenFragmentContainsSlash_throwsException() {
		// Arrange
		final FullQualifiedNameBuilder b = Builders.fullQualifiedName();
		expected.expect(IllegalArgumentException.class);
		
		// Act
		b.addFragment("foo/bar");
			
		// Assert
	}

	@Test
	public void build_when1FragmentSpecified_returnsInstanceHas1Depth() {
		// Arrange
		final FullQualifiedNameBuilder b = Builders.fullQualifiedName();
		b.addFragment("foo");
		
		// Act
		final FullQualifiedName n = b.build();
			
		// Assert
		assertThat(n.toString(), equalTo("/foo"));
	}

	@Test
	public void build_when2FragmentSpecified_returnsInstanceHas2Depth() {
		// Arrange
		final FullQualifiedNameBuilder b = Builders.fullQualifiedName();
		b.addFragment("foo").addFragment("bar");
		
		// Act
		final FullQualifiedName n = b.build();
			
		// Assert
		assertThat(n.toString(), equalTo("/foo/bar"));
	}
}
