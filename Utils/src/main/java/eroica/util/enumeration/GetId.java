package eroica.util.enumeration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used by EnumUtil.getById, on methods of enums. Ids of status and types are
 * usually stored in database and tranferred throw http requests as keys, so
 * that EnumUtil.getById is useful.
 * 
 * @author Minhua HUANG
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GetId {
}
