package hk.org.ha.iams.tool.direct;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Marker for business manager contracts. Implementation is *ManagerPlaceholder. */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DirectManager {
}
