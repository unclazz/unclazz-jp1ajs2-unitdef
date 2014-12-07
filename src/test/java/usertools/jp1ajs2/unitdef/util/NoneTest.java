package usertools.jp1ajs2.unitdef.util;

import static org.junit.Assert.*;

import org.junit.Test;

import usertools.jp1ajs2.unitdef.util.Option.NoneHasNoValueException;
import static org.hamcrest.CoreMatchers.*;

public class NoneTest {

	@Test
	public void equalsは対象がNoneインスタンスのときのみtrueを返す() {
		assertTrue(Option.NONE.equals(Option.none()));
		assertFalse(Option.NONE.equals(Option.some("hello")));
	}

	@Test
	public void hashCodeはマイナス1を返す() {
		assertThat(Option.NONE.hashCode(), is(-1));
	}

	@Test
	public void toStringはNoneを返す() {
		assertThat(Option.NONE.toString(), is("None"));
	}

	@Test
	public void iteratorはZeroIteratorインスタンスを返す() {
		assertTrue(Option.NONE.iterator() instanceof ZeroIterator);
		for (@SuppressWarnings("unused") Object o : Option.NONE) {
			fail("None has no value.");
		}
	}

	@Test
	public void getは例外をスローする() {
		try {
			Option.NONE.get();
			fail("None has no value.");
		} catch(NoneHasNoValueException e) {
			// Ok.
		}
	}

	@Test
	public void getOrElseは必ず第1引数を返す() {
		final Option<String> none0 = Option.none();
		assertThat(none0.getOrElse("hello"), is("hello"));
		assertThat(none0.getOrElse("bonjour"), is("bonjour"));
		assertNull(none0.getOrElse(null));
		final Option<Integer> none1 = Option.none();
		assertThat(none1.getOrElse(0), is(0));
		assertThat(none1.getOrElse(1), is(1));
		assertNull(none1.getOrElse(null));
	}

	@Test
	public void isSomeは必ずfalseを返す() {
		final Option<String> none0 = Option.none();
		assertFalse(none0.isSome());
	}

	@Test
	public void isNoneは必ずtrueを返す() {
		final Option<String> none0 = Option.none();
		assertTrue(none0.isNone());
	}

	@Test
	public void toEitherは必ずLeftを返す() {
		final Option<String> none0 = Option.none();
		assertTrue(none0.toEither().isLeft());
	}

	@Test
	public void toEither_Stringは必ずLeftを返す() {
		final Option<String> none0 = Option.none();
		assertTrue(none0.toEither("hello").isLeft());
	}

}
