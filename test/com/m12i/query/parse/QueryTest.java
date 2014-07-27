package com.m12i.query.parse;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.m12i.query.parse.Accessor;
import com.m12i.query.parse.Query;
import com.m12i.query.parse.QueryFactory;

public class QueryTest {

	private static final QueryFactory<HashMap<String,String>> factory = new QueryFactory<HashMap<String,String>>(new Accessor<HashMap<String,String>>() {
		@Override
		public String accsess(HashMap<String, String> elem, String prop) {
			return elem.get(prop);
		}
	});
	
	private static final List<HashMap<String, String>> list0 = new ArrayList<HashMap<String,String>>();
	
	private static final List<HashMap<String, String>> list1 = new ArrayList<HashMap<String,String>>();
	
	private static Query<HashMap<String,String>> create(String expr) {
		try {
			return factory.create(expr);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			throw new RuntimeException(e);
		}
	}
	
	private static HashMap<String, String> makeMap(String id, String... args) {
		final HashMap<String, String> result = new HashMap<String, String>();
		result.put("id", id);
		for (int i = 0; i < args.length; i ++) {
			result.put("key" + i, args[i]);
		}
		return result;
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		list1.add(makeMap("map0", "foo", "bar", "baz"));
		list1.add(makeMap("map1", "foo", "bar", "bax"));
		list1.add(makeMap("map2", "hello", "world"));
		list1.add(makeMap("map3", "0000", "1111", "2222", "3333"));
	}

	@Test
	public void selectOneTest00() {
		assertNull(create("key0 == foo").selectOneFrom(list0));
	}

	@Test
	public void selectOneTest10() {
		assertThat(create("key0 == foo").selectOneFrom(list1).get("id"), is("map0"));
	}

	@Test
	public void selectAllTest00() {
		assertThat(create("key0 == foo").selectAllFrom(list0).size(), is(0));
	}

	@Test
	public void selectAllTest10() {
		final List<HashMap<String, String>> res = create("key0 == foo").selectAllFrom(list1);
		assertThat(res.size(), is(2));
		assertThat(res.get(0).get("id"), is("map0"));
		assertThat(res.get(1).get("id"), is("map1"));
	}

	@Test
	public void selectAllTest11() {
		final List<HashMap<String, String>> res = create("key0 != foo").selectAllFrom(list1);
		assertThat(res.size(), is(2));
		assertThat(res.get(0).get("id"), is("map2"));
		assertThat(res.get(1).get("id"), is("map3"));
	}

	@Test
	public void selectAllTest12() {
		assertThat(create("key0 == hello").selectAllFrom(list1).get(0).get("id"), is("map2"));
	}

	@Test
	public void selectAllTest13() {
		final List<HashMap<String, String>> res = create("key0 == foo or key1 == world").selectAllFrom(list1);
		assertThat(res.size(), is(3));
	}

	@Test
	public void selectAllTest14() {
		final List<HashMap<String, String>> res = create("(key0 == foo and key1 == bar) or key1 == world").selectAllFrom(list1);
		assertThat(res.size(), is(3));
	}

	@Test
	public void selectAllTest15() {
		final List<HashMap<String, String>> res = create("key0 == foo and (key1 == bar or key1 == world)").selectAllFrom(list1);
		assertThat(res.size(), is(2));
	}

	@Test
	public void selectAllTest16() {
		final List<HashMap<String, String>> res = create("key0 == foo and ((key1 == bar) or key1 == world)").selectAllFrom(list1);
		assertThat(res.size(), is(2));
	}

	@Test
	public void selectAllTest17() {
		final List<HashMap<String, String>> res = create("key0 == foo and (key1 == bar or (key1 == world))").selectAllFrom(list1);
		assertThat(res.size(), is(2));
	}

	@Test
	public void selectAllTest18() {
		final List<HashMap<String, String>> res = create("key0 ^= f").selectAllFrom(list1);
		assertThat(res.size(), is(2));
	}

	@Test
	public void selectAllTest19() {
		final List<HashMap<String, String>> res = create("key0 $= oo").selectAllFrom(list1);
		assertThat(res.size(), is(2));
	}

	@Test
	public void selectAllTest20() {
		final List<HashMap<String, String>> res = create("key0 *= oo").selectAllFrom(list1);
		assertThat(res.size(), is(2));
	}

	@Test
	public void selectAllTest21() {
		final List<HashMap<String, String>> res = create("key0 *= o").selectAllFrom(list1);
		assertThat(res.size(), is(3));
	}

}
