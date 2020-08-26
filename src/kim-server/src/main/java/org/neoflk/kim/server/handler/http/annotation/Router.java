package org.neoflk.kim.server.handler.http.annotation;

import java.lang.annotation.*;

/**
 * @author neoflk
 * 创建时间：2020年05月08日
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface Router {

    String value();
}
