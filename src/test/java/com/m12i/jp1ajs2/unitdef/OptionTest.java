package com.m12i.jp1ajs2.unitdef;

import static org.junit.Assert.*;

import org.junit.Test;

import com.m12i.jp1ajs2.unitdef.Maybe;

public class OptionTest {

	@Test
	public void 静的メソッドnoneはNoneを返す() {
		assertTrue(Maybe.nothing().isNothing());
		assertTrue(Maybe.nothing().equals(Maybe.NOTHING));
	}

	@Test
	public void 静的メソッドsomeは第1引数をラップしたSomeを返す() {
		assertTrue(Maybe.just("hello").isOne());
		assertTrue(Maybe.just("hello").get().equals("hello"));
	}

	@Test
	public void 静的メソッドwrapは第1引数に応じてSomeもしくはNoneを返す() {
		assertTrue(Maybe.wrap(null).isNothing());
		assertTrue(Maybe.wrap("hello").isOne());
		assertTrue(Maybe.wrap("hello").get().equals("hello"));
	}

}
