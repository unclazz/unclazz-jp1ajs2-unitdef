package org.unclazz.jp1ajs2.unitdef.query2;

/**
 * キャッシュ機能付きのクエリ.
 * <p>ベースとなるクエリの問合せ結果をキャッシュし2度目以降の問合せを省略する。</p>
 * @param <T> 問合せ対象オブジェクトの型
 * @param <U> 問合せ結果オブジェクトの型
 */
public final class CachedQuery<T, U> implements Query<T, U> {
	/**
	 * クエリをラップしてキャッシュ機能付きのクエリを返す.
	 * @param q ベースとなるクエリ
	 * @return キャッシュ機能付きのクエリ
	 */
	public static<T, U> Query<T, U> wrap(Query<T, U> q) {
		return new CachedQuery<T, U>(q);
	}
	
	private T lastTarget = null;
	private boolean done = false;
	private Query<T, U> baseQuery = null;
	private U cachedResult = null;
	
	private CachedQuery(final Query<T, U> q) {
		QueryUtils.assertNotNull(q, "argument must not be null.");
		
		this.baseQuery = q;
	}
	
	@Override
	public U queryFrom(T t) {
		QueryUtils.assertNotNull(t, "argument must not be null.");
		
		// 問合せ済み かつ 直前の問合せ対象と同じオブジェクト参照であるかチェック
		if (done && lastTarget == t) {
			// 条件に適合する場合はキャッシュ済みの結果を返す
			return cachedResult;
		}
		
		// 問合せ済みでない もしくは 新規の問合せ対象である場合は問合せを実施
		cachedResult = baseQuery.queryFrom(t);
		done = true;
		baseQuery = null;
		return cachedResult;
	}
}
