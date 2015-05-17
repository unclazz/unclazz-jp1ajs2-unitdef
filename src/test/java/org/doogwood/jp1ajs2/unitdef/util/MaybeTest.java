package org.doogwood.jp1ajs2.unitdef.util;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.doogwood.jp1ajs2.unitdef.util.Maybe;
import org.junit.Test;

public class MaybeTest {
	@Test
	public void testWrap() {
		assertTrue(Maybe.NOTHING.equals(Maybe.nothing()));
		assertTrue(Maybe.wrap(null).equals(Maybe.nothing()));
		assertThat(Maybe.NOTHING.size(), is(0));
		assertTrue(Maybe.NOTHING.isEmpty());
		assertTrue(Maybe.NOTHING.isNothing());
		assertFalse(Maybe.NOTHING.isJust());
		assertFalse(Maybe.NOTHING.isOne());
		assertFalse(Maybe.NOTHING.isMultiple());
		
		assertThat(Maybe.wrap("hello").get(), is("hello"));
		assertThat(Maybe.wrap("hello").size(), is(1));
		assertThat(Maybe.wrap("hello").isEmpty(), is(false));
		assertThat(Maybe.wrap("hello").isNothing(), is(false));
		assertThat(Maybe.wrap("hello").isOne(), is(true));
		assertThat(Maybe.wrap("hello").isJust(), is(true));
		assertThat(Maybe.wrap("hello").isMultiple(), is(false));

		assertThat(Maybe.wrap("hello", "world").size(), is(2));
		assertThat(Maybe.wrap("hello", "world").isEmpty(), is(false));
		assertThat(Maybe.wrap("hello", "world").isNothing(), is(false));
		assertThat(Maybe.wrap("hello", "world").isOne(), is(false));
		assertThat(Maybe.wrap("hello", "world").isJust(), is(false));
		assertThat(Maybe.wrap("hello", "world").isMultiple(), is(true));
		
		assertThat(Maybe.wrap("foo", "bar", "baz").size(), is(3));
		assertThat(Maybe.wrap("foo", "bar", "baz").isEmpty(), is(false));
		assertThat(Maybe.wrap("foo", "bar", "baz").isNothing(), is(false));
		assertThat(Maybe.wrap("foo", "bar", "baz").isOne(), is(false));
		assertThat(Maybe.wrap("foo", "bar", "baz").isJust(), is(false));
		assertThat(Maybe.wrap("foo", "bar", "baz").isMultiple(), is(true));
		
		assertThat(Maybe.wrap("foo", "bar", "baz", "123", "456").size(), is(5));
		assertThat(Maybe.wrap("foo", "bar", "baz", "123", "456").isNothing(), is(false));
		assertThat(Maybe.wrap("foo", "bar", "baz", "123", "456").isOne(), is(false));
		assertThat(Maybe.wrap("foo", "bar", "baz", "123", "456").isMultiple(), is(true));
	}
	
	@Test
	public void testGet() {
		assertNull(Maybe.nothing().get());
		assertThat(Maybe.wrap("hello").get(), is("hello"));
		assertThat(Maybe.wrap("hello", "world").get(), is("hello"));
	}
	
	@Test
	public void testGetList() {
		assertThat(Maybe.nothing().getList().isEmpty(), is(true));
		assertThat(Maybe.wrap("hello").getList().get(0), is("hello"));
		assertThat(Maybe.wrap("foo", "bar").getList().get(0), is("foo"));
	}
	
	@Test
	public void testGetOrElse() {
		final Maybe<String> m = Maybe.nothing();
		assertThat(m.getOrElse("hello"), is("hello"));
	}
	
	@Test
	public void testIterator() {
		final Maybe<String> m = Maybe.nothing();
		for (final String s : m) {
			fail(s);
		}
		int i = 0;
		for (final String s : Maybe.wrap("hello")) {
			assertThat(s, is("hello"));
			assertTrue(i == 0);
			i ++;
		}
		i = 0;
		for (final String s : Maybe.wrap("hello", "world")) {
			assertThat(s, is(new String[]{"hello", "world"}[i]));
			i ++;
		}
	}
}
