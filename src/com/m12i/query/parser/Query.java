package com.m12i.query.parser;

import java.util.Collection;
import java.util.List;

public interface Query<E> {
	List<E> selectAllFrom(Collection<E> source);
	E selectOneFrom(Collection<E> source);
}
