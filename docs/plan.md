# Plan de Aprendizaje: Carrito de Compras

---

## Fase 1: Fundamentos y Configuración (Semana 1-2)

### Backend — Spring Boot
- Crea un proyecto básico con **Spring Initializr**  
  *(dependencies: Web, JPA, MySQL, Lombok, Validation)*  
- Configura `application.properties` para conectar a MySQL.
- Aprende qué hace cada anotación antes de usarla:
  - `@Entity`
  - `@Service`
  - `@RestController`

### Frontend — Angular
- Instala **Angular CLI** y crea un proyecto nuevo.
- Entiende la estructura:
  - Componentes
  - Servicios
  - Módulos
- Aprende sobre **HttpClient** para llamadas API.

### Base de datos
- Diseña el modelo entidad–relación en papel primero.
- Entidades básicas:
  - Usuario
  - Producto
  - Carrito
  - ItemCarrito
  - Orden

---

## Fase 2: Construcción por Capas (Semana 3-4)

Empieza con **UN módulo simple (Productos)**:

### Capa de Modelo (Entity)
- Crea la entidad `Producto`.
- Entiende las relaciones JPA.

### Capa de Repositorio
- Interface que extiende `JpaRepository`.
- Aprende qué métodos vienen “gratis”.

### Capa de Servicio
- Implementa la lógica de negocio.
- Manejo de excepciones personalizadas.

### Capa de Controlador (API REST)
- Endpoints CRUD básicos.
- Usa DTOs para no exponer entidades directamente.

### Frontend
- Servicio Angular para consumir la API.
- Componente para listar productos.
- Formulario para agregar/editar.

👉 Repite este proceso para cada módulo:
- Categorías
- Usuarios
- Carrito
- Órdenes

---

## Fase 3: Funcionalidad del Carrito (Semana 5)

Ahora que entiendes las capas:

- Implementa agregar productos al carrito.
- Actualizar cantidades.
- Calcular totales.
- Persistir carrito en base de datos.

---

## Fase 4: Mejoras y Seguridad (Semana 6-7)

- Spring Security — autenticación JWT.
- Validaciones — Bean Validation en backend.
- Manejo de errores — `@ControllerAdvice`.
- Guards en Angular — proteger rutas.

---

## Buenas Prácticas a Aplicar

### Backend
- Separación en capas  
  *(Controller → Service → Repository)*  
- DTOs para transferencia de datos.
- Inyección de dependencias por constructor.
- Manejo centralizado de excepciones.
- Validaciones con Bean Validation.
- Nombres descriptivos en endpoints REST.

### Frontend
- Componentes pequeños y reutilizables.
- Servicios para lógica de negocio y llamadas HTTP.
- Observables para manejo asíncrono.
- Lazy loading de módulos.
- Manejo de errores con interceptores.

### General
- Git: commits atómicos y descriptivos.
- No hardcodear valores (usar archivos de configuración).
- Documentar código complejo.
- Testing unitario básico.

---

## Método de Estudio Recomendado

- Lee documentación oficial antes de implementar.
- Escribe el código a mano, sin copiar.
- Rompe cosas — modifica y observa qué pasa.
- Haz preguntas específicas cuando te atores.
- Refactoriza — mejora tu código cuando funcione.
