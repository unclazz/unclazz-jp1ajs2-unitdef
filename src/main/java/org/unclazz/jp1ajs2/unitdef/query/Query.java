package org.unclazz.jp1ajs2.unitdef.query;

public interface Query<T, U> {
	U queryFrom(T t);
}
