package com.m12i.code.parse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import com.m12i.code.parse.Parsers.Options;

public abstract class AbstractParser<T> {
	protected final Parsers.Options options;
	protected final Parsers parsers;
	public AbstractParser(final Parsers.Options options) {
		this.options = options;
		this.parsers = new Parsers(options);
	}
	public AbstractParser() {
		this.options = new Options();
		this.parsers = new Parsers(options);
	}
	public final Result<T> parse(final String string) {
		return parse(Readers.createReader(string));
	}
	public final Result<T> parse(final InputStream stream) throws IOException {
		return parse(Readers.parse(stream));
	}
	public final Result<T> parse(final InputStream stream, final Charset charset) throws IOException {
		return parse(Readers.parse(stream, charset));
	}
	public final Result<T> parse(final InputStream stream, final String charset) throws IOException {
		return parse(Readers.parse(stream, charset));
	}
	public abstract Result<T> parse(final Reader reader);
}
