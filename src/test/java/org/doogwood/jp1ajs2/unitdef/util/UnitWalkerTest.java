package org.doogwood.jp1ajs2.unitdef.util;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.doogwood.jp1ajs2.unitdef.Param;
import org.doogwood.jp1ajs2.unitdef.TestUtils;
import org.doogwood.jp1ajs2.unitdef.Unit;
import org.doogwood.jp1ajs2.unitdef.util.UnitWalker;
import org.junit.Test;

public class UnitWalkerTest {
	
	private static class UnitWalkerWithStringBuilder extends UnitWalker<StringBuilder> {
		@Override
		protected void handleStart(Unit root, StringBuilder context) {}
		@Override
		protected void handleUnitStart(Unit unit, int depth, StringBuilder context) {}
		@Override
		protected void handleUnitEnd(Unit unit, int depth, StringBuilder context) {}
		@Override
		protected void handleParam(Param param, int depth, StringBuilder context) {}
		@Override
		protected void handleEnd(Unit root, StringBuilder context) {}
	};

	@Test
	public void handleCancelledTest() {
		final UnitWalkerWithStringBuilder w0 = new UnitWalkerWithStringBuilder() {
			@Override
			protected void handleCancelled(Unit root, StringBuilder context, CancelException cancel) {
				assertThat(cancel.getMessage(), is("foo"));
			}
			@Override
			protected boolean handleUnit(Unit unit, int depth, StringBuilder context) {
				throw new CancelException("foo", unit, depth);
			}
		};
		w0.walk(TestUtils.jobnetUnitDef1(), new StringBuilder());

		final UnitWalkerWithStringBuilder w1 = new UnitWalkerWithStringBuilder() {
			@Override
			protected void handleCancelled(Unit root, StringBuilder context, CancelException cancel) {
				assertThat(cancel.getMessage(), is("bar"));
			}
			@Override
			protected void handleStart(Unit root, StringBuilder context) {
				throw new CancelException("bar", root, 0);
			}
		};
		w1.walk(TestUtils.jobnetUnitDef1(), new StringBuilder());

		final UnitWalkerWithStringBuilder w2 = new UnitWalkerWithStringBuilder() {
			@Override
			protected void handleCancelled(Unit root, StringBuilder context, CancelException cancel) {
				assertThat(cancel.getMessage(), is("baz"));
			}
			@Override
			protected void handleUnitStart(Unit unit, int depth, StringBuilder context) {
				throw new CancelException("baz", unit, depth);
			}
		};
		w2.walk(TestUtils.jobnetUnitDef1(), new StringBuilder());

		final UnitWalkerWithStringBuilder w3 = new UnitWalkerWithStringBuilder() {
			@Override
			protected void handleCancelled(Unit root, StringBuilder context, CancelException cancel) {
				assertThat(cancel.getMessage(), is("hello"));
			}
			@Override
			protected void handleUnitEnd(Unit unit, int depth, StringBuilder context) {
				throw new CancelException("hello", unit, depth);
			}
		};
		w3.walk(TestUtils.jobnetUnitDef1(), new StringBuilder());

		final UnitWalkerWithStringBuilder w4 = new UnitWalkerWithStringBuilder() {
			@Override
			protected void handleCancelled(Unit root, StringBuilder context, CancelException cancel) {
				assertThat(cancel.getMessage(), is("world"));
			}
			@Override
			protected void handleParam(Param param, int depth, StringBuilder context) {
				throw new CancelException("world", param.getUnit(), depth);
			}
		};
		w4.walk(TestUtils.jobnetUnitDef1(), new StringBuilder());

		final UnitWalkerWithStringBuilder w5 = new UnitWalkerWithStringBuilder() {
			@Override
			protected void handleCancelled(Unit root, StringBuilder context, CancelException cancel) {
				assertThat(cancel.getMessage(), is("bonjour"));
			}
			@Override
			protected void handleEnd(Unit root, StringBuilder context) {
				throw new CancelException("bonjour", root, 0);
			}
		};
		w5.walk(TestUtils.jobnetUnitDef1(), new StringBuilder());

		final UnitWalkerWithStringBuilder w6 = new UnitWalkerWithStringBuilder() {
			@Override
			protected void handleCancelled(Unit root, StringBuilder context, CancelException cancel) {
				throw new RuntimeException(cancel);
			}
			@Override
			protected void handleEnd(Unit root, StringBuilder context) {
				throw new CancelException("error", root, 0);
			}
		};
		try {
			w6.walk(TestUtils.jobnetUnitDef1(), new StringBuilder());
			fail();
		} catch (final RuntimeException e) {
			// Ok.
		}

		final UnitWalkerWithStringBuilder w7 = new UnitWalkerWithStringBuilder() {
			@Override
			protected void handleCancelled(Unit root, StringBuilder context, CancelException cancel) {
				throw cancel;
			}
			@Override
			protected void handleEnd(Unit root, StringBuilder context) {
				throw new CancelException("error", root, 0);
			}
		};
		try {
			w7.walk(TestUtils.jobnetUnitDef1(), new StringBuilder());
		} catch (final RuntimeException e) {
			fail();
		}
	}
	
