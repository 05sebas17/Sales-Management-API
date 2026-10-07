# PROYECTO OFICIAL DEL MÓDULO 4

## API Backend de Gestión de Usuarios, Productos y Ventas

### 1. Contexto del proyecto

Una empresa dedicada a la comercialización de productos tecnológicos necesita desarrollar un sistema backend que permita administrar sus usuarios, productos y ventas.

Actualmente, la empresa requiere centralizar estas operaciones mediante una **API REST**, de manera que diferentes aplicaciones cliente puedan consumir los servicios del sistema.

El sistema deberá permitir:

* Registrar usuarios.
* Autenticar usuarios mediante credenciales.
* Proteger los recursos mediante autenticación con JWT.
* Administrar productos.
* Registrar ventas.
* Consultar información de las ventas.
* Relacionar usuarios, productos y ventas.
* Controlar el acceso dependiendo del rol del usuario.
* Persistir toda la información en una base de datos relacional.

El proyecto deberá ser desarrollado utilizando **Spring Boot**, siguiendo una arquitectura organizada por capas y aplicando buenas prácticas de desarrollo backend.

---

# 2. Objetivo general

Desarrollar una **API REST backend profesional** que permita gestionar usuarios, productos y ventas, integrando:

* Spring Boot.
* Spring Web.
* Spring Data JPA.
* Base de datos relacional.
* Spring Security.
* Autenticación mediante JWT.
* Autorización basada en roles.
* Validación de datos.
* Manejo adecuado de respuestas HTTP.
* Arquitectura por capas.

El proyecto deberá representar una aplicación backend funcional y estructurada, similar a un sistema que podría ser utilizado como base para una aplicación comercial.

---

# 3. Tecnologías requeridas

El proyecto deberá utilizar como mínimo:

* **Java**
* **Spring Boot**
* **Spring Web**
* **Spring Data JPA**
* **Spring Security**
* **JWT**
* **Maven**
* **Base de datos relacional**
* **Postman** para realizar pruebas de la API.

La base de datos podrá ser PostgreSQL, MySQL u otra base de datos relacional autorizada por el docente.

---

# 4. Actores del sistema

El sistema tendrá inicialmente dos tipos de usuarios:

### ADMIN

El administrador tendrá permisos para realizar operaciones de administración del sistema.

Entre sus responsabilidades estarán:

* Gestionar productos.
* Consultar usuarios.
* Consultar ventas.
* Realizar operaciones administrativas permitidas por la API.

### USER

El usuario normal podrá:

* Registrarse.
* Autenticarse.
* Consultar productos.
* Realizar compras.
* Consultar sus propias ventas.

El sistema deberá impedir que un usuario normal acceda a operaciones exclusivas del administrador.

---

# 5. Entidades principales

El sistema deberá estar construido alrededor de tres entidades principales:

## 5.1 Usuario

Representa a una persona que utiliza el sistema.

Como mínimo deberá contener información equivalente a:

* Identificador.
* Nombre.
* Correo electrónico.
* Contraseña.
* Rol.

El correo electrónico deberá permitir identificar de manera única al usuario.

La contraseña **no deberá almacenarse en texto plano**.

---

## 5.2 Producto

Representa un producto disponible para la venta.

Como mínimo deberá contener:

* Identificador.
* Nombre.
* Descripción.
* Precio.
* Stock.
* Estado.

El sistema deberá permitir determinar si un producto está disponible para la venta.

---

## 5.3 Venta

Representa una compra realizada por un usuario.

Como mínimo deberá registrar:

* Identificador.
* Usuario que realiza la compra.
* Fecha de la venta.
* Total de la venta.

Una venta deberá estar relacionada con el usuario que la realizó.

Además, una venta deberá contener uno o varios productos adquiridos.

Para representar correctamente esta información, el estudiante deberá diseñar las relaciones necesarias entre las entidades.

---

# 6. Reglas principales del negocio

El sistema deberá cumplir las siguientes reglas.

### Usuarios

1. Un usuario debe registrarse utilizando un correo electrónico.
2. No pueden existir dos usuarios con el mismo correo electrónico.
3. La contraseña debe almacenarse utilizando un mecanismo de cifrado/hash apropiado.
4. Un usuario debe autenticarse antes de acceder a recursos protegidos.
5. Después de autenticarse correctamente, el sistema deberá generar un token JWT.
6. El rol del usuario deberá determinar qué operaciones puede realizar.

