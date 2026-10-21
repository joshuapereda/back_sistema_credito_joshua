# Sistema de Crédito Joshua — Backend (ControlClientes)

API REST para gestionar **clientes, productos, compras (al contado o a crédito) y pagos/abonos** de un negocio, con usuarios, roles y permisos.

| | |
|---|---|
| **Lenguaje** | Java 25 |
| **Framework** | Spring Boot 4.1.0 (Web MVC, Data JPA, Validation, DevTools) |
| **Base de datos** | PostgreSQL (`control-clientes`) |
| **ORM** | Hibernate / JPA (`ddl-auto: update`) |
| **Utilidades** | Lombok |
| **Build** | Maven (wrapper `mvnw`) |
| **Puerto** | `9600` |

> Para una explicación visual con diagramas, abre [`docs/arquitectura.html`](docs/arquitectura.html) en el navegador.

---

## 1. Cómo ejecutarlo

### Requisitos
- JDK 25
- PostgreSQL corriendo en `localhost:5432`
- Una base de datos llamada `control-clientes`

```sql
CREATE DATABASE "control-clientes";
```

### Configuración
Archivo: [`src/main/resources/application.yaml`](src/main/resources/application.yaml)

```yaml
server:
  port: 9600
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/control-clientes
    username: postgres
    password: admin
  jpa:
    hibernate:
      ddl-auto: update   # Hibernate crea/actualiza las tablas solo
    show-sql: true       # muestra el SQL en consola
```

### Arrancar

```bash
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:9600/api/...`. Las tablas se crean automáticamente al iniciar.

---

## 2. Arquitectura

Es una **arquitectura en capas** clásica de Spring:

```
Cliente HTTP (Postman / Frontend)
        │  JSON
        ▼
┌──────────────────┐   @RestController  — recibe la petición, valida con @Valid
│   controller/    │
└────────┬─────────┘
         │  DTO (Create...Dto)
         ▼
┌──────────────────┐   @Service — reglas de negocio (totales, saldos, estados)
│    service/      │   Interfaz I...Service + implementación ...Service
└────────┬─────────┘
         │  Entidad
         ▼
┌──────────────────┐   JpaRepository — acceso a datos sin escribir SQL
│   repository/    │
└────────┬─────────┘
         ▼
     PostgreSQL
```

### Paquetes

| Paquete | Responsabilidad |
|---|---|
| `com.codeper.ControlClientes` | Clase principal `ControlClientesApplication` (escanea también el paquete `validator`). |
| `controller/` | Endpoints REST. Solo delegan al servicio y devuelven `ResponseEntity`. |
| `service/` | Lógica de negocio. Cada servicio tiene una interfaz (`ICompraService`) y su implementación (`CompraService`). |
| `repository/` | Interfaces `JpaRepository<Entidad, Integer>` con consultas derivadas (`existsByGmail`, `existsByUsername`, `existsByNombre`, `findByNombre`). |
| `entity/` | Clases `@Entity` que se mapean a tablas. |
| `dto/` | Objetos de entrada (`Create...Dto`, con validaciones) y de salida (`...Dto`, con métodos `fromEntity...`). |
| `validator/` | Anotación propia `@UniqueName` para validar que un nombre/username no esté repetido. |

### Patrón DTO
- **Entrada** → `CreateXxxDto`: tiene las anotaciones de validación (`@NotNull`, `@NotBlank`, `@Email`, `@Pattern`, `@Positive`, `@UniqueName`) y métodos estáticos `toEntity...()` para convertir a entidad.
- **Salida** → `XxxDto`: tiene métodos estáticos `fromEntity...()` que aplanan la entidad (por ejemplo, `CompraDto` devuelve el nombre del cliente en vez del objeto completo).

Así nunca se exponen las entidades JPA directamente en el JSON.

---

## 3. Modelo de datos

```
Persona 1──1 Cliente 1──N Compra N──1 TipoCompra
   │                       │  │
   1                       │  └──N──1 Usuario N──M Rol N──M Permiso
   │                       │
Usuario                    1──N DetalleCompra N──1 Producto N──1 Categoria
                           │
                           1──N Pago
```

| Entidad | Tabla | Campos principales | Relaciones |
|---|---|---|---|
| `Persona` | `persona` | nombre, gmail, telefono, direccion | Datos comunes de Cliente y Usuario |
| `Cliente` | `cliente` | docIdentidad | 1:1 Persona, 1:N Compra |
| `Usuario` | `usuario` | username, passwprd | 1:1 Persona, N:M Rol (`usuario_rol`) |
| `Rol` | `roles` | nombre, descripcion | N:M Permiso (`rol_permiso`, EAGER) |
| `Permiso` | `permisos` | nombre, recurso, accion | N:M Rol |
| `Categoria` | `categoria` | nombre, descripcion | — |
| `Producto` | `pproducto` | nombre, foto, precioVenta, precioCompra | N:1 Categoria |
| `TipoCompra` | `tipo_compra` | nombre (`Contado` / `Credito`), descripcion | — |
| `Compra` | `compra` | fechaHora, total, saldo, estado | N:1 Cliente, Usuario, TipoCompra; 1:N DetalleCompra (cascade ALL + orphanRemoval) |
| `DetalleCompra` | `detalle_compra` | cantidad, precioUnitario, subtotal | N:1 Compra, N:1 Producto |
| `Pago` | `pago` | monto, saldoRestante, fechaHora | N:1 Compra |
| `EstadoCompra` (enum) | — | `PENDIENTE`, `PAGADO` | Se guarda como texto |

**Persona** es la tabla base: un `Cliente` y un `Usuario` "tienen" una persona (composición 1:1), en lugar de usar herencia.

