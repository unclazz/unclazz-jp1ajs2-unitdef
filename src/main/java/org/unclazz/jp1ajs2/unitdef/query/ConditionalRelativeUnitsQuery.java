package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.FullQualifiedName;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;

public final class ConditionalRelativeUnitsQuery extends FunctionalListUnitQuery<Unit> {
	private final RelativeUnitsQuery q;
	private final Function<Unit, Unit> f;
	
	ConditionalRelativeUnitsQuery(final RelativeUnitsQuery q, final Function<Unit, Unit> f) {
		this.q = q;
		this.f = f;
	}
	ConditionalRelativeUnitsQuery(final ConditionalRelativeUnitsQuery c, final Function<Unit, Unit> g) {
		this.q = c.q;
		this.f = SyntheticFunction.synthesize(c.f, g);
	}
	
	@Override
	protected Function<Unit, Unit> function() {
		return f;
	}
	
	@Override
	protected Iterable<Unit> source(Unit u) {
		return q.source(u);
	}
	
	static final Function<Unit, Unit> returnsUnitWhoseTypeIs(final UnitType t) {
		return new Function<Unit, Unit>() {
			@Override
			public Unit apply(Unit target) {
				return target.getType().equals(t) ? target : null;
			}
		};
	}
	
	static final Function<Unit, Unit> returnsUnitWhoseNameEquals(final String n) {
		return new Function<Unit, Unit>() {
			@Override
			public Unit apply(Unit target) {
				return target.getName().equals(n) ? target : null;
			}
		};
	}
	
	static final Function<Unit, Unit> returnsUnitWhoseNameStartsWith(final String s) {
		return new Function<Unit, Unit>() {
			@Override
			public Unit apply(Unit target) {
				return target.getName().startsWith(s) ? target : null;
			}
		};
	}
	
	static final Function<Unit, Unit> returnsUnitWhoseNameEndsWith(final String s) {
		return new Function<Unit, Unit>() {
			@Override
			public Unit apply(Unit target) {
				return target.getName().endsWith(s) ? target : null;
			}
		};
	}
	
	static final Function<Unit, Unit> returnsUnitWhoseNameContains(final String s) {
		return new Function<Unit, Unit>() {
			@Override
			public Unit apply(Unit target) {
				return target.getName().contains(s) ? target : null;
			}
		};
	}
	
	static final Function<Unit, Unit> returnsUnitWhoseFqnEquals(final String n) {
		return new Function<Unit, Unit>() {
			@Override
			public Unit apply(Unit target) {
				return target.getFullQualifiedName().toString().equals(n) ? target : null;
			}
		};
	}
	
	static final Function<Unit, Unit> returnsUnitWhoseFqnStartsWith(final String s) {
		return new Function<Unit, Unit>() {
			@Override
			public Unit apply(Unit target) {
				return target.getFullQualifiedName().toString().startsWith(s) ? target : null;
			}
		};
	}
	
	static final Function<Unit, Unit> returnsUnitWhoseFqnEndsWith(final String s) {
		return new Function<Unit, Unit>() {
			@Override
			public Unit apply(Unit target) {
				return target.getFullQualifiedName().toString().endsWith(s) ? target : null;
			}
		};
	}
	
	static final Function<Unit, Unit> returnsUnitWhoseFqnContains(final String s) {
		return new Function<Unit, Unit>() {
			@Override
			public Unit apply(Unit target) {
				return target.getFullQualifiedName().toString().contains(s) ? target : null;
			}
		};
	}
	
	static final Function<Unit, Unit> returnsUnitWhoHasChildren() {
		return new Function<Unit, Unit>() {
			@Override
			public Unit apply(Unit target) {
				return target.getSubUnits().size() > 0 ? target : null;
			}
		};
	}
	
	static final Function<Unit, Unit> returnsUnitWhoHasChild(final Function<Unit, Boolean> f) {
		return new Function<Unit, Unit>() {
			@Override
			public Unit apply(Unit target) {
				for (final Unit s : target.getSubUnits()) {
					if (f.apply(s)) {
						return target;
					}
				}
				return null;
			}
		};
	}
	
	static final Function<Unit, Unit> returnsUnitWhoHasChild(final String n) {
		return new Function<Unit, Unit>() {
			@Override
			public Unit apply(Unit target) {
				return target.getSubUnit(n) == null ? null : target;
			}
		};
	}
	
	static final Function<Unit, Unit> returnsUnitWhoHasParameter(final String n) {
		return new Function<Unit, Unit>() {
			@Override
			public Unit apply(Unit target) {
				return target.getParameter(n) == null ? null : target;
			}
		};
	}
	
	static final Function<Unit, Unit> returnsUnitWhoHasParameter(final Function<Parameter, Boolean> f) {
		return new Function<Unit, Unit>() {
			@Override
			public Unit apply(Unit target) {
				for (final Parameter p : target.getParameters()) {
					if (f.apply(p)) {
						return target;
					}
				}
				return null;
			}
		};
	}
	
	public ConditionalRelativeUnitsQuery andTypeIs(final UnitType t) {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoseTypeIs(t));
	}

	public ConditionalRelativeUnitsQuery andNameEquals(final String n) {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoseNameEquals(n));
	}

	public ConditionalRelativeUnitsQuery andNameStartsWith(final String s) {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoseNameStartsWith(s));
	}

	public ConditionalRelativeUnitsQuery andNameEndsWith(final String s) {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoseNameEndsWith(s));
	}

	public ConditionalRelativeUnitsQuery andNameContains(final String s) {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoseNameEndsWith(s));
	}

	public ConditionalRelativeUnitsQuery andFqnEquals(final String n) {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoseFqnEquals(n));
	}

	public ConditionalRelativeUnitsQuery andFqnStartsWith(final String s) {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoseFqnStartsWith(s));
	}

	public ConditionalRelativeUnitsQuery andFqnEndsWith(final String s) {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoseFqnEndsWith(s));
	}

	public ConditionalRelativeUnitsQuery andFqnContains(final String s) {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoseFqnEndsWith(s));
	}

	public ConditionalRelativeUnitsQuery andHasChildren() {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoHasChildren());
	}

	public ConditionalRelativeUnitsQuery andHasChild(final Function<Unit, Boolean> f) {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoHasChild(f));
	}

	public ConditionalRelativeUnitsQuery andHasChild(final String n) {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoHasChild(n));
	}

	public ConditionalRelativeUnitsQuery andHasParameter(final String n) {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoHasParameter(n));
	}

	public ConditionalRelativeUnitsQuery andHasParameter(final Function<Parameter, Boolean> f) {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoHasParameter(f));
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
