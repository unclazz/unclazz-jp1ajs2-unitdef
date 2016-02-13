package org.unclazz.jp1ajs2.unitdef.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class LazyListTest {

	@Rule
	public final ExpectedException expected = ExpectedException.none();
	
	@Test
	public void isEmpty_whenNumberOfItemIs0_returnsTrue() {
		// Arrange
		final LazyList<Integer> l = LazyList.of();
		
		// Act
		final boolean r = l.isEmpty();
		
		// Assert
		assertTrue(r);
		assertThat(l.minimumSize(), equalTo(0));
	}
	
	@Test
	public void isEmpty_whenNumberOfItemGreaterThan0_returnsFalse() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0);
		
		// Act
		final boolean r = l.isEmpty();
		
		// Assert
		assertFalse(r);
		assertThat(l.minimumSize(), equalTo(0));
	}
	
	@Test
	public void isEmpty_whenNumberOfItemGreaterThan1_returnsFalse() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1);
		
		// Act
		final boolean r = l.isEmpty();
		
		// Assert
		assertFalse(r);
		assertThat(l.minimumSize(), equalTo(0));
	}
	
	@Test
	public void head_whenNumberOfItemGreaterThan0_returnsFirstItem() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0);
		
		// Act
		final int i = l.head();
		
		// Assert
		assertThat(i, equalTo(0));
		assertThat(l.minimumSize(), equalTo(1));
	}
	
	@Test
	public void head_whenNumberOfItemGreaterThan1_returnsFirstItem() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1);
		
		// Act
		final int i = l.head();
		
		// Assert
		assertThat(i, equalTo(0));
		assertThat(l.minimumSize(), equalTo(1));
	}
	
	@Test
	public void head_whenNumberOfItemGreaterThan2_returnsFirstItem() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1, 2);
		
		// Act
		final int i = l.head();
		
		// Assert
		assertThat(i, equalTo(0));
		assertThat(l.minimumSize(), equalTo(1));
	}
	
	@Test
	public void head_whenNumberOfItemIs0_throwsException() {
		// Arrange
		final LazyList<Integer> l = LazyList.of();
		expected.expect(NoSuchElementException.class);
		
		// Act
		l.head();
		
		// Assert
	}
	
	@Test
	public void get0_whenNumberOfItemIs1_returnsFirstItem() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0);
		
		// Act
		final int i = l.get(0);
		
		// Assert
		assertThat(i, equalTo(0));
		assertThat(l.minimumSize(), equalTo(1));
	}
	
	@Test
	public void get1_whenNumberOfItemIs2_returnsSecondItem() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1);
		
		// Act
		final int i = l.get(1);
		
		// Assert
		assertThat(i, equalTo(1));
		assertThat(l.minimumSize(), equalTo(2));
	}
	
	@Test
	public void get2_whenNumberOfItemIs3_returnsThirdItem() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1, 2);
		
		// Act
		final int i = l.get(2);
		
		// Assert
		assertThat(i, equalTo(2));
		assertThat(l.minimumSize(), equalTo(3));
	}
	
	@Test
	public void get1_whenNumberOfItemIs1_throwsException() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0);
		expected.expect(IndexOutOfBoundsException.class);
		
		// Act
		l.get(1);
		
		// Assert
	}
	
	@Test
	public void get2_whenNumberOfItemIs2_throwsException() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0);
		expected.expect(IndexOutOfBoundsException.class);
		
		// Act
		l.get(2);
		
		// Assert
	}
	
	@Test
	public void get3_whenNumberOfItemIs3_throwsException() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0);
		expected.expect(IndexOutOfBoundsException.class);
		
		// Act
		l.get(3);
		
		// Assert
	}
	
	@Test
	public void size_whenNumberOfItemIs1_returns1() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0);
		
		// Act
		final int i = l.size();
		
		// Assert
		assertThat(i, equalTo(1));
		assertThat(l.minimumSize(), equalTo(1));
	}
	
	@Test
	public void size_whenNumberOfItemIs2_returns2() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1);
		
		// Act
		final int i = l.size();
		
		// Assert
		assertThat(i, equalTo(2));
		assertThat(l.minimumSize(), equalTo(2));
	}
	
	@Test
	public void size_whenNumberOfItemIs3_returns3() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1, 2);
		
		// Act
		final int i = l.size();
		
		// Assert
		assertThat(i, equalTo(3));
		assertThat(l.minimumSize(), equalTo(3));
	}
	
	@Test
	public void tail_whenNumberOfItemIs1_returnsEmptyList() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0);
		
		// Act
		final LazyList<Integer> r = l.tail();
		
		// Assert
		assertTrue(r.isEmpty());
	}
	
	@Test
	public void tail_whenNumberOfItemIs2_returnsSingletonList() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1);
		
		// Act
		final LazyList<Integer> r = l.tail();
		
		// Assert
		assertThat(r.size(), equalTo(1));
	}
	
	@Test
	public void tail_whenNumberOfItemIs3_returnsListContains2Elements() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1, 2);
		
		// Act
		final LazyList<Integer> r = l.tail();
		
		// Assert
		assertThat(r.size(), equalTo(2));
	}
	
	@Test
	public void indexOf1_whenListMadeOf012_returns1() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1, 2);
		
		// Act
		final int r = l.indexOf(1);
		
		// Assert
		assertThat(r, equalTo(1));
		assertThat(l.minimumSize(), equalTo(2));
	}
	
	@Test
	public void indexOf3_whenListMadeOf012_returnsMinus1() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1, 2);
		
		// Act
		final int r = l.indexOf(3);
		
		// Assert
		assertThat(r, equalTo(-1));
		assertThat(l.minimumSize(), equalTo(3));
	}
	
	@Test
	public void lastIndexOf1_whenListMadeOf010_returns1() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1, 0);
		
		// Act
		final int r = l.lastIndexOf(1);
		
		// Assert
		assertThat(r, equalTo(1));
		assertThat(l.minimumSize(), equalTo(3));
	}
	
	@Test
	public void lastIndexOf3_whenListMadeOf010_returnsMinus1() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1, 2);
		
		// Act
		final int r = l.lastIndexOf(3);
		
		// Assert
		assertThat(r, equalTo(-1));
		assertThat(l.minimumSize(), equalTo(3));
	}
	
	@Test
	public void minumusSize_whenListHasBeenCreatedByTailMethod_returns0() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1, 2, 3);
		
		// Act
		final LazyList<Integer> r = l.tail();
		
		// Assert
		assertThat(r.minimumSize(), equalTo(0));
		assertThat(r.head(), equalTo(1));
		assertThat(r.minimumSize(), equalTo(1));
	}
	
	@Test
	public void minumusSize_whenTailListMinimumSizeReturns1_returns2() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1, 2, 3);
		
		// Act
		final LazyList<Integer> r = l.tail();
		
		// Assert
		assertThat(r.head(), equalTo(1));
		assertThat(r.minimumSize(), equalTo(1));
		assertThat(l.minimumSize(), equalTo(2));
	}
	
	@Test
	public void subList_always_makesMinimumSizeUpToDate() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1, 2, 3, 4);
		
		// Act
		final List<Integer> r = l.subList(2, 4);
		
		// Assert
		assertThat(l.minimumSize(), equalTo(4));
		assertThat(r.size(), equalTo(2));
	}
	
	@Test
	public void contains1_whenListMadeOf01234_returnsTrueAndMakesMinimumSize2() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1, 2, 3, 4);
		
		// Act
		final boolean r = l.contains(1);
		
		// Assert
		assertTrue(r);
		assertThat(l.minimumSize(), equalTo(2));
	}
	
	@Test
	public void contains4_whenListMadeOf01234_returnsTrueAndMakesMinimumSize5() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1, 2, 3, 4);
		
		// Act
		final boolean r = l.contains(4);
		
		// Assert
		assertTrue(r);
		assertThat(l.minimumSize(), equalTo(5));
	}
	
	@Test
	public void contains5_whenListMadeOf01234_returnsFalseAndMakesMinimumSize5() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1, 2, 3, 4);
		
		// Act
		final boolean r = l.contains(5);
		
		// Assert
		assertFalse(r);
		assertThat(l.minimumSize(), equalTo(5));
	}
	
	@Test
	public void containsAll123_whenListMadeOf01234_returnsTrueAndMakesMinimumSize4() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1, 2, 3, 4);
		
		// Act
		final boolean r = l.containsAll(Arrays.asList(1,2,3));
		
		// Assert
		assertTrue(r);
		assertThat(l.minimumSize(), equalTo(4));
	}
	
	@Test
	public void containsAll125_whenListMadeOf01234_returnsFalseAndMakesMinimumSize5() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1, 2, 3, 4);
		
		// Act
		final boolean r = l.containsAll(Arrays.asList(1,2,5));
		
		// Assert
		assertFalse(r);
		assertThat(l.minimumSize(), equalTo(5));
	}
	
	@Test
	public void addIntT_always_throwsException() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1, 2, 3);
		expected.expect(UnsupportedOperationException.class);
		
		// Act
		l.add(1, 1);
	}
	
	@Test
	public void addT_always_throwsException() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1, 2, 3);
		expected.expect(UnsupportedOperationException.class);
		
		// Act
		l.add(1);
	}
	
	@Test
	public void addAllCollection_always_throwsException() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1, 2, 3);
		expected.expect(UnsupportedOperationException.class);
		
		// Act
		l.addAll(Arrays.asList(4, 5, 6));
	}
	
	@Test
	public void addAllIntCollection_always_throwsException() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1, 2, 3);
		expected.expect(UnsupportedOperationException.class);
		
		// Act
		l.addAll(1, Arrays.asList(4, 5, 6));
	}
	
	@Test
	public void clear_always_throwsException() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1, 2, 3);
		expected.expect(UnsupportedOperationException.class);
		
		// Act
		l.clear();
	}
	
	@Test
	public void removeInt_always_throwsException() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1, 2, 3);
		expected.expect(UnsupportedOperationException.class);
		
		// Act
		l.remove((int) 1);
	}
	
	@Test
	public void removeObject_always_throwsException() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1, 2, 3);
		expected.expect(UnsupportedOperationException.class);
		
		// Act
		l.remove(new Object());
	}
	
	@Test
	public void removeAll_always_throwsException() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1, 2, 3);
		expected.expect(UnsupportedOperationException.class);
		
		// Act
		l.removeAll(Arrays.asList(1, 2));
	}
	
	@Test
	public void retainAll_always_throwsException() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1, 2, 3);
		expected.expect(UnsupportedOperationException.class);
		
		// Act
		l.retainAll(Arrays.asList(1, 2));
	}
	
	@Test
	public void set_always_throwsException() {
		// Arrange
		final LazyList<Integer> l = LazyList.of(0, 1, 2, 3);
		expected.expect(UnsupportedOperationException.class);
		
		// Act
		l.set(1, 1);
	}
}