### Productos

1. Los productos deben almacenarse en la base de datos.
2. El precio debe ser un valor válido.
3. El stock no puede ser negativo.
4. Un producto sin stock no debería poder venderse.
5. Las operaciones de administración de productos deberán estar protegidas.
6. Los usuarios normales podrán consultar los productos disponibles.

### Ventas

1. Una venta debe estar asociada a un usuario.
2. Una venta debe contener al menos un producto.
3. No se debe permitir vender una cantidad superior al stock disponible.
4. Al realizar una venta, el stock del producto deberá actualizarse.
5. El total de la venta deberá corresponder al valor de los productos adquiridos.
6. Un usuario normal solamente podrá consultar sus propias ventas.
7. Un administrador podrá consultar las ventas según los permisos definidos para el sistema.

---

# 7. Funcionalidades obligatorias

## 7.1 Registro de usuarios

La API deberá proporcionar un endpoint que permita registrar nuevos usuarios.

El registro deberá:

* Recibir los datos necesarios.
* Validar la información.
* Verificar que el correo no esté registrado.
* Encriptar/hash de la contraseña.
* Asignar un rol.
* Guardar el usuario en la base de datos.
* Retornar una respuesta HTTP apropiada.

---

## 7.2 Autenticación

La API deberá proporcionar un endpoint para iniciar sesión.

El proceso deberá:

1. Recibir correo y contraseña.
2. Buscar el usuario.
3. Validar las credenciales.
4. Generar un JWT si las credenciales son correctas.
5. Retornar el token al cliente.

El token deberá utilizarse posteriormente para acceder a los endpoints protegidos.

---

## 7.3 Gestión de productos

El sistema deberá permitir administrar productos.

Como mínimo deberá contemplarse:

* Crear producto.
* Consultar todos los productos.
* Consultar un producto por ID.
* Actualizar producto.
* Eliminar producto.

Estas operaciones deberán tener control de acceso.

No todas las operaciones estarán disponibles para todos los usuarios.

---

## 7.4 Registro de ventas

La API deberá permitir registrar una nueva venta.

Para realizar una venta, el sistema deberá:

1. Identificar al usuario autenticado mediante el JWT.
2. Recibir los productos y cantidades solicitadas.
3. Verificar que los productos existan.
4. Verificar que exista stock suficiente.
5. Calcular el valor de la venta.
6. Registrar la venta.
7. Actualizar el stock.
8. Asociar la venta con el usuario autenticado.

La información deberá persistirse en la base de datos.

---

## 7.5 Consulta de ventas

La API deberá permitir consultar las ventas registradas.

Un usuario normal podrá consultar únicamente sus propias ventas.

Un administrador podrá consultar las ventas del sistema, de acuerdo con las reglas de autorización definidas para el proyecto.

---

# 8. Seguridad

Uno de los requisitos fundamentales del proyecto será la implementación de seguridad.

El sistema deberá utilizar **Spring Security y JWT**.

Como mínimo deberá existir el siguiente flujo:

```text
Usuario
   │
   │ Login
   ▼
POST /auth/login
   │
   │ Credenciales válidas
   ▼
JWT
   │
   │ Authorization: Bearer <token>
   ▼
Spring Security
   │
   ├── Usuario autenticado
   │
   ├── Rol USER
   │
   └── Rol ADMIN
   │
   ▼
Endpoint protegido
```

Los endpoints deberán clasificarse de acuerdo con su nivel de protección.

Deberán existir recursos:

* Públicos.
* Protegidos para usuarios autenticados.
* Protegidos exclusivamente para administradores.

---

# 9. API REST

La aplicación deberá exponer una API REST organizada.

Como punto de partida, deberán contemplarse recursos equivalentes a:

### Autenticación

```text
POST /auth/register
POST /auth/login
```

### Productos

```text
GET    /api/products
GET    /api/products/{id}
POST   /api/products
PUT    /api/products/{id}
DELETE /api/products/{id}
```

### Ventas

```text
POST /api/sales
GET  /api/sales
GET  /api/sales/{id}
```

