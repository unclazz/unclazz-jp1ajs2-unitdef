package org.unclazz.jp1ajs2.unitdef.builder;

public final class Builders {
	private Builders() {}
	
	public static TupleBuilder forTuple() {
		return new TupleBuilder();
	}
	public static ParameterBuilder forParameter() {
		return new ParameterBuilder();
	}
	public static UnitBuilder forUnit() {
		return new UnitBuilder();
	}
	public static AttributesBuilder forAttributes() {
		return new AttributesBuilder();
	}
	public static FullQualifiedNameBuilder forFullQualifiedName() {
		return new FullQualifiedNameBuilder();
	}
}
