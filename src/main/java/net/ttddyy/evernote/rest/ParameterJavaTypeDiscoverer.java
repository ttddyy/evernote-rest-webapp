package net.ttddyy.evernote.rest;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Find jackson's {@link JavaType}.
 *
 * @author Tadaya Tsuyukubo
 */
public class ParameterJavaTypeDiscoverer {

	@Autowired
	private ObjectMapper objectMapper = new ObjectMapper();  // set default

	/**
	 * Resolve jackson's {@link JavaType}s for given method's parameters.
	 *
	 * @param actualMethod
	 * @return
	 */
	public JavaType[] getParameterJavaTypes(Method actualMethod) {
		final Class<?>[] parameterTypes = actualMethod.getParameterTypes();
		final List<JavaType> javaTypes = new ArrayList<JavaType>(parameterTypes.length);
		for (int i = 0; i < parameterTypes.length; i++) {
			final Class<?> parameterType = parameterTypes[i];
			final boolean isList = parameterType.isAssignableFrom(List.class);
			final boolean isSet = parameterType.isAssignableFrom(Set.class);

			final JavaType type;
			if (isList || isSet) {
				// resolve generic type
				final ResolvableType resolvableType = ResolvableType.forMethodParameter(actualMethod, i);
				final Class<?> genericClass = resolvableType.getGeneric(0).resolve();
				if (genericClass == null) {
					// if couldn't resolve generic type, fallback to parameter type
					type = this.objectMapper.constructType(parameterType);
				} else {
					if (isList) {
						type = this.objectMapper.getTypeFactory().constructCollectionType(List.class, genericClass);
					} else {
						type = this.objectMapper.getTypeFactory().constructCollectionType(Set.class, genericClass);
					}
				}
			} else {
				type = this.objectMapper.constructType(parameterType);
			}

			javaTypes.add(type);
		}
		return javaTypes.toArray(new JavaType[javaTypes.size()]);
	}

}
