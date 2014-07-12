package usertools.jp1ajs2.unitdef.util;

import java.util.Iterator;

public class Either<L, R> implements Iterable<R> {
	public static<L, R> Either<L, R> left(L l) {
		return new Either<L, R>(l, null);
	}
	public static<L, R> Either<L, R> right(R r) {
		return new Either<L, R>(null, r);
	}
	public static<R> Either<Throwable, R> failure(String message) {
		return new Either<Throwable, R>(new RuntimeException(message), null);
	}
	public static<R> Either<Throwable, R> failure(Throwable error) {
		return new Either<Throwable, R>(error, null);
	}
	public static<R> Either<Throwable, R> success(R r) {
		return new Either<Throwable, R>(null, r);
	}
	
	private final L l;
	private final R r;
	public Either (L left, R right) {
		this.l = left;
		this.r = right;
	}
	public boolean isLeft() {
		return l != null;
	}
	public boolean isRight() {
		return l == null;
	}
	public L left() {
		if (isLeft()) {
			return l;
		}
		throw new RuntimeException();
	}
	public R right() {
		if (isRight()) {
			return r;
		}
		throw new RuntimeException();
	}
	public Iterator<R> iterator() {
		return isLeft() ? new ZeroIterator<R>() : new OneIterator<R>(r);
	}
	public Option<R> toOption() {
		if (isLeft()) {
			return Option.none();
		} else {
			return Option.some(r);
		}
	}
	@Override
	public String toString() {
		return (isLeft() ? "Left(" : "Right(") + left() + ")";
	}
	@Override
	public boolean equals(Object other) {
		if (other == null || other.getClass() != Either.class) {
			return false;
		}
		Either<?, ?> that = (Either<?, ?>) other;
		if (this.isLeft() != that.isRight()) {
			return false;
		}
		return this.isLeft() ? (this.left().equals(that.left())) : (this.right().equals(that.right()));
	}
	@Override
	public int hashCode() {
		return 37 * (this.isLeft() ? this.left() : this.right()).hashCode();
	}
}
