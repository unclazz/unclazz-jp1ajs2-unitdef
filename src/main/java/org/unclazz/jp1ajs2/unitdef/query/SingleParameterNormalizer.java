package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.query.ParameterNormalizer.Normalize;
import org.unclazz.jp1ajs2.unitdef.query.ParameterNormalizer.QueryFactory;

public final class SingleParameterNormalizer implements Query<Parameter, Parameter>,
ParameterNormalizer<SingleParameterNormalizer> {
	private final Normalize normalize;
	
	SingleParameterNormalizer(Normalize n) {
		this.normalize = n;
	}
	SingleParameterNormalizer() {
		this.normalize = null;
	}
	
	@Override
	public WhenValueAtNClause<SingleParameterNormalizer> whenValueAt(int i) {
		return new DefaultWhenValueAtNClause<SingleParameterNormalizer>
		(new QueryFactoryForParameterQuery(normalize), i);
	}
	@Override
	public WhenValueCountNClause<SingleParameterNormalizer> whenValueCount(int c) {
		return new DefaultWhenValueCountNClause<SingleParameterNormalizer>
		(new QueryFactoryForParameterQuery(normalize), c);
	}
	@Override
	public Parameter queryFrom(Parameter t) {
		return normalize.apply(t);
	}
}

class QueryFactoryForParameterQuery implements QueryFactory<SingleParameterNormalizer> {
	private final Normalize baseNormalize;
	QueryFactoryForParameterQuery(Normalize n) {
		this.baseNormalize = n;
	}
	@Override
	public SingleParameterNormalizer create(Normalize normalize) {
		return new SingleParameterNormalizer(baseNormalize == null ?
				normalize : baseNormalize.and(normalize));
	}
}
