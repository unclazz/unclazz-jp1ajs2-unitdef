package org.unclazz.jp1ajs2.unitdef.query;

import static org.unclazz.jp1ajs2.unitdef.query.QueryUtils.*;

import java.util.LinkedList;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.ParameterValueType;
import org.unclazz.jp1ajs2.unitdef.Tuple;
import org.unclazz.jp1ajs2.unitdef.Tuple.Entry;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.Yield;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.YieldCallable;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;

/**
 * ユニット定義パラメータ値のタプル条件を付与された{@link UnitIterableQuery}を生成するためのファクトリ.
 * <p>デフォルトでは値のマッチングは1つ目のユニット定義パラメータ値（添字は0）を対象に行われる。
 * この動作は{@link #valueAt(int)}メソッドでマッチング対象の値を添字により指定することで変更することができる。</p>
 */
public final class UnitIterableQueryTupleConditionFactotry {
	private final String parameterName;
	private final int valueIndex;
	private final Query<Unit, Iterable<Unit>> func;
	private final List<Predicate<Unit>> preds;
	
	UnitIterableQueryTupleConditionFactotry(
			final Query<Unit, Iterable<Unit>> func,
			final List<Predicate<Unit>> preds,
			final String parameterName) {
		this(func, preds, parameterName, -1);
	}
	
	UnitIterableQueryTupleConditionFactotry(
			final Query<Unit, Iterable<Unit>> func,
			final List<Predicate<Unit>> preds,
			final String parameterName,
			final int valueIndex) {
		this.func = func;
		this.preds = preds;
		this.parameterName = parameterName;
		this.valueIndex = valueIndex;
	}
	
	private UnitIterableQuery createQueryWithNewPredicate(final Predicate<Unit> newPred) {
		final LinkedList<Predicate<Unit>> newPreds = new LinkedList<Predicate<Unit>>();
		newPreds.addAll(preds);
		newPreds.add(newPred);
		return new UnitIterableQuery(func, newPreds);
	}
	
	private Iterable<Tuple> fetchTuples(final Unit u) {
		return LazyIterable.forEach(u.getParameters(), 
				new YieldCallable<Parameter,Tuple>(){
			@Override
			public Yield<Tuple> yield(final Parameter item, final int index) {
				if (!item.getName().equals(parameterName)) {
					return Yield.yieldVoid();
				}
				if (!(item.getValues().size() > valueIndex)) {
					return Yield.yieldVoid();
				}
				final ParameterValue v = item.getValues().get(valueIndex == -1 ? 0 : valueIndex);
				if (v.getType() != ParameterValueType.TUPLE) {
					return Yield.yieldVoid();
				}
				return Yield.yieldReturn(v.getTuple());
			}
		});
	}
	
	/**
	 * マッチング対象のパラメータ値を添字指定する.
	 * @param i 添字
	 * @return ファクトリ
	 * @throws IllegalStateException ファクトリにすでに添字指定がなされていた場合
	 */
	public UnitIterableQueryTupleConditionFactotry valueAt(final int i) {
		assertTrue(i >= 0, "argument must be greater than or equal 0.");
		if (valueIndex != -1) {
			throw new IllegalStateException("value index has been specified.");
		}
		return new UnitIterableQueryTupleConditionFactotry(func, preds, parameterName, i);
	}
	
	/**
	 * タプルのエントリー数を条件とするクエリを生成する.
	 * @param i エントリー数
	 * @return クエリ
	 */
	public UnitIterableQuery entryCount(final int i) {
		return createQueryWithNewPredicate(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit t) {
				for (final Tuple tuple : fetchTuples(t)) {
					if (tuple.size() == i) {
						return true;
					}
				}
				return false;
			}
		});
	}
	
	/**
	 * タプルのエントリーを条件とするクエリを生成する.
	 * @param k エントリー・キー
	 * @param v エントリー値
	 * @return クエリ
	 */
	public UnitIterableQuery hasEntry(final String k, final CharSequence v) {
		return createQueryWithNewPredicate(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit t) {
				for (final Tuple tuple : fetchTuples(t)){
					if (tuple.keySet().contains(k) && tuple.get(k).equals(v.toString())) {
						return true;
					}
				}
				return false;
			}
		});
	}
	
	/**
	 * タプルのエントリー・キーを条件とするクエリを生成する.
	 * @param k エントリー・キー
	 * @return クエリ
	 */
	public UnitIterableQuery hasKey(final String k) {
		return createQueryWithNewPredicate(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit t) {
				for (final Tuple tuple : fetchTuples(t)){
					if (tuple.keySet().contains(k)) {
						return true;
					}
				}
				return false;
			}
		});
	}
	
	/**
	 * タプルのエントリー値を条件とするクエリを生成する.
	 * @param v エントリー値
	 * @return クエリ
	 */
	public UnitIterableQuery hasValue(final CharSequence v) {
		return createQueryWithNewPredicate(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit t) {
				for (final Tuple tuple : fetchTuples(t)){
					for (final Entry e : tuple) {
						if (CharSequenceUtils.contentsAreEqual(e.getValue(), v)) {
							return true;
						}
					}
				}
				return false;
			}
		});
	}
}
