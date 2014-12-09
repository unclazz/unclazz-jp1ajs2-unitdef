package com.m12i.jp1ajs2.unitdef;

import static org.junit.Assert.*;

import org.junit.Test;

import com.m12i.jp1ajs2.unitdef.OneIterator;
import com.m12i.jp1ajs2.unitdef.Option;

import static org.hamcrest.CoreMatchers.*;

public class SomeTest {

	@Test
	public void equalsはラップするオブジェクト同士が等価であるときのみtrueを返す() {
		assertTrue(Option.some("hello").equals(Option.some("hello")));
		assertFalse(Option.some("hello").equals(Option.some("hello_")));
		assertFalse(Option.some("hello").equals(Option.some(new Object())));
		assertFalse(Option.some("hello").equals(Option.some(1)));
		assertFalse(Option.some("hello").equals(Option.none()));
	}

	@Test
	public void hashCodeはラップするオブジェクトに応じた値を返す() {
		assertTrue(Option.some("hello").hashCode() == Option.some("hello").hashCode());
		assertFalse(Option.some("hello").hashCode() == "hello".hashCode());
		assertFalse(Option.some("hello").hashCode() == Option.some("hello_").hashCode());
		assertFalse(Option.some("hello").hashCode() == Option.some(1).hashCode());
		assertFalse(Option.some("hello").hashCode() == Option.none().hashCode());
	}

	@Test
	public void toStringはラップするオブジェクトに応じた値を返す() {
		assertThat(Option.some("hello").toString(), is("Some(hello)"));
		assertThat(Option.some(1).toString(), is("Some(1)"));
	}

	@Test
	public void iteratorはOneIteratorインスタンスを返す() {
		assertTrue(Option.some("hello").iterator() instanceof OneIterator);
		int count = 0;
		for (String s : Option.some("hello")) {
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
		assertThat(Option.some("hello").get(), is("hello"));
	}

	@Test
	public void getOrElseは必ずラップしたオブジェクトを返す() {
		final Option<String> some0 = Option.some("hello");
		assertThat(some0.getOrElse("hello_"), is("hello"));
		assertThat(some0.getOrElse("bonjour"), is("hello"));
		assertThat(some0.getOrElse(null), is("hello"));
		final Option<Integer> some1 = Option.some(-1);
		assertThat(some1.getOrElse(0), is(-1));
		assertThat(some1.getOrElse(1), is(-1));
		assertThat(some1.getOrElse(null), is(-1));
	}

	@Test
	public void isSomeは必ずtrueを返す() {
		final Option<String> some0 = Option.some("hello");
		assertTrue(some0.isSome());
	}

	@Test
	public void isNoneは必ずfalseを返す() {
		final Option<String> some0 = Option.some("hello");
		assertFalse(some0.isNone());
	}
}
