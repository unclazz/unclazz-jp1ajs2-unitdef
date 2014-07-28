package com.m12i.query.parse;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class QueryFactoryTest {
	
	static class FooBarBean {
		private final String a;
		private final String b;
		private final String foo;
		private final String bar;
		public FooBarBean(String propA, String propB, String propFoo, String propBar) {
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
		public String getFoo() {
			return foo;
		}
		public String getBar() {
			return bar;
		}
	}

	private static final List<FooBarBean> beanList = new ArrayList<QueryFactoryTest.FooBarBean>();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		beanList.add(new FooBarBean("1", "2", "3", "4"));
		beanList.add(new FooBarBean("2", "2", "3", "4"));
		beanList.add(new FooBarBean("1", "1", "3", "3"));
		beanList.add(new FooBarBean("2", "3", "4", "4"));
	}

	@Test
	public void createBeanQueryFactoryTest00() {
		final QueryFactory<FooBarBean> factory = QueryFactory.createBeanQueryFactory(FooBarBean.class);
		try {
			final Query<FooBarBean> query0 = factory.create("a == 1 and foo == 3");
			assertTrue(query0.selectAllFrom(beanList).size() == 2);
			final Query<FooBarBean> query1 = factory.create("a == 1 and foo == 3 && baz is null");
			assertTrue(query1.selectAllFrom(beanList).size() == 2);
			
		} catch (QueryParseException e) {
			e.printStackTrace();
			fail();
		}
	}

}
