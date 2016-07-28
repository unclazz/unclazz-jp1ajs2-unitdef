package org.unclazz.jp1ajs2.unitdef.parameter;

import org.unclazz.jp1ajs2.unitdef.Component;
import org.unclazz.jp1ajs2.unitdef.util.StringUtils;
import org.unclazz.jp1ajs2.unitdef.util.Integral;

abstract class DefaultIntegral implements Integral {
	private final int val;
	DefaultIntegral(int val) {
		this.val = val;
	}
	@Override
	public int intValue() {
		return val;
	}
	@Override
	public long longValue() {
		return val;
	}
	@Override
	public String toString(final int radix) {
		return Integer.toString(val, radix);
	}
	@Override
	public String toString() {
		return serialize().toString();
	}
	@Override
	public CharSequence serialize() {
		final StringBuilder buff = StringUtils.builder().append(val);
		buff.trimToSize();
		return buff;
	}
	@Override
	public int compareTo(final Integral other) {
		return intValue() - other.intValue();
	}
	@Override
	public boolean contentEquals(CharSequence other) {
		return StringUtils.contentsAreEqual(serialize(), other);
	}
	@Override
	public boolean contentEquals(Component other) {
		return contentEquals(other.serialize());
	}
}
