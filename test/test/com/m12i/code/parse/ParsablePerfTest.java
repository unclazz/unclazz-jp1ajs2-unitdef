package test.com.m12i.code.parse;

import static org.junit.Assert.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.m12i.code.parse.DefaultParsable;
import com.m12i.code.parse.LazyReadParsable;
import com.m12i.code.parse.Parsable;

public class ParsablePerfTest {

	private static final String filePath = "aiueo.txt";
	
	public static long now() {
		return System.currentTimeMillis();
	}
	
	public static void printDelta(String msg, long start) {
		final long now = System.currentTimeMillis();
		System.out.println(msg + String.format(" (start: %d, end: %d, delta: %d)", start, now, now - start));
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		final FileOutputStream fos = new FileOutputStream(filePath);
		final BufferedOutputStream bos = new BufferedOutputStream(fos);
		final OutputStreamWriter osw = new OutputStreamWriter(bos, Charset.defaultCharset());
		final PrintWriter pr = new PrintWriter(osw);
		for (int i = 0; i < 1000; i++) {
			// あいうえお * 10
			pr.println("あいうえおあいうえおあいうえおあいうえおあいうえおあいうえおあいうえおあいうえおあいうえおあいうえお\r\n");
		}
		pr.close();
	}

	@Test
	public void DefaultParsablePerf性能確認() throws FileNotFoundException, IOException {
		final long start = now();
		final Parsable p = new DefaultParsable(new FileInputStream(filePath), Charset.defaultCharset().name());
		final StringBuilder sb = new StringBuilder();
		while (!p.hasReachedEof()) {
			sb.append(p.current());
			p.next();
		}
		printDelta("DefaultParsablePerf性能確認", start);
		assertTrue(true);
	}

	@Test
	public void LazyReadParsablePerf性能確認() throws FileNotFoundException, IOException {
		final long start = now();
		final Parsable p = new LazyReadParsable(new FileInputStream(filePath), Charset.defaultCharset().name());
		final StringBuilder sb = new StringBuilder();
		while (!p.hasReachedEof()) {
			sb.append(p.current());
			p.next();
		}
		printDelta("LazyReadParsablePerf性能確認", start);
		assertTrue(true);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		new File(filePath).delete();
	}
	
}
