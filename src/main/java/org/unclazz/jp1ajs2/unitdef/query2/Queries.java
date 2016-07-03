package org.unclazz.jp1ajs2.unitdef.query2;

import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.util.Function;
import org.unclazz.jp1ajs2.unitdef.util.UnitTreeNodesIterable;

public final class Queries {
	private Queries() {}
	
	private static final UnitListQuery children 
	= new UnitListQuery(new Function<Unit, Iterable<Unit>>() {
		@Override
		public Iterable<Unit> apply(final Unit t) {
			return t.getSubUnits();
		}
	});
	private static final UnitListQuery descendants 
	= new UnitListQuery(new Function<Unit, Iterable<Unit>>() {
		@Override
		public Iterable<Unit> apply(final Unit t) {
			return UnitTreeNodesIterable.ofBreadthFirst(t, false);
		}
	});
	private static final UnitListQuery descendantsDepthFirst 
	= new UnitListQuery(new Function<Unit, Iterable<Unit>>() {
		@Override
		public Iterable<Unit> apply(final Unit t) {
			return UnitTreeNodesIterable.ofDepthFirst(t, false);
		}
	});
	private static final UnitListQuery itSelfAndDescendants 
	= new UnitListQuery(new Function<Unit, Iterable<Unit>>() {
		@Override
		public Iterable<Unit> apply(final Unit t) {
			return UnitTreeNodesIterable.ofBreadthFirst(t, true);
		}
	});
	private static final UnitListQuery itSelfAndDescendantsDepthFirst 
	= new UnitListQuery(new Function<Unit, Iterable<Unit>>() {
		@Override
		public Iterable<Unit> apply(final Unit t) {
			return UnitTreeNodesIterable.ofDepthFirst(t, true);
		}
	});
	private static final ParameterListQuery parameters
	= new ParameterListQuery(new UnitListQuery(new Function<Unit, Iterable<Unit>>() {
		@Override
		public Iterable<Unit> apply(final Unit t) {
			return IdQuery.<Unit>getInstance().queryFrom(t);
		}
	}));
	
	/**
	 * 子ユニット（直接の下位ユニット）を問合せるクエリを返す.
	 * @return クエリ
	 */
	public static UnitListQuery children() {
		return children;
	}
	/**
	 * 子孫ユニット（直接・間接の下位ユニット）を問合せるクエリを返す.
	 * <p>ユニットの探索は幅優先に行われる。</p>
	 * @return クエリ
	 */
	public static UnitListQuery descendants() {
		return descendants;
	}
	/**
	 * 子孫ユニット（直接・間接の下位ユニット）を問合せるクエリを返す.
	 * <p>ユニットの探索を幅優先で行うか深さ優先で行うかはパラメータで制御できる。</p>
	 * @param depthFirst {@code true}の場合 深さ優先で探索する
	 * @return クエリ
	 */
	public static UnitListQuery descendants(final boolean depthFirst) {
		return depthFirst ? descendantsDepthFirst : descendants;
	}
	/**
	 * そのユニット自身と子孫ユニットを問合せるクエリを返す.
	 * <p>ユニットの探索は幅優先に行われる。</p>
	 * @return クエリ
	 */
	public static UnitListQuery itSelfAndDescendants() {
		return itSelfAndDescendants;
	}
	/**
	 * そのユニット自身と子孫ユニットを問合せるクエリを返す.
	 * <p>ユニットの探索を幅優先で行うか深さ優先で行うかはパラメータで制御できる。</p>
	 * @param depthFirst {@code true}の場合 深さ優先で探索する
	 * @return クエリ
	 */
	public static UnitListQuery itSelfAndDescendants(final boolean depthFirst) {
		return depthFirst ? itSelfAndDescendantsDepthFirst : itSelfAndDescendants;
	}
	/**
	 * そのユニットのユニット定義パラメータを問合せるクエリを返す.
	 * @return クエリ
	 */
	public static ParameterListQuery parameters() {
		return parameters;
	}
}
