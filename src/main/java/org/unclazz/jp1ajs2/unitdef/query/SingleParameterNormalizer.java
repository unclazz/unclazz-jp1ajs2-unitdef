package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.query.ParameterNormalizer.NormalizerFactory;
import org.unclazz.jp1ajs2.unitdef.query.ParameterNormalizer.WhenThenList;

public final class SingleParameterNormalizer implements Query<Parameter, Parameter>,
ParameterNormalizer<SingleParameterNormalizer> {
	private final WhenThenList whenThenList;
	
	SingleParameterNormalizer(WhenThenList whenThenList) {
		this.whenThenList = whenThenList;
	}
	SingleParameterNormalizer() {
		this.whenThenList = null;
	}
	
	@Override
	public WhenValueAtNClause<SingleParameterNormalizer> whenValueAt(int i) {
		return new DefaultWhenValueAtNClause<SingleParameterNormalizer>
		(new QueryFactoryForParameterQuery(whenThenList), i);
	}
	@Override
	public WhenValueCountNClause<SingleParameterNormalizer> whenValueCount(int c) {
		return new DefaultWhenValueCountNClause<SingleParameterNormalizer>
		(new QueryFactoryForParameterQuery(whenThenList), c);
	}
	@Override
	public Parameter queryFrom(Parameter t) {
		return whenThenList.apply(t);
	}
}

class QueryFactoryForParameterQuery implements NormalizerFactory<SingleParameterNormalizer> {
	private final WhenThenList whenThenList;
	QueryFactoryForParameterQuery(WhenThenList n) {
		this.whenThenList = n;
	}
	@Override
	public SingleParameterNormalizer apply(WhenThenList whenThenList) {
		return new SingleParameterNormalizer(this.whenThenList == null ?
				whenThenList : this.whenThenList.concat(whenThenList));
	}
}
