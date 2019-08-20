package eroica.util.enumeration;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import eroica.util.StringUtils;
import eroica.util.reflect.ReflectUtils;

/**
 * Tools for enums.
 * 
 * @author Minhua HUANG
 *
 */
public class EnumUtil {
	public static final String ALL = "ALL";

	/**
	 * Judge whether the enum class has such an enum who has this id.
	 * 
	 * @param           <U> the same as enumClass
	 * @param enumClass the enum class
	 * @param id        id of the enum
	 * @return has or has not
	 */
	public static <U extends Enum<U>> boolean existById(Class<U> enumClass, Object id) {
		return getByIdIgnoreMismatching(enumClass, id) != null;
	}

	/**
	 * Get the enum object by its id. The enumClass should has a no-args method
	 * annotated with {@link GetId} or having the name of "getId". If the enum
	 * object is not found, a NoSuchElementException will be thrown.
	 * 
	 * @param           <U> the same as enumClass
	 * @param enumClass the enum class
	 * @param id        id of the enum
	 * @return proper enum object
	 */
	public static <U extends Enum<U>> U getById(Class<U> enumClass, Object id) {
		U u = getByIdIgnoreMismatching(enumClass, id);
		if (u == null)
			throw new NoSuchElementException(enumClass.getName() + " has not such element whose id is:" + id + "("
					+ id.getClass().getName() + ").");
		return u;
	}

	/**
	 * Get the enum object by its id. The enumClass should has a no-args method
	 * annotated with {@link GetId} or having the name of "getId". This function
	 * will transfer id into string before comparison, and the comparison is between
	 * strings. If the enum object is not found, a NoSuchElementException will be
	 * thrown.
	 * 
	 * @param           <U> the same as enumClass
	 * @param enumClass the enum class
	 * @param id        id of the enum
	 * @return proper enum object
	 */
	public static <U extends Enum<U>> U getByIdInStr(Class<U> enumClass, Object id) {
		U u = getByIdInStrIgnoreMismatching(enumClass, id);
		if (u == null)
			throw new NoSuchElementException(enumClass.getName() + " has not such element whose id is:" + id + ".");
		return u;
	}

	/**
	 * Get the enum object by its id. The enumClass should has a non-args method
	 * annotated with {@link GetId} or having the name of "getId". If the enum
	 * object is not found, null will be returned.
	 * 
	 * @param           <U> the same as enumClass
	 * @param enumClass the enum class
	 * @param id        id of the enum
	 * @return proper enum object
	 */
	public static <U extends Enum<U>> U getByIdIgnoreMismatching(Class<U> enumClass, Object id) {
		return getByIdIgnoreMismatchingWhetherInStr(enumClass, id, false);
	}

	/**
	 * Get the enum object by its id. The enumClass should has a non-args method
	 * annotated with {@link GetId} or having the name of "getId". This function
	 * will transfer id into string before comparison, and the comparison is between
	 * strings. If the enum object is not found, null will be returned.
	 * 
	 * @param           <U> the same as enumClass
	 * @param enumClass the enum class
	 * @param id        id of the enum
	 * @return proper enum object
	 */
	public static <U extends Enum<U>> U getByIdInStrIgnoreMismatching(Class<U> enumClass, Object id) {
		return getByIdIgnoreMismatchingWhetherInStr(enumClass, id, true);
	}

