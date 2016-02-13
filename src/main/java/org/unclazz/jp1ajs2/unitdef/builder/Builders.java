package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.Tuple;

public final class Builders {
	private Builders() {}
	
	public static TupleBuilder tuple() {
		return new TupleBuilder();
	}
	public static ParameterBuilder parameter() {
		return new ParameterBuilder();
	}
	public static UnitBuilder unit() {
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
	public static ParameterValue charSequenceParameterValue(final CharSequence value) {
		return new CharSequenceParameterValue(value);
	}
	public static ParameterValue quotedParameterValue(final CharSequence value) {
		return new QuotedParameterValue(value);
	}
	public static ParameterValue tupleParameterValue(final Tuple value) {
		return new TupleParameterValue(value);
	}
}
