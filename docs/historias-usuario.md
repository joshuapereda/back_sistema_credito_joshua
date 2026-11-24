# Historias de usuario — Correcciones del backend

Backlog que sale de la revisión del código. Cada historia se puede entregar y probar por separado.

**Prioridad:** Alta = afecta dinero o datos · Media = afecta la experiencia o la integridad · Baja = deuda técnica
**Estimación:** puntos de historia (1 = muy simple, 8 = compleja)

| ID | Historia | Épica | Prioridad | Pts |
|---|---|---|---|---|
| HU-01 | Registrar pagos de forma segura (atómica) | Compras y pagos | Alta | 1 |
| HU-02 | Reglas de Contado/Crédito consistentes | Compras y pagos | Alta | 3 |
| HU-03 | Corregir la edición de pagos | Compras y pagos | Alta | 3 |
| HU-04 | Impedir borrar compras que tienen pagos | Compras y pagos | Media | 2 |
| HU-05 | Respuestas de error claras | Errores | Media | 3 |
| HU-06 | Editar un cliente sin que rechace su propio correo | Clientes | Media | 1 |
| HU-07 | Contraseñas cifradas | Seguridad | Alta | 3 |
| HU-08 | Inicio de sesión y acceso por roles/permisos | Seguridad | Media | 8 |
| HU-09 | Corregir nombres con errores de escritura | Deuda técnica | Baja | 2 |
| HU-10 | Resumen para el dashboard | Dashboard | Media | 3 |

**Orden sugerido:** HU-01 → HU-02 → HU-03 → HU-05 → HU-04 → HU-06 → HU-07 → HU-09 → HU-08
(HU-05 antes de HU-04 y HU-06 porque estas necesitan devolver códigos 404/409 claros).

---

## Épica: Compras y pagos

### HU-01 · Registrar pagos de forma segura

**Como** vendedor
**quiero** que al registrar un abono el pago y el saldo de la compra se guarden juntos
**para** que nunca quede un pago registrado sin descontar el saldo (o al revés).

**Criterios de aceptación**
- **Dado** que guardar la compra falla después de guardar el pago, **cuando** registro el pago, **entonces** no queda ningún pago guardado y el saldo no cambia.
- **Dado** un pago válido, **cuando** lo registro, **entonces** el pago y el nuevo saldo de la compra quedan guardados.

**Notas técnicas**
- Agregar `@Transactional` a `PagoService.crearPago()`.

---

### HU-02 · Reglas de Contado/Crédito consistentes

**Como** vendedor
**quiero** que una compra a crédito quede siempre pendiente con su saldo completo, y una al contado quede siempre pagada
**para** confiar en el saldo de cada cliente, tanto al crear la compra como al editarla.

**Criterios de aceptación**
- **Dado** el tipo de compra "Crédito" (con o sin tilde, con cualquier mayúscula), **cuando** creo o edito la compra, **entonces** `saldo = total` y `estado = PENDIENTE`.
- **Dado** el tipo de compra "Contado", **cuando** creo o edito la compra, **entonces** `saldo = 0` y `estado = PAGADO`.
- **Dado** una compra a crédito que ya tiene pagos, **cuando** la edito y cambia el total, **entonces** `saldo = nuevo total − suma de pagos` y el estado se recalcula (PAGADO si el saldo es 0).
- **Dado** que la suma de los pagos es mayor que el nuevo total, **cuando** edito la compra, **entonces** el sistema rechaza el cambio con un mensaje claro.

**Notas técnicas**
- Hoy `crearCompra` compara con `"Credito"` y `calcularSaldo` con `"CONTADO"`. Usar una sola función para decidir saldo y estado en los dos casos.
- Recomendado: no depender del texto. Agregar a `TipoCompra` un campo `esCredito` (boolean) o un enum.
- `actualizarCompra` debe recalcular `estado`, que hoy no se toca.

---

### HU-03 · Corregir la edición de pagos

**Como** vendedor
**quiero** poder corregir el monto de un pago mal digitado
**para** que el saldo de la compra refleje lo que el cliente realmente pagó.

