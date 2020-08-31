package eroica.util;

import java.util.concurrent.locks.Lock;
import java.util.function.Function;

/**
 * Tools for functions.
 * 
 * @author Minhua HUANG
 */
public class FunctionUtils {

	/**
	 * Generate a new function from the original function, imposing the locks to it.
	 * 
	 * @param <T>              the type of the input to the function and func
	 * @param <R>              the type of the result of the function and func
	 * @param originalFunction the original function
	 * @param locks            the locks that will be imposed to the original
	 *                         function, locked sequentially and unlocked reverse
	 *                         sequentially
	 * @return a new function
	 */
	public static <T, R> Function<T, R> withLocks(Function<T, R> originalFunction, Lock... locks) {
		Function<T, R> r = originalFunction;
		for (int i = locks.length - 1; i >= 0; i--)
			r = withLock(r, locks[i]);
		return r;
	}

	private static <T, R> Function<T, R> withLock(Function<T, R> originalFunction, Lock lock) {
		return (T t) -> {
			lock.lock();
			try {
				return originalFunction.apply(t);
			} finally {
				lock.unlock();
			}
		};
	}

}
