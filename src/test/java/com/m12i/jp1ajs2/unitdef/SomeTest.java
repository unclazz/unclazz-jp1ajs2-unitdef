package com.m12i.jp1ajs2.unitdef;

import static org.junit.Assert.*;

import org.junit.Test;

import com.m12i.jp1ajs2.unitdef.OneIterator;
import com.m12i.jp1ajs2.unitdef.Maybe;

import static org.hamcrest.CoreMatchers.*;

public class SomeTest {

	@Test
	public void equalsはラップするオブジェクト同士が等価であるときのみtrueを返す() {
		assertTrue(Maybe.just("hello").equals(Maybe.just("hello")));
		assertFalse(Maybe.just("hello").equals(Maybe.just("hello_")));
		assertFalse(Maybe.just("hello").equals(Maybe.just(new Object())));
		assertFalse(Maybe.just("hello").equals(Maybe.just(1)));
		assertFalse(Maybe.just("hello").equals(Maybe.nothing()));
	}

	@Test
	public void hashCodeはラップするオブジェクトに応じた値を返す() {
		assertTrue(Maybe.just("hello").hashCode() == Maybe.just("hello").hashCode());
		assertFalse(Maybe.just("hello").hashCode() == "hello".hashCode());
		assertFalse(Maybe.just("hello").hashCode() == Maybe.just("hello_").hashCode());
		assertFalse(Maybe.just("hello").hashCode() == Maybe.just(1).hashCode());
		assertFalse(Maybe.just("hello").hashCode() == Maybe.nothing().hashCode());
	}

	@Test
	public void toStringはラップするオブジェクトに応じた値を返す() {
		assertThat(Maybe.just("hello").toString(), is("Just(hello)"));
		assertThat(Maybe.just(1).toString(), is("Just(1)"));
	}

	@Test
	public void iteratorはOneIteratorインスタンスを返す() {
		assertTrue(Maybe.just("hello").iterator() instanceof OneIterator);
		int count = 0;
		for (String s : Maybe.just("hello")) {
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
		assertThat(Maybe.just("hello").get(), is("hello"));
	}

	@Test
	public void getOrElseは必ずラップしたオブジェクトを返す() {
		final Maybe<String> some0 = Maybe.just("hello");
		assertThat(some0.getOrElse("hello_"), is("hello"));
		assertThat(some0.getOrElse("bonjour"), is("hello"));
		assertThat(some0.getOrElse(null), is("hello"));
		final Maybe<Integer> some1 = Maybe.just(-1);
		assertThat(some1.getOrElse(0), is(-1));
		assertThat(some1.getOrElse(1), is(-1));
		assertThat(some1.getOrElse(null), is(-1));
	}

	@Test
	public void isSomeは必ずtrueを返す() {
		final Maybe<String> some0 = Maybe.just("hello");
		assertTrue(some0.isOne());
	}

	@Test
	public void isNoneは必ずfalseを返す() {
		final Maybe<String> some0 = Maybe.just("hello");
		assertFalse(some0.isNothing());
	}
}