**Criterios de aceptación**
- **Dado** un pago existente, **cuando** cambio su monto, **entonces** el saldo de su compra se recalcula como `total − suma de todos los pagos`.
- **Dado** que el nuevo monto deja el saldo negativo, **cuando** guardo, **entonces** se rechaza con "El monto excede el saldo de la compra".
- **Dado** que el saldo queda en 0, **entonces** la compra pasa a PAGADO. Si deja de ser 0, vuelve a PENDIENTE.
- El `saldoRestante` de los pagos de esa compra queda coherente con el orden en que se hicieron.
- La edición no cambia la compra a la que pertenece el pago.

**Notas técnicas**
- Hoy `actualizarPago` busca la compra con `actualizarPagoDto.getId()` (el id del **pago**) y copia `saldoRestante` tal como viene del cliente HTTP.
- Recibir un DTO de entrada (`CreatePagoDto` o uno nuevo `UpdatePagoDto`) en lugar de `PagoDto`, y marcar el método con `@Transactional`.

---

### HU-04 · Impedir borrar compras que tienen pagos

**Como** administrador
**quiero** que el sistema no me deje borrar una compra que ya tiene abonos
**para** no perder el historial de pagos del cliente.

**Criterios de aceptación**
- **Dado** una compra con al menos un pago, **cuando** intento borrarla, **entonces** recibo **409 Conflict** con "No se puede eliminar una compra con pagos registrados".
- **Dado** una compra sin pagos, **cuando** la borro, **entonces** se eliminan la compra y sus detalles, y recibo **204 No Content**.
- **Dado** una compra que no existe, **entonces** recibo **404**.

**Notas técnicas**
- Agregar `existsByCompraId(Integer id)` en `PagoRepository`.
- Más adelante se puede pensar en un estado `ANULADA` en lugar de borrar.

---

## Épica: Manejo de errores

### HU-05 · Respuestas de error claras

**Como** desarrollador del frontend
**quiero** que la API devuelva el código HTTP correcto y un mensaje legible
**para** mostrarle al usuario qué salió mal sin tener que interpretar un error 500.

**Criterios de aceptación**
- Recurso inexistente → **404** con el mensaje (ej. "Cliente no encontrado con id: 5").
- Falla una validación de `@Valid` → **400** con la lista de campos y sus mensajes.
- Regla de negocio violada (compra ya pagada, monto excede saldo, correo en uso) → **400** o **409** con el mensaje.
- Error inesperado → **500** con un mensaje genérico, sin mostrar el stack trace.
- Todas las respuestas de error usan el mismo formato:
  ```json
  { "status": 404, "error": "Not Found", "message": "Cliente no encontrado con id: 5",
    "path": "/api/clientes/5", "timestamp": "2026-10-21T10:00:00" }
  ```

**Notas técnicas**
- Crear un `@RestControllerAdvice` (`GlobalExceptionHandler`).
- Crear excepciones propias, por ejemplo `RecursoNoEncontradoException` y `ReglaNegocioException`, y reemplazar los `RuntimeException` genéricos de los servicios.
- `MethodArgumentNotValidException` → 400 con `fieldErrors`.

**Qué se creó o cambió (24/11/2026)**

| Archivo | Para qué sirve |
|---|---|
| `exception/RestExceptionHandler` | Responde el código correcto (antes siempre 500) |
| `exception/BadRequestException` (nuevo) | 400: datos mal enviados |
| `exception/UnauthorizedException` (nuevo) | 401: token inválido |
| `exception/ConflictException` (ya existía) | 409: regla de negocio |
| `common/CustomErrorResponse` | Agrega `fieldErrors` (campo → mensaje) |
| `service/*` | Cambian `RuntimeException` por la excepción que toca |
| `auth/AuthService`, `JwtAuthenticationFilter` | Token malo → 401 |
| `PagoController`, `TipoCompraController` | Les faltaba `@Valid` |
| `test/exception/RestExceptionHandlerTest` (nuevo) | Prueba cada código |

