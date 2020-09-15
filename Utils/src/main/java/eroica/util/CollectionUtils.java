package eroica.util;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * Tools for Collection.
 * @author Minhua HUANG
 *
 */
public class CollectionUtils {
	/**
	 * A graceful replacement of Collection.toArray.
	 * @param <E>
	 * @param coll
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <E> E[] toArray(Collection<E> coll, Class<? super E> clazz) {
		return coll.toArray((E[]) Array.newInstance(clazz, 0));
	}
	
}
