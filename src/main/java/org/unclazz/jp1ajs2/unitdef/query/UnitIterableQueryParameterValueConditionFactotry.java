package org.unclazz.jp1ajs2.unitdef.query;

import static org.unclazz.jp1ajs2.unitdef.query.QueryUtils.*;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.Yield;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.YieldCallable;

/**
 * ユニット定義パラメータ値条件を付与された{@link UnitIterableQuery}を生成するためのファクトリ.
 * <p>デフォルトでは値のマッチングは1つ目のユニット定義パラメータ値（添字は0）を対象に行われる。
 * この動作は{@link #valueAt(int)}メソッドでマッチング対象の値を添字により指定することで変更することができる。</p>
 */
public final class UnitIterableQueryParameterValueConditionFactotry {
	private final String parameterName;
	private final int valueIndex;
	private final Query<Unit, Iterable<Unit>> srcQuery;
	private final List<Predicate<Unit>> preds;
	
	UnitIterableQueryParameterValueConditionFactotry(
			final Query<Unit, Iterable<Unit>> srcQuery,
			final List<Predicate<Unit>> preds,
			final String parameterName) {
		this(srcQuery, preds, parameterName, -1);
	}
	
	UnitIterableQueryParameterValueConditionFactotry(
			final Query<Unit, Iterable<Unit>> srcQuery,
			final List<Predicate<Unit>> preds,
			final String parameterName,
			final int valueIndex) {
		this.srcQuery = srcQuery;
		this.preds = preds;
		this.parameterName = parameterName;
		this.valueIndex = valueIndex;
	}
	
	private UnitIterableQuery createQueryWithNewPredicate(final Predicate<Unit> newPred) {
		final LinkedList<Predicate<Unit>> newPreds = new LinkedList<Predicate<Unit>>();
		newPreds.addAll(preds);
		newPreds.add(newPred);
		return new DefaultUnitIterableQuery(srcQuery, newPreds);
	}
	
	private Iterable<String> fetchParameterValues(final Unit u) {
		return LazyIterable.forEach(u.getParameters(), 
				new YieldCallable<Parameter,String>(){
			@Override
			public Yield<String> yield(final Parameter item, final int index) {
				if (!item.getName().equals(parameterName)) {
					return Yield.yieldVoid();
				}
				if (!(item.getValues().size() > valueIndex)) {
					return Yield.yieldVoid();
				}
				return Yield.yieldReturn(item.getValues().
						get(valueIndex == -1 ? 0 : valueIndex).getStringValue());
			}
		});
	}
	
	/**
	 * マッチング対象のパラメータ値を添字指定する.
	 * @param i 添字
	 * @return ファクトリ
	 * @throws IllegalStateException ファクトリにすでに添字指定がなされていた場合
	 */
	public UnitIterableQueryParameterValueConditionFactotry valueAt(final int i) {
		assertTrue(i >= 0, "argument must be greater than or equal 0.");
		if (valueIndex != -1) {
			throw new IllegalStateException("value index has been specified.");
		}
		return new UnitIterableQueryParameterValueConditionFactotry(srcQuery, preds, parameterName, i);
	}
	
	/**
	 * パラメータ値の存在だけを条件とするクエリを生成する.
	 * @return クエリ
	 */
	public UnitIterableQuery anyValue() {
		return createQueryWithNewPredicate(new Predicate<Unit>() {
			@Override
			public boolean test(Unit t) {
				for (final Parameter p : t.getParameters()) {
					if (p.getName().equals(parameterName) && p.getValues().size() > valueIndex) {
						return true;
					}
				}
				return false;
			}
		});
	}
	
	/**
	 * パラメータ値と文字列が適合するかを条件とするクエリを生成する.
	 * @param s 文字列
	 * @return クエリ
	 */
	public UnitIterableQuery contentEquals(final CharSequence s) {
		return createQueryWithNewPredicate(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit t) {
				for (final String v : fetchParameterValues(t)) {
					if (CharSequenceUtils.contentsAreEqual(v, s)) {
						return true;
					}
				}
				return false;
			}
		});
	}
	
	/**
	 * パラメータ値と部分文字列が適合するかを条件とするクエリを生成する.
	 * @param s 部分文字列
	 * @return クエリ
	 */
	public UnitIterableQuery startsWith(final CharSequence s) {
		return createQueryWithNewPredicate(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit t) {
				for (final String v : fetchParameterValues(t)) {
					if (CharSequenceUtils.arg0StartsWithArg1(v, s)) {
						return true;
					}
				}
				return false;
			}
		});
	}
	
	/**
	 * パラメータ値と部分文字列が適合するかを条件とするクエリを生成する.
	 * @param s 部分文字列
	 * @return クエリ
	 */
	public UnitIterableQuery endsWith(final CharSequence s) {
		return createQueryWithNewPredicate(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit t) {
				for (final String v : fetchParameterValues(t)) {
					if (CharSequenceUtils.arg0EndsWithArg1(v, s)) {
						return true;
					}
				}
				return false;
			}
		});
	}
	
	/**
	 * パラメータ値と部分文字列が適合するかを条件とするクエリを生成する.
	 * @param s 部分文字列
	 * @return クエリ
	 */
	public UnitIterableQuery contains(final CharSequence s) {
		return createQueryWithNewPredicate(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit t) {
				for (final String v : fetchParameterValues(t)) {
					if (CharSequenceUtils.arg0ContainsArg1(v, s)) {
						return true;
					}
				}
				return false;
			}
		});
	}
	
	/**
	 * パラメータ値と正規表現パターンが適合するかを条件とするクエリを生成する.
	 * @param regex 正規表現パターン
	 * @return クエリ
	 */
	public UnitIterableQuery matches(final Pattern regex) {
		return createQueryWithNewPredicate(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit t) {
				for (final String v : fetchParameterValues(t)) {
					if (regex.matcher(v).matches()) {
						return true;
					}
				}
				return false;
			}
		});
	}
	
	/**
	 * パラメータ値と正規表現パターンが適合するかを条件とするクエリを生成する.
	 * @param regex 正規表現パターン
	 * @return クエリ
	 */
	public UnitIterableQuery matches(final CharSequence regex) {
		return matches(Pattern.compile(regex.toString()));
	}
	
	/**
	 * タプル条件を付与された{@link UnitIterableQuery}を生成するためのファクトリを返す.
	 * @return ファクトリ
	 */
	public UnitIterableQueryTupleConditionFactotry typeIsTuple() {
		return new UnitIterableQueryTupleConditionFactotry(srcQuery, preds, 
				parameterName, valueIndex);
	}
}
