package com.soulcode.elas_help.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import com.soulcode.elas_help.service.TecnicoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.UUID;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Validate that the usuario value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = TecnicoUsuarioUnique.TecnicoUsuarioUniqueValidator.class
)
public @interface TecnicoUsuarioUnique {

    String message() default "{Exists.tecnico.usuario}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class TecnicoUsuarioUniqueValidator implements ConstraintValidator<TecnicoUsuarioUnique, String> {

        private final TecnicoService tecnicoService;
        private final HttpServletRequest request;

        public TecnicoUsuarioUniqueValidator(final TecnicoService tecnicoService,
                final HttpServletRequest request) {
            this.tecnicoService = tecnicoService;
            this.request = request;
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equalsIgnoreCase(tecnicoService.get(UUID.fromString(currentId)).getUsuario())) {
                // value hasn't changed
                return true;
            }
            return !tecnicoService.usuarioExists(value);
        }

    }

}
