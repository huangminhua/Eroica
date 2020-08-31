package eroica.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Properties;

import org.junit.jupiter.api.Test;

public class PropertyUtilsTest {
	@Test
	void fulfill() {
		Properties p = new Properties();
		p.setProperty("b", "c");
		p.setProperty("c", "d");
		p.setProperty("bd", "ef");
		p.setProperty("g", "h}");
		p.setProperty("bh", "ij");
		assertEquals("acd", PropertyUtils.fulfill("a{b}{c}", "{", "}", p));
		assertEquals("aef", PropertyUtils.fulfill("a{b{c}}", "{", "}", p));
		assertEquals("aij", PropertyUtils.fulfill("a{b{g}", "{", "}", p));
	}
}
