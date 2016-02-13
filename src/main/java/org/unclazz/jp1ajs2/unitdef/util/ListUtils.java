package org.unclazz.jp1ajs2.unitdef.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class ListUtils {
	private static final List<?> emptyImmutableList = Collections.emptyList();
	
	@SuppressWarnings("unchecked")
	public static<T> List<T> emptyImmutableList() {
		return (List<T>) emptyImmutableList;
	}
	
	public static<T> LinkedList<T> linkedList() {
		return new LinkedList<T>();
	}
	
	public static<T> LinkedList<T> linkedList(Collection<T> c) {
		return new LinkedList<T>(c);
	}
	
	public static<T> ArrayList<T> arrayList() {
		return new ArrayList<T>();
	}
	
	public static<T> ArrayList<T> arrayList(Collection<T> c) {
		return new ArrayList<T>(c);
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
