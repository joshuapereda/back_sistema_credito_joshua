package com.codeper.ControlClientes.exception;

import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.codeper.ControlClientes.common.CustomErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

//Indica que este va ser un manejador global de errores
//Spring elige el método con la excepción más específica
@RestControllerAdvice
public class RestExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    // Nuestras excepciones: cada una ya trae su código (400, 401, 404, 409)
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<CustomErrorResponse<String>> handleDomainException(DomainException x, HttpServletRequest req) {
        logger.warn("{}: {}", x.getClass().getSimpleName(), x.getMessage());
        return responder(x.getStatusCode(), x.getErrorCode(), x.getMessage(), req);
    }

    // 400: no se cumple una validación del DTO (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse<List<String>>> handleValidationException(
            MethodArgumentNotValidException x, HttpServletRequest req) {
        // Un mensaje por campo; si hay varios, gana el de "obligatorio"
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        x.getBindingResult().getFieldErrors().stream()
            .sorted(Comparator.comparing(RestExceptionHandler::noEsObligatorio))
            .forEach(e -> fieldErrors.putIfAbsent(e.getField(), mensajeDeCampo(e)));
        logger.warn("Validación en {}: {}", req.getRequestURI(), fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(CustomErrorResponse.<List<String>>builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .errorCode("VALIDATION_ERROR")
                .message(List.copyOf(fieldErrors.values()))
                .fieldErrors(fieldErrors)
                .timestamp(new Date().toString())
                .path(req.getRequestURI())
                .build());
    }

    // 400: el JSON está mal escrito o trae un tipo equivocado
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomErrorResponse<String>> handleNotReadable(
            HttpMessageNotReadableException x, HttpServletRequest req) {
        logger.warn("JSON inválido en {}: {}", req.getRequestURI(), x.getMessage());
        return responder(HttpStatus.BAD_REQUEST, "El cuerpo de la petición no es válido", req);
    }

    // 400: por ejemplo /api/compra/abc
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomErrorResponse<String>> handleTypeMismatch(
            MethodArgumentTypeMismatchException x, HttpServletRequest req) {
        return responder(HttpStatus.BAD_REQUEST, "El valor de '" + x.getName() + "' no es válido", req);
    }

    // 401: usuario o contraseña incorrectos
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CustomErrorResponse<String>> handleAuthentication(
            AuthenticationException x, HttpServletRequest req) {
        return responder(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos", req);
    }

    // 403: inició sesión pero no tiene permiso
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CustomErrorResponse<String>> handleAccessDenied(
            AccessDeniedException x, HttpServletRequest req) {
        return responder(HttpStatus.FORBIDDEN, "No tienes permiso para realizar esta acción", req);
    }

    // 409: la BD rechazó el cambio (dato repetido o usado en otra tabla)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CustomErrorResponse<String>> handleDataIntegrity(
            DataIntegrityViolationException x, HttpServletRequest req) {
        logger.warn("Integridad de datos en {}: {}", req.getRequestURI(), x.getMostSpecificCause().getMessage());
        return responder(HttpStatus.CONFLICT,
            "No se pudo completar: falta un dato, está repetido o está en uso", req);
    }

    // Lo demás. Los errores propios de Spring (404 de ruta, 405, 413...) ya traen su código;
    // solo lo inesperado es 500. Una falla interna del login tampoco es culpa del usuario.
    @ExceptionHandler({Exception.class, AuthenticationServiceException.class})
    public ResponseEntity<CustomErrorResponse<String>> handleException(Exception x, HttpServletRequest req) {
        if (x instanceof ErrorResponse er && er.getStatusCode().is4xxClientError()) {
            HttpStatus status = HttpStatus.valueOf(er.getStatusCode().value());
            logger.warn("{} en {}: {}", status, req.getRequestURI(), x.getMessage());
            return responder(status, mensajeDeEstado(status), req);
        }
        // El detalle va al log, no al usuario
        logger.error("Error inesperado en {}", req.getRequestURI(), x);
        return responder(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado en el servidor", req);
    }

    //---------------- AYUDAS ----------------//
    private static boolean noEsObligatorio(FieldError e) {
        String codigo = e.getCode() == null ? "" : e.getCode();
        return !List.of("NotBlank", "NotNull", "NotEmpty").contains(codigo);
    }

    // isBindingFailure: mandaron texto donde va un número
    private static String mensajeDeCampo(FieldError e) {
        return e.isBindingFailure() ? "El valor ingresado no es válido" : e.getDefaultMessage();
    }

    private static String mensajeDeEstado(HttpStatus status) {
        return switch (status.value()) {
            case 404 -> "La ruta solicitada no existe";
            case 405 -> "Método HTTP no permitido en esta ruta";
            case 413 -> "El archivo supera el tamaño permitido";
            case 415 -> "Tipo de contenido no soportado";
            default -> "La petición no es válida";
        };
    }

    private static ResponseEntity<CustomErrorResponse<String>> responder(
            HttpStatus status, String message, HttpServletRequest req) {
        return responder(status.value(), status.name(), message, req);
    }

    // Único lugar donde se arma la respuesta de error
    private static ResponseEntity<CustomErrorResponse<String>> responder(
            int status, String errorCode, String message, HttpServletRequest req) {
        return ResponseEntity.status(status).body(CustomErrorResponse.<String>builder()
            .statusCode(status)
            .errorCode(errorCode)
            .message(message)
            .timestamp(new Date().toString())
            .path(req.getRequestURI())
            .build());
    }
}
