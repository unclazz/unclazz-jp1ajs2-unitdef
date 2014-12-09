package com.m12i.jp1ajs2.unitdef.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.m12i.jp1ajs2.unitdef.Option;
import com.m12i.jp1ajs2.unitdef.Param;
import com.m12i.jp1ajs2.unitdef.Unit;
import com.m12i.jp1ajs2.unitdef.UnitType;

import static com.m12i.jp1ajs2.unitdef.Helpers.*;

final class UnitImpl implements Unit {

	private static final List<Unit> emptyUnitDefList = Collections.emptyList();
	
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
	public List<Param> getParams(String paramName) {
		final List<Param> result = new ArrayList<Param>();
		for (final Param p : params) {
			if (p.getName().equals(paramName)) {
				result.add(p);
			}
		}
		return result;
	}

	private void collectSubUnits(List<Unit> list, Unit unit) {
		list.add(unit);
		for (final Unit child : unit.getSubUnits()) {
			collectSubUnits(list, child);
		}
	}
	
	@Override
	public Iterator<Unit> iterator() {
		final ArrayList<Unit> list = new ArrayList<>();
		collectSubUnits(list, this);
		return list.iterator();
	}
}
