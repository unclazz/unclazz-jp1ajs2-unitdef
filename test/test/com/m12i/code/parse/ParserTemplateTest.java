package test.com.m12i.code.parse;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;

import org.junit.Test;

import com.m12i.code.parse.Parsable;
import com.m12i.code.parse.ParseException;
import com.m12i.code.parse.ParserTemplate;

public class ParserTemplateTest {

	private static class ParserMock extends ParserTemplate<Object> {
		private Parsable code = null;
		public void code(Parsable p) {
			this.code = p;
		}
		public Parsable code() {
			return this.code;
		}
		@Override
		protected Object parseMain() throws ParseException {
			return null;
		}
		@Override
		protected char escapePrefixInSingleQuotes() {
			return '\\';
		}
		@Override
		protected char escapePrefixInDoubleQuotes() {
			return '\\';
		}
		@Override
		protected String lineCommentStart() {
			return "//";
		}
		@Override
		protected String blockCommentStart() {
			return "/*";
		}
		@Override
		protected String blockCommentEnd() {
			return "*/";
		}
		@Override
		protected boolean skipCommentWithSpace() {
			return true;
		}
	}
	
	private static ParserMock createMock(String s) {
		final ParserMock m = new ParserMock();
		m.code(createParsable(s));
		return m;
	}
	
