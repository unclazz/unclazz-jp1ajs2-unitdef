package org.unclazz.jp1ajs2.unitdef;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.unclazz.jp1ajs2.unitdef.FullQualifiedName;
import org.unclazz.jp1ajs2.unitdef.builder.Builders;
import org.unclazz.jp1ajs2.unitdef.builder.FullQualifiedNameBuilder;

public class FullQualifiedNameTest {

	@Rule
	public final ExpectedException expected = ExpectedException.none();

	@Test
	public void getFragments_whenInstanceMadeOf1fragments_returns1Fragments() {
		// Arrange
		final FullQualifiedNameBuilder b = Builders.fullQualifiedName();
		b.addFragment("foo");
		
		// Act
		final FullQualifiedName n = b.build();
			
		// Assert
		assertThat(n.getFragments(), equalTo(new CharSequence[]{"foo"}));
	}

	@Test
	public void getFragments_whenInstanceMadeOf2fragments_returns2Fragments() {
		// Arrange
		final FullQualifiedNameBuilder b = Builders.fullQualifiedName();
		b.addFragment("foo").addFragment("bar");
		
		// Act
		final FullQualifiedName n = b.build();
			
		// Assert
		assertThat(n.getFragments(), equalTo(new CharSequence[]{"foo", "bar"}));
	}

	@Test
	public void getSuperUnitName_whenInstanceMadeOfFooAndBar_returnsInstanceMadeOfFoo() {
		// Arrange
		final FullQualifiedNameBuilder b = Builders.fullQualifiedName();
		b.addFragment("foo").addFragment("bar");
		
		// Act
		final FullQualifiedName n = b.build();
		final FullQualifiedName n2 = n.getSuperUnitName();
			
		// Assert
		assertThat(n2.toString(), equalTo("/foo"));
	}

	@Test
	public void getSuperUnitName_whenInstanceMadeOfFooBarAndBar_returnsInstanceMadeOfFooAndBar() {
		// Arrange
		final FullQualifiedNameBuilder b = Builders.fullQualifiedName();
		b.addFragment("foo").addFragment("bar").addFragment("baz");
		
		// Act
		final FullQualifiedName n = b.build();
		final FullQualifiedName n2 = n.getSuperUnitName();
			
		// Assert
		assertThat(n2.toString(), equalTo("/foo/bar"));
	}

	@Test
	public void getSuperUnitName_whenInstanceMadeOfFooAndBar_returnsInstanceMadeOfFooBarAndBaz() {
		// Arrange
		final FullQualifiedNameBuilder b = Builders.fullQualifiedName();
		b.addFragment("foo").addFragment("bar");
		
		// Act
		final FullQualifiedName n = b.build().getSubUnitName("baz");
			
		// Assert
		assertThat(n.toString(), equalTo("/foo/bar/baz"));
	}

	@Test
	public void getSuperUnitName_whenArgIsNull_throwsException() {
		// Arrange
		final FullQualifiedNameBuilder b = Builders.fullQualifiedName();
		b.addFragment("foo").addFragment("bar");
		expected.expect(NullPointerException.class);
		
		// Act
		b.build().getSubUnitName(null);
			
		// Assert
	}

	@Test
	public void getSuperUnitName_whenArgIsEmpty_throwsException() {
		// Arrange
		final FullQualifiedNameBuilder b = Builders.fullQualifiedName();
		b.addFragment("foo").addFragment("bar");
		expected.expect(IllegalArgumentException.class);
		
		// Act
		b.build().getSubUnitName("");
			
		// Assert
	}
}
