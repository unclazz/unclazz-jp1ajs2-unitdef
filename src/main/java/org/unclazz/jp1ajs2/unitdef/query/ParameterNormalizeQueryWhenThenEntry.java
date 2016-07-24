package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.util.Function;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;

final class ParameterNormalizeQueryWhenThenEntry {
	private final Predicate<Parameter> condition;
	private final Function<Parameter, Parameter> operation;
	ParameterNormalizeQueryWhenThenEntry(final Predicate<Parameter> cond
			, final Function<Parameter, Parameter> ope) {
		this.condition = cond;
		this.operation = ope;
	}
	Predicate<Parameter> getCondition() {
		return condition;
	}
	Function<Parameter, Parameter> getOperation() {
		return operation;
	}
}
