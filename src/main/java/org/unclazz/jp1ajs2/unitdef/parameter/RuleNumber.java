package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * 上位ジョブネットのスケジュール・ルール番号を表わすオブジェクト.
 * 指定できる数値は{@code 0}から{@code 144}の範囲であるが、
 * {@code 0}は特殊な意味を持っておりsd=0,udのかたちでしか指定できない。
 */
public final class RuleNumber implements Comparable<RuleNumber> {
	/** 実行開始日が未定義（sd=0,ud）のときのみ使用される値. */
	public static final RuleNumber UNDEFINED = new RuleNumber(0);
	/** 最小値. {@code 1}を表わす。上述のとおり{@code 0}は特殊な意味を持っており
	 * ふつう利用できる値の最小値は{@code 1}である。 */
	public static final RuleNumber MIN = new RuleNumber(1);
	/** 最大値. {@code 144}を表わす。 */
	public static final RuleNumber MAX = new RuleNumber(144);
	/** デフォルト値. {@link #MIN}と同じ。 */
	public static final RuleNumber DEFAULT = MIN;
	
	/**
	 * 指定されたルール番号に対応するインスタンスを返す.
	 * @param n ルール番号
	 * @return インスタンス
	 * @throws IllegalArgumentException 指定された値が表現可能な範囲を超えていた場合
	 */
	public static RuleNumber of(final int n) {
		switch (n) {
		case 0:
			return UNDEFINED;
		case 1:
			return MIN;
		case 144:
			return MAX;
		default:
			return new RuleNumber(n);
		}
	}
	
	private final int n;
	
	private RuleNumber(final int n) {
		if (n < 0 || 144 < n) {
			throw new IllegalArgumentException("Rule number must be between 0 and 144");
		}
		this.n = n;
	}
	
	public int intValue() {
		return n;
	}
	@Override
	public String toString() {
		return Integer.toString(n);
	}
	@Override
	public int compareTo(RuleNumber o) {
		return this.n - o.n;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + n;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RuleNumber other = (RuleNumber) obj;
		if (n != other.n)
			return false;
		return true;
	}
}
