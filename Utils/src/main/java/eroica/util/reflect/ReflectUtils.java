package eroica.util.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.compress.utils.Sets;

import eroica.util.exception.MoreThanOneElementException;

/**
 * Tools for reflection.
 * 
 * @author Minhua HUANG
 */
public class ReflectUtils {

	public static Method[] getMethodsByAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass)
			throws NoSuchMethodException {
		Method[] methods = clazz.getMethods();
		List<Method> tmp = new ArrayList<Method>();
		for (Method method : methods)
			if (method.isAnnotationPresent(annotationClass))
				tmp.add(method);
		if (tmp.isEmpty())
			throw new NoSuchMethodException(
					"No such method. Please add @" + annotationClass.getName() + " to a proper method.");
		return tmp.toArray(new Method[0]);
	}

	public static Method[] getDeclaredMethodsByAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass)
			throws NoSuchMethodException {
		Method[] methods = clazz.getDeclaredMethods();
		List<Method> tmp = new ArrayList<Method>();
		for (Method method : methods)
			if (method.isAnnotationPresent(annotationClass))
				tmp.add(method);
		if (tmp.isEmpty())
			throw new NoSuchMethodException(
					"No such method. Please add @" + annotationClass.getName() + " to a proper method.");
		return tmp.toArray(new Method[0]);
	}

	public static Method getMethodByAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass)
			throws NoSuchMethodException {
		Method[] ms = getMethodsByAnnotation(clazz, annotationClass);
		if (ms.length > 1)
			throw new MoreThanOneElementException("Class " + clazz.getName()
					+ " has multiple methods that have the annotation of @" + annotationClass.getName());
		return ms[0];
	}

	public static Method getDeclaredMethodByAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass)
			throws NoSuchMethodException {
		Method[] ms = getDeclaredMethodsByAnnotation(clazz, annotationClass);
		if (ms.length > 1)
			throw new MoreThanOneElementException("Class " + clazz.getName()
					+ " has multiple declared methods that have the annotation of @" + annotationClass.getName());
		return ms[0];
	}

	public static List<Class<?>> getSuperClasses(Class<?> clazz, boolean includingThis) {
		List<Class<?>> l = getSuperClasses(clazz);
		if (includingThis)
			l.add(0, clazz);
		return l;
	}

	public static List<Class<?>> getSuperClasses(Class<?> clazz) {
		Class<?> superClass = clazz.getSuperclass();
		if (superClass == null)
			return new LinkedList<Class<?>>();
		else {
			List<Class<?>> l = getSuperClasses(superClass);
			l.add(0, superClass);
			return l;
		}
	}

	public static Set<Class<?>> getInterfaces(Class<?> clazz) {
		Class<?>[] cs = clazz.getInterfaces();
		if (cs.length == 0)
			return null;
		else {
			Set<Class<?>> set = new HashSet<Class<?>>(Arrays.asList(cs));
			for (Class<?> c : cs) {
				Set<Class<?>> cs1 = getInterfaces(c);
				if (cs1 != null)
					set.addAll(cs1);
			}
			return set;
		}
	}

//	public static Class<?> getNearestCommonSuperClass(Class<?>... classes) {
//		Set<Class<?>> commonSuperClasses = null;
//		Set<Class<?>> commonInterfaces = null;
//		for (Class<?> clazz : classes) {
//			if (commonSuperClasses == null)
//				commonSuperClasses = getInterfaces(clazz);
//			else
//				commonSuperClasses.retainAll(getInterfaces(clazz));
//			if (commonInterfaces == null)
//				commonInterfaces = new HashSet<Class<?>>(getSuperClasses(clazz, true));
//				commonInterfaces.retainAll(getSuperClasses(clazz, true));
//		}
//		Class<?>[] commonSuperClassesArray = commonSuperClasses.toArray(new Class<?>[0]);
//		Arrays.sort(commonSuperClassesArray, (a, b) -> a.equals(b) ? 0 : a.isAssignableFrom(b) ? 1 : -1);
//		if (!commonSuperClassesArray[0].equals(Object.class))
//			return commonSuperClassesArray[0];
//		
//	}
}
