package usertools.jp1ajs2.unitdef.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.m12i.query.parse.Accessor;
import com.m12i.query.parse.QueryFactory;
import com.m12i.query.parse.QueryParseException;

import usertools.jp1ajs2.unitdef.util.Option;
import static usertools.jp1ajs2.unitdef.util.Helpers.*;

class UnitImpl implements usertools.jp1ajs2.unitdef.core.Unit {

	private static final List<Unit> emptyUnitDefList = Collections.emptyList();
	private static final QueryFactory<Unit> queryFactory = 
			new QueryFactory<Unit>(new Accessor<Unit>() {
				@Override
				public String accsess(Unit elem, String prop) {
					final Option<Param> p = findParamOne(elem, prop);
					if (p.isNone()) {
						return null;
					} else {
						return p.get().getValue();
					}
				}
			});
	
	private final String unitName;
	private final String permissionMode;
	private final String ownerName;
	private final String resourceGroup;
	private final String fullQualifiedName;
	private final List<Param> params;
	private final List<Unit> subUnits;
	
	public UnitImpl(final String unitName, 
			final String permissionMode, 
			final String ownerName, 
			final String resourceGroup,
			final String fullQualifiedName,
			final List<Param> params,
			final List<Unit> subUnitDefs
			){
		this.unitName = unitName;
		this.permissionMode = permissionMode;
		this.ownerName = ownerName;
		this.resourceGroup = resourceGroup;
		this.fullQualifiedName = fullQualifiedName;
		this.params = Collections.unmodifiableList(params);
		this.subUnits = (subUnitDefs == null || subUnitDefs.size() == 0 ? emptyUnitDefList : subUnitDefs);
	}
	
	@Override
	public Option<String> getPermissionMode() {
		return Option.wrap(permissionMode);
	}

	@Override
	public Option<String> getOwnerName() {
		return Option.wrap(ownerName);
	}
	
	@Override
	public Option<String> getResourceGroupName() {
		return Option.wrap(resourceGroup);
	}

	
	@Override
	public String getName() {
		return unitName;
	}
	
	@Override
	public List<Param> getParams() {
		return params;
	}

	@Override
	public List<Unit> getSubUnits() {
		return subUnits;
	}

	
	@Override
	public Option<Unit> getSubUnit(final String targetUnitName){
		for (final Unit u : getSubUnits()) {
			if (u.getName() != null
					&& u.getName().equals(targetUnitName)) {
				return Option.some(u);
			}
		}
		return Option.none();
	}
	
	@Override
	public UnitType getType(){
		return UnitType.searchByAbbr(findParamOne(this, "ty").get().getValues().get(0)
				.getUnclassifiedValue());
	}
	
	@Override
	public Option<String> getComment(){
		return Option.some(findParamOne(this, "cm", ""));
	}

	@Override
	public String getFullQualifiedName() {
		return fullQualifiedName;
	}

	@Override
	public List<Unit> getDescendentUnits(String query) {
		final List<Unit> result = new ArrayList<Unit>();
		try {
			result.addAll(queryFactory.create(query).selectAllFrom(getDescendentUnits(this)));
		} catch (QueryParseException e) {
			// Do nothing.
		}
		return result;
	}
	
	private List<Unit> getDescendentUnits(Unit parent) {
		final List<Unit> result = new ArrayList<Unit>();
		for (final Unit child : parent.getSubUnits()) {
			result.add(child);
			result.addAll(getDescendentUnits(child));
		}
		return result;
	}
}
