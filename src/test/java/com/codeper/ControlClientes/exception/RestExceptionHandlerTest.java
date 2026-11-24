package com.codeper.ControlClientes.exception;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.codeper.ControlClientes.dto.CreateCompraDto;
import com.codeper.ControlClientes.dto.CreatePagoDto;
import com.codeper.ControlClientes.dto.CreatePermisoDto;
import jakarta.validation.Valid;

// Comprueba que cada error salga con su código HTTP (no usa la BD)
class RestExceptionHandlerTest {

    // Controller falso: solo lanza errores
    @RestController
    static class ControllerFalso {
        @GetMapping("/error/{tipo}")
        void lanzar(@PathVariable String tipo) {
            throw switch (tipo) {
                case "400" -> new BadRequestException("dato malo");
                case "401" -> new UnauthorizedException("sin token");
                case "403" -> new AccessDeniedException("x");
                case "404" -> new ResourNotFoundException("Compra ", 5);
                case "409" -> new ConflictException("ya pagada");
                case "login" -> new BadCredentialsException("x");
                case "loginInterno" -> new AuthenticationServiceException("bd caida");
                case "bd" -> new DataIntegrityViolationException("sql secreto");
                default -> new RuntimeException("clave secreta");
            };
        }

        @GetMapping("/numero/{id}")
        void numero(@PathVariable Integer id) { }

        @PostMapping("/pago")
        void pago(@Valid @RequestBody CreatePagoDto dto) { }

        @PostMapping("/permiso")
        void permiso(@Valid @RequestBody CreatePermisoDto dto) { }

        @PostMapping("/compra")
        void compra(@Valid @RequestBody CreateCompraDto dto) { }
    }

    private final MockMvc mvc = MockMvcBuilders.standaloneSetup(new ControllerFalso())
        .setControllerAdvice(new RestExceptionHandler()).build();

    private void espera(String tipo, int codigo, String mensaje) throws Exception {
        mvc.perform(get("/error/" + tipo))
            .andExpect(status().is(codigo))
            .andExpect(jsonPath("$.statusCode").value(codigo))
            .andExpect(jsonPath("$.message").value(mensaje))
            .andExpect(jsonPath("$.path").value("/error/" + tipo))
            .andExpect(jsonPath("$.fieldErrors").doesNotExist());
    }

    @Test
    void cadaExcepcionSaleConSuCodigo() throws Exception {
        espera("400", 400, "dato malo");
        espera("401", 401, "sin token");
        espera("403", 403, "No tienes permiso para realizar esta acción");
        espera("404", 404, "Compra con id 5 no encontrado");
        espera("409", 409, "ya pagada");
        espera("login", 401, "Usuario o contraseña incorrectos");
    }

    @Test
    void loInesperadoEs500YNoMuestraDetalles() throws Exception {
        espera("otro", 500, "Ocurrió un error inesperado en el servidor");
        espera("loginInterno", 500, "Ocurrió un error inesperado en el servidor");
        mvc.perform(get("/error/otro")).andExpect(content().string(not(containsString("secreta"))));
    }

    @Test
    void errorDeLaBaseEs409YNoMuestraElSql() throws Exception {
        mvc.perform(get("/error/bd"))
            .andExpect(status().isConflict())
            .andExpect(content().string(not(containsString("secreto"))));
    }

    @Test
    void validacionEs400ConElMensajeDeCadaCampo() throws Exception {
        mvc.perform(post("/pago").contentType(MediaType.APPLICATION_JSON).content("{}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.message.length()").value(2))
            .andExpect(jsonPath("$.fieldErrors.monto").value("El monto es obligatorio"))
            .andExpect(jsonPath("$.fieldErrors.compraId").value("La compra es obligatoria"));
    }

    @Test
    void siUnCampoFallaDosVecesGanaElObligatorio() throws Exception {
        mvc.perform(post("/permiso").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"\",\"recurso\":\"\",\"accion\":\"\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.fieldErrors.recurso").value("El recurso es obligatorio"))
            .andExpect(jsonPath("$.fieldErrors.accion").value("La acción es obligatoria"));
    }

    @Test
    void tambienValidaLasLineasDeLaCompra() throws Exception {
        mvc.perform(post("/compra").contentType(MediaType.APPLICATION_JSON)
                .content("{\"clienteId\":1,\"usuarioId\":1,\"tipoCompraId\":1,\"detalles\":[{\"productoId\":1,\"cantidad\":0}]}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.fieldErrors['detalles[0].cantidad']").value("La cantidad debe ser mayor a 0"));
    }

    @Test
    void peticionesMalHechas() throws Exception {
        mvc.perform(post("/pago").contentType(MediaType.APPLICATION_JSON).content("{mal"))
            .andExpect(status().isBadRequest());
        mvc.perform(get("/numero/abc")).andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("El valor de 'id' no es válido"));
        mvc.perform(get("/pago")).andExpect(status().isMethodNotAllowed());
        mvc.perform(post("/pago").contentType(MediaType.TEXT_PLAIN).content("x"))
            .andExpect(status().isUnsupportedMediaType());
    }
}
