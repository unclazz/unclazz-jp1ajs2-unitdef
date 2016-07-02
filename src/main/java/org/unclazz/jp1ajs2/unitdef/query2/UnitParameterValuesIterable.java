package org.unclazz.jp1ajs2.unitdef.query2;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;

final class UnitParameterValuesIterable implements Iterable<ParameterValue> {
	private static final class IteratorBasedFlattenIterator implements Iterator<ParameterValue>{
		private final Iterator<Parameter> baseIterator;
		private Iterator<ParameterValue> subIterator;

		private IteratorBasedFlattenIterator(final Iterator<Parameter> baseIterator) {
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
					subIterator = baseIterator.next().getValues().iterator();
				} else {
					subIterator = ZeroIterator.getInstance();
				}
			}
			return subIterator.hasNext() || baseIterator.hasNext();
		}

		@Override
		public ParameterValue next() {
			while (hasNext()) {
				while (hasNext()) {
					if (subIterator.hasNext()) {
						return subIterator.next();
					} else if (baseIterator.hasNext()) {
						subIterator = baseIterator.next().getValues().iterator();
					}
				}
			}
			throw new NoSuchElementException();
		}
	}

	private final Iterable<Parameter> baseIterable;
	
	UnitParameterValuesIterable(final Iterable<Parameter> baseIterable) {
		this.baseIterable = baseIterable;
	}
	
	@Override
	public Iterator<ParameterValue> iterator() {
		return new IteratorBasedFlattenIterator(baseIterable.iterator());
	}
}
