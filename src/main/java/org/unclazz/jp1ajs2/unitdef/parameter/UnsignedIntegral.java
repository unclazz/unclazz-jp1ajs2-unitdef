package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * 符号なし32ビット整数を表わすオブジェクト.
 * このオブジェクトが表わすことのできる値の範囲は{@code 0L}以上{@code 4294967296L}以下。
 */
public class UnsignedIntegral implements Integral {
	private static final UnsignedIntegral zero = new UnsignedIntegral(0);
	private static final UnsignedIntegral one = new UnsignedIntegral(1);
	private static final UnsignedIntegral two = new UnsignedIntegral(2);
	
	/**
	 * 指定された{@code long}値に対応するインスタンスを返す.
	 * このオブジェクトが表わすことのできる範囲を超える値が指定された場合は例外をスローする。
	 * @param value 整数値
	 * @return インスタンス
	 * @throws IllegalArgumentException このオブジェクトが表現可能な範囲を超える値が指定された場合
	 */
	public static UnsignedIntegral of(final long value) {
		if (value < 0) {
			throw new IllegalArgumentException("Value must not be less than 0.");
		}
		if (value > 4294967296L) {
			throw new IllegalArgumentException("Value must not be greater than 4,294,967,296.");
		}
		if (value == 0) {
			return zero;
		} else if (value == 1) {
			return one;
		} else if (value == 2) {
			return two;
		}
		return new UnsignedIntegral(value);
	}
	
	/**
	 * このオブジェクトが表わす整数値.
	 */
	private final long value;
	
	/**
	 * コンストラクタ.
	 * @param value 整数値
	 */
	private UnsignedIntegral(final long value) {
		this.value = value;
	}

	@Override
	public int compareTo(Integral o) {
		if (value < o.longValue()) {
			return -1;
		} else if (value > o.longValue()) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public CharSequence toCharSequence() {
		final StringBuilder buff = new StringBuilder();
		buff.append(value).trimToSize();
		return buff;
	}

	@Override
	public int intValue() {
		return (int) value;
	}

	@Override
	public long longValue() {
		return value;
	}

	@Override
	public String toString(int radix) {
		return Long.toString(value, radix);
	}
}
