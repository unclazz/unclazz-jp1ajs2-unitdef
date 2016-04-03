package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.FullQualifiedName;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;

public abstract class RelativeUnitsQuery extends FunctionalListUnitQuery<Unit> {
	RelativeUnitsQuery() {}
	
	private static final Function<Unit, Unit> noop = new Function<Unit, Unit>() {
		@Override
		public Unit apply(final Unit target) {
			return target;
		}
	};
	
	@Override
	protected Function<Unit, Unit> function() {
		return noop;
	}
	
	public ConditionalRelativeUnitsQuery typeIs(UnitType t) {
		return new ConditionalRelativeUnitsQuery(this, 
				ConditionalRelativeUnitsQuery.returnsUnitWhoseTypeIs(t));
	}
	
	public ConditionalRelativeUnitsQuery nameEquals(String n) {
		return new ConditionalRelativeUnitsQuery(this, 
				ConditionalRelativeUnitsQuery.returnsUnitWhoseNameEquals(n));
	}
	
	public ConditionalRelativeUnitsQuery nameStartsWith(String s) {
		return new ConditionalRelativeUnitsQuery(this, 
				ConditionalRelativeUnitsQuery.returnsUnitWhoseNameStartsWith(s));
	}
	
	public ConditionalRelativeUnitsQuery nameEndsWith(String s) {
		return new ConditionalRelativeUnitsQuery(this, 
				ConditionalRelativeUnitsQuery.returnsUnitWhoseNameEndsWith(s));
	}
	
	public ConditionalRelativeUnitsQuery nameContains(String s) {
		return new ConditionalRelativeUnitsQuery(this, 
				ConditionalRelativeUnitsQuery.returnsUnitWhoseNameContains(s));
	}

	public ConditionalRelativeUnitsQuery fqnEquals(final String n) {
		return new ConditionalRelativeUnitsQuery(this,  
				ConditionalRelativeUnitsQuery.returnsUnitWhoseFqnEquals(n));
	}

	public ConditionalRelativeUnitsQuery fqnStartsWith(final String s) {
		return new ConditionalRelativeUnitsQuery(this,  
				ConditionalRelativeUnitsQuery.returnsUnitWhoseFqnStartsWith(s));
	}

	public ConditionalRelativeUnitsQuery fqnEndsWith(final String s) {
		return new ConditionalRelativeUnitsQuery(this,  
				ConditionalRelativeUnitsQuery.returnsUnitWhoseFqnEndsWith(s));
	}

	public ConditionalRelativeUnitsQuery fqnContains(final String s) {
		return new ConditionalRelativeUnitsQuery(this,  
				ConditionalRelativeUnitsQuery.returnsUnitWhoseFqnEndsWith(s));
	}

	public ConditionalRelativeUnitsQuery hasChildren() {
		return new ConditionalRelativeUnitsQuery(this,  
				ConditionalRelativeUnitsQuery.returnsUnitWhoHasChildren());
	}

	public ConditionalRelativeUnitsQuery hasChild(final Function<Unit, Boolean> f) {
		return new ConditionalRelativeUnitsQuery(this,  
				ConditionalRelativeUnitsQuery.returnsUnitWhoHasChild(f));
	}

	public ConditionalRelativeUnitsQuery hasChild(final String n) {
		return new ConditionalRelativeUnitsQuery(this,  
				ConditionalRelativeUnitsQuery.returnsUnitWhoHasChild(n));
	}

	public ConditionalRelativeUnitsQuery hasParameter(final String n) {
		return new ConditionalRelativeUnitsQuery(this,  
				ConditionalRelativeUnitsQuery.returnsUnitWhoHasParameter(n));
	}

	public ConditionalRelativeUnitsQuery hasParameter(final Function<Parameter, Boolean> f) {
		return new ConditionalRelativeUnitsQuery(this,  
				ConditionalRelativeUnitsQuery.returnsUnitWhoHasParameter(f));
	}
	public FunctionalListUnitQuery<Unit> asUnit() {
		return this;
	}
	
	public FunctionalListUnitQuery<String> asName() {
		return and(new Function<Unit, String>() {
			@Override
			public String apply(final Unit target) {
				return target.getName();
			}
		});
	}
	
	public FunctionalListUnitQuery<FullQualifiedName> asFullQualifiedName() {
		return and(new Function<Unit, FullQualifiedName>() {
			@Override
			public FullQualifiedName apply(final Unit target) {
				return target.getFullQualifiedName();
			}
		});
	}
}
