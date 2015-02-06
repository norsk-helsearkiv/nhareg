package no.arkivverket.helsearkiv.nhareg.domene.constraints;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 *
 * @author robing
 */
@Target( { METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = DagEllerAarValidator.class)
@Documented
public @interface DagEllerAar {

    String message() default "{no.arkivverket.helsearkiv.nhareg.domene.constraints.DagEllerAar}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
