package validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import validator.spec.IUniqueNameCheker;

@Component 
public class UniqueNameValidator implements ConstraintValidator<UniqueName, String> {
    
    @Autowired 
    private IUniqueNameCheker uniqueNameCheker;

    private String fieldName;

    //Initialize y isValid: es un metodo de la interfaz  ConstraintValidator
    //El initialize va inicializar parametros que tengamos al momento de empezar a utilizar el validador 
    @Override
    public void initialize(UniqueName constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //Validar si el valor es nulo o vacío
        if (value == null || value.isBlank()) {
            return true; // Consider null or empty values as valid
        }

        //boolean isUnique = uniqueNameCheker.isUniqueName(value);
         boolean isUnique = uniqueNameCheker.isUniqueName(fieldName, value); 
        //LOGICA DE LA VALIDACION 
        if (!isUnique) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "El valor de '" + fieldName + "' ya fue registrado"
            ).addConstraintViolation();
            return false;
        }

        return true;
    }

}