### Usuarios

Deberán definirse endpoints para las operaciones administrativas relacionadas con usuarios que sean necesarias para el sistema.

La lista anterior representa una **guía inicial de recursos**. Durante el diseño del proyecto, el estudiante deberá determinar los endpoints definitivos, sus métodos HTTP, parámetros, cuerpos de solicitud y respuestas.

---

# 10. Arquitectura del proyecto

El backend deberá organizarse utilizando una arquitectura por capas.

Como mínimo deberá contemplar una estructura conceptual similar a:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

El proyecto deberá separar adecuadamente las responsabilidades.

Por ejemplo:

### Controller

Responsable de:

* Recibir solicitudes HTTP.
* Validar la entrada cuando corresponda.
* Invocar los servicios.
* Construir las respuestas HTTP.

### Service

Responsable de:

* Implementar la lógica de negocio.
* Aplicar las reglas del sistema.
* Coordinar operaciones entre diferentes componentes.

### Repository

Responsable de:

* Acceder a la base de datos.
* Consultar información.
* Persistir entidades.

### Model / Entity

Representará las entidades persistentes del sistema.

### Security

Contendrá los componentes relacionados con:

* Spring Security.
* JWT.
* Autenticación.
* Autorización.
* Filtros de seguridad.

La estructura definitiva será diseñada por el estudiante durante la primera fase del proyecto.

---

# 11. Relaciones entre entidades

El proyecto deberá implementar relaciones entre las entidades utilizando JPA.

Como mínimo deberá existir una relación entre:

```text
Usuario ─────────── Venta
```

y una relación que permita representar los productos incluidos en una venta:

```text
Venta ─────────── Producto
```

El estudiante deberá analizar cuál es la cardinalidad correcta de cada relación y cómo representarla mediante JPA.

La solución deberá permitir consultar correctamente la información relacionada sin duplicar innecesariamente los datos.

---

# 12. Validación de información

La API deberá validar los datos recibidos por el cliente.

Por ejemplo:

* El correo debe tener un formato válido.
* Los campos obligatorios no deben estar vacíos.
* El precio debe ser válido.
* El stock no puede ser negativo.
* Las cantidades de productos deben ser mayores que cero.

Cuando una solicitud sea inválida, la API deberá retornar un código HTTP apropiado y una respuesta que permita identificar el problema.

---

# 13. Manejo de errores

La aplicación deberá manejar adecuadamente situaciones como:

* Usuario inexistente.
* Correo ya registrado.
* Credenciales incorrectas.
* Producto inexistente.
* Venta inexistente.
* Stock insuficiente.
* Acceso no autorizado.
* Datos inválidos.
* Acceso a recursos sin autenticación.

La API deberá utilizar códigos de estado HTTP apropiados.

Por ejemplo:

```text
200 OK
201 CREATED
400 BAD REQUEST
401 UNAUTHORIZED
403 FORBIDDEN
404 NOT FOUND
409 CONFLICT
500 INTERNAL SERVER ERROR
```

El estudiante deberá determinar cuál corresponde a cada situación.

---

# 14. Persistencia

Toda la información relevante del sistema deberá almacenarse en una base de datos.

No se permitirá utilizar únicamente estructuras en memoria como:

```java
List<Producto>
Map<Long, Usuario>
```

como mecanismo definitivo de almacenamiento.

El backend deberá utilizar JPA/Hibernate para realizar la persistencia.

Al reiniciar la aplicación, los datos previamente almacenados deberán permanecer disponibles en la base de datos.

---

# 15. Pruebas con Postman

El proyecto deberá ser probado utilizando Postman.

El estudiante deberá demostrar, como mínimo, los siguientes escenarios:

### Registro

```text
Crear un usuario correctamente.
Intentar registrar un correo duplicado.
Enviar datos inválidos.
```

### Login

```text
Login exitoso.
Login con contraseña incorrecta.
Login con usuario inexistente.
```

### Seguridad

```text
Acceder a un endpoint protegido sin token.
Acceder con un token válido.
Intentar acceder a un recurso ADMIN utilizando un usuario USER.
Acceder correctamente utilizando un usuario ADMIN.
```

### Productos

