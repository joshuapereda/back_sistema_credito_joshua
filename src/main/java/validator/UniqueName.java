package validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
//Indicando cual la va ser la clase que va a validar el campo
@Constraint (validatedBy = UniqueNameValidator.class)
@Target ({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)

public @interface UniqueName {
    //Nombre del campo que se va a validar
    String fieldName();
    String message() default "El valor de '{fieldName}' ya fue registrado";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
