package net.ttddyy.evernote.rest;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Tadaya Tsuyukubo
 */
@RunWith(Parameterized.class)
public class ParameterJavaTypeDiscovererTest {

	public static class Foo {
		public void takeString(String string) {
		}

		public void takeNothing() {
		}

		public void takeStringList(List<String> list) {
		}

		public void takeIntegerList(List<Integer> list) {
		}

		public void takeLongList(List<Long> list) {
		}

		public void takeStringSet(Set<String> list) {
		}

		public void takeIntegerSet(Set<Integer> list) {
		}

		public void takeLongSet(Set<Long> list) {
		}

		public void takeStringAndInteger(String string, Integer integer) {
		}
	}

	private Method method;
	private JavaType[] javaTypes;

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		TypeFactory typeFactory = TypeFactory.defaultInstance();

		return Arrays.asList(new Object[][]{
				// method, expected JavaTypes
				{ReflectionUtils.findMethod(Foo.class, "takeString", null),  // null indicates search by name
						new JavaType[]{typeFactory.constructType(String.class)}},
				{ReflectionUtils.findMethod(Foo.class, "takeNothing", null), new JavaType[]{}},
				{ReflectionUtils.findMethod(Foo.class, "takeStringList", null),
						new JavaType[]{typeFactory.constructCollectionType(List.class, String.class)}},
				{ReflectionUtils.findMethod(Foo.class, "takeIntegerList", null),
						new JavaType[]{typeFactory.constructCollectionType(List.class, Integer.class)}},
				{ReflectionUtils.findMethod(Foo.class, "takeLongList", null),
						new JavaType[]{typeFactory.constructCollectionType(List.class, Long.class)}},
				{ReflectionUtils.findMethod(Foo.class, "takeStringSet", null),
						new JavaType[]{typeFactory.constructCollectionType(Set.class, String.class)}},
				{ReflectionUtils.findMethod(Foo.class, "takeIntegerSet", null),
						new JavaType[]{typeFactory.constructCollectionType(Set.class, Integer.class)}},
				{ReflectionUtils.findMethod(Foo.class, "takeLongSet", null),
						new JavaType[]{typeFactory.constructCollectionType(Set.class, Long.class)}},
				{ReflectionUtils.findMethod(Foo.class, "takeStringAndInteger", null),
						new JavaType[]{typeFactory.constructType(String.class), typeFactory.constructType(Integer.class)}},
		});
	}

	public ParameterJavaTypeDiscovererTest(Method method, JavaType[] javaTypes) {
		this.method = method;
		this.javaTypes = javaTypes;
	}

	@Test
	public void testGetParameterJavaTypes() {
		ParameterJavaTypeDiscoverer discoverer = new ParameterJavaTypeDiscoverer();

		JavaType[] types = discoverer.getParameterJavaTypes(this.method);
		assertThat(types, is(javaTypes));
	}

}
