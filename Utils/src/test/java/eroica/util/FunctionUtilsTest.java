package eroica.util;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.jupiter.api.Test;

public class FunctionUtilsTest {
	final static Lock l1 = new ReentrantLock();
	final static Lock l2 = new ReentrantLock();

	@Test
	void withLocks() {
//		assertEquals("Hello Tom.", FunctionUtils.withLocks((t) -> "Hello " + t + ".", l1, l2).apply("Tom"));
		new Thread(() -> {
			l1.lock();
			System.out.println(l1 + " locked by Thread " + Thread.currentThread().getName());
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			l1.unlock();
			System.out.println(l1 + " released by Thread " + Thread.currentThread().getName());
			try {
				Thread.sleep(5000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
		try {
			Thread.sleep(5000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTimeout(Duration.ofSeconds(11),
				() -> FunctionUtils.withLocks((t) -> "Hello " + t + ".", l1, l2).apply("Tom"));

	}
}
