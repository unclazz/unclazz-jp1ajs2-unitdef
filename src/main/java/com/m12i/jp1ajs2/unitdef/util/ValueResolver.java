package com.m12i.jp1ajs2.unitdef.util;

public interface ValueResolver<T> {
	T resolve(String rawValue);
}
