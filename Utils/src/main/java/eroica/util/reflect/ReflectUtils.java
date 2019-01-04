package eroica.util.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Tools for reflection
 * 
 * @author Minhua HUANG
 */
public class ReflectUtils {
	/**
	 * Get a method with no args by annotation or method name from clazz.
	 * 
	 * @param clazz           Class object
	 * @param annotationClass annotation class
	 * @param methodName      method name
	 * @return proper method
	 * @throws NoSuchMethodException no such method or the annotated method has args
	 */
	public static Method getNoArgsMethodByAnnotationOrName(Class<?> clazz, Class<? extends Annotation> annotationClass,
			String methodName) throws NoSuchMethodException {
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(annotationClass)) {
				if (method.getParameterTypes().length > 0)
					throw new NoSuchMethodException("The method " + clazz.getName() + "." + method.getName()
							+ " should not have any arguments.");
				return method;
			}
		}
		try {
			return clazz.getMethod(methodName, new Class[0]);
		} catch (NoSuchMethodException e) {
			throw new NoSuchMethodException("No such method. Either add @" + annotationClass.getName()
					+ " to a proper method or a public " + methodName + "() method to " + clazz.getName() + ".");
		}
	}
}
