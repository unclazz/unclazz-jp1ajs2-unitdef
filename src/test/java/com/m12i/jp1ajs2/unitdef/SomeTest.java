package com.m12i.jp1ajs2.unitdef;

import static org.junit.Assert.*;

import org.junit.Test;

import com.m12i.jp1ajs2.unitdef.OneIterator;
import com.m12i.jp1ajs2.unitdef.Maybe;

import static org.hamcrest.CoreMatchers.*;

public class SomeTest {

	@Test
	public void equalsはラップするオブジェクト同士が等価であるときのみtrueを返す() {
		assertTrue(Maybe.wrap("hello").equals(Maybe.wrap("hello")));
		assertFalse(Maybe.wrap("hello").equals(Maybe.wrap("hello_")));
		assertFalse(Maybe.wrap("hello").equals(Maybe.wrap(new Object())));
		assertFalse(Maybe.wrap("hello").equals(Maybe.wrap(1)));
		assertFalse(Maybe.wrap("hello").equals(Maybe.nothing()));
	}

	@Test
	public void hashCodeはラップするオブジェクトに応じた値を返す() {
		assertTrue(Maybe.wrap("hello").hashCode() == Maybe.wrap("hello").hashCode());
		assertFalse(Maybe.wrap("hello").hashCode() == "hello".hashCode());
		assertFalse(Maybe.wrap("hello").hashCode() == Maybe.wrap("hello_").hashCode());
		assertFalse(Maybe.wrap("hello").hashCode() == Maybe.wrap(1).hashCode());
		assertFalse(Maybe.wrap("hello").hashCode() == Maybe.nothing().hashCode());
	}

	@Test
	public void toStringはラップするオブジェクトに応じた値を返す() {
		assertThat(Maybe.wrap("hello").toString(), is("wrap(hello)"));
		assertThat(Maybe.wrap(1).toString(), is("wrap(1)"));
	}

	@Test
	public void iteratorはOneIteratorインスタンスを返す() {
		assertTrue(Maybe.wrap("hello").iterator() instanceof OneIterator);
		int count = 0;
		for (String s : Maybe.wrap("hello")) {
			if (count == 0) {
				count ++;
				assertThat(s, is("hello"));
			} else {
				fail();
			}
		}
	}

	@Test
	public void getはラップしている値を返す() {
		assertThat(Maybe.wrap("hello").get(), is("hello"));
	}

	@Test
	public void getOrElseは必ずラップしたオブジェクトを返す() {
		final Maybe<String> some0 = Maybe.wrap("hello");
		assertThat(some0.getOrElse("hello_"), is("hello"));
		assertThat(some0.getOrElse("bonjour"), is("hello"));
		assertThat(some0.getOrElse(null), is("hello"));
		final Maybe<Integer> some1 = Maybe.wrap(-1);
		assertThat(some1.getOrElse(0), is(-1));
		assertThat(some1.getOrElse(1), is(-1));
		assertThat(some1.getOrElse(null), is(-1));
	}

	@Test
	public void isSomeは必ずtrueを返す() {
		final Maybe<String> some0 = Maybe.wrap("hello");
		assertTrue(some0.isOne());
	}

	@Test
	public void isNoneは必ずfalseを返す() {
		final Maybe<String> some0 = Maybe.wrap("hello");
		assertFalse(some0.isNothing());
	}
}
