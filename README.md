
# User API - 

## Requisitos

- Java 8+
- Maven

## Cómo ejecutar

```bash
mvn spring-boot:run
```

Accede a la consola H2 en: `http://localhost:8080/h2-console`

## Endpoints

- `POST /usuarios` - Crear usuario
- `GET /usuarios/{id}` - Obtener usuario
- `PUT /usuarios/{id}` - Actualizar usuario
- `PATCH /usuarios/{id}` - Modificar parcialmente
- `DELETE /usuarios/{id}` - Eliminar usuario

## Base de datos en memoria

- H2 se usa como base de datos en memoria.
