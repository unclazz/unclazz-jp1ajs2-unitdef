package org.unclazz.jp1ajs2.unitdef.query2;

import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.util.Function;

public final class Queries {
	private Queries() {}
	
	private static final UnitListQuery children 
	= new UnitListQuery(new Function<Unit, Iterable<Unit>>() {
		@Override
		public Iterable<Unit> apply(final Unit t) {
			return t.getSubUnits();
		}
	});
	private static final UnitListQuery descendants 
	= new UnitListQuery(new Function<Unit, Iterable<Unit>>() {
		@Override
		public Iterable<Unit> apply(final Unit t) {
			return new UnitDescendantsIterable(t);
		}
	});
	private static final ParameterListQuery parameters
	= new ParameterListQuery(new UnitListQuery(new Function<Unit, Iterable<Unit>>() {
		@Override
		public Iterable<Unit> apply(final Unit t) {
			return IdQuery.<Unit>getInstance().queryFrom(t);
		}
	}));
	
	public static UnitListQuery children() {
		return children;
	}
	
	public static UnitListQuery descendants() {
		return descendants;
	}
	
	public static ParameterListQuery parameters() {
		return parameters;
	}
}
