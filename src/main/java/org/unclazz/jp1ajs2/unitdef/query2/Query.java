package org.unclazz.jp1ajs2.unitdef.query2;

public interface Query<T, U> {
	U queryFrom(T t);
}
