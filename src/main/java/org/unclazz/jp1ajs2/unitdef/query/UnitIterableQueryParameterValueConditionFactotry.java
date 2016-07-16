package org.unclazz.jp1ajs2.unitdef.query;

import static org.unclazz.jp1ajs2.unitdef.query.QueryUtils.*;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;
import org.unclazz.jp1ajs2.unitdef.util.Function;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;

/**
 * ユニット定義パラメータ値条件を付与された{@link UnitIterableQuery}を生成するためのファクトリ.
 * <p>デフォルトでは値のマッチングはユニット定義パラメータ値全体（カンマ切りで1～N個の値を持つ）のシーケンスを対象に行われる。
 * この動作は{@link #valueAt(int)}メソッドでマッチング対象の値を添字により指定することで変更することができる。</p>
 */
public class UnitIterableQueryParameterValueConditionFactotry {
	private final String parameterName;
	private final int valueIndex;
	private final Function<Unit, Iterable<Unit>> func;
	private final List<Predicate<Unit>> preds;
	
	UnitIterableQueryParameterValueConditionFactotry(
			final Function<Unit, Iterable<Unit>> func,
			final List<Predicate<Unit>> preds,
			final String parameterName) {
		this(func, preds, parameterName, -1);
	}
	
	UnitIterableQueryParameterValueConditionFactotry(
			final Function<Unit, Iterable<Unit>> func,
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
	
	private CharSequence fetchParameterValue(final Unit u) {
		for (final Parameter p : u.getParameters()) {
			if (!p.getName().equals(parameterName)) {
				continue;
			}
			if (!(p.getValues().size() > valueIndex)) {
				continue;
			}
			if (valueIndex == -1) {
				final StringBuilder b = new StringBuilder();
				for (final ParameterValue v : p.getValues()) {
					if (b.length() > 0) {
						b.append(',');
					}
					b.append(v.serialize());
				}
				return b;
			}
			return p.getValues().get(valueIndex).getString();
		}
		return null;
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
		return new UnitIterableQueryParameterValueConditionFactotry(func, preds, parameterName, i);
	}
	
	/**
	 * パラメータ値が存在だけを条件とするクエリを生成する.
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
				final CharSequence cs = fetchParameterValue(t);
				return cs != null && CharSequenceUtils.contentsAreEqual(cs, s);
			}
		});
	}
	
	/**
	 * パラメータ値と部分文字列が適合するかを条件とするクエリを生成する.
	 * @param s 部分文字列
	 * @return クエリ
	 */
	public UnitIterableQuery startsWith(final String s) {
		return createQueryWithNewPredicate(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit t) {
				final CharSequence cs = fetchParameterValue(t);
				return cs != null && CharSequenceUtils.arg0StartsWithArg1(cs, s);
			}
		});
	}
	
	/**
	 * パラメータ値と部分文字列が適合するかを条件とするクエリを生成する.
	 * @param s 部分文字列
	 * @return クエリ
	 */
	public UnitIterableQuery endsWith(final String s) {
		return createQueryWithNewPredicate(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit t) {
				final CharSequence cs = fetchParameterValue(t);
				return cs != null && CharSequenceUtils.arg0EndsWithArg1(cs, s);
			}
		});
	}
	
	/**
	 * パラメータ値と部分文字列が適合するかを条件とするクエリを生成する.
	 * @param s 部分文字列
	 * @return クエリ
	 */
	public UnitIterableQuery contains(final String s) {
		return createQueryWithNewPredicate(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit t) {
				final CharSequence cs = fetchParameterValue(t);
				return cs != null && CharSequenceUtils.arg0ContainsArg1(cs, s);
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
				final CharSequence cs = fetchParameterValue(t);
				return cs != null && regex.matcher(cs).matches();
			}
		});
	}
	
	/**
	 * パラメータ値と正規表現パターンが適合するかを条件とするクエリを生成する.
	 * @param regex 正規表現パターン
	 * @return クエリ
	 */
	public UnitIterableQuery matches(final String regex) {
		return matches(Pattern.compile(regex));
	}
	
	/**
	 * タプル条件を付与された{@link UnitIterableQuery}を生成するためのファクトリを返す.
	 * @return ファクトリ
	 */
	public UnitIterableQueryTupleConditionFactotry typeIsTuple() {
		return new UnitIterableQueryTupleConditionFactotry(func, preds, 
				parameterName, valueIndex);
	}
}
