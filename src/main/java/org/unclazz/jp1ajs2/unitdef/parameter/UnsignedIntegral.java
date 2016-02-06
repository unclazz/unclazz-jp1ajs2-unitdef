package org.unclazz.jp1ajs2.unitdef.parameter;

public class UnsignedIntegral implements Integral {
	private static final UnsignedIntegral zero = new UnsignedIntegral(0);
	private static final UnsignedIntegral one = new UnsignedIntegral(1);
	private static final UnsignedIntegral two = new UnsignedIntegral(2);
	
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
	
	private final long value;
	
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

	public long longValue() {
		return value;
	}

	@Override
	public String toString(int radix) {
		return Long.toString(value, radix);
	}
}
