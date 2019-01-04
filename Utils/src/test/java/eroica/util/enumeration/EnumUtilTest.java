package eroica.util.enumeration;

import eroica.util.enumeration.PrimaryEnum.CompositeEnum;

public class EnumUtilTest {
	public static void main(String[] args) {
		System.out.println(EnumUtil.getById(PrimaryEnum.class, 2));
		System.out.println(EnumUtil.getById(CompositeEnum.class, 2));
		System.out.println(EnumUtil.toPrimaryIdsStr(CompositeEnum.class, "1 , 2","3"));
		System.out.println(EnumUtil.toPrimaryIdsStr(CompositeEnum.class, "1 ,2"));
		System.out.println(EnumUtil.toPrimaryIdsStr(CompositeEnum.class, "ALL"));
	}
}
