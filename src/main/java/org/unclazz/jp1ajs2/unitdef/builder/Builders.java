package org.unclazz.jp1ajs2.unitdef.builder;

public final class Builders {
	private Builders() {}
	
	public static TupleParameterValueBuilder tupleParameterValue() {
		return new TupleParameterValueBuilder();
	}
	public static ParameterBuilder parameter() {
		return new ParameterBuilder();
	}
	public static UnitBuilder forUnit() {
		return new UnitBuilder();
	}
	public static AttributesBuilder attributes() {
		return new AttributesBuilder();
	}
	public static FullQualifiedNameBuilder fullQualifiedName() {
		return new FullQualifiedNameBuilder();
	}
	public static AnteroposteriorRelationshipBuilder parameterAR() {
		return new AnteroposteriorRelationshipBuilder();
	}
	public static EndScheduledTimeBuilder parameterEY() {
		return new EndScheduledTimeBuilder();
	}
	public static StartScheduledTimeBuilder parameterSY() {
		return new StartScheduledTimeBuilder();
	}
	public static StartTimeBuilder parameterST() {
		return new StartTimeBuilder();
	}
	public static StartDateBuilder parameterSD() {
		return new StartDateBuilder();
	}
	public static ElementBuilder parameterEL() {
		return new ElementBuilder();
	}
}
