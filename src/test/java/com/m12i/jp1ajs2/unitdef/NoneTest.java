package com.m12i.jp1ajs2.unitdef;

import static org.junit.Assert.*;

import org.junit.Test;

import com.m12i.jp1ajs2.unitdef.Maybe;
import com.m12i.jp1ajs2.unitdef.ZeroIterator;

import static org.hamcrest.CoreMatchers.*;

public class NoneTest {

	@Test
	public void equalsは対象がNoneインスタンスのときのみtrueを返す() {
		assertTrue(Maybe.NOTHING.equals(Maybe.nothing()));
		assertFalse(Maybe.NOTHING.equals(Maybe.wrap("hello")));
	}

	@Test
	public void hashCodeはマイナス1を返す() {
		assertThat(Maybe.NOTHING.hashCode(), is(-1));
	}

	@Test
	public void toStringはNoneを返す() {
		assertThat(Maybe.NOTHING.toString(), is("Nothing"));
	}

	@Test
	public void iteratorはZeroIteratorインスタンスを返す() {
		assertTrue(Maybe.NOTHING.iterator() instanceof ZeroIterator);
		for (@SuppressWarnings("unused") Object o : Maybe.NOTHING) {
			fail("None has no value.");
		}
	}

	@Test
	public void getは例外をnullを返す() {
		assertNull(Maybe.NOTHING.get());
	}

	@Test
	public void getOrElseは必ず第1引数を返す() {
		final Maybe<String> none0 = Maybe.nothing();
		assertThat(none0.getOrElse("hello"), is("hello"));
		assertThat(none0.getOrElse("bonjour"), is("bonjour"));
		assertNull(none0.getOrElse(null));
		final Maybe<Integer> none1 = Maybe.nothing();
		assertThat(none1.getOrElse(0), is(0));
		assertThat(none1.getOrElse(1), is(1));
		assertNull(none1.getOrElse(null));
	}

	@Test
	public void isSomeは必ずfalseを返す() {
		final Maybe<String> none0 = Maybe.nothing();
		assertFalse(none0.isOne());
	}

	@Test
	public void isNoneは必ずtrueを返す() {
		final Maybe<String> none0 = Maybe.nothing();
		assertTrue(none0.isNothing());
	}

}
