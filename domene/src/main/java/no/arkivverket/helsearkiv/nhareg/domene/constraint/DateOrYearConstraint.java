package no.arkivverket.helsearkiv.nhareg.domene.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target( { METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = DateOrYearValidator.class)
@Documented
public @interface DateOrYearConstraint {

    String message() default "{no.arkivverket.helsearkiv.nhareg.domene.constraints.DagEllerAar}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}