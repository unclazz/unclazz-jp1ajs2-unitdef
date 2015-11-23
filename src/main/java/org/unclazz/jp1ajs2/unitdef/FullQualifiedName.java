package org.unclazz.jp1ajs2.unitdef;

public interface FullQualifiedName {
	CharSequence[] getFragments();
	FullQualifiedName getSuperUnitName();
	FullQualifiedName getSubUnitName(final CharSequence fragment);
}
