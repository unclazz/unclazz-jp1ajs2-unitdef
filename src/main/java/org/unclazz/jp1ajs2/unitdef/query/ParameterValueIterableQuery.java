package org.unclazz.jp1ajs2.unitdef.query;

import static org.unclazz.jp1ajs2.unitdef.query.QueryUtils.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.ParameterValueType;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;
import org.unclazz.jp1ajs2.unitdef.util.ChunkLazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.ChunkLazyIterable.ChunkYield;
import org.unclazz.jp1ajs2.unitdef.util.ChunkLazyIterable.ChunkYieldCallable;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.Yield;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.YieldCallable;

/**
 * ユニット定義パラメータ値を問合せるクエリ.
 */
public final class ParameterValueIterableQuery
extends IterableQuerySupport<Unit, ParameterValue> {
	
	private final ParameterIterableQuery baseQuery;
	private final List<Predicate<ParameterValue>> preds;
	private final int at;

	ParameterValueIterableQuery(ParameterIterableQuery baseQuery, List<Predicate<ParameterValue>> preds, int index) {
		assertNotNull(baseQuery, "argument must not be null.");
		assertNotNull(preds, "argument must not be null.");
		
		this.baseQuery = baseQuery;
		this.preds = preds;
		this.at = index;
	}

	ParameterValueIterableQuery(ParameterIterableQuery baseQuery, List<Predicate<ParameterValue>> preds) {
		this(baseQuery, preds, -1);
	}

	ParameterValueIterableQuery(ParameterIterableQuery baseQuery) {
		this(baseQuery, Collections.<Predicate<ParameterValue>>emptyList(), -1);
	}
	
	private Iterable<ParameterValue> fetchParameterValues(final Unit t) {
		final Iterable<Parameter> ps = baseQuery.queryFrom(t);
		// パラメータ値の位置の指定状態をチェック
		if (at == -1) {
			// パラメータ値の位置が指定されていない場合「すべて」を対象とする
			return ChunkLazyIterable.forEach(ps,
					new ChunkYieldCallable<Parameter,ParameterValue>(){
				@Override
				public ChunkYield<ParameterValue> yield(final Parameter item, final int index) {
					return ChunkYield.yieldReturn(item.getValues());
				}
			}); 
		}
		// パラメータ値の位置が指定されている場合はその位置のパラメータのみを対象とする
		return LazyIterable.forEach(ps,
				new YieldCallable<Parameter,ParameterValue>(){
			@Override
			public Yield<ParameterValue> yield(final Parameter item, final int index) {
				if (item.getValues().size() > at) {
					return Yield.yieldReturn(item.getValues().get(at));
				} else {
					return Yield.yieldVoid();
				}
			}
		}); 
	}
	
	@Override
	public Iterable<ParameterValue> queryFrom(final Unit t) {
		assertNotNull(t, "argument must not be null.");
		
		final Iterable<ParameterValue> pvs = fetchParameterValues(t);
		return LazyIterable.forEach(pvs, new YieldCallable<ParameterValue,ParameterValue>(){
			@Override
			public Yield<ParameterValue> yield(final ParameterValue item, final int index) {
				for (final Predicate<ParameterValue> pred : preds) {
					if (!pred.test(item)) {
						return Yield.yieldVoid();
					}
				}
				return Yield.yieldReturn(item);
			}
		});
	}
	
	/**
	 * パラメータのタイプの条件を付与したクエリを返す.
	 * @param t タイプ
	 * @return クエリ
	 */
	public final ParameterValueIterableQuery typeIs(final ParameterValueType t) {
		assertNotNull(t, "argument must not be null.");
		return and(new Predicate<ParameterValue>() {
			private final ParameterValueType t1 = t;
			@Override
			public boolean test(ParameterValue t) {
				return t1 == t.getType();
			}
		});
	}
	
	/**
	 * タプルを問合せるクエリを返す.
	 * <p>タプルでないパラメータ値はクエリの結果セットに含まれない。</p>
	 * @return クエリ
	 */
	public final TupleIterableQuery<Unit> typeIsTuple() {
		return new TupleIterableQuery<Unit>(this);
	}
	
	/**
	 * パラメータ値を文字シーケンスに変換するクエリを返す.
	 * @return クエリ
	 */
	public final CharSequenceIterableQuery<Unit> asString() {
		return new CharSequenceIterableQuery<Unit>(this);
	}
	
	/**
	 * パラメータ値を整数に変換するクエリを返す.
	 * <p>クエリの結果セットには整数に変換できなかったものは含まれない。</p>
	 * @return クエリ
	 */
	public final IntegerIterableQuery<Unit> asInteger() {
		return new IntegerIterableQuery<Unit>(this, null);
	}
	
	/**
	 * パラメータ値を整数に変換するクエリを返す.
	 * <p>整数に変換できないパラメータ値はデフォルト値で置き換えられる。</p>
	 * @param defaultValue デフォルト値
	 * @return クエリ
	 */
	public final IntegerIterableQuery<Unit> asInteger(int defaultValue) {
		return new IntegerIterableQuery<Unit>(this, defaultValue);
	}
	
	/**
	 * パラメータ値の条件を付与したクエリを返す.
	 * @param s 文字列
	 * @return クエリ
	 */
	public final ParameterValueIterableQuery contentEquals(final CharSequence s) {
		assertNotNull(s, "argument must not be null.");
		assertFalse(s.length() == 0, "argument must not be empty.");
		
		return and(new Predicate<ParameterValue>() {
			@Override
			public boolean test(ParameterValue t) {
				return CharSequenceUtils.contentsAreEqual(t.getStringValue(),s);
			}
		});
	}
	
	/**
	 * パラメータ値の条件を付与したクエリを返す.
	 * @param s 部分文字列
	 * @return クエリ
	 */
	public final ParameterValueIterableQuery startsWith(final String s) {
		assertNotNull(s, "argument must not be null.");
		assertFalse(s.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<ParameterValue>() {
			@Override
			public boolean test(ParameterValue t) {
				return CharSequenceUtils.arg0StartsWithArg1(t.getStringValue(),s);
			}
		});
	}
	
	/**
	 * パラメータ値の条件を付与したクエリを返す.
	 * @param s 部分文字列
	 * @return クエリ
	 */
	public final ParameterValueIterableQuery endsWith(final String s) {
		assertNotNull(s, "argument must not be null.");
		assertFalse(s.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<ParameterValue>() {
			@Override
			public boolean test(ParameterValue t) {
				return CharSequenceUtils.arg0EndsWithArg1(t.getStringValue(), s);
			}
		});
	}
	
	/**
	 * パラメータ値の条件を付与したクエリを返す.
	 * @param s 部分文字列
	 * @return クエリ
	 */
	public final ParameterValueIterableQuery contains(final String s) {
		assertNotNull(s, "argument must not be null.");
		assertFalse(s.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<ParameterValue>() {
			@Override
			public boolean test(ParameterValue t) {
				return CharSequenceUtils.arg0ContainsArg1(t.getStringValue(), s);
			}
		});
	}
	
	/**
	 * パラメータ値の条件を付与したクエリを返す.
	 * @param regex 正規表現パターン
	 * @return クエリ
	 */
	public final ParameterValueIterableQuery matches(final String regex) {
		assertNotNull(regex, "argument must not be null.");
		assertFalse(regex.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<ParameterValue>() {
			private final Pattern pat = Pattern.compile(regex);
			@Override
			public boolean test(ParameterValue t) {
				return pat.matcher(t.getStringValue()).matches();
			}
		});
	}
	
	/**
	 * パラメータ値の条件を付与したクエリを返す.
	 * @param regex 正規表現パターン
	 * @return クエリ
	 */
	public final ParameterValueIterableQuery matches(final Pattern regex) {
		assertNotNull(regex, "argument must not be null.");
		
		return and(new Predicate<ParameterValue>() {
			private final Pattern pat = regex;
			@Override
			public boolean test(ParameterValue t) {
				return pat.matcher(t.getStringValue()).matches();
			}
		});
	}
	
	/**
	 * パラメータ値の位置の条件を追加したクエリを返す.
	 * @param i パラメータ値の位置（{@code 0}始まり）
	 * @return クエリ
	 * @throws IllegalStateException パラメータの位置を指定済みの場合
	 */
	public ParameterValueIterableQuery at(final int i) {
		assertTrue(0 <= i, "argument must not be greater than or equal 0.");
		if (at != -1) {
			throw new IllegalStateException("value index has been specified.");
		}
		
		return new ParameterValueIterableQuery(baseQuery, preds, i);
	}
	
	@Override
	public ParameterValueIterableQuery and(final Predicate<ParameterValue> pred) {
		assertNotNull(pred, "argument must not be null.");
		
		final LinkedList<Predicate<ParameterValue>> newPreds = new LinkedList<Predicate<ParameterValue>>();
		newPreds.addAll(this.preds);
		newPreds.addLast(pred);
		return new ParameterValueIterableQuery(this.baseQuery, newPreds);
	}
}
