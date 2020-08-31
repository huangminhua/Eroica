package eroica.util.enumeration;

import eroica.util.enumeration.PrimaryEnum.CompositeEnum;

public class EnumUtilTest {
	public static void main(String[] args) {
		System.out.println(EnumUtils.getById(PrimaryEnum.class, 2));
		System.out.println(EnumUtils.getById(CompositeEnum.class, 2));
		System.out.println(EnumUtils.toPrimaryIdsStr(CompositeEnum.class, "1 , 2","3"));
		System.out.println(EnumUtils.toPrimaryIdsStr(CompositeEnum.class, "1 ,2"));
		System.out.println(EnumUtils.toPrimaryIdsStr(CompositeEnum.class, "ALL"));
	}
}
