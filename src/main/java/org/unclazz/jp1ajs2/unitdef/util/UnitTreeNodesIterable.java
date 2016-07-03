package org.unclazz.jp1ajs2.unitdef.util;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.unclazz.jp1ajs2.unitdef.Unit;

/**
 * 木構造を構成するユニットを反復処理するための{@link Iterable}オブジェクト.
 * <p>初期化の際に指定されたユニットが子孫ユニットの探索の起点となる。
 * 反復処理の対象ユニットのなかに起点となるユニットが含まれるかどうかは初期化時のパラメータで制御できる。</p>
 * <p>{@link #iterator()}が返す{@link Iterator}オブジェクトの
 * {@link Iterator#remove()}メソッドはサポートされていない。</p>
 */
public final class UnitTreeNodesIterable implements Iterable<Unit> {
	private static final class BFSIterator implements Iterator<Unit> {
		private final Queue<Unit> queue = new LinkedList<Unit>();
		private BFSIterator(final Unit root, final boolean includesRoot) {
			if (includesRoot) queue.add(root);
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
	private static final class DFSIterator implements Iterator<Unit> {
		private final Deque<Unit> deq = new LinkedList<Unit>();
		private DFSIterator(final Unit root, final boolean includesRoot) {
			if (includesRoot) deq.add(root);
			deq.addAll(root.getSubUnits());
		}
		@Override
		public boolean hasNext() {
			return !deq.isEmpty();
		}
		@Override
		public Unit next() {
			final Unit next = deq.remove();
			final List<Unit> subUnitList = next.getSubUnits();
			for (int i = subUnitList.size() - 1; 0 <= i; i --) {
				deq.push(subUnitList.get(i));
			}
			return next;
		}
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	/**
	 * 深さ優先探索を行う{@link Iterable}を返す.
	 * @param root 探索の起点となるユニット
	 * @param includesRoot {@code true}の場合 反復子が返すユニットに起点となる要素も含まれる
	 * @return {@link Iterable}インスタンス
	 */
	public static UnitTreeNodesIterable ofDepthFirst(Unit root, boolean includesRoot) {
		return new UnitTreeNodesIterable(root, true, includesRoot);
	}
	/**
	 * 幅優先探索を行う{@link Iterable}を返す.
	 * @param root 探索の起点となるユニット
	 * @param includesRoot {@code true}の場合 反復子が返すユニットに起点となる要素も含まれる
	 * @return {@link Iterable}インスタンス
	 */
	public static UnitTreeNodesIterable ofBreadthFirst(Unit root, boolean includesRoot) {
		return new UnitTreeNodesIterable(root, false, includesRoot);
	}

	private final Unit root;
	private final boolean depthFirst;
	private final boolean includingRoot;
	
	/**
	 * 探索の起点となるユニットを返す.
	 * <p>このユニットは反復子{@link Iterator}から取得できるユニットには含まれない。</p>
	 * @return 探索の起点となるユニット
	 */
	public Unit getRoot() {
		return root;
	}
	/**
	 * 探索が深さ優先で行われるかどうかを示す.
	 * @return {@code true}の場合 探索は深さ優先に行われる
	 */
	public boolean isDepthFirst() {
		return depthFirst;
	}
	
	public boolean isIncludingRoot() {
		return includingRoot;
	}
	
	private UnitTreeNodesIterable(final Unit root, 
	final boolean depthFirst, final boolean includingRoot) {
		this.root = root;
		this.depthFirst = depthFirst;
		this.includingRoot = includingRoot;
	}
	
	@Override
	public Iterator<Unit> iterator() {
		if (depthFirst) {
			return new DFSIterator(root, includingRoot);
		} else {
			return new BFSIterator(root, includingRoot);
		}
	}
}
