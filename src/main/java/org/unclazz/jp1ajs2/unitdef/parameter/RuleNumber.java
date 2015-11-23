package org.unclazz.jp1ajs2.unitdef.parameter;

public final class RuleNumber implements Comparable<RuleNumber> {
	public static final RuleNumber UNDEFINED = new RuleNumber(0);
	public static final RuleNumber MIN = new RuleNumber(1);
	public static final RuleNumber MAX = new RuleNumber(144);
	public static final RuleNumber DEFAULT = MIN;
	
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
