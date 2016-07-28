package org.unclazz.jp1ajs2.unitdef.query;

import java.util.List;
import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.FullQualifiedName;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;

/**
 * ユニットに問合せを行い子ユニット（直接の下位ユニット）や子孫ユニット（直接・間接の下位ユニット）を返すクエリ.
 * 
 * <p>このクエリのインスタンスを得るには{@link Q}の提供する静的メソッドを利用する。
 * {@link Unit#query(Query)}メソッドをクエリに対して適用すると問合せが行われる：</p>
 * 
 * <pre> import static org.unclazz.jp1ajs2.unitdef.query.Queries.*;
 * Unit u = ...;
 * Iterable&lt;Unit&gt; ui = u.query(children());</pre>
 * 
 * <p>このオブジェクト自身が提供するメソッドを通じてクエリに種々の条件を追加することが可能である。
 * これらの条件は内部的に記憶されて問い合わせの時に利用される。
 * クエリはイミュータブルでありステートレスであるので、複雑な条件を設定したインスタンスの参照を保持しておくことで、
 * 複数の異なるユニットに対して繰り返し問合せを行うことができる。</p>
 * 
 * <pre> Query&lt;Unit,Iterable&gt; q0 = children().hasChildren();
 * Query&lt;Unit,Iterable&gt; q1 = q0.typeIs(UnitType.PC_JOB);
 * Query&lt;Unit,Iterable&gt; q2 = q1.hasParameter("cm");
 * Iterable&lt;Unit&gt; ui = u.query(q1); // cmパラメータを持つこと という条件は付かない</pre>
 * 
 * <p>{@link #queryFrom(Unit)}メソッドから返えされる{@link Iterable}は遅延評価に基づき値を返す。
 * 問合せのロジックの起動は可能な限り遅らせられるので、仮に1つ取得するだけで{@link Iterable}を破棄したとしても、
 * そのために消費されるCPUとメモリのコストは当該の1ユニットを問合せるのに必要な分だけである。</p>
 * 
 * <p>なおこのように問合せ結果のうち最初の1つだけを取得したい場合は、
 * {@link #one()}もしくはそのオーバーロードを呼び出して{@link OneQuery}のインスタンスを得ると便利である。
 * また問合せ結果として遅延評価{@link Iterable}の代わりに正格評価{@link List}を取得したい場合は、
 * {@link #list()}メソッドを呼び出してクエリ{@link ListQuery}のインスタンスを得るとよい。</p>
 * 
 * <pre> Unit u2 = u.query(children().one());
 * List&lt;Unit&gt; ul = u.query(children().list());</pre>
 */
public interface UnitIterableQuery extends IterableQuery<Unit, Unit> {
	/**
	 * 問合せの結果のユニットが持つユニット定義パラメータを問合せるクエリを返す.
	 * @return クエリ
	 */
	ParameterIterableQuery theirParameters();
	/**
	 * 問合せの結果のユニットが持つユニット定義パラメータを問合せるクエリを返す.
	 * @param name パラメータ名
	 * @return クエリ
	 */
	ParameterIterableQuery theirParameters(final String name);
	/**
	 * 問合せの結果のユニットが持つ完全名を問合せるクエリを返す.
	 * @return クエリ
	 */
	IterableQuery<Unit, FullQualifiedName> theirFqn();
	/**
	 * 問合せの結果のユニットが持つユニット名を問合せるクエリを返す.
	 * @return クエリ
	 */
	IterableQuery<Unit, String> theirName();
	/**
	 * ユニット種別の条件を追加したクエリを返す.
	 * @param t ユニット種別
	 * @return クエリ
	 */
	UnitIterableQuery typeIs(final UnitType t);
	/**
	 * ユニット完全名の条件を追加したクエリを返す.
	 * @param n ユニット完全名
	 * @return クエリ
	 */
	UnitIterableQuery fqnEquals(final String n);
	/**
	 * ユニット完全名の条件を追加したクエリを返す.
	 * @param n ユニット完全名の部分文字列
	 * @return クエリ
	 */
	UnitIterableQuery fqnStartsWith(final String n);
	/**
	 * ユニット完全名の条件を追加したクエリを返す.
	 * @param n ユニット完全名の部分文字列
	 * @return クエリ
	 */
	UnitIterableQuery fqnEndsWith(final String n);
	/**
	 * ユニット完全名の条件を追加したクエリを返す.
	 * @param n ユニット完全名の部分文字列
	 * @return クエリ
	 */
	UnitIterableQuery fqnContains(final String n);
	/**
	 * ユニット名の条件を追加したクエリを返す.
	 * @param n ユニット名
	 * @return クエリ
	 */
	UnitIterableQuery nameEquals(final String n);
	/**
	 * ユニット名の条件を追加したクエリを返す.
	 * @param n ユニット名の部分文字列
	 * @return クエリ
	 */
	UnitIterableQuery nameStartsWith(final String n);
	/**
	 * ユニット名の条件を追加したクエリを返す.
	 * @param n ユニット名の部分文字列
	 * @return クエリ
	 */
	UnitIterableQuery nameEndsWith(final String n);
	/**
	 * ユニット名の条件を追加したクエリを返す.
	 * @param n ユニット名の部分文字列
	 * @return クエリ
	 */
	UnitIterableQuery nameContains(final String n);
	/**
	 * ユニット名の条件を追加したクエリを返す.
	 * @param regex ユニット名の正規表現パターン
	 * @return クエリ
	 */
	UnitIterableQuery nameMatches(final Pattern regex);
	/**
	 * ユニット名の条件を追加したクエリを返す.
	 * @param regex ユニット名の正規表現パターン
	 * @return クエリ
	 */
	UnitIterableQuery nameMatches(final String regex);
	/**
	 * 子ユニット（直接の下位ユニット）の条件を追加したクエリを返す.
	 * @return クエリ
	 */
	UnitIterableQuery hasChildren();
	/**
	 * ユニット定義パラメータの条件を追加したクエリのファクトリを返す.
	 * @param name パラメータ名
	 * @return ファクトリ
	 */
	HasParameterValueAtN hasParameter(final String name);
	