```text
Crear producto.
Consultar productos.
Consultar producto por ID.
Actualizar producto.
Eliminar producto.
```

### Ventas

```text
Crear una venta válida.
Crear una venta sin stock suficiente.
Crear una venta con un producto inexistente.
Consultar las ventas del usuario.
Consultar ventas como administrador.
```

---

# 16. Criterio fundamental del proyecto

El proyecto no será evaluado únicamente por "que funcione".

El estudiante deberá demostrar que es capaz de construir un backend **organizado, seguro, persistente y mantenible**.

Se tendrá en cuenta:

* Diseño de la solución.
* Organización del código.
* Arquitectura por capas.
* Uso correcto de Spring Boot.
* Uso correcto de JPA.
* Diseño de relaciones.
* Implementación de la lógica de negocio.
* Seguridad.
* Autenticación.
* Autorización.
* Validaciones.
* Manejo de errores.
* Persistencia.
* Diseño de endpoints.
* Pruebas mediante Postman.

---

# 17. Resultado esperado

Al finalizar el proyecto deberá existir una API backend que permita realizar el siguiente flujo completo:

```text
                    ┌─────────────────┐
                    │    USUARIO      │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │    REGISTRO     │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │      LOGIN      │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │       JWT       │
                    └────────┬────────┘
                             │
                             ▼
                 ┌───────────────────────┐
                 │ ENDPOINTS PROTEGIDOS  │
                 └───────────┬───────────┘
                             │
                 ┌───────────┴───────────┐
                 ▼                       ▼
          ┌─────────────┐          ┌─────────────┐
          │  PRODUCTOS  │          │   VENTAS    │
          └──────┬──────┘          └──────┬──────┘
                 │                        │
                 └───────────┬────────────┘
                             ▼
                    ┌─────────────────┐
                    │   BASE DE DATOS │
                    └─────────────────┘
```

El resultado final deberá ser un backend capaz de ser consumido por una aplicación cliente mediante HTTP y que mantenga la información de usuarios, productos y ventas de forma persistente y segura.

---

# 18. Restricción importante del desarrollo

El proyecto deberá desarrollarse de manera incremental.

No se espera que el estudiante construya todo el sistema en una sola etapa.

El desarrollo se dividirá en diferentes fases:

```text
FASE 1
Diseño del sistema
        ↓
FASE 2
Configuración y arquitectura
        ↓
FASE 3
Usuarios y persistencia
        ↓
FASE 4
Autenticación y JWT
        ↓
FASE 5
Seguridad y roles
        ↓
FASE 6
Productos
        ↓
FASE 7
Ventas y relaciones
        ↓
FASE 8
Validaciones y manejo de errores
        ↓
FASE 9
Pruebas completas con Postman
```

Cada fase deberá quedar funcional antes de continuar con la siguiente.

---

# 19. Entregable final

Al finalizar el módulo, el estudiante tendrá:

1. Proyecto Spring Boot completo.
2. Código fuente organizado.
3. Base de datos utilizada por el sistema.
4. Configuración necesaria para ejecutar el proyecto.
5. Colección de pruebas de Postman.
6. Documentación básica de la API.
7. README con instrucciones para ejecutar el proyecto.
8. Evidencias de funcionamiento de los principales casos de uso.

El proyecto deberá poder ser ejecutado por otra persona siguiendo las instrucciones proporcionadas por el estudiante.

---

# 20. Desafío del proyecto

El objetivo final no es simplemente construir tres tablas y algunos endpoints.

El desafío consiste en construir un **backend completo**, donde diferentes componentes trabajen conjuntamente:

```text
HTTP
 ↓
Controller
 ↓
Security
 ↓
Service
 ↓
Business Logic
 ↓
Repository
 ↓
JPA / Hibernate
 ↓
Database
```

y donde la seguridad determine quién puede acceder a cada recurso:

```text
                 API
                  │
          ┌───────┴───────┐
          │               │
        USER             ADMIN
          │               │
          ▼               ▼
     Consultar        Administrar
     productos        productos
     Comprar          usuarios
     Ver ventas       consultar ventas
                      y operaciones
                      administrativas
```

El proyecto representa la integración de los conocimientos adquiridos durante el programa y deberá ser desarrollado con criterios propios de un desarrollador backend profesional.
