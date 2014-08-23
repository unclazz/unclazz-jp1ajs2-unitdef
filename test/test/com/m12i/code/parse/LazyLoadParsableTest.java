package test.com.m12i.code.parse;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.ByteArrayInputStream;

import org.junit.Test;

import com.m12i.code.parse.LazyLoadParsable;
import com.m12i.code.parse.Reader;

public class LazyLoadParsableTest {

	private LazyLoadParsable newInstance(String code) {
		try {
			return new LazyLoadParsable(new ByteArrayInputStream(code.getBytes("utf-8")));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Test
	public void 対象コードが空文字列の場合hasReachedEofはtrueを返す() {
		final Reader p = newInstance(""); 
		assertTrue(p.hasReachedEof());
	}

	@Test
	public void 対象コードが空文字列の場合hasReachedEolはtrueを返す() {
		final Reader p = newInstance(""); 
		assertTrue(p.hasReachedEol());
	}

	@Test
	public void 対象コードが空文字列の場合columnNoは0を返す() {
		final Reader p = newInstance("");
		assertThat(p.columnNo(), is(1));
	}

	@Test
	public void 対象コードが空文字列の場合currentはヌル文字を返す() {
		final Reader p = newInstance("");
		assertThat(p.current(), is('\u0000'));
	}

	@Test
	public void 対象コードが空文字列の場合lineはnullを返す() {
		final Reader p = newInstance("");
		assertTrue(p.hasReachedEof());
		assertNull(p.line());
	}

	@Test
	public void 対象コードが空文字列の場合lineNoは1を返す() {
		final Reader p = newInstance("");
		assertThat(p.lineNo(), is(1));
	}

	@Test
	public void 対象コードが空文字列の場合nextはヌル文字を返す() {
		final Reader p = newInstance("");
		assertThat(p.next(), is('\u0000'));
	}

	@Test
	public void 現在文字がCRの場合columnNoはCRの行内位置を返す() {
		final Reader p = newInstance("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		assertThat(p.columnNo(), is(4));
	}

	@Test
	public void 現在文字がCRの場合currentはCRを返す() {
		final Reader p = newInstance("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		assertThat(p.current(), is('\r'));
	}

	@Test
	public void 現在文字がCRの場合hasReachedEofはfalseを返す() {
		final Reader p = newInstance("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		assertThat(p.hasReachedEof(), is(false));
	}

	@Test
	public void 現在文字がCRの場合hasReachedEolはtrueを返す() {
		final Reader p = newInstance("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		assertThat(p.hasReachedEol(), is(true));
	}

	@Test
	public void 現在文字がCRの場合lineは現在行を返す() {
		final Reader p = newInstance("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		assertThat(p.line(), is("abc"));
	}

	@Test
	public void 現在文字がCRの場合lineNoは現在位置を返す() {
		final Reader p = newInstance("abc\r\n123");
		assertThat(p.next(), is('b'));
		assertThat(p.next(), is('c'));
		assertThat(p.lineNo(), is(1));
		assertThat(p.next(), is('\r'));
		assertThat(p.lineNo(), is(1));
	}

	@Test
	public void 現在文字がCRの場合nextは現在位置の次の文字を返す() {
		final Reader p = newInstance("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		assertThat(p.next(), is('\n'));
	}

	@Test
	public void 現在文字がLFの場合columnNoはLFの行内位置を返す() {
		final Reader p = newInstance("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		assertThat(p.columnNo(), is(5));
	}
	
	@Test
	public void 現在文字がLFの場合currentはLFを返す() {
		final Reader p = newInstance("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		assertThat(p.current(), is('\n'));
	}

	@Test
	public void 現在文字がLFの場合hasReachedEofはfalseを返す() {
		final Reader p = newInstance("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		assertThat(p.hasReachedEof(), is(false));
	}

	@Test
	public void 現在文字がLFの場合hasReachedEolはtrueを返す() {
		final Reader p = newInstance("abc\r\n123");
		assertThat(p.next(), is('b'));
		assertThat(p.next(), is('c'));
		assertThat(p.next(), is('\r'));
		assertThat(p.next(), is('\n'));
		assertThat(p.hasReachedEol(), is(true));
	}

	@Test
	public void 現在文字がLFの場合lineは現在行を返す() {
		final Reader p = newInstance("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		assertThat(p.line(), is("abc"));
	}

	@Test
	public void 現在文字がLFの場合lineNoは現在位置を返す() {
		final Reader p = newInstance("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		assertThat(p.lineNo(), is(1));
	}

	@Test
	public void 現在文字がLFの場合nextは現在位置の次の文字を返す() {
		final Reader p = newInstance("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		assertThat(p.next(), is('1'));
	}

	@Test
	public void 現在文字が2行目1文字目の場合culumnNoは1を返す() {
		final Reader p = newInstance("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		p.next(); // '1'
		assertThat(p.columnNo(), is(1));
	}

	@Test
	public void 現在文字が2行目1文字目の場合currentは現在文字を返す() {
		final Reader p = newInstance("abc\r\n123");
		assertThat(p.next(), is('b'));
		assertThat(p.next(), is('c'));
		assertThat(p.next(), is('\r'));
		assertThat(p.next(), is('\n'));
		assertThat(p.next(), is('1'));
		assertThat(p.current(), is('1'));
	}

	@Test
	public void 現在文字が2行目1文字目の場合hasReachedEofはfalseを返す() {
		final Reader p = newInstance("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		p.next(); // '1'
		assertThat(p.hasReachedEof(), is(false));
	}

	@Test
	public void 現在文字が2行目1文字目の場合hasReachedEolはfalseを返す() {
		final Reader p = newInstance("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		p.next(); // '1'
		assertThat(p.hasReachedEol(), is(false));
	}

	@Test
	public void 現在文字が2行目1文字目の場合lineは現在行を返す() {
		final Reader p = newInstance("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		p.next(); // '1'
		assertThat(p.line(), is("123"));
	}

	@Test
	public void 現在文字が2行目1文字目の場合lineNoは2を返す() {
		final Reader p = newInstance("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		p.next(); // '1'
		assertThat(p.lineNo(), is(2));
	}

	@Test
	public void 現在文字が2行目1文字目の場合nextは2文字目を返す() {
		final Reader p = newInstance("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		p.next(); // '1'
		assertThat(p.next(), is('2'));
	}

	@Test
	public void 現在文字が2行目末尾EOFにあるときculumnNoは4を返す() {
		final Reader p = newInstance("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		p.next(); // '1'
		p.next(); // '2'
		p.next(); // '3'
		p.next(); // EOF
		assertThat(p.columnNo(), is(1));
	}

	@Test
	public void 現在文字が2行目末尾EOFにあるときcurrentはヌル文字を返す() {
		final Reader p = newInstance("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		p.next(); // '1'
		p.next(); // '2'
		p.next(); // '3'
		p.next(); // EOF
		assertThat(p.current(), is('\u0000'));
	}

	@Test
	public void 現在文字が2行目末尾EOFにあるときhasReachedEofはtrueを返す() {
		final Reader p = newInstance("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		p.next(); // '1'
		p.next(); // '2'
		p.next(); // '3'
		p.next(); // EOF
		assertThat(p.hasReachedEof(), is(true));
	}

	@Test
	public void 現在文字が2行目末尾EOFにあるときhasReachedEolはtrueを返す() {
		final Reader p = newInstance("abc\r\n123");
		assertThat(p.next(), is('b'));
		assertThat(p.next(), is('c'));
		assertThat(p.next(), is('\r'));
		assertThat(p.next(), is('\n'));
		assertThat(p.next(), is('1'));
		assertThat(p.next(), is('2'));
		assertThat(p.next(), is('3'));
		p.next(); // EOF
		assertThat(p.hasReachedEol(), is(true));
	}

	@Test
	public void 現在文字が2行目末尾EOFにあるときlineは現在行を返す() {
		final Reader p = newInstance("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		p.next(); // '1'
		p.next(); // '2'
		p.next(); // '3'
		p.next(); // EOF
		assertNull(p.line());
	}

	@Test
	public void 現在文字が2行目末尾EOFにあるときlineNoは2を返す() {
		final Reader p = newInstance("abc\r\n123");
		assertThat(p.next(), is('b'));
		assertThat(p.next(), is('c'));
		assertThat(p.next(), is('\r'));
		assertThat(p.next(), is('\n'));
		assertThat(p.lineNo(), is(1));
		assertThat(p.next(), is('1'));
		assertThat(p.next(), is('2'));
		assertThat(p.next(), is('3'));
		p.next(); // EOF
		assertThat(p.lineNo(), is(2));
	}

	@Test
	public void 現在文字が2行目末尾EOFにあるときnextはヌル文字を返す() {
		final Reader p = newInstance("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		p.next(); // '1'
		p.next(); // '2'
		p.next(); // '3'
		p.next(); // EOF
		assertThat(p.next(), is('\u0000'));
	}

	@Test
	public void 空行から始まるファイルのパース() {
		final Reader p0 = newInstance("\r\n\r\n");
		assertThat(p0.current(), is('\r'));
		assertThat(p0.columnNo(), is(1));
		assertThat(p0.lineNo(), is(1));
		assertThat(p0.hasReachedEol(), is(true));
		
		assertThat(p0.next(), is('\n'));
		assertThat(p0.columnNo(), is(2));
		assertThat(p0.lineNo(), is(1));
		assertThat(p0.hasReachedEol(), is(true));
		
		assertThat(p0.next(), is('\r'));
		assertThat(p0.columnNo(), is(1));
		assertThat(p0.lineNo(), is(2));
		assertThat(p0.hasReachedEol(), is(true));

		assertThat(p0.next(), is('\n'));
		assertThat(p0.columnNo(), is(2));
		assertThat(p0.lineNo(), is(2));
		assertThat(p0.hasReachedEol(), is(true));

		assertThat(p0.next(), is('\u0000'));
		assertThat(p0.hasReachedEof(), is(true));

		final Reader p1 = newInstance("\r\n");
		assertThat(p1.current(), is('\r'));
		assertThat(p1.columnNo(), is(1));
		assertThat(p1.lineNo(), is(1));
		assertThat(p1.hasReachedEol(), is(true));

		assertThat(p1.next(), is('\n'));
		assertThat(p1.columnNo(), is(2));
		assertThat(p1.lineNo(), is(1));
		assertThat(p1.hasReachedEol(), is(true));

		assertThat(p1.next(), is('\u0000'));
		assertThat(p1.hasReachedEof(), is(true));

		final Reader p2 = newInstance("\r\nABC");
		assertThat(p2.current(), is('\r'));
		assertThat(p2.columnNo(), is(1));
		assertThat(p2.lineNo(), is(1));
		assertThat(p2.hasReachedEol(), is(true));

		assertThat(p2.next(), is('\n'));
		assertThat(p2.columnNo(), is(2));
		assertThat(p2.lineNo(), is(1));
		assertThat(p2.hasReachedEol(), is(true));

		assertThat(p2.next(), is('A'));
		assertThat(p2.columnNo(), is(1));
		assertThat(p2.lineNo(), is(2));
		assertThat(p2.hasReachedEol(), is(false));

		assertThat(p2.next(), is('B'));
		assertThat(p2.columnNo(), is(2));
		assertThat(p2.lineNo(), is(2));
		assertThat(p2.hasReachedEol(), is(false));

		assertThat(p2.next(), is('C'));
		assertThat(p2.columnNo(), is(3));
		assertThat(p2.lineNo(), is(2));
		assertThat(p2.hasReachedEol(), is(false));

		assertThat(p2.next(), is('\u0000'));
		assertThat(p2.hasReachedEof(), is(true));

		final Reader p3 = newInstance("\r\nABC\r\n");
		assertThat(p3.current(), is('\r'));
		assertThat(p3.columnNo(), is(1));
		assertThat(p3.lineNo(), is(1));
		assertThat(p3.hasReachedEol(), is(true));

		assertThat(p3.next(), is('\n'));
		assertThat(p3.columnNo(), is(2));
		assertThat(p3.lineNo(), is(1));
		assertThat(p3.hasReachedEol(), is(true));

		assertThat(p3.next(), is('A'));
		assertThat(p3.columnNo(), is(1));
		assertThat(p3.lineNo(), is(2));
		assertThat(p3.hasReachedEol(), is(false));

		assertThat(p3.next(), is('B'));
		assertThat(p3.columnNo(), is(2));
		assertThat(p3.lineNo(), is(2));
		assertThat(p3.hasReachedEol(), is(false));

		assertThat(p3.next(), is('C'));
		assertThat(p3.columnNo(), is(3));
		assertThat(p3.lineNo(), is(2));
		assertThat(p3.hasReachedEol(), is(false));
		
		assertThat(p3.next(), is('\r'));
		assertThat(p3.columnNo(), is(4));
		assertThat(p3.lineNo(), is(2));
		assertThat(p3.hasReachedEol(), is(true));

		assertThat(p3.next(), is('\n'));
		assertThat(p3.columnNo(), is(5));
		assertThat(p3.lineNo(), is(2));
		assertThat(p3.hasReachedEol(), is(true));

		assertThat(p3.next(), is('\u0000'));
		assertThat(p3.hasReachedEof(), is(true));

		final Reader p4 = newInstance("\r\nABC\r\n\r\nDEF");
		assertThat(p4.current(), is('\r'));
		assertThat(p4.next(), is('\n'));
		assertThat(p4.next(), is('A'));
		assertThat(p4.next(), is('B'));
		assertThat(p4.next(), is('C'));
		assertThat(p4.next(), is('\r'));
		assertThat(p4.next(), is('\n'));
		assertThat(p4.next(), is('\r'));
		assertThat(p4.next(), is('\n'));
		assertThat(p4.next(), is('D'));
		assertThat(p4.next(), is('E'));
		assertThat(p4.next(), is('F'));
		assertThat(p4.next(), is('\u0000'));
		assertThat(p4.hasReachedEof(), is(true));
	}

}