| Código | Cuándo |
|---|---|
| 400 | Falla una validación o la petición está mal hecha |
| 401 | Login incorrecto o token inválido |
| 403 | No tiene permiso |
| 404 | No existe el registro, la ruta o el archivo |
| 409 | Choca con una regla (compra ya pagada, registro en uso) |
| 500 | Solo errores inesperados, sin mostrar detalles |

---

## Épica: Clientes

### HU-06 · Editar un cliente sin que rechace su propio correo

**Como** vendedor
**quiero** actualizar el teléfono o la dirección de un cliente sin cambiar su correo
**para** mantener sus datos al día sin errores falsos.

**Criterios de aceptación**
- **Dado** un cliente con correo `ana@gmail.com`, **cuando** lo edito y mantengo ese correo, **entonces** se guarda sin error.
- **Dado** que pongo un correo que ya usa **otra** persona, **entonces** se rechaza con "El correo electrónico ya está en uso".
- **Dado** que dejo el correo vacío (es opcional), **entonces** no se valida si está duplicado.
- Si el cliente no existe → 404.

**Notas técnicas**
- Agregar `existsByGmailAndIdNot(String gmail, Integer id)` en `PersonaRepository`, usando el id de la persona del cliente.
- Ojo: `@UniqueName(fieldName = "nombre")` en `CreateClienteDto` tiene el mismo problema al editar sin cambiar el nombre. Aplica la misma corrección (validar en el servicio excluyendo el propio id, o usar un DTO de edición sin `@UniqueName`).

---

## Épica: Seguridad

### HU-07 · Contraseñas cifradas

**Como** administrador
**quiero** que las contraseñas de los usuarios se guarden cifradas
**para** que nadie pueda leerlas aunque tenga acceso a la base de datos.

**Criterios de aceptación**
- **Dado** que creo o edito un usuario, **cuando** se guarda, **entonces** la contraseña queda como hash BCrypt, nunca en texto plano.
- La contraseña **no** aparece en ninguna respuesta de la API (`UsuarioDto`).
- La contraseña exige un mínimo de 8 caracteres.
- Los usuarios que ya existen se migran (o se les pide restablecer la contraseña).

**Notas técnicas**
- Agregar `spring-security-crypto` (o `spring-boot-starter-security`) y usar `BCryptPasswordEncoder`.
- La columna actual es `length = 20` y un hash BCrypt mide 60 caracteres: ampliarla a 60 o más.
- Se puede hacer junto con HU-09 (renombrar `passwprd` → `password`).

### HU-08 · Inicio de sesión y acceso por roles/permisos

**Como** administrador
**quiero** que solo los usuarios que iniciaron sesión usen la API, y cada uno solo según sus permisos
**para** que, por ejemplo, un vendedor no pueda borrar productos ni crear usuarios.

**Criterios de aceptación**
- `POST /api/auth/login` con username y contraseña correctos devuelve un token (JWT).
- Una petición sin token o con un token inválido o vencido → **401**.
- Una petición con token pero sin el permiso necesario → **403**.
- Los permisos se leen de `Rol → Permiso` (`recurso` + `accion`, ej. `compra:crear`).
- Debe existir al menos un usuario administrador inicial para no quedar bloqueados.

**Notas técnicas**
- Depende de HU-07 y HU-05.
- `spring-boot-starter-security` + un filtro JWT; mapear los permisos a `GrantedAuthority` y proteger los endpoints con `@PreAuthorize` o reglas en `SecurityFilterChain`.
- Es grande: conviene dividirla en (a) login + token, (b) proteger endpoints por rol/permiso.

---

## Épica: Deuda técnica

### HU-09 · Corregir nombres con errores de escritura

**Como** desarrollador
**quiero** que tablas, campos y clases tengan nombres correctos
**para** que el código sea más fácil de leer y no arrastremos los errores al frontend.

**Criterios de aceptación**
- La tabla `pproducto` pasa a llamarse `producto`, **sin perder datos**.
- El campo `passwprd` pasa a llamarse `password` (entidad, DTOs y JSON).
- `UniqueNameCheker` / `IUniqueNameCheker` pasan a `UniqueNameChecker` / `IUniqueNameChecker`.
- La aplicación arranca y todos los endpoints responden igual que antes (salvo el nuevo nombre del campo `password`).