	@Test
	public void handleUnitTest() {
		final UnitWalkerWithStringBuilder w0 = new UnitWalkerWithStringBuilder() {
			@Override
			protected boolean handleUnit(Unit unit, int depth, StringBuilder context) {
				context.append("ok");
				return false;
			}
			@Override
			protected void handleUnitStart(Unit unit, int depth, StringBuilder context) {
				context.append("ng");
				throw new RuntimeException();
			}
		};
		final StringBuilder sb0 = new StringBuilder();
		w0.walk(TestUtils.jobnetUnitDef1(), sb0);
		assertThat(sb0.toString(), is("ok"));

		final UnitWalkerWithStringBuilder w1 = new UnitWalkerWithStringBuilder() {
			@Override
			protected boolean handleUnit(Unit unit, int depth, StringBuilder context) {
				context.append("ok");
				return true;
			}
			@Override
			protected void handleUnitStart(Unit unit, int depth, StringBuilder context) {
				context.append("ng");
				throw new RuntimeException();
			}
		};
		final StringBuilder sb1 = new StringBuilder();
		try {
			w1.walk(TestUtils.jobnetUnitDef1(), sb1);
			fail();
		} catch (final RuntimeException e) {
			assertThat(sb1.toString(), is("okng"));
		}
	}
	
	@Test
	public void handleStartTest() {
		final UnitWalkerWithStringBuilder w0 = new UnitWalkerWithStringBuilder() {
			@Override
			protected void handleStart(Unit root, StringBuilder context) {
				context.append(root.getName());
			}
		};
		final StringBuilder sb0 = new StringBuilder();
		w0.walk(TestUtils.nestedUnitDef1(), sb0);
		assertThat(sb0.toString(), is("XXXX0000"));
	}
	
	@Test
	public void handleUnitStartTest() {
		final UnitWalkerWithStringBuilder w0 = new UnitWalkerWithStringBuilder() {
			@Override
			protected void handleUnitStart(Unit unit, int depth, StringBuilder context) {
				context.append(unit.getName());
			}
		};
		final StringBuilder sb0 = new StringBuilder();
		w0.walk(TestUtils.nestedUnitDef1(), sb0);
		assertThat(sb0.toString(), is("XXXX0000XXXX0001XXXX0002"));
	}
	
	@Test
	public void handleUnitEndTest() {
		final UnitWalkerWithStringBuilder w0 = new UnitWalkerWithStringBuilder() {
			@Override
			protected void handleUnitEnd(Unit unit, int depth, StringBuilder context) {
				context.append(unit.getName());
			}
		};
		final StringBuilder sb0 = new StringBuilder();
		w0.walk(TestUtils.nestedUnitDef1(), sb0);
		assertThat(sb0.toString(), is("XXXX0001XXXX0002XXXX0000"));
	}
	
	@Test
	public void handleParamTest() {
		final UnitWalkerWithStringBuilder w0 = new UnitWalkerWithStringBuilder() {
			@Override
			protected void handleParam(Param param, int depth, StringBuilder context) {
				context.append(param.getName()).append(',');
			}
		};
		final StringBuilder sb0 = new StringBuilder();
		w0.walk(TestUtils.nestedUnitDef1(), sb0);
		assertThat(sb0.toString(), is("ty,el,el,ar,ar,cm,xx,un,fd,ty,cm,ty,cm,"));
	}

	@Test
	public void handleEndTest() {
		final UnitWalkerWithStringBuilder w0 = new UnitWalkerWithStringBuilder() {
			@Override
			protected void handleEnd(Unit root, StringBuilder context) {
				context.append(root.getName());
			}
		};
		final StringBuilder sb0 = new StringBuilder();
		w0.walk(TestUtils.nestedUnitDef1(), sb0);
		assertThat(sb0.toString(), is("XXXX0000"));
		
		final UnitWalkerWithStringBuilder w1 = new UnitWalkerWithStringBuilder() {
			@Override
			protected boolean handleUnit(Unit unit, int depth, StringBuilder context) {
				context.append("handleUnit,");
				return true;
			}
			@Override
			protected void handleStart(Unit root, StringBuilder context) {
				context.append("handleStart,");
			}
			@Override
			protected void handleUnitStart(Unit unit, int depth, StringBuilder context) {
				context.append("handleUnitStart,");
			}
			@Override
			protected void handleUnitEnd(Unit unit, int depth, StringBuilder context) {
				context.append("handleUnitEnd,");
			}
			@Override
			protected void handleParam(Param param, int depth, StringBuilder context) {
				context.append("handleParam,");
			}
			@Override
			protected void handleEnd(Unit root, StringBuilder context) {
				context.append("handleEnd");
			}
		};
		final StringBuilder sb1 = new StringBuilder();
		w1.walk(TestUtils.nestedUnitDef1(), sb1);
		assertThat(sb1.toString(), is(""
				+ "handleStart,"
				+ "handleUnit,"
				+ "handleUnitStart,"
				+ "handleParam,handleParam,handleParam,handleParam,"
				+ "handleParam,handleParam,handleParam,handleParam,handleParam,"
				+ "handleUnit,"
				+ "handleUnitStart,"
				+ "handleParam,handleParam,"
				+ "handleUnitEnd,"
				+ "handleUnit,"
				+ "handleUnitStart,"
				+ "handleParam,handleParam,"
				+ "handleUnitEnd,"
				+ "handleUnitEnd,"
				+ "handleEnd"
				+ ""));
	}
}
