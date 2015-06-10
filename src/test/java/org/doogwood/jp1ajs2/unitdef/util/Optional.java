package org.doogwood.jp1ajs2.unitdef.util;

import java.util.Collection;
import java.util.NoSuchElementException;

public final class Optional<T> {
	@SuppressWarnings("unchecked")
	public static<T> Optional<T> empty() {
		return (Optional<T>) EMPTY;
	}
	
	public static<T> Optional<T> of(final T value) {
		if (value == null) {
			throw new NullPointerException();
		} else {
			return new Optional<T>(value);
		}
	}
	
	public static<T> Optional<T> ofNullable(final T value) {
		if (value == null) {
			return empty();
		} else {
			return new Optional<T>(value);
		}
	}
	
	public static<T> Optional<T> ofFirst(final Collection<T> values) {
		if (values == null || values.isEmpty()) {
			return empty();
		} else {
			return ofNullable(values.iterator().next());
		}
	}
	
	public static<T> Optional<T> ofFirst(final T[] values) {
		if (values == null || values.length == 0) {
			return empty();
		} else {
			return ofNullable(values[0]);
		}
	}
	
	private static final Optional<?> EMPTY = new Optional<Object>(null);
	
	private final T value;
	
	private Optional(final T value) {
		this.value = value;
	}
	
	public T get() {
		if (value == null) {
			throw new NoSuchElementException();
		} else {
			return value;
		}
	}
	
	public T orElse(final T other) {
		return value == null ? other : value;
	}
	
	public boolean isPresent() {
		return value != null;
	}

	public boolean isNotPresent() {
		return value == null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Optional<?> other = (Optional<?>) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		if (value == null) {
			return "Optional()";
		} else {
			return String.format("Optional(%s)", value);
		}
	}
}
