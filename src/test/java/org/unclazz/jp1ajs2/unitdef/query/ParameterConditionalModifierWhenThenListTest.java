package org.unclazz.jp1ajs2.unitdef.query;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.builder.Builders;
import org.unclazz.jp1ajs2.unitdef.builder.ParameterBuilder;
import org.unclazz.jp1ajs2.unitdef.query.ParameterConditionalModifier.WhenThenEntry;
import org.unclazz.jp1ajs2.unitdef.query.ParameterConditionalModifier.WhenThenList;
import org.unclazz.jp1ajs2.unitdef.util.Function;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;

public class ParameterConditionalModifierWhenThenListTest {
	public static class PredeterminedPredicate implements Predicate<Parameter> {
		public static final PredeterminedPredicate ALWAYS_TRUE = new PredeterminedPredicate(true);
		public static final PredeterminedPredicate ALWAYS_FALSE = new PredeterminedPredicate(false);
		private final boolean b;
		private PredeterminedPredicate(boolean b) {
			this.b = b;
		}
		@Override
		public boolean test(Parameter t) {
			return b;
		}
	}
	public static class ReplaceValueAt0 implements Function<Parameter, Parameter> {
		private final String s;
		private ReplaceValueAt0(String s) {
			this.s = s;
		}
		@Override
		public Parameter apply(Parameter t) {
			final LinkedList<ParameterValue> vs = new LinkedList<ParameterValue>(t.getValues());
			vs.poll();
			vs.addFirst(Builders.rawStringParameterValue(s));
			return Builders.parameter().setName(t.getName()).addValues(vs).build();
		}
	}
	
	private Parameter makeParameter(String name, String... values) {
		final ParameterBuilder b = Builders.parameter().setName(name);
		for (final String value : values) {
			b.addRawCharSequence(value);
		}
		return b.build();
	}
	
	@Test
	public void iterator_whenContainsNoElement_returnsEmptyIterator() {
		// Arrange
		final WhenThenList l0 = WhenThenList.emptyInstance();
		
		// Act
		
		// Assert
		for (WhenThenEntry e : l0) {
			fail(e.toString());
		}
	}
	@Test
	public void cons_always_returnsNewInstance() {
		// Arrange
		final WhenThenList l0 = WhenThenList.emptyInstance();
		
		// Act
		final WhenThenList l1 = l0.cons(new WhenThenEntry
				(PredeterminedPredicate.ALWAYS_TRUE,
				new ReplaceValueAt0("hello")));
		
		// Assert
		assertFalse(l0 == l1);
		final Iterator<WhenThenEntry> iter = l1.iterator();
		assertTrue(iter.next().getCondition() == PredeterminedPredicate.ALWAYS_TRUE);
		assertFalse(iter.hasNext());
	}
	@Test
	public void concat_always_returnsNewInstance() {
		// Arrange
		final WhenThenList l0 = WhenThenList.emptyInstance()
				.cons(new WhenThenEntry(PredeterminedPredicate.ALWAYS_FALSE,
				new ReplaceValueAt0("hello")));
		final WhenThenList l1 = WhenThenList.emptyInstance()
				.cons(new WhenThenEntry(PredeterminedPredicate.ALWAYS_TRUE,
				new ReplaceValueAt0("bonjour")));
		
		// Act
		final WhenThenList l2 = l0.concat(l1);
		
		// Assert
		assertFalse(l0 == l1);
		assertFalse(l0 == l2);
		assertFalse(l1 == l2);
		final Iterator<WhenThenEntry> iter = l2.iterator();
		assertTrue(iter.next().getCondition() == PredeterminedPredicate.ALWAYS_FALSE);
		assertTrue(iter.next().getCondition() == PredeterminedPredicate.ALWAYS_TRUE);
		assertFalse(iter.hasNext());
	}
	@Test
	public void apply_always_applysTestToParameterInOrder() {
		// Arrange
		final WhenThenList l0 = WhenThenList.emptyInstance()
				.cons(new WhenThenEntry(PredeterminedPredicate.ALWAYS_FALSE,
				new ReplaceValueAt0("hello")))
				.cons(new WhenThenEntry(PredeterminedPredicate.ALWAYS_TRUE,
				new ReplaceValueAt0("bonjour")))
				.cons(new WhenThenEntry(PredeterminedPredicate.ALWAYS_TRUE,
				new ReplaceValueAt0("こんにちは")));
		
		// Act
		final Parameter p0 = l0.apply(makeParameter("foo", "bar", "baz"));
		final Parameter p1 = l0.apply(makeParameter("foo", "baz", "bar"));
		
		// Assert
		assertThat(p0.getValues().get(0).getStringValue(), equalTo("bonjour"));
		assertThat(p0.getValues().get(1).getStringValue(), equalTo("baz"));
		assertThat(p1.getValues().get(0).getStringValue(), equalTo("bonjour"));
		assertThat(p1.getValues().get(1).getStringValue(), equalTo("bar"));
	}
}
