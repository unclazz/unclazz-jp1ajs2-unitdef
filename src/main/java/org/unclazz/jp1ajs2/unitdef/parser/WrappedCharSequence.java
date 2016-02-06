package org.unclazz.jp1ajs2.unitdef.parser;

/**
 * 文字列をラップする{@link WrappedSequence}の実装.
 */
public final class WrappedCharSequence implements WrappedSequence {
	private int pos = 0;
	private int mem = 0;
	private CharSequence inner;
	public WrappedCharSequence(CharSequence s) {
		this.inner = s;
	}
	@Override
	public int read() {
		return (inner.length() > pos) ? inner.charAt(pos ++) : -1;
	}
	@Override
	public void close() {
		// Do nothing.
	}
	@Override
	public void mark(int i) {
		mem = pos;
	}
	@Override
	public void reset() {
		pos = mem;
	}
}