	public static interface HasParameterValueAtN {
		/**
		 * マッチング対象のパラメータ値を添字指定する.
		 * @param i 添字
		 * @return ファクトリ
		 * @throws IllegalStateException ファクトリにすでに添字指定がなされていた場合
		 */
		HasParameterValueAtN valueAt(final int i);
		/**
		 * パラメータ値の存在だけを条件とするクエリを生成する.
		 * @return クエリ
		 */
		UnitIterableQuery anyValue();
		/**
		 * パラメータ値と文字列が適合するかを条件とするクエリを生成する.
		 * @param s 文字列
		 * @return クエリ
		 */
		UnitIterableQuery contentEquals(final CharSequence s);
		/**
		 * パラメータ値と部分文字列が適合するかを条件とするクエリを生成する.
		 * @param s 部分文字列
		 * @return クエリ
		 */
		UnitIterableQuery startsWith(final CharSequence s);
		/**
		 * パラメータ値と部分文字列が適合するかを条件とするクエリを生成する.
		 * @param s 部分文字列
		 * @return クエリ
		 */
		UnitIterableQuery endsWith(final CharSequence s);
		/**
		 * パラメータ値と部分文字列が適合するかを条件とするクエリを生成する.
		 * @param s 部分文字列
		 * @return クエリ
		 */
		UnitIterableQuery contains(final CharSequence s);
		/**
		 * パラメータ値と正規表現パターンが適合するかを条件とするクエリを生成する.
		 * @param regex 正規表現パターン
		 * @return クエリ
		 */
		UnitIterableQuery matches(final Pattern regex);
		/**
		 * パラメータ値と正規表現パターンが適合するかを条件とするクエリを生成する.
		 * @param regex 正規表現パターン
		 * @return クエリ
		 */
		UnitIterableQuery matches(final CharSequence regex);
		/**
		 * タプル条件を付与された{@link UnitIterableQuery}を生成するためのファクトリを返す.
		 * @return ファクトリ
		 */
		HasParameterValueAtNIsTuple typeIsTuple();
	}
	/**
	 * ユニット定義パラメータ値のタプル条件を付与された{@link UnitIterableQuery}を生成するためのファクトリ.
	 * <p>デフォルトでは値のマッチングは1つ目のユニット定義パラメータ値（添字は0）を対象に行われる。
	 * この動作は{@link #valueAt(int)}メソッドでマッチング対象の値を添字により指定することで変更することができる。</p>
	 */
	public static interface HasParameterValueAtNIsTuple{
		/**
		 * マッチング対象のパラメータ値を添字指定する.
		 * @param i 添字
		 * @return ファクトリ
		 * @throws IllegalStateException ファクトリにすでに添字指定がなされていた場合
		 */
		HasParameterValueAtNIsTuple valueAt(final int i);
		/**
		 * タプルのエントリー数を条件とするクエリを生成する.
		 * @param i エントリー数
		 * @return クエリ
		 */
		UnitIterableQuery entryCount(final int i);
		/**
		 * タプルのエントリーを条件とするクエリを生成する.
		 * @param k エントリー・キー
		 * @param v エントリー値
		 * @return クエリ
		 */
		UnitIterableQuery hasEntry(final String k, final CharSequence v);
		/**
		 * タプルのエントリー・キーを条件とするクエリを生成する.
		 * @param k エントリー・キー
		 * @return クエリ
		 */
		UnitIterableQuery hasKey(final String k);
		/**
		 * タプルのエントリー値を条件とするクエリを生成する.
		 * @param v エントリー値
		 * @return クエリ
		 */
		UnitIterableQuery hasValue(final CharSequence v);
	}
}
