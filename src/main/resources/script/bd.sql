create database EstiloAlPaso;

use EstiloAlPaso;

CREATE TABLE Roles (
                       Id INT PRIMARY KEY AUTO_INCREMENT,
                       Nombre VARCHAR(50) NOT NULL
);

CREATE TABLE Usuarios (
                          Id INT PRIMARY KEY AUTO_INCREMENT,
                          NombreUser VARCHAR(50) NOT NULL UNIQUE,
                          Contraseña VARCHAR(255) NOT NULL,
                          FechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP,
                          RolId INT,
                          FOREIGN KEY (RolId) REFERENCES Roles(Id)
);

CREATE TABLE Clientes (
                          Id INT PRIMARY KEY AUTO_INCREMENT,
                          NombreReal VARCHAR(100),
                          UsuarioTiktok VARCHAR(100) NOT NULL,
                          DNI VARCHAR(20),
                          Telefono VARCHAR(20) NOT NULL,
                          Direccion VARCHAR(200),
                          Ciudad VARCHAR(100),
                          Estado VARCHAR(20) DEFAULT 'ACTIVO',
                          FechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP
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
                         EstadoPrenda VARCHAR(50),
                         FechaVenta DATETIME DEFAULT CURRENT_TIMESTAMP,
                         CHECK (PrecioPagado <= PrecioTotal),
                         FOREIGN KEY (PaqueteId) REFERENCES Paquetes(Id)
);

CREATE TABLE Envios (
                        Id INT PRIMARY KEY AUTO_INCREMENT,
                        PaqueteId INT NOT NULL UNIQUE,
                        Estado VARCHAR(20) DEFAULT 'PENDIENTE',
                        TipoEnvio VARCHAR(20) NOT NULL,
                        Clave VARCHAR(4),
                        FechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP,
                        FechaEnvio DATETIME,

                        FOREIGN KEY (PaqueteId) REFERENCES Paquetes(Id)
);

-- Dashboard General

DELIMITER $$

CREATE PROCEDURE sp_dashboard_general()
BEGIN
SELECT
    COUNT(p.Id) AS total_prendas,
    IFNULL(SUM(p.PrecioTotal), 0) AS total_vendido,
    IFNULL(SUM(p.PrecioPagado), 0) AS total_pagado,
    IFNULL(SUM(p.PrecioTotal - p.PrecioPagado), 0) AS total_pendiente,

    (SELECT COUNT(*) FROM Paquetes WHERE Estado = 'ACTIVO') AS paquetes_activos,

    (SELECT COUNT(DISTINCT c.Id)
     FROM Clientes c
              JOIN Paquetes pa ON pa.ClienteId = c.Id
              JOIN Prendas pr ON pr.PaqueteId = pa.Id
     WHERE pr.PrecioPagado < pr.PrecioTotal
    ) AS clientes_con_deuda

FROM Prendas p;
END$$

DELIMITER ;

DELIMITER $$

-- Clientes con deudas

CREATE PROCEDURE sp_clientes_con_deuda()
BEGIN
SELECT DISTINCT c.*
FROM Clientes c
         JOIN Paquetes p ON p.ClienteId = c.Id
         JOIN Prendas pr ON pr.PaqueteId = p.Id
WHERE pr.PrecioPagado < pr.PrecioTotal
  AND c.Estado = 'ACTIVO';
END$$

DELIMITER ;

-- Resumen de Cliente
DELIMITER $$

CREATE PROCEDURE sp_resumen_cliente(IN cliente_id INT)
BEGIN
SELECT
    c.Id,
    c.UsuarioTiktok,
    c.Telefono,

    COUNT(pr.Id) AS total_prendas,
    IFNULL(SUM(pr.PrecioPagado), 0) AS total_pagado,
    IFNULL(SUM(pr.PrecioTotal - pr.PrecioPagado), 0) AS total_pendiente

FROM Clientes c
         LEFT JOIN Paquetes pa ON pa.ClienteId = c.Id AND pa.Estado = 'ACTIVO'
         LEFT JOIN Prendas pr ON pr.PaqueteId = pa.Id

WHERE c.Id = cliente_id
GROUP BY c.Id;
END$$

DELIMITER ;

-- Resumen de Paquete
DELIMITER $$

CREATE PROCEDURE sp_resumen_paquete(IN paquete_id INT)
BEGIN
SELECT
    p.Id,

    COUNT(pr.Id) AS total_prendas,
    IFNULL(SUM(pr.PrecioPagado), 0) AS total_pagado,
    IFNULL(SUM(pr.PrecioTotal - pr.PrecioPagado), 0) AS total_pendiente

FROM Paquetes p
         LEFT JOIN Prendas pr ON pr.PaqueteId = p.Id

WHERE p.Id = paquete_id
GROUP BY p.Id;
END$$

DELIMITER ;

-- Lusta de envios Pendientes
DELIMITER $$

CREATE PROCEDURE sp_envios_pendientes()
BEGIN
SELECT
    e.Id AS envioId,
    e.TipoEnvio,
    c.UsuarioTiktok,
    c.NombreReal,
    c.DNI,
    c.Telefono,
    c.Direccion,
    c.Ciudad,
    COUNT(pr.Id) AS cantidad_prendas

FROM Envios e
         JOIN Paquetes p ON e.PaqueteId = p.Id
         JOIN Clientes c ON p.ClienteId = c.Id
         LEFT JOIN Prendas pr ON pr.PaqueteId = p.Id

WHERE e.Estado = 'PENDIENTE'

GROUP BY e.Id;
END$$

DELIMITER ;