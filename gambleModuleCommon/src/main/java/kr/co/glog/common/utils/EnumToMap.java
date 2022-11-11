package kr.co.glog.common.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enum 객체를 front 에서 사용하기 위해서 map 으로 만들어서 제공 하기위한 대상이 되는 class 에 선언한다.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface EnumToMap {
}
