package org.unclazz.jp1ajs2.unitdef;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.parameter.AnteroposteriorRelationship;
import org.unclazz.jp1ajs2.unitdef.parameter.Element;
import org.unclazz.jp1ajs2.unitdef.parameter.ExitCodeThreshold;
import org.unclazz.jp1ajs2.unitdef.parameter.FixedDuration;
import org.unclazz.jp1ajs2.unitdef.parameter.MapSize;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;

public final class UnitQueries {
	private UnitQueries() {}
	
	private static final<T> List<T> list() {
		return new LinkedList<T>();
	}
	
	private static final<T> List<T> wrap(T value) {
		if (value == null) {
			return Collections.emptyList();
		} else {
			return Collections.singletonList(value);
		}
	}
	
	public static final UnitQuery<MapSize> SZ = new UnitQuery<MapSize>() {
		private final Pattern pattern = Pattern.compile("^(\\d+)[^\\d]+(\\d+)$");
		@Override
		public List<MapSize> queryFrom(final Unit unit) {
			final Parameter p = unit.getParameter("sz");
			if (p == null) {
				return null;
			}
			final Matcher m = pattern.matcher(p.getValue(0).getRawCharSequence());
			final int w = Integer.parseInt(m.group(1));
			final int h = Integer.parseInt(m.group(2));
			if (m.matches()) {
				return UnitQueries.<MapSize>wrap(new MapSize(w, h));
			}
			throw new RuntimeException("Invalid parameter value");
		}
	};
	public static final UnitQuery<UnitType> TY = new UnitQuery<UnitType>() {
		@Override
		public List<UnitType> queryFrom(final Unit unit) {
			final Parameter p = unit.getParameter("ty");
			return UnitQueries.wrap(UnitType.valueOfCode(p.getValue(0).getRawCharSequence().toString()));
		}
	};
	public static final UnitQuery<CharSequence> CM = new UnitQuery<CharSequence>() {
		@Override
		public List<CharSequence> queryFrom(final Unit unit) {
			final Parameter p = unit.getParameter("cm");
			return UnitQueries.wrap(p == null ? null : p.getValue(0).getRawCharSequence());
		}
	};
	public static final UnitQuery<Element> EL =
			new UnitQuery<Element>() {
		private final Pattern PARAM_EL_VALUE_3 = Pattern.compile("^\\+(\\d+)\\s*\\+(\\d+)$");
		@Override
		public List<Element> queryFrom(final Unit unit) {
			final List<Element> result = UnitQueries.list();
			for (final Parameter el : unit.getParameters("el")) {
				final String pos = el.getValue(2).getRawCharSequence().toString();
				final String unitName = el.getValue(0).getRawCharSequence().toString();
				final Matcher m = PARAM_EL_VALUE_3.matcher(pos);
				if (!m.matches()) {
					continue;
				}
				final Unit subunit = unit.getSubUnit(unitName);
				final int horizontalPixel = Integer.parseInt(m.group(1));
				final int verticalPixel = Integer.parseInt(m.group(2));
				result.add(new Element(subunit, horizontalPixel, verticalPixel));
			}
			return result;
		}
	};
	public static final UnitQuery<AnteroposteriorRelationship> AR =
			new UnitQuery<AnteroposteriorRelationship>() {
		
		@Override
		public List<AnteroposteriorRelationship> queryFrom(final Unit unit) {
			final List<AnteroposteriorRelationship> result = UnitQueries.list();
			for (final Parameter p : unit.getParameters("ar")) {
				if (p.getValue(0).getType() == ParameterValueType.TUPLE) {
					final Tuple t = p.getValue(0).getTuple();
					result.add(new AnteroposteriorRelationship(
							unit.getSubUnit(t.get("f").toString()),
							unit.getSubUnit(t.get("t").toString()),
							t.size() == 3 ? UnitConnectionType.forCode(t.get(2).toString()) : UnitConnectionType.SEQUENTIAL));
				}
			}
			return result;
		}
	};
	public static UnitQuery<Parameter> parameterNamed(final String paramName) {
		return new UnitQuery<Parameter>() {
			@Override
			public List<Parameter> queryFrom(final Unit unit) {
				final List<Parameter> result = UnitQueries.list();
				for (final Parameter p : unit.getParameters()) {
					if (p.getName().equalsIgnoreCase(paramName)) {
						result.add(p);
					}
				}
				return result;
			}
		};
	}
	public static UnitQuery<Unit> subUnitNamed(final String name) {
		return new UnitQuery<Unit>() {
			@Override
			public List<Unit> queryFrom(Unit unit) {
				for (final Unit subUnit : unit.getSubUnits()) {
					if (subUnit.getAttributes().getUnitName().equals(name)) {
						return Collections.singletonList(subUnit);
					}
				}
				return Collections.emptyList();
			}
		};
	}
	public static UnitQuery<MatchResult> parameterNamed(final String paramName, final String pattern) {
		return parameterNamed(paramName, Pattern.compile(pattern));
	}
	public static UnitQuery<MatchResult> parameterNamed(final String paramName, final Pattern pattern) {
		return new UnitQuery<MatchResult>() {
			private final StringBuilder buff = new StringBuilder();
			@Override
			public List<MatchResult> queryFrom(Unit unit) {
				final List<MatchResult> result = UnitQueries.list();
				for (final Parameter param : unit.getParameters(paramName)) {
					result.add(helper(param));
				}
				return result;
			}
			private MatchResult helper(Parameter param) {
				buff.setLength(0);
				for (final ParameterValue val : param) {
					if (buff.length() > 0) {
						buff.append(',');
						buff.append(val.toString());
					}
				}
				final Matcher mat = pattern.matcher(buff);
				mat.matches();
				return mat;
			}
		};
	}
	public static<T> UnitQuery<T> parameterNamed(final String paramName, final ParameterQuery<T> paramQuery) {
		return new UnitQuery<T>() {
			@Override
			public List<T> queryFrom(final Unit unit) {
				final List<T> result = list();
				for (final Parameter p : unit.getParameters()) {
					if (p.getName().equals(paramName)) {
						final T t = paramQuery.queryFrom(p);
						if (t != null) {
							result.add(t);
						}
					}
				}
				return result;
			}
		};
	}
	
	public static final UnitQuery<CharSequence> cm() {
		return parameterNamed("cm", ParameterQueries.CM);
	}
	
	public static final UnitQuery<MapSize> sz() {
		return parameterNamed("sz", ParameterQueries.SZ);
	}
	
	public static final UnitQuery<UnitType> ty() {
		return parameterNamed("ty", ParameterQueries.TY);
	}
	
	public static final UnitQuery<FixedDuration> fd() {
		return parameterNamed("fd", ParameterQueries.FD);
	}
	
	public static final UnitQuery<ExitCodeThreshold> tho() {
		return parameterNamed("tho", ParameterQueries.THO);
	}
	
	public static final UnitQuery<ExitCodeThreshold> wth() {
		return parameterNamed("wth", ParameterQueries.WTH);
	}
}