---

## 4. Flujo de negocio (lo más importante)

### 4.1 Registrar una compra — `POST /api/compra`

```json
{
  "clienteId": 1,
  "usuarioId": 1,
  "tipoCompraId": 2,
  "detalles": [
    { "productoId": 3, "cantidad": 2 },
    { "productoId": 5, "cantidad": 1 }
  ]
}
```

Lo que hace `CompraService.crearCompra()` (en una sola transacción):

1. Busca el usuario, cliente y tipo de compra (si alguno no existe → `EntityNotFoundException`).
2. Crea la `Compra` con la fecha/hora actual.
3. Por cada línea de `detalles` (`cargarDetalles`):
   - busca el producto,
   - toma su `precioVenta` como `precioUnitario`,
   - `subtotal = precioVenta × cantidad`,
   - lo suma al `total`.
4. Según el tipo de compra:
   - **Crédito** → `saldo = total`, `estado = PENDIENTE`.
   - **Contado** (cualquier otro) → `saldo = 0`, `estado = PAGADO`.
5. Guarda la compra; los detalles se guardan solos por el `cascade = ALL`.

> El precio **no** lo manda el cliente: siempre se toma del producto en la base de datos.

### 4.2 Registrar un pago/abono — `POST /api/pago`

```json
{ "compraId": 10, "monto": 50.00 }
```

`PagoService.crearPago()`:

1. Busca la compra.
2. Si ya está `PAGADO` → error *"Esta compra ya está pagada."*
3. `saldoRestante = saldo − monto`. Si queda negativo → error *"El monto del pago excede el saldo"*.
4. Guarda el `Pago` con su `saldoRestante` y actualiza el `saldo` de la compra.
5. Si el saldo llega a `0` → la compra pasa a `PAGADO`.

### 4.3 Validación `@UniqueName`

```java
@UniqueName(fieldName = "nombre")    // en CreateClienteDto
@UniqueName(fieldName = "username")  // en CreateUsuarioDto
```

`@UniqueName` → `UniqueNameValidator` → `IUniqueNameCheker` (`UniqueNameCheker`) consulta el repositorio correspondiente (`existsByNombre` / `existsByUsername`). Si ya existe, la petición se rechaza con *"El valor de 'x' ya fue registrado"*.

---

## 5. Endpoints

Todos siguen el mismo CRUD: `GET` (listar), `GET /{id}`, `POST`, `PUT /{id}`, `DELETE /{id}`.

| Recurso | Ruta base |
|---|---|
| Categorías | `/api/categorias` |
| Productos | `/api/productos` |
| Clientes | `/api/clientes` |
| Usuarios | `/api/usuarios` |
| Roles | `/api/roles` |
| Permisos | `/api/permisos` |
| Tipos de compra | `/api/tipoCompras` |
| Compras | `/api/compra` |
| Pagos | `/api/pago` |

### Orden recomendado para probar desde cero
1. `POST /api/categorias` → `POST /api/productos`
2. `POST /api/tipoCompras` → crear `Contado` y `Credito`
3. `POST /api/permisos` → `POST /api/roles` → `POST /api/usuarios`
4. `POST /api/clientes`
5. `POST /api/compra` (con tipo Crédito)
6. `POST /api/pago` varias veces hasta saldar → la compra queda `PAGADO`

---

## 6. Observaciones y pendientes detectados

Cosas que vi revisando el código y que conviene corregir (convertidas en historias de usuario en [`docs/historias-usuario.md`](docs/historias-usuario.md)):

- **Sin manejador global de errores**: no hay `@RestControllerAdvice`, así que las `RuntimeException` / `EntityNotFoundException` salen como **500** en lugar de 404/400.
- **Crédito vs. Contado inconsistente**: `crearCompra` compara con `"Credito"` y `calcularSaldo` (usado al actualizar) compara con `"CONTADO"`. Si el tipo tiene otro nombre (p. ej. "Crédito" con tilde) se comporta distinto al crear y al actualizar. Además `actualizarCompra` no recalcula el `estado`.
- **`PagoService.actualizarPago`** busca la compra con `actualizarPagoDto.getId()` (el id del pago) en vez del id de la compra, y no recalcula saldos.
- **`crearPago` sin `@Transactional`**: guarda el pago y la compra por separado; si falla el segundo, quedan inconsistentes.
- **Eliminar una compra con pagos** fallará por la llave foránea de `pago.compra_id`.
- **`actualizarCliente`** lanza "correo ya en uso" aunque el correo sea el mismo del cliente que se está editando.
- **Seguridad**: la contraseña se guarda en texto plano (campo `passwprd`, con typo) y no hay Spring Security; roles y permisos se guardan pero aún no se aplican.
- **Typos de nombres**: tabla `pproducto`, campo `passwprd`, clase `UniqueNameCheker`.
- El código antiguo comentado en `CompraService` se puede borrar (ya está en el historial de git).

---

## 7. Estructura del proyecto

```
src/main/java/
├── com/codeper/ControlClientes/
│   ├── ControlClientesApplication.java
│   ├── controller/   (9 controladores REST)
│   ├── dto/          (Create...Dto de entrada y ...Dto de salida)
│   ├── entity/       (12 entidades + enum EstadoCompra)
│   ├── repository/   (10 JpaRepository)
│   └── service/      (interfaces I...Service + implementaciones)
└── validator/
    ├── UniqueName.java            (anotación)
    ├── UniqueNameValidator.java   (lógica de validación)
    ├── spec/IUniqueNameCheker.java
    └── impl/UniqueNameCheker.java
src/main/resources/application.yaml
```
