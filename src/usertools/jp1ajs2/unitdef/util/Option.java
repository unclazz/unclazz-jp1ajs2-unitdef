package usertools.jp1ajs2.unitdef.util;

import java.util.Iterator;

public abstract class Option<T> implements Iterable<T>{
	public static final class NoneHasNoValueException extends RuntimeException {
		private static final long serialVersionUID = -2964252894387685610L;
	}
	public static final class Some<T> extends Option<T> {
		private final T v;
		public Some(T value) {
			this.v = value;
		}
		@Override
		public Iterator<T> iterator() {
			return new OneIterator<T>(v);
		}
		@Override
		public boolean isSome() {
			return true;
		}
		@Override
		public boolean isNone() {
			return false;
		}
		@Override
		public T get() {
			return v;
		}
		@Override
		public String toString() {
			return "Some(" + v + ")";
		}
		@Override
		public boolean equals(Object other) {
			if (other == null || other.getClass() != Some.class) {
				return false;
			}
			Some<?> that = (Some<?>) other;
			Object thatValue = that.get();
			return v.equals(thatValue);
		}
		@Override
		public int hashCode() {
			return 37 * v.hashCode();
		}
	}
	
	public static final class None<T> extends Option<T> {
		@Override
		public Iterator<T> iterator() {
			return new ZeroIterator<T>();
		}
		@Override
		public boolean isSome() {
			return false;
		}
		@Override
		public boolean isNone() {
			return true;
		}
		@Override
		public T get() {
			throw new NoneHasNoValueException();
		}
		@Override
		public String toString() {
			return "None";
		}
		@Override
		public boolean equals(Object other) {
			return !(other == null || other.getClass() != None.class);
		}
		@Override
		public int hashCode() {
			return -1;
		}
	}
	public static<T> Option<T> some(final T value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}
		return new Some<T>(value);
	}
	@SuppressWarnings("unchecked")
	public static<T> Option<T> none() {
		return (Option<T>) NONE;
	}
	@SuppressWarnings("rawtypes")
	public static final Option<?> NONE = new None();
	public static<T> Option<T> wrap(final T value) {
		if (value != null) {
			return some(value);
		} else {
			return none();
		}
	}
	public abstract boolean isSome();
	public abstract boolean isNone();
	public abstract T get();
	public T getOrElse(T alt) {
		return isSome() ? get() : alt;
	}
	public Either<Throwable, T> toEither() {
		if (isNone()) {
			return Either.left((Throwable)new NoneHasNoValueException());
		} else {
			return Either.right(get());
		}
	}
	public Either<Throwable, T> toEither(String message) {
		if (isNone()) {
			return Either.failure(message);
		} else {
			return Either.success(get());
		}
	}
}
