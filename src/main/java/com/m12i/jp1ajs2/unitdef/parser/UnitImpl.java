package com.m12i.jp1ajs2.unitdef.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.m12i.jp1ajs2.unitdef.Param;
import com.m12i.jp1ajs2.unitdef.Params;
import com.m12i.jp1ajs2.unitdef.Unit;
import com.m12i.jp1ajs2.unitdef.UnitType;
import com.m12i.jp1ajs2.unitdef.Units;
import com.m12i.jp1ajs2.unitdef.util.Maybe;

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
		for (final Param p : params) {
			((ParamImpl) p).setUnit(this);
		}
	}
	
	@Override
	public Maybe<String> getPermissionMode() {
		return Maybe.wrap(permissionMode);
	}

	@Override
	public Maybe<String> getOwnerName() {
		return Maybe.wrap(ownerName);
	}
	
	@Override
	public Maybe<String> getResourceGroupName() {
		return Maybe.wrap(resourceGroup);
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
	public Maybe<Unit> getSubUnits(final String unitName){
		return Units.getSubUnits(this, unitName);
	}
	
	@Override
	public Maybe<Unit> getDescendentUnits(final String unitName){
		return Units.getDescendentUnits(this, unitName);
	}
	
	@Override
	public UnitType getType(){
		return UnitType.forCode(Params.getStringValues(this, "ty").get());
	}
	
	@Override
	public Maybe<String> getComment(){
		return Params.getStringValues(this, "cm");
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

	@Override
	public Iterator<Unit> iterator() {
		return Units.asList(this).iterator();
	}
}
