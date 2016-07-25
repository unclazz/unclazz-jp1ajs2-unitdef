package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.query.ParameterConditionalModifier.ModifierFactory;
import org.unclazz.jp1ajs2.unitdef.query.ParameterConditionalModifier.WhenThenList;

public interface SingleParameterConditionalModifier extends Query<Parameter, Parameter>,
ParameterConditionalModifier<SingleParameterConditionalModifier> {
	
}

final class DefaultSingleParameterConditionalModifier 
implements SingleParameterConditionalModifier {
	private final WhenThenList whenThenList;
	
	DefaultSingleParameterConditionalModifier(WhenThenList whenThenList) {
		this.whenThenList = whenThenList;
	}
	DefaultSingleParameterConditionalModifier() {
		this.whenThenList = null;
	}
	
	@Override
	public WhenValueAtNClause<SingleParameterConditionalModifier> whenValueAt(int i) {
		return new DefaultWhenValueAtNClause<SingleParameterConditionalModifier>
		(new QueryFactoryForParameterQuery(whenThenList), i);
	}
	@Override
	public WhenValueCountNClause<SingleParameterConditionalModifier> whenValueCount(int c) {
		return new DefaultWhenValueCountNClause<SingleParameterConditionalModifier>
		(new QueryFactoryForParameterQuery(whenThenList), c);
	}
	@Override
	public Parameter queryFrom(Parameter t) {
		return whenThenList.apply(t);
	}
}

final class QueryFactoryForParameterQuery implements ModifierFactory<SingleParameterConditionalModifier> {
	private final WhenThenList whenThenList;
	QueryFactoryForParameterQuery(WhenThenList n) {
		this.whenThenList = n;
	}
	@Override
	public SingleParameterConditionalModifier apply(WhenThenList whenThenList) {
		return new DefaultSingleParameterConditionalModifier(this.whenThenList == null ?
				whenThenList : this.whenThenList.concat(whenThenList));
	}
}
