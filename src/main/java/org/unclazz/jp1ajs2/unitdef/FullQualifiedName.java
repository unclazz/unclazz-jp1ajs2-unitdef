package org.unclazz.jp1ajs2.unitdef;

import java.util.Arrays;
import java.util.List;

public final class FullQualifiedName {
	public static FullQualifiedName build(final CharSequence... fragments) {
		notNullAndNotEmpty(fragments);
		return new FullQualifiedName(fragments);
	}
	
	public static FullQualifiedName build(final List<CharSequence> fragments) {
		final CharSequence[] array = new CharSequence[fragments.size()];
		fragments.toArray(array);
		return build(array);
	}
	
	private static void notNullAndNotEmpty(final CharSequence... args) {
		if (args.length == 0) {
			throw new IllegalArgumentException();
		}
		for (final CharSequence arg : args) {
			if (arg == null || arg.length() == 0) {
				throw new IllegalArgumentException();
			}
		}
	}
	
	private final CharSequence[] fragments;
	private final String v;
	
	FullQualifiedName(CharSequence... fragments) {
		this.fragments = fragments;
		final StringBuilder buff = new StringBuilder();
		for (final CharSequence f : fragments) {
			buff.append('/').append(f);
		}
		v = buff.toString();
	}
	
	public CharSequence[] getFragments() {
		return Arrays.copyOf(fragments, fragments.length);
	}
	public FullQualifiedName getSuperUnitName() {
		final int len = fragments.length;
		if (len == 1) {
			throw new RuntimeException();
		}
		return new FullQualifiedName(Arrays.copyOfRange(fragments, 0, len - 1));
	}
	public FullQualifiedName getSubUnitName(final CharSequence fragment) {
		final int len = fragments.length;
		if (fragment == null || fragment.length() == 0) {
			throw new IllegalArgumentException();
		}
		final CharSequence[] newFragments = Arrays.copyOf(fragments, len + 1);
		newFragments[len] = fragment;
		return new FullQualifiedName(newFragments);
	}
	@Override
	public String toString() {
		return v;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + v.hashCode();
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FullQualifiedName other = (FullQualifiedName) obj;
		if (!v.equals(other.v))
			return false;
		return true;
	}
}
