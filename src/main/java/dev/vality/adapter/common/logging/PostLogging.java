package dev.vality.adapter.common.logging;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PostLogging {

    @AliasFor("value")
    String endpoint() default "/";

    @AliasFor("endpoint")
    String value() default "/";

    RequestMethod method() default RequestMethod.POST;

}
