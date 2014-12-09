package com.m12i.jp1ajs2.unitdef.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * {@link Reader}インターフェースの実装を生成するためのユーティリティ.
 */
public final class Readers {
	private Readers() {}
	public static final Reader createReader(final String string) {
		try {
			return new Reader(new ByteArrayInputStream(string.getBytes()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static final Reader parse(final InputStream stream) throws IOException {
		return new Reader(stream, Charset.defaultCharset());
	}
	public static final Reader parse(final InputStream stream, final Charset charset) throws IOException {
		return new Reader(stream, charset);
	}
	public static final Reader parse(final InputStream stream, final String charset) throws IOException {
		return new Reader(stream, charset);
	}
}
