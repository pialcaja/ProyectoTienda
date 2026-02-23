# 🎮 Carrito de Compras - Videojuegos

E-commerce de videojuegos desarrollado con Spring Boot y Angular.

## 🚀 Tecnologías

**Backend:**
- Spring Boot 4.0.2
- Spring Security + JWT
- MySQL 8.0
- Java 17

**Frontend:**
- Angular 21
- TypeScript
- CSS

## 🏗️ Arquitectura
```
Cliente (Angular) → API REST (Spring Boot) → MySQL
```

## 🛠️ Instalación Local

### Prerrequisitos
- Java 17+
- Node.js 20+
- MySQL 8.0+
- Maven 3.9+

### Backend
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
ng serve
```

### Con Docker
```bash
docker-compose up --build
```

## 📚 Documentación API

- API Docs: [docs/api-docs.md](docs/api-docs.md)

## 🗄️ Base de Datos

Schema completo: [docs/database-schema.sql](docs/database-schema.sql)

## 🚢 Despliegue

Ver [docs/deployment-guide.md](docs/deployment-guide.md)
