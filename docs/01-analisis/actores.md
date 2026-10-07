## Definición de actores
Un actor es alguien o algo que interactúa con el sistema. Los actores pueden ser personas, organizaciones, sistemas externos o cualquier entidad que tenga un interés en el sistema y que pueda influir en su comportamiento. 
En nuestro proyecto tendremos inicialmente dos actores principales:
 - **USER**: Representa al usuario final que interactúa con el sistema para realizar operaciones de gestión de ventas básico (usuario normal del sistema). Podr+á:
 * Registrarse en el sistema.
 * Iniciar sesión en el sistema.
 * Consultar productos disponibles.
 * Realizar compras de productos.
 * Consultar su historial de ventas.

 - **ADMIN**: Representa al administrador del sistema que tiene privilegios para gestionar usuarios, productos y configuraciones del sistema, también puede ver todas las operaciones realizadas por los usuarios y generar reportes de ventas (administrador).
 Podrá:
 * Iniciar sesión en el sistema.
 * Gestionar productos (agregar, modificar, eliminar).
 * Consultar usuarios registrados en el sistema.
 * Consultar ventas,
 * Ejectar operaciones administrativas y generar reportes de ventas.



 **Autenticación***: ¿Quién eres?
 Ejemplo: 
 ```text
 correo: carlos@devsenior.com
 password: 123456
 ```
El sistema verfica las credenciales.
Si son correctas responde:
```text
 Uusario autenticado correctamente
 ```

** Atorización**: ¿Qué puedes hacer?
Ejemplo:
```text
 Juan --> USER
 ```
 Juan está autenticado, pero no necesariamente puede:
 ```text
 DELETE /api/products/10
 ```

Mientras que un ADMIN sí podría ejecutar la misma operación:
```text
 DELETE /api/products/10
 ```

