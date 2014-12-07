package com.m12i.query.parse;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class QueryFactoryTest {
	
	static class FooBarBean {
		private final String a;
		private final String b;
		private final String foo;
		private final int bar;
		public FooBarBean(String propA, String propB, String propFoo, int propBar) {
			this.a = propA;
			this.b = propB;
			this.foo = propFoo;
			this.bar = propBar;
		}
		public String getA() {
			return a;
		}
		public String getB() {
			return b;
		}
		public int c() {
			return Integer.parseInt(a) + Integer.parseInt(b);
		}
		public String getFoo() {
			return foo;
		}
		public int bar() {
			return bar;
		}
		public String getBar() {
			return "BAR";
		}
	}

	private static final List<FooBarBean> beanList = new ArrayList<QueryFactoryTest.FooBarBean>();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		beanList.add(new FooBarBean("1", "2", "3", 4));
		beanList.add(new FooBarBean("2", "2", "3", 4));
		beanList.add(new FooBarBean("1", "1", "3", 3));
		beanList.add(new FooBarBean("2", "3", "4", 1));
	}

	@Test
	public void createBeanQueryFactoryTest00() {
		final QueryFactory<FooBarBean> factory = QueryFactory.createBeanQueryFactory(FooBarBean.class);
		try {
			// JavaBeansの規約どおりのケース
			final Query<FooBarBean> query0 = factory.create("a == 1 and foo == 3");
			assertThat(query0.selectAllFrom(beanList).size(), is(2));
		} catch (QueryParseException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void createBeanQueryFactoryTest01() {
		final QueryFactory<FooBarBean> factory = QueryFactory.createBeanQueryFactory(FooBarBean.class);
		try {
			// メソッドが存在しなければnullとみなされる
			final Query<FooBarBean> query1 = factory.create("a == 1 and foo == 3 && baz is null");
			assertThat(query1.selectAllFrom(beanList).size(), is(2));
		} catch (QueryParseException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void createBeanQueryFactoryTest02() {
		final QueryFactory<FooBarBean> factory = QueryFactory.createBeanQueryFactory(FooBarBean.class);
		try {
			// getBar()とbar()があればgetBar()がコールされる
			final Query<FooBarBean> query2 = factory.create("a == 1 and foo == 3 && bar == BAR");
			assertThat(query2.selectAllFrom(beanList).size(), is(2));
		} catch (QueryParseException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void createBeanQueryFactoryTest04() {
		final QueryFactory<FooBarBean> factory = QueryFactory.createBeanQueryFactory(FooBarBean.class);
		try {
			// getC()がなければc()がコールされる
			final Query<FooBarBean> query3 = factory.create("a == 1 and foo == 3 && c == 3");
			assertThat(query3.selectAllFrom(beanList).size(), is(1));
		} catch (QueryParseException e) {
			e.printStackTrace();
			fail();
		}
	}

}
