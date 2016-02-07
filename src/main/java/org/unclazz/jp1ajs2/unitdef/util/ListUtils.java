package org.unclazz.jp1ajs2.unitdef.util;

import java.util.Collections;
import java.util.List;

public final class ListUtils {
	private static final List<?> emptyImmutableList = Collections.emptyList();
	
	@SuppressWarnings("unchecked")
	public static<T> List<T> emptyImmutableList() {
		return (List<T>) emptyImmutableList;
	}
	
	public static<T> List<T> singletonImmutableList(final T value) {
		return Collections.singletonList(value);
	}
	
	public static<T> List<T> immutableList(final List<T> values) {
		if (values == null || values.isEmpty()) {
			return emptyImmutableList();
		} else if (values.size() == 1) {
			return singletonImmutableList(values.iterator().next());
		} else {
			return Collections.unmodifiableList(values);
		}
	}
}
