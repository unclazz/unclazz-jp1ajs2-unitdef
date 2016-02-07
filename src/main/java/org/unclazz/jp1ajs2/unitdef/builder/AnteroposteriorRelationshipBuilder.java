package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.parameter.AnteroposteriorRelationship;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitConnectionType;

public final class AnteroposteriorRelationshipBuilder {
	AnteroposteriorRelationshipBuilder() {}
	
	private CharSequence fromUnitName;
	private CharSequence toUnitName;
	private UnitConnectionType connectionType = UnitConnectionType.SEQUENTIAL;
	
	public AnteroposteriorRelationshipBuilder setFromUnitName(final CharSequence name) {
		this.fromUnitName = name;
		return this;
	}
	public AnteroposteriorRelationshipBuilder setToUnitName(final CharSequence name) {
		this.toUnitName = name;
		return this;
	}
	public AnteroposteriorRelationshipBuilder setConnectionType(final UnitConnectionType type) {
		this.connectionType = type;
		return this;
	}
	public AnteroposteriorRelationship build() {
		if (fromUnitName == null || toUnitName == null) {
			throw new NullPointerException("Both unit name must be not-null.");
		}
		if (toUnitName.length() == 0 || toUnitName.length() == 0) {
			throw new IllegalArgumentException("Both unit name must be not-empty");
		}
		if (connectionType == null) {
			throw new IllegalArgumentException("Connection-type must be not-null");
		}
		return new DefaultAnteroposteriorRelationship(fromUnitName, toUnitName, connectionType);
	}
}
