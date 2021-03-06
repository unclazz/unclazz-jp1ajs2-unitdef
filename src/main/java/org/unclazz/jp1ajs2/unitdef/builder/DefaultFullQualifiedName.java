package org.unclazz.jp1ajs2.unitdef.builder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.FullQualifiedName;

final class DefaultFullQualifiedName implements FullQualifiedName {
	private final List<CharSequence> fragments;
	private String stringCache;
	
	DefaultFullQualifiedName(List<CharSequence> fragments) {
		this.fragments = fragments;
	}
	
	public List<CharSequence> getFragments() {
		return Collections.unmodifiableList(fragments);
	}
	public DefaultFullQualifiedName getSuperUnitName() {
		final int len = fragments.size();
		if (len == 1) {
			return null;
		}
		return new DefaultFullQualifiedName(fragments.subList(0, len - 1));
	}
	public DefaultFullQualifiedName getSubUnitName(final CharSequence fragment) {
		if (fragment == null) {
			throw new NullPointerException("fragment of fqn must not be null.");
		}
		if (fragment.length() == 0) {
			throw new IllegalArgumentException("fragment of fqn must not be empty.");
		}
		final List<CharSequence> newFragments = new LinkedList<CharSequence>(fragments);
		newFragments.add(fragment);
		return new DefaultFullQualifiedName(newFragments);
	}
	@Override
	public String toString() {
		if (stringCache == null) {
			final StringBuilder buff = new StringBuilder();
			for (final CharSequence f : fragments) {
				buff.append('/').append(f);
			}
			stringCache = buff.toString();
		}
		return stringCache;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + toString().hashCode();
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
		if (!toString().equals(other.toString()))
			return false;
		return true;
	}

	@Override
	public CharSequence getUnitName() {
		return fragments.get(fragments.size() - 1);
	}
}
