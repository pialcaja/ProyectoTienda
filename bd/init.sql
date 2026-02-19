CREATE DATABASE IF NOT EXISTS bd_carrito;
USE bd_carrito;

-- DROP DATABASE bd_carrito;

CREATE TABLE tb_rol (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL,
    descripcion VARCHAR(250) DEFAULT 'SIN DESCRIPCIÓN'
);

CREATE TABLE tb_usuario (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    pwd VARCHAR(250) NOT NULL,
    fechaRegistro DATETIME DEFAULT CURRENT_TIMESTAMP,
    fechaActualizacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    estado BOOLEAN DEFAULT TRUE,
    rolId BIGINT NOT NULL DEFAULT 2,
    FOREIGN KEY (rolId) REFERENCES tb_rol (id) ON DELETE RESTRICT
);

CREATE TABLE tb_categoria (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL,
    descripcion VARCHAR(250) DEFAULT 'SIN DESCRIPCIÓN',
    estado BOOLEAN DEFAULT TRUE
);

CREATE TABLE tb_producto (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(250) DEFAULT 'SIN DESCRIPCIÓN',
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    imagenUrl VARCHAR(250) DEFAULT NULL,
    categoriaId BIGINT NOT NULL,
    fechaRegistro DATETIME DEFAULT CURRENT_TIMESTAMP,
    fechaActualizacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    estado BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (categoriaId) REFERENCES tb_categoria (id) ON DELETE RESTRICT
);

CREATE TABLE tb_genero (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE tb_plataforma (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE tb_videojuego (
	productoId BIGINT PRIMARY KEY,
    desarrollador VARCHAR(100) DEFAULT 'NO ESPECIFICADO',
    fechaLanzamiento DATE NOT NULL,
    FOREIGN KEY (productoId) REFERENCES tb_producto (id) ON DELETE CASCADE
);

CREATE TABLE tb_videojuego_genero (
	videojuegoId BIGINT NOT NULL,
    generoId BIGINT NOT NULL,
    PRIMARY KEY (videojuegoId, generoId),
    FOREIGN KEY (videojuegoId) REFERENCES tb_videojuego (productoId) ON DELETE CASCADE,
    FOREIGN KEY (generoId) REFERENCES tb_genero (id) ON DELETE RESTRICT
);

CREATE TABLE tb_videojuego_plataforma (
	videojuegoId BIGINT NOT NULL,
    plataformaId BIGINT NOT NULL,
    PRIMARY KEY (videojuegoId, plataformaId),
    FOREIGN KEY (videojuegoId) REFERENCES tb_videojuego (productoId) ON DELETE CASCADE,
    FOREIGN KEY (plataformaId) REFERENCES tb_plataforma (id) ON DELETE RESTRICT
);

CREATE TABLE tb_carrito (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuarioId BIGINT UNIQUE NOT NULL,
    fechaRegistro DATETIME DEFAULT CURRENT_TIMESTAMP,
    fechaActualizacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (usuarioId) REFERENCES tb_usuario (id) ON DELETE CASCADE
);

CREATE TABLE tb_item_carrito (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    carritoId BIGINT NOT NULL,
    productoId BIGINT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precioUnitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (carritoId) REFERENCES tb_carrito (id) ON DELETE CASCADE,
    FOREIGN KEY (productoId) REFERENCES tb_producto (id) ON DELETE RESTRICT,
    UNIQUE KEY unique_producto_carrito (carritoId, productoId)
);

CREATE TABLE tb_orden (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuarioId BIGINT NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    estado ENUM('PENDIENTE','PAGADO','COMPLETADO','CANCELADO') DEFAULT 'PENDIENTE',
    fechaOrden DATETIME DEFAULT CURRENT_TIMESTAMP,
    metodoPago ENUM('MERCADOPAGO','TRANSFERENCIA','YAPE/PLIN') DEFAULT 'MERCADOPAGO',
    direccionEnvio VARCHAR(250) NOT NULL,
    FOREIGN KEY (usuarioId) REFERENCES tb_usuario (id) ON DELETE RESTRICT
);

CREATE TABLE tb_item_orden (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ordenId BIGINT NOT NULL,
    productoId BIGINT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precioUnitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (ordenId) REFERENCES tb_orden (id) ON DELETE RESTRICT,
    FOREIGN KEY (productoId) REFERENCES tb_producto (id) ON DELETE RESTRICT
);

INSERT INTO tb_rol (nombre, descripcion) 
VALUES ('ADMIN', 'Administrador de la aplicación'),
		('CLIENTE', 'Cliente de la aplicación');