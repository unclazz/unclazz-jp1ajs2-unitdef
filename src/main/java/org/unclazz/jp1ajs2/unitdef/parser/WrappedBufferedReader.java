package org.unclazz.jp1ajs2.unitdef.parser;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * リーダーをラップする{@link WrappedSequence}の実装.
 */
public final class WrappedBufferedReader implements WrappedSequence {
	private final BufferedReader inner;
	public WrappedBufferedReader(BufferedReader br) {
		this.inner = br;
	}
	@Override
	public int read() throws IOException {
		return inner.read();
	}
	@Override
	public void close() throws IOException {
		inner.close();
	}
	@Override
	public void mark(int readAheadLimit) throws IOException {
		inner.mark(readAheadLimit);
	}
	@Override
	public void reset() throws IOException {
		inner.reset();
	}
}
