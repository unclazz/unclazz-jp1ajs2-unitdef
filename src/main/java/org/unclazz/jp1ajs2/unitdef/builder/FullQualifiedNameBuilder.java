package org.unclazz.jp1ajs2.unitdef.builder;

import java.util.List;

import org.unclazz.jp1ajs2.unitdef.FullQualifiedName;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;

import static org.unclazz.jp1ajs2.unitdef.util.ListUtils.*;

public final class FullQualifiedNameBuilder {
	FullQualifiedNameBuilder() {}
	
	private final List<CharSequence> list = linkedList();
	
	public FullQualifiedNameBuilder addFragment(CharSequence cs) {
		if (CharSequenceUtils.indexOf(cs, '/') != -1) {
			throw new IllegalArgumentException("unit-name must not contain '/'.");
		}
		list.add(cs);
		return this;
	}
	
	public FullQualifiedNameBuilder addFragments(final List<CharSequence> fragments) {
		for (final CharSequence cs : fragments) {
			addFragment(cs);
		}
		return this;
	}
	
	public FullQualifiedNameBuilder addFragments(final CharSequence... fragments) {
		for (final CharSequence cs : fragments) {
			addFragment(cs);
		}
		return this;
	}
	
	public FullQualifiedName build() {
		final CharSequence[] array = list.toArray(new CharSequence[list.size()]);
		notNullAndNotEmpty(array);
		return new DefaultFullQualifiedName(array);
	}
	
	private void notNullAndNotEmpty(final CharSequence... args) {
		if (args.length == 0) {
			throw new IllegalArgumentException();
		}
		for (final CharSequence arg : args) {
			if (arg == null) {
				throw new NullPointerException();
			}
			if (arg.length() == 0) {
				throw new IllegalArgumentException();
			}
		}
	}
}