**Notas técnicas**
- ⚠️ Con `ddl-auto: update`, Hibernate **no renombra**: crearía una tabla `producto` vacía y una columna `password` nueva, y los datos se quedarían en las viejas. Antes de cambiar las entidades, renombra a mano en PostgreSQL:
  ```sql
  ALTER TABLE pproducto RENAME TO producto;
  ALTER TABLE usuario RENAME COLUMN passwprd TO password;
  ```
- Avisar al frontend del cambio de `passwprd` → `password` en el JSON.

---

## Épica: Dashboard

### HU-10 · Resumen para el dashboard

**Como** administrador
**quiero** pedir los totales del negocio en una sola llamada
**para** mostrarlos en el dashboard sin descargar todas las compras.

**Criterios de aceptación**
- **Dado** un usuario con token, **cuando** pide `GET /api/dashboard/resumen`, **entonces** recibe saldo por cobrar, ventas del mes, compras pendientes, clientes, cobrado hoy y los 5 productos más vendidos.
- **Dado** que no hay compras ni pagos, **entonces** las sumas valen 0, no `null`.
- **Dado** un usuario sin token, **entonces** recibe **401**.

**Qué se creó o cambió**

| Archivo | Para qué sirve |
|---|---|
| `dto/DashboardResumenDto` (nuevo) | La respuesta completa |
| `dto/ProductoVendidoDto` (nuevo) | Una fila del ranking |
| `repository/CompraRepository` | Suma saldos y ventas, cuenta por estado |
| `repository/PagoRepository` | Suma pagos entre dos fechas |
| `repository/DetalleCompraRepository` | Productos más vendidos |
| `service/IDashboardService`, `DashboardService` (nuevos) | Juntan los datos |
| `controller/DashboardController` (nuevo) | Expone el endpoint |
| `test/service/DashboardServiceTest` (nuevo) | Pruebas sin BD |

**Notas técnicas**
- No se creó ninguna entidad: todo sale de tablas que ya existen.
- "Cobrado hoy" suma solo pagos de créditos: las ventas al contado no generan pago.

---

## Épica: Correcciones de datos

### HU-13 · No borrar ni repetir datos por error

**Como** administrador
**quiero** que eliminar un rol borre solo ese rol y que no haya correos repetidos
**para** no perder permisos ni romper el inicio de sesión.

**Criterios de aceptación**
- **Dado** un rol sin permisos, **cuando** lo elimino, **entonces** se borra ese rol y ningún permiso.
- **Dado** un correo que ya usa otra persona, **cuando** creo o edito un cliente, **entonces** recibo **409** con "El correo electrónico ya está en uso".
- **Dado** que edito un cliente y mantengo su correo, o lo dejo vacío, **entonces** se guarda sin error.

**Qué se creó o cambió**

| Archivo | Para qué sirve |
|---|---|
| `service/RolService` | Borra en `rolRepository` (antes en `permisoRepository`) |
| `repository/PersonaRepository` | Busca correos repetidos |
| `service/ClienteService` | Valida el correo al crear y editar |
| `test/service/RolServiceTest`, `ClienteServiceTest` (nuevos) | Pruebas sin BD |

---

### HU-17 · Las fotos cargan aunque se mueva el proyecto

**Como** vendedor
**quiero** ver las fotos de los productos
**para** reconocerlos rápido.

**Criterios de aceptación**
- **Dado** una foto guardada, **cuando** pido `GET /api/files/{nombre}`, **entonces** recibo la foto (200).
- **Dado** que el proyecto cambia de carpeta, **entonces** las fotos siguen cargando.
- **Dado** una foto que no existe, **entonces** recibo **404**.

**Qué se cambió**

| Archivo | Para qué sirve |
|---|---|
| `application.yaml` | `storage.location` pasa de una ruta fija a `images` (relativa) |

---

## Fuera de alcance (decidido)

- **Credenciales de la BD en `application.yaml`**: se mantienen así por ahora.
