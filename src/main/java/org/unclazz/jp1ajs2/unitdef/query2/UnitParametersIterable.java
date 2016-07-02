package org.unclazz.jp1ajs2.unitdef.query2;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.Unit;

final class UnitParametersIterable implements Iterable<Parameter> {
	private static final class IteratorBasedFlattenIterator implements Iterator<Parameter>{
		private final Iterator<Unit> baseIterator;
		private Iterator<Parameter> subIterator;
		private IteratorBasedFlattenIterator
		(final Iterator<Unit> baseIterator) {
			this.baseIterator = baseIterator;
		}
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		@Override
		public boolean hasNext() {
			if (subIterator == null) {
				if (baseIterator.hasNext()) {
					subIterator = baseIterator.next().getParameters().iterator();
				} else {
					subIterator = ZeroIterator.getInstance();
				}
			}
			return subIterator.hasNext() || baseIterator.hasNext();
		}

		@Override
		public Parameter next() {
			while (hasNext()) {
				if (subIterator.hasNext()) {
					return subIterator.next();
				} else if (baseIterator.hasNext()) {
					subIterator = baseIterator.next().getParameters().iterator();
				}
			}
			throw new NoSuchElementException();
		}
	}

	private final Iterable<Unit> baseIterable;
	
	UnitParametersIterable(final Iterable<Unit> baseIterable) {
		this.baseIterable = baseIterable;
	}
	
	@Override
	public Iterator<Parameter> iterator() {
		return new IteratorBasedFlattenIterator(baseIterable.iterator());
	}
}
