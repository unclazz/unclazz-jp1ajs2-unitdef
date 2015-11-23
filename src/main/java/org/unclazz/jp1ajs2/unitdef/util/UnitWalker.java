package org.unclazz.jp1ajs2.unitdef.util;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.Unit;

public abstract class UnitWalker<T> {
	public static final class CancelException extends RuntimeException {
		private static final long serialVersionUID = 6618246991255457812L;
		private final Unit unit;
		private final int depth;
		public CancelException(final Unit unit, final int depth) {
			this.unit = unit;
			this.depth = depth;
		}
		public CancelException(final String message, final Unit unit, final int depth) {
			super(message);
			this.unit = unit;
			this.depth = depth;
		}
		public CancelException(final Throwable cause, final Unit unit, final int depth) {
			super(cause);
			this.unit = unit;
			this.depth = depth;
		}
		public Unit getUnit() {
			return unit;
		}
		public int getDepth() {
			return depth;
		}
	}
	
	protected abstract void handleStart(Unit root, T context);
	protected boolean handleUnit(Unit unit, int depth, T context) {
		// Default implementation always returns true.
		return true;
	}
	protected abstract void handleUnitStart(Unit unit, int depth, T context);
	protected abstract void handleUnitEnd(Unit unit, int depth, T context);
	protected abstract void handleParam(Unit unit, Parameter param, int depth, T context);
	protected abstract void handleEnd(Unit root, T context);
	protected void handleCancelled(Unit root, T context, CancelException cancel) {
		throw cancel;
	}
	protected void walk(Unit root, T context) {
		try {
			handleStart(root, context);
			walkHelper(root, 0, context);
			handleEnd(root, context);
		} catch (final CancelException cancel) {
			try {
				handleCancelled(root, context, cancel);
			} catch (final CancelException cancel2) {
				// Do nothing.
				return;
			}
		}
	}
	private void walkHelper(Unit unit, int depth, T context) {
		if (!handleUnit(unit, depth, context)) {
			return;
		}
		// unit=...,...,...,...;{
		handleUnitStart(unit, depth, context);
		final int subDepth = depth + 1;
		for (final Parameter param : unit.getParameters()) {
			// xx=...,...,...;
			handleParam(unit, param, subDepth, context);
		}
		for (final Unit subUnit : unit.getSubUnits()) {
			// unit=...,...,...,...;{...}
			walkHelper(subUnit, subDepth, context);
		}
		// }
		handleUnitEnd(unit, depth, context);
	}
}
