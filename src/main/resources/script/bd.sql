CREATE DATABASE EstiloAlPaso;

USE EstiloAlPaso;

CREATE TABLE Roles (
                       Id INT PRIMARY KEY AUTO_INCREMENT,
                       Nombre VARCHAR(50) NOT NULL
);

CREATE TABLE Usuarios (
                          Id INT PRIMARY KEY AUTO_INCREMENT,
                          NombreUser VARCHAR(50) NOT NULL UNIQUE,
                          Contrasena VARCHAR(255) NOT NULL,
                          FechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP,
                          RolId INT,
                          FOREIGN KEY (RolId) REFERENCES Roles(Id)
);

CREATE TABLE Agencias (
                          Id INT PRIMARY KEY AUTO_INCREMENT,
                          Nombre VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE Clientes (
                          Id INT PRIMARY KEY AUTO_INCREMENT,
                          NombreReal VARCHAR(100),
                          UsuarioTiktok VARCHAR(100) UNIQUE NOT NULL,
                          DNI VARCHAR(20),
                          Telefono VARCHAR(20) NOT NULL,
                          Direccion VARCHAR(200),
                          Ciudad VARCHAR(100),
                          Estado VARCHAR(20) DEFAULT 'ACTIVO',
                          FechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP,
                          AgenciaId INT,
                          FOREIGN KEY (AgenciaId) REFERENCES Agencias(Id)
);

CREATE TABLE Paquetes (
                          Id INT PRIMARY KEY AUTO_INCREMENT,
                          ClienteId INT NOT NULL,
                          Estado VARCHAR(20) DEFAULT 'ACTIVO',
                          FechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (ClienteId) REFERENCES Clientes(Id)
);

CREATE TABLE Prendas (
                         Id INT PRIMARY KEY AUTO_INCREMENT,
                         PaqueteId INT NOT NULL,
                         Descripcion VARCHAR(255),
                         PrecioTotal DECIMAL(10,2) NOT NULL,
                         PrecioPagado DECIMAL(10,2) DEFAULT 0,
                         Estado VARCHAR(50),
                         FechaVenta DATETIME DEFAULT CURRENT_TIMESTAMP,
                         CHECK (PrecioPagado <= PrecioTotal),
                         FOREIGN KEY (PaqueteId) REFERENCES Paquetes(Id)
);

CREATE TABLE Envios (
                        Id INT PRIMARY KEY AUTO_INCREMENT,
                        PaqueteId INT NOT NULL,
                        Estado VARCHAR(20) DEFAULT 'PENDIENTE',
                        TipoEnvio VARCHAR(20) NOT NULL,
                        Clave VARCHAR(4),
                        FechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP,
                        FechaEnvio DATETIME,
                        CantidadPrendas INT,
                        Pagado DECIMAL(10,2),
                        Total DECIMAL(10,2),
                        FOREIGN KEY (PaqueteId) REFERENCES Paquetes(Id)
);