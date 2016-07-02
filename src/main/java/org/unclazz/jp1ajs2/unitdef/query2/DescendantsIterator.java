package org.unclazz.jp1ajs2.unitdef.query2;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.unclazz.jp1ajs2.unitdef.Unit;

/**
 * 子孫ユニットを返す{@link Iterable}の実装オブジェクト.
 * <p>初期化の際に指定されたユニットが子孫ユニットの探索の起点となる。ユニットの探索は幅優先で行われる。</p>
 * <p>{@link #iterator()}が返す{@link Iterator}オブジェクトの{@link Iterator#remove()}メソッドはサポートされていない。</p>
 */
final class UnitDescendantsIterable implements Iterable<Unit> {
	private static final class DescendantsIterator implements Iterator<Unit> {
		private final Queue<Unit> queue = new LinkedList<Unit>();
		private DescendantsIterator(final Unit root) {
			queue.addAll(root.getSubUnits());
		}
		@Override
		public boolean hasNext() {
			return !queue.isEmpty();
		}
		@Override
		public Unit next() {
			final Unit next = queue.remove();
			queue.addAll(next.getSubUnits());
			return next;
		}
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private final Unit root;
	
	/**
	 * コンストラクタ.
	 * @param root 子孫ユニット探索の起点となるユニット
	 */
	UnitDescendantsIterable(final Unit root) {
		this.root = root;
	}
	@Override
	public Iterator<Unit> iterator() {
		return new DescendantsIterator(root);
	}
}
