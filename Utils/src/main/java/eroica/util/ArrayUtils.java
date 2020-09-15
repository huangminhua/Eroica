package eroica.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Tools for arrays.
 * 
 * @author Minhua HUANG
 *
 */
public class ArrayUtils {
	/**
	 * To construct a stream from the elements.
	 * 
	 * @param <E>
	 * @param elements
	 * @return
	 */
	public static <E> Stream<E> stream(@SuppressWarnings("unchecked") E... elements) {
		return Arrays.asList(elements).stream();
	}
	
	

	/**
	 * To filter the elements.
	 * 
	 * @param <E>
	 * @param predicate
	 * @param elements
	 * @return
	 */
	public static <E> List<E> filterToList(Predicate<? super E> predicate,
			@SuppressWarnings("unchecked") E... elements) {
		return stream(elements).filter(predicate).collect(Collectors.toList());
	}

	/**
	 * To filter the elements.
	 * 
	 * @param <E>
	 * @param predicate
	 * @param elements
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <E> E[] filter(Predicate<? super E> predicate, E... elements) {
		Class<?> eClass = elements.getClass().getComponentType();
		return filterToList(predicate, elements).toArray((E[]) Array.newInstance(eClass, 0));
	}
}