	private static Parsable createParsable(String s) {
		try {
			return new ParserTemplate.InputStreamBasedParsable(s);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Test
	public void blockCommentEndはブロック・コメントの終了文字列を返す() {
		assertThat(createMock("").blockCommentEnd(), is("*/"));
	}
	
	@Test
	public void blockCommentStartはブロック・コメントの開始文字列を返す() {
		assertThat(createMock("").blockCommentStart(), is("/*"));
	}
	
	@Test
	public void codeは読み取り対象のParsableインスタンスを返す() throws IOException {
		final Parsable p = new ParserTemplate.InputStreamBasedParsable("");
		final ParserMock m = new ParserMock();
		m.code(p);
		assertThat(m.code(), is(p));
	}
	
	@Test
	public void columnNoは読み取り位置のある列数を返す() {
		final ParserMock m = createMock("abc\r\ndef\r\n123");
		assertThat(m.columnNo(), is(1));
		m.next();		//  a[b]c
		assertThat(m.columnNo(), is(2));
		m.next();		//  a b[c]
		assertThat(m.columnNo(), is(3));
		m.next();		// [d]e f
		assertThat(m.columnNo(), is(1));
		m.nextLine();	// [1]2 3
		assertThat(m.columnNo(), is(1));
	}
	
	@Test
	public void currentは現在文字を返す() {
		final ParserMock m = createMock("abc\r\ndef\r\n123");
		assertThat(m.current(), is('a'));
		m.next();		//  a[b]c
		assertThat(m.current(), is('b'));
		m.next();		//  a b[c]
		assertThat(m.current(), is('c'));
		m.next();		// [d]e f
		assertThat(m.current(), is('d'));
		m.nextLine();	// [1]2 3
		assertThat(m.current(), is('1'));
		m.nextLine();	// eof
		assertThat(m.current(), is('\u0000'));
	}
	
	@Test
	public void currentIsは現在文字と引数で指定された文字が一致するかどうかを判定する() {
		final ParserMock m = createMock("abc\r\ndef\r\n123");
		assertThat(m.currentIs('a'), is(true));
		assertThat(m.currentIs('b'), is(false));
		m.next();		//  a[b]c
		assertThat(m.currentIs('b'), is(true));
		assertThat(m.currentIs('c'), is(false));
		m.next();		//  a b[c]
		assertThat(m.currentIs('c'), is(true));
		assertThat(m.currentIs('d'), is(false));
		m.next();		// [d]e f
		assertThat(m.currentIs('d'), is(true));
		assertThat(m.currentIs('e'), is(false));
		m.nextLine();	// [1]2 3
		assertThat(m.currentIs('1'), is(true));
		assertThat(m.currentIs('2'), is(false));
		m.nextLine();	// eof
		assertThat(m.currentIs('\u0000'), is(true));
	}
	
	@Test
	public void currentIsAnyOfは現在文字が引数で指定された文字のうちのいずれかと一致するかどうか判定する() {
		final ParserMock m = createMock("abc\r\ndef\r\n123");
		assertThat(m.currentIsAnyOf('a', 'd', '1'), is(true));
		assertThat(m.currentIsAnyOf('b', 'c', 'e', 'f', '2', '3'), is(false));
		m.nextLine();	// [d]e f
		assertThat(m.currentIsAnyOf('a', 'd', '1'), is(true));
		assertThat(m.currentIsAnyOf('b', 'c', 'e', 'f', '2', '3'), is(false));
		m.nextLine();	// [1]2 3
		assertThat(m.currentIsAnyOf('a', 'd', '1'), is(true));
		assertThat(m.currentIsAnyOf('b', 'c', 'e', 'f', '2', '3'), is(false));
		m.nextLine();	// eof
		assertThat(m.currentIsAnyOf('a', 'd', '1'), is(false));
	}
	
	@Test
	public void currentIsFollowedByは現在文字の後方に引数で指定された文字列が続くかどうかを判定する() {
		final ParserMock m = createMock("abc\r\ndef\r\n123");
		assertThat(m.currentIsFollowedBy("bc"), is(true));
		assertThat(m.currentIsFollowedBy("abc"), is(false));
		assertThat(m.currentIsFollowedBy(""), is(false));
		assertThat(m.currentIsFollowedBy(null), is(false));
		m.next();
		m.next();
		assertThat(m.currentIsFollowedBy("c"), is(false));
	}
	
	@Test
	public void currentIsNotはcurrentIsの反対() {
		final ParserMock m = createMock("abc\r\ndef\r\n123");
		assertThat(m.currentIsNot('a'), is(false));
		assertThat(m.currentIsNot('b'), is(true));
		m.next();		//  a[b]c
		assertThat(m.currentIsNot('b'), is(false));
		assertThat(m.currentIsNot('c'), is(true));
		m.next();		//  a b[c]
		assertThat(m.currentIsNot('c'), is(false));
		assertThat(m.currentIsNot('d'), is(true));
		m.next();		// [d]e f
		assertThat(m.currentIsNot('d'), is(false));
		assertThat(m.currentIsNot('e'), is(true));
		m.nextLine();	// [1]2 3
		assertThat(m.currentIsNot('1'), is(false));
		assertThat(m.currentIsNot('2'), is(true));
		m.nextLine();	// eof
		assertThat(m.currentIsNot('\u0000'), is(false));
	}
	
	@Test
	public void currentIsNotAnyOfはcurrentIsAnyOfの反対() {
		final ParserMock m = createMock("abc\r\ndef\r\n123");
		assertThat(m.currentIsNotAnyOf('a', 'd', '1'), is(false));
		assertThat(m.currentIsNotAnyOf('b', 'c', 'e', 'f', '2', '3'), is(true));
		m.nextLine();	// [d]e f
		assertThat(m.currentIsNotAnyOf('a', 'd', '1'), is(false));
		assertThat(m.currentIsNotAnyOf('b', 'c', 'e', 'f', '2', '3'), is(true));
		m.nextLine();	// [1]2 3
		assertThat(m.currentIsNotAnyOf('a', 'd', '1'), is(false));
		assertThat(m.currentIsNotAnyOf('b', 'c', 'e', 'f', '2', '3'), is(true));
		m.nextLine();	// eof
		assertThat(m.currentIsNotAnyOf('a', 'd', '1'), is(true));
	}
	
	@Test
	public void currentIsNotFollowedByはcurrentIsFollowedByの反対() {
		final ParserMock m = createMock("abc\r\ndef\r\n123");
		assertThat(m.currentIsNotFollowedBy("bc"), is(false));
		assertThat(m.currentIsNotFollowedBy("abc"), is(true));
		assertThat(m.currentIsNotFollowedBy(""), is(true));
		assertThat(m.currentIsNotFollowedBy(null), is(true));
		m.next();
		m.next();
		assertThat(m.currentIsNotFollowedBy("c"), is(true));
	}
	
	@Test
	public void currentMustBeは現在文字が引数で指定された文字と一致するか検証した上でその現在文字を返す() {
		try {
			assertThat(createMock("a").currentMustBe('a'), is('a'));
		} catch (ParseException e) {
			fail();
		}
		try {
			createMock("a").currentMustBe('b');
			fail();
		} catch (ParseException e) {
			// do nothing
		}
	}
	
	@Test
	public void currentMustNotBeはcurrentMustBeの反対() {
		try {
			assertThat(createMock("a").currentMustNotBe('b'), is('a'));
		} catch (ParseException e) {
			fail();
		}
		try {
			createMock("a").currentMustNotBe('a');
			fail();
		} catch (ParseException e) {
			// do nothing
		}
	}
	
	@Test
	public void hasReachedEofは読み取り位置がEOFに到達しているかどうかを返す() throws ParseException {
		final ParserMock m = createMock("abc");
		assertThat(m.hasReachedEof(), is(false));
		m.next();
		assertThat(m.hasReachedEof(), is(false));
		m.next();
		assertThat(m.hasReachedEof(), is(false));
		m.next();
		assertThat(m.hasReachedEof(), is(true));
	}
	
	@Test
	public void lineは読み取り位置のある行を返す() {
		final ParserMock m = createMock("abc\r\ndef\n123");
		assertThat(m.line(), is("abc"));
		m.nextLine();
		assertThat(m.line(), is("def"));
		m.nextLine();
		assertThat(m.line(), is("123"));
		m.nextLine();
		assertNull(m.line());
	}
	
	@Test
	public void lineCommentStartは行コメント開始文字列を返す() {
		assertThat(createMock("a").lineCommentStart(), is("//"));
	}
	
	@Test
	public void lineNoは読み取り位置のある行数を返す() {
		final ParserMock m = createMock("abc\r\ndef\n123");
		assertThat(m.lineNo(), is(1));
		m.nextLine();
		assertThat(m.lineNo(), is(2));
		m.nextLine();
		assertThat(m.lineNo(), is(3));
	}
	
	@Test
	public void nextは読み取り位置を1つ前進させた上でその時点の現在文字を返す() {
		final ParserMock m = createMock("abc\r\ndef\n123");
		assertThat(m.next(), is('b'));
		assertThat(m.next(), is('c'));
		assertThat(m.next(), is('d'));
	}
	
	@Test
	public void nextIsは読み取り位置を1つ前進させたあと現在文字と引数で指定された文字が一致するかどうかを判定する() {
		final ParserMock m1 = createMock("abc\r\ndef\n123");
		assertThat(m1.nextIs('b'), is(true));
		assertThat(m1.nextIs('c'), is(true));
		assertThat(m1.nextIs('d'), is(true));
		final ParserMock m2 = createMock("abc\r\ndef\n123");
		assertThat(m2.nextIs('a'), is(false));
		assertThat(m2.nextIs('b'), is(false));
		assertThat(m2.nextIs('c'), is(false));
	}
	
	@Test
	public void nextIsNotはnextIsの反対() {
		final ParserMock m1 = createMock("abc\r\ndef\n123");
		assertThat(m1.nextIsNot('b'), is(false));
		assertThat(m1.nextIsNot('c'), is(false));
		assertThat(m1.nextIsNot('d'), is(false));
		final ParserMock m2 = createMock("abc\r\ndef\n123");
		assertThat(m2.nextIsNot('a'), is(true));
		assertThat(m2.nextIsNot('b'), is(true));
		assertThat(m2.nextIsNot('c'), is(true));
	}

	@Test
	public void nextLineは読み取り位置を次の行の先頭に移動してその行を返す() {
		final ParserMock m = createMock("abc\r\ndef\n123");
		assertThat(m.nextLine(), is("def"));
		assertThat(m.nextLine(), is("123"));
		assertNull(m.nextLine());
	}
	
	@Test
	public void nextMustBeは読み取り位置を1つ前進させたあと現在文字が引数で指定された文字と一致するか検証した上でその現在文字を返す() {
		try {
			assertThat(createMock("abc").nextMustBe('b'), is('b'));
		} catch (ParseException e) {
			fail();
		}
		try {
			createMock("abc").nextMustBe('a');
			fail();
		} catch (ParseException e) {
			// do nothing.
		}
	}
	
	@Test
	public void nextMustNotBeはnextMustBeの反対() {
		try {
			assertThat(createMock("abc").nextMustNotBe('a'), is('b'));
		} catch (ParseException e) {
			fail();
		}
		try {
			createMock("abc").nextMustNotBe('b');
			fail();
		} catch (ParseException e) {
			// do nothing.
		}
	}
	
	@Test
	public void parseAlphabetはアルファベットのみで構成された文字列を読み取って返す() {
		assertThat(createMock("abcABC123...").parseAlphabet(), is("abcABC"));
	}
	
	@Test
	public void parseAlphanumはアルファベットと数字のみで構成された文字列を読み取って返す() {
		assertThat(createMock("abcABC123...").parseAlphanum(), is("abcABC123"));
	}
	
	@Test
	public void parseQuotedStringはダブルクオテーションもしくはシングルクオテーションで囲われた文字列を読み取って返す() throws ParseException {
		assertThat(createMock("\"abcABC123...\"abc").parseQuotedString(), is("abcABC123..."));
		assertThat(createMock("\"abcABC\\\"123...\"abc").parseQuotedString(), is("abcABC\"123..."));
		assertThat(createMock("\"abcABC\'123...\"abc").parseQuotedString(), is("abcABC'123..."));
		assertThat(createMock("\"abcABC\"123...\"abc").parseQuotedString(), is("abcABC"));
		assertThat(createMock("\"\"abc").parseQuotedString(), is(""));
		try {
			assertThat(createMock("\"abcABC123...").parseQuotedString(), is("abcABC"));
			fail();
		} catch(ParseException e) {
			// do nothing.
		}
	}
	
	@Test
	public void parseUntilは引数で指定された文字が現れる前までの文字列を読み取って返す() throws ParseException {
		assertThat(createMock("abc").parseUntil('c'), is("ab"));
		assertThat(createMock("abc").parseUntil('a'), is(""));
	}
	
	@Test
	public void remainingCodeStartsWithは現在文字も含めパース対象コードの残りの部分が引数で指定された文字列で始まるかどうかを判定する() throws ParseException {
		createMock("a").remainingCodeStartsWith("abc");
	}
	
	@Test
	public void skipCommentはコメント文字列をスキップする() throws ParseException {
		final ParserMock m1 = createMock("//abc\r\ndef\n123");
		m1.skipComment();
		assertThat(m1.current(), is('d'));
		final ParserMock m2 = createMock("/*ab*/c\r\ndef\n123");
		m2.skipComment();
		assertThat(m2.current(), is('c'));
	}
	
	@Test
	public void skipCommentWithSpaceは空白文字列とともにコメントもスキップするかどうかを返す() throws ParseException {
		assertThat(createMock("a").skipCommentWithSpace(), is(true));
	}
	
	@Test
	public void skipSpaceは空白文字からなる文字列をスキップする() throws ParseException {
		final ParserMock m1 = createMock("  //abc\r\n  def\n123");
		m1.skipSpace();
		assertThat(m1.current(), is('d'));
		final ParserMock m2 = createMock("/*abc*/  \r\n  def\n123");
		m2.skipSpace();
		assertThat(m2.current(), is('d'));
	}
	
	@Test
	public void skipWordは引数で指定された文字列をスキップする() throws ParseException {
		final ParserMock m1 = createMock("abcABC...");
		m1.skipWord("abc");
		assertThat(m1.current(), is('A'));
		try{
			final ParserMock m2 = createMock("abcABC...");
			m2.skipWord("acb");
		} catch(ParseException e) {
			// do nothing
		}
	}
	
	@Test
	public void currentIsBetweenは現在文字が引数で指定された2つの文字の間のいずれかの文字であるかどうか判定する() {
		final ParserMock m = createMock("abcde");
		assertThat(m.currentIsBetween('b', 'd'), is(false));
		m.next();
		assertThat(m.currentIsBetween('b', 'd'), is(true));
		m.next();
		assertThat(m.currentIsBetween('b', 'd'), is(true));
		m.next();
		assertThat(m.currentIsBetween('b', 'd'), is(true));
		m.next();
		assertThat(m.currentIsBetween('b', 'd'), is(false));
		
	}
	
	@Test
	public void currentIsNotBetweenはcurrentIsBetweenの反対() {
		final ParserMock m = createMock("abcde");
		assertThat(m.currentIsNotBetween('b', 'd'), is(true));
		m.next();
		assertThat(m.currentIsNotBetween('b', 'd'), is(false));
		m.next();
		assertThat(m.currentIsNotBetween('b', 'd'), is(false));
		m.next();
		assertThat(m.currentIsNotBetween('b', 'd'), is(false));
		m.next();
		assertThat(m.currentIsNotBetween('b', 'd'), is(true));
		
	}
	
}
