package usertools.jp1ajs2.unitdef.core;

import java.util.Collections;
import java.util.List;

import usertools.jp1ajs2.unitdef.util.Option;
import static usertools.jp1ajs2.unitdef.util.Helpers.*;

class UnitImpl implements usertools.jp1ajs2.unitdef.core.Unit {

	private static final List<Unit> EMPTY_UNIT_DEFS = Collections.emptyList();
	
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
		this.subUnits = (subUnitDefs == null || subUnitDefs.size() == 0 ? EMPTY_UNIT_DEFS : subUnitDefs);
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
}
