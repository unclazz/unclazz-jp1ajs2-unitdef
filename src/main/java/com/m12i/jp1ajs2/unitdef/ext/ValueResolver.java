package com.m12i.jp1ajs2.unitdef.ext;

public interface ValueResolver<T> {
	T resolve(String rawValue);
}
