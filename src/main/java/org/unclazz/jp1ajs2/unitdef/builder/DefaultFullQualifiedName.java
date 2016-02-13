package org.unclazz.jp1ajs2.unitdef.builder;

import java.util.Arrays;

import org.unclazz.jp1ajs2.unitdef.FullQualifiedName;

final class DefaultFullQualifiedName implements FullQualifiedName {
	private final CharSequence[] fragments;
	private final String v;
	
	DefaultFullQualifiedName(CharSequence... fragments) {
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
	public DefaultFullQualifiedName getSuperUnitName() {
		final int len = fragments.length;
		if (len == 1) {
			throw new RuntimeException();
		}
		return new DefaultFullQualifiedName(Arrays.copyOfRange(fragments, 0, len - 1));
	}
	public DefaultFullQualifiedName getSubUnitName(final CharSequence fragment) {
		final int len = fragments.length;
		if (fragment.length() == 0) {
			throw new IllegalArgumentException();
		}
		final CharSequence[] newFragments = Arrays.copyOf(fragments, len + 1);
		newFragments[len] = fragment;
		return new DefaultFullQualifiedName(newFragments);
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
		DefaultFullQualifiedName other = (DefaultFullQualifiedName) obj;
		if (!v.equals(other.v))
			return false;
		return true;
	}
}
