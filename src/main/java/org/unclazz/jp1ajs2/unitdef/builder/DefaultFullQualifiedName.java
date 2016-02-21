package org.unclazz.jp1ajs2.unitdef.builder;

import java.util.Collections;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.FullQualifiedName;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;
import org.unclazz.jp1ajs2.unitdef.util.ListUtils;

final class DefaultFullQualifiedName implements FullQualifiedName {
	private final List<CharSequence> fragments;
	private final String v;
	
	DefaultFullQualifiedName(List<CharSequence> fragments) {
		this.fragments = fragments;
		final StringBuilder buff = CharSequenceUtils.builder();
		for (final CharSequence f : fragments) {
			buff.append('/').append(f);
		}
		v = buff.toString();
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
		final List<CharSequence> newFragments = ListUtils.linkedList(fragments);
		newFragments.add(fragment);
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

	@Override
	public CharSequence getUnitName() {
		return fragments.get(fragments.size() - 1);
	}
}
