package usertools.jp1ajs2.unitdef.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class EitherTest {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void コンストラクタは引数に応じてLeftもしくはRightを返す() {
		assertTrue(new Either("error?", null).isLeft());
		assertTrue(new Either("error?", "hello").isLeft());
		assertTrue(new Either(null, "hello").isRight());
	}

	@Test
	public void 静的メソッドleftはLeftを返す() {
		assertTrue(Either.left("hello").isLeft());
	}

	@Test
	public void 静的メソッドrightはRightを返す() {
		assertTrue(Either.right("hello").isRight());
	}

	@Test
	public void 静的メソッドfailure_StringはLeftを返す() {
		assertTrue(Either.failure("hello").isLeft());
	}

	@Test
	public void 静的メソッドfailure_ThrowableはLeftを返す() {
		final Throwable t = new RuntimeException();
		assertTrue(Either.failure(t).isLeft());
		assertTrue(Either.failure(t).left().equals(t));
	}

	@Test
	public void 静的メソッドsuccessはRightを返す() {
		assertTrue(Either.success("hello").isRight());
		assertTrue(Either.success("hello").right().equals("hello"));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void leftは左側の値を返す() {
		assertTrue(new Either("left", "right").left().equals("left"));
		try {
			Either.right("left").left();
			fail();
		} catch(Exception e) {
			// Ok.
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void rightは右側の値を返す() {
		assertTrue(new Either(null, "right").right().equals("right"));
		try {
			Either.left("left").right();
			fail();
		} catch(Throwable e) {
			// Ok.
		}
	}

	@Test
	public void LeftのiteratorはZeroIteratorを返す() {
		assertTrue(Either.left("hello").iterator() instanceof ZeroIterator);
		final Either<String, Integer> e = Either.left("hello");
		for (@SuppressWarnings("unused") Integer i : e) {
			fail();
		}
	}

	@Test
	public void RightのiteratorはOneIteratorを返す() {
		assertTrue(Either.right("hello").iterator() instanceof OneIterator);
		int count = 0;
		for (String s : Either.right("hello")) {
			if (count == 0) {
				count ++;
				assertThat(s, is("hello"));
			} else {
				fail();
			}
		}
	}

	@Test
	public void LeftのtoOptionはNoneを返す() {
		assertTrue(Either.left("hello").toOption().isNone());
	}

	@Test
	public void RightのtoOptionはSomeを返す() {
		assertTrue(Either.right("hello").toOption().isSome());
		assertTrue(Either.right("hello").toOption().get().equals("hello"));
	}

}
