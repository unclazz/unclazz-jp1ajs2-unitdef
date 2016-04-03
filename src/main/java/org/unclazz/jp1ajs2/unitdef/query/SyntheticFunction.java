package org.unclazz.jp1ajs2.unitdef.query;

/**
 * 関数合成のためのメソッドを提供する抽象関数クラス.
 * <p>このクラスを拡張するには{@link #apply(Object)}の実装を行うだけでよい。
 * またすでに{@link Function}のインスタンスが存在するのであれば
 * 具象クラスの作成をするまでもなく{@link #synthesize(Function, Function)}を利用すればよい。</p>
 * <p>{@link #and(Function)}もしくは{@link #synthesize(Function, Function)}により左辺と右辺の関数が合成される
 * （ここで左辺とは{@link #and(Function)}のレシーバ もしくは {@link #synthesize(Function, Function)}の第1引数となる関数。
 * 右辺はもう片方の関数）。</p>
 * <p>合成された関数の{@link #apply(Object)}が呼び出されると
 * すぐさま左辺の関数の{@link #apply(Object)}が呼び出される。
 * ここで戻り値として{@code null}でない値が得られれば、
 * それを引数にして右辺の{@link #apply(Object)}が呼び出される。
 * その戻り値として得られた値が合成された関数の{@link #apply(Object)}の戻り値となる。</p>
 * <p>左辺の関数の{@link #apply(Object)}の戻り値として{@code null}が得られた場合、
 * 右辺の{@link #apply(Object)}は呼び出されず、
 * {@code null}が合成された関数の{@link #apply(Object)}の戻り値となる。</p>
 *
 * @param <A> 引数の型
 * @param <B> 戻り値の型
 */
public abstract class SyntheticFunction<A,B> implements Function<A,B> {
	@Override
	public abstract B apply(A target);
	
	/**
	 * レシーバの関数オブジェクトと引数の関数オブジェクトを合成した新しい関数オブジェクトを返す.
	 * @param other 別の関数
	 * @return 合成された関数
	 * @param <C> 別の関数の戻り値の型
	 */
	public final<C> SyntheticFunction<A,C> and(Function<B,C> other) {
		return new SynthesizedFunction<A, B, C>(this, other);
	}
	
	/**
	 * 2つの関数を合成してつくられた新しい関数を返す.
	 * 
	 * @param left 1つ目の関数
	 * @param right 2つ目の関数
	 * @return 新しい関数
	 * @param <A> 1つ目の関数の引数の型
	 * @param <B> 1つ目の関数の戻り値の型 かつ 2つ目の関数の引数の型
	 * @param <C> 2つ目の関数の戻り値の型
	 */
	public static<A,B,C> SyntheticFunction<A,C> synthesize(Function<A,B> left, Function<B,C> right) {
		return new SynthesizedFunction<A, B, C>(left, right);
	}
	
	private final static class SynthesizedFunction<A,B,C> extends SyntheticFunction<A,C> {
		private final Function<A,B> left;
		private final Function<B,C> right;
		
		SynthesizedFunction(Function<A,B> left, Function<B,C> right) {
			if (left == null || right == null) {
				throw new NullPointerException();
			}
			this.left = left;
			this.right = right;
		}
		
		/**
		 * 1つ目の関数の結果値が{@code null}の場合、このメソッドは即座に{@code null}を返す.
		 * そして2つ目の関数は実行されない。
		 */
		@Override
		public C apply(A target) {
			final B leftResult = left.apply(target);
			if (leftResult == null) {
				return null;
			}
			return right.apply(leftResult);
		}
	}
}
