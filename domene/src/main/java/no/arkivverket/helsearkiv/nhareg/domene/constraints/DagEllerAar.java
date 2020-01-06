package no.arkivverket.helsearkiv.nhareg.domene.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

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