	private static <U extends Enum<U>> U getByIdIgnoreMismatchingWhetherInStr(Class<U> enumClass, Object id,
			boolean inStr) {
		U[] values = getEnumValues(enumClass);
		for (U u : values) {
			Object gotId;
			try {
				gotId = getId(u);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException e) {
				throw new NoSuchElementException(
						"Failed to get " + enumClass.getName() + " object by ID. The cause is: " + e.getMessage());
			}
			if (!inStr && gotId.equals(id) || inStr && gotId.toString().equals(id.toString()))
				return u;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static <U extends Enum<U>> U[] getEnumValues(Class<U> enumClass) {
		try {
			return (U[]) enumClass.getMethod("values", new Class[0]).invoke(null, new Object[0]);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			throw new RuntimeException("System error. Cannot get values of the enum " + enumClass.getName(), e);
		}
	}

	/**
	 * Translate the composite enum object to its primary objects. The enumClass
	 * should has a non-args method annotated with {@link GetPrimaries} or having
	 * the name of "getPrimaries".
	 * 
	 * @param           <U> the same as enumClass
	 * @param enumClass the composite enum class
	 * @param ids       id array, each may contain more than one id (ie.
	 *                  1,2,3,4...)(if all ids are required , then "ALL" should be
	 *                  input)
	 * @return primary enum objects
	 */
	public static <U extends Enum<U>> Enum<? extends Enum<?>>[] toPrimaries(Class<U> enumClass, String... ids) {
		Set<Enum<?>> values = new TreeSet<>();// avoid replication
		Class<?> pClass = null;
		try {
			idsLoop: for (String idStr : ids)// idStr may contain more than one id
			{
				for (String id : StringUtils.splitAndTrim(idStr, ",")) {
					// if "ALL" is required
					if (ALL.equals(id)) {
						for (U compositeValue : getEnumValues(enumClass)) {
							Enum<?>[] primaries = getPrimaries(compositeValue);
							Collections.addAll(values, primaries);
							if (pClass == null)
								pClass = primaries.getClass().getComponentType();
						}
						break idsLoop;
					}
					// if "ALL" is not required
					else {
						Enum<?>[] primaries = getPrimaries(getByIdInStr(enumClass, id));
						Collections.addAll(values, primaries);
						if (pClass == null)
							pClass = primaries.getClass().getComponentType();
					}
				}
			}
			Enum<?>[] temp = (Enum<?>[]) Array.newInstance(pClass, 0);
			return values.toArray(temp);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException e) {
			throw new EnumTransformException("Failed to resolve composite status or type.", e);
		}
	}

	/**
	 * Translate the composite enum object to its primary objects. The enumClass
	 * should has a non-args method annotated with {@link GetPrimaries} or having
	 * the name of "getPrimaries".
	 * 
	 * @param           <U> the same as enumClass
	 * @param enumClass the composite enum class
	 * @param ids       id array, each may contain more than one id (ie.
	 *                  1,2,3,4...)(if all ids are required , then "ALL" should be
	 *                  input)
	 * @return primary enum ids splitted by ","
	 */
	public static <U extends Enum<U>> String toPrimaryIdsStr(Class<U> enumClass, String... ids) {
		StringBuilder sb = new StringBuilder();
		Enum<?>[] primaryValues = toPrimaries(enumClass, ids);
		for (Enum<?> p : primaryValues) {
			if (sb.length() > 0)
				sb.append(",");
			try {
				sb.append(getId(p));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException e) {
				throw new EnumTransformException("Failed to format ids.", e);
			}
		}
		return sb.toString();
	}

	private static Method getIdMethod(@SuppressWarnings("rawtypes") Class<? extends Enum> enumClass)
			throws NoSuchMethodException {
		return ReflectUtils.getNoArgsMethodByAnnotationOrName(enumClass, GetId.class, "getId");
	}

	private static Method getPrimariesMethod(@SuppressWarnings("rawtypes") Class<? extends Enum> enumClass)
			throws NoSuchMethodException {
		return ReflectUtils.getNoArgsMethodByAnnotationOrName(enumClass, GetPrimaries.class, "getPrimaries");
	}

	private static Object getId(Enum<?> compositeValue)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		Method m = getIdMethod(compositeValue.getClass());
		return m.invoke(compositeValue, new Object[0]);
	}

	private static Enum<? extends Enum<?>>[] getPrimaries(Enum<?> compositeValue)
			throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return (Enum<?>[]) getPrimariesMethod(compositeValue.getClass()).invoke(compositeValue, new Object[0]);
	}

}
