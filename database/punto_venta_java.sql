-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 12-03-2022 a las 23:13:22
-- Versión del servidor: 10.4.22-MariaDB
-- Versión de PHP: 8.1.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `punto_venta_java`
--
CREATE DATABASE IF NOT EXISTS `punto_venta_java` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `punto_venta_java`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

CREATE TABLE `clientes` (
  `ID` int(11) NOT NULL,
  `DNI` int(20) NOT NULL,
  `NOMBRE` varchar(100) NOT NULL,
  `TELEFONO` bigint(15) NOT NULL,
  `DIRECCION` varchar(100) NOT NULL,
  `RAZON_SOCIAL` varchar(100) NOT NULL,
  `FECHA` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`ID`, `DNI`, `NOMBRE`, `TELEFONO`, `DIRECCION`, `RAZON_SOCIAL`, `FECHA`) VALUES
(1, 1234, 'Juan Estevez', 1238961900, 'Chiquinquirá', '', '2022-02-28 14:39:27'),
(6, 1001789654, 'Pedro Rosales', 3008765429, 'Fusagasugá', '', '2022-02-28 17:25:02'),
(10, 1002521678, 'Pedro Correa', 3133887654, 'Manizales', '', '2022-03-11 16:24:01'),
(11, 1035678900, 'Maluma Baby', 3105809976, 'Medellín', 'N/A', '2022-03-12 11:36:33');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `configuracion`
--

CREATE TABLE `configuracion` (
  `ID` int(11) NOT NULL,
  `NOMBRE` varchar(100) NOT NULL,
  `RUT` bigint(20) DEFAULT NULL,
  `TELEFONO` bigint(20) DEFAULT NULL,
  `DIRECCION` varchar(100) NOT NULL,
  `RAZON_SOCIAL` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `configuracion`
--

INSERT INTO `configuracion` (`ID`, `NOMBRE`, `RUT`, `TELEFONO`, `DIRECCION`, `RAZON_SOCIAL`) VALUES
(1, 'BAR JAZZ', 1234567890, 3219113202, 'BARBOSA', 'NA');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle`
--

CREATE TABLE `detalle` (
  `ID` int(11) NOT NULL,
  `CODIGO_PRODUCTO` varchar(30) DEFAULT NULL,
  `CANTIDAD` int(11) NOT NULL,
  `PRECIO` decimal(10,2) NOT NULL,
  `ID_VENTA` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `detalle`
--

INSERT INTO `detalle` (`ID`, `CODIGO_PRODUCTO`, `CANTIDAD`, `PRECIO`, `ID_VENTA`) VALUES
(1, '10', 10, '25000.00', 0),
(2, '4', 4, '1200.00', 0),
(3, '89', 89, '25000.00', 0),
(4, '4', 4, '1200.00', 0),
(5, '12', 12, '25000.00', 0),
(6, '100', 100, '25000.00', 0),
(7, '30', 30, '25000.00', 0),
(8, '34', 34, '1200.00', 0),
(9, '30', 30, '25000.00', 0),
(10, '34', 34, '1200.00', 0),
(11, '13', 13, '25000.00', 0),
(12, '4', 4, '25000.00', 0),
(13, '2', 2, '1200.00', 0),
(14, '56', 56, '25000.00', 0),
(15, '5', 5, '25000.00', 0),
(16, '4', 4, '25000.00', 0),
(17, '12', 12, '1200.00', 0),
(18, '5', 5, '25000.00', 0),
(19, '4', 4, '25000.00', 0),
(20, '12', 12, '25000.00', 0),
(21, '4', 4, '2000000.00', 0),
(22, '4', 4, '25000.00', 0),
(23, '1', 1, '25000.00', 0),
(24, '2', 2, '25000.00', 0),
(25, '6', 6, '25000.00', 0),
(26, '', 5, '25000.00', 0),
(27, '', 5, '25000.00', 0),
(28, 'e100', 8, '25000.00', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `producto`
--

CREATE TABLE `producto` (
  `ID` int(11) NOT NULL,
  `CODIGO` varchar(50) NOT NULL,
  `DESCRIPCION` varchar(255) NOT NULL,
  `PROVEEDOR` varchar(100) NOT NULL,
  `STOCK` int(11) NOT NULL,
  `PRECIO` decimal(10,2) NOT NULL,
  `FECHA` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `producto`
--

INSERT INTO `producto` (`ID`, `CODIGO`, `DESCRIPCION`, `PROVEEDOR`, `STOCK`, `PRECIO`, `FECHA`) VALUES
(2, 't67', 'Jabon Palmolive', 'Margot Robbie', 234, '1200.00', '2022-03-01 17:18:22'),
(4, 'e100', 'Silla Rimax', 'Martín Caceres', 492, '25000.00', '2022-03-04 10:39:15'),
(6, 'g89', 'Computador Compumax', 'Margot Robbie', 200, '2000000.00', '2022-03-11 16:50:40');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `proveedor`
--

CREATE TABLE `proveedor` (
  `ID` int(20) NOT NULL,
  `RUT` bigint(20) NOT NULL,
  `NOMBRE` varchar(100) NOT NULL,
  `TELEFONO` bigint(15) NOT NULL,
  `DIRECCION` varchar(100) NOT NULL,
  `RAZON_SOCIAL` varchar(100) NOT NULL,
  `FECHA` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `proveedor`
--

INSERT INTO `proveedor` (`ID`, `RUT`, `NOMBRE`, `TELEFONO`, `DIRECCION`, `RAZON_SOCIAL`, `FECHA`) VALUES
(1, 1082345900, 'Martín Caceres', 3000897000, 'Pereira', 'PEj8', '2022-02-28 17:25:27'),
(4, 10098903456, 'Margot Robbie', 3154670220, 'Barranquilla', '', '2022-03-11 16:36:31');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `ID` int(11) NOT NULL,
  `NOMBRE` varchar(100) NOT NULL,
  `CORREO` varchar(100) NOT NULL,
  `PASSWORD` varchar(100) NOT NULL,
  `ROL` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`ID`, `NOMBRE`, `CORREO`, `PASSWORD`, `ROL`) VALUES
(1, 'Juan Carlos Estevez Vargas', 'juan@example.com', '1234', 'Administrador'),
(4, 'Pedro', 'Pedro', '1234', 'Asistente');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ventas`
--

CREATE TABLE `ventas` (
  `ID` int(11) NOT NULL,
  `CLIENTE` varchar(100) NOT NULL,
  `VENDEDOR` varchar(100) NOT NULL,
  `TOTAL` decimal(12,2) NOT NULL,
  `FECHA` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `ventas`
--

INSERT INTO `ventas` (`ID`, `CLIENTE`, `VENDEDOR`, `TOTAL`, `FECHA`) VALUES
(1, 'Juan Estevez', 'Juan Estevez', '75000.00', '2022-03-04 10:51:05'),
(2, 'Juan Estevez', 'Juan Estevez', '93600.00', '2022-03-05 13:40:03'),
(3, 'Juan Estevez', 'Juan Estevez', '254800.00', '2022-03-05 16:20:14'),
(4, 'Juan Estevez', 'Juan Estevez', '2225000.00', '2022-03-05 16:30:33'),
(5, 'Juan Estevez', 'Juan Estevez', '3075000.00', '2022-03-05 16:32:01'),
(6, 'Juan Estevez', 'Juan Estevez', '304800.00', '2022-03-05 16:34:09'),
(7, 'Juan Estevez', 'Juan Estevez', '2500000.00', '2022-03-05 16:34:55'),
(8, 'Juan Estevez', 'Juan Estevez', '790800.00', '2022-03-06 14:38:21'),
(9, 'Juan Estevez', 'Juan Estevez', '790800.00', '2022-03-06 14:39:20'),
(10, 'Blanca Vargas', 'Juan Estevez', '325000.00', '2022-03-07 07:16:14'),
(11, 'Blanca Vargas', 'Juan Estevez', '102400.00', '2022-03-07 10:58:17'),
(12, 'Pedro Rosales', 'Juan Estevez', '1400000.00', '2022-03-07 10:59:37'),
(13, 'Juan Estevez', 'Juan Estevez', '125000.00', '2022-03-07 16:33:29'),
(14, 'Blanca Vargas', 'Juan Estevez', '114400.00', '2022-03-08 15:37:22'),
(15, 'Juan Estevez', 'Juan Estevez', '125000.00', '2022-03-09 16:08:09'),
(16, 'Juan Estevez', 'Juan Estevez', '100000.00', '10/03/2022'),
(17, 'Pedro Correa', 'Juan Estevez', '8300000.00', '11/03/2022'),
(18, 'Juan Estevez', 'Juan Estevez', '100000.00', '12/03/2022'),
(19, 'Juan Estevez', 'Juan Estevez', '25000.00', '12/03/2022'),
(20, 'Juan Estevez', 'Juan Estevez', '50000.00', '12/03/2022'),
(21, 'Juan Estevez', 'Juan Estevez', '150000.00', '12/03/2022'),
(22, 'Juan Estevez', 'Juan Estevez', '125000.00', '12/03/2022'),
(23, 'Juan Estevez', 'Juan Estevez', '125000.00', '12/03/2022'),
(24, 'Juan Estevez', 'Juan Estevez', '200000.00', '12/03/2022');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`ID`);

--
-- Indices de la tabla `configuracion`
--
ALTER TABLE `configuracion`
  ADD PRIMARY KEY (`ID`);

--
-- Indices de la tabla `detalle`
--
ALTER TABLE `detalle`
  ADD PRIMARY KEY (`ID`);

--
-- Indices de la tabla `producto`
--
ALTER TABLE `producto`
  ADD PRIMARY KEY (`ID`);

--
-- Indices de la tabla `proveedor`
--
ALTER TABLE `proveedor`
  ADD PRIMARY KEY (`ID`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`ID`);

--
-- Indices de la tabla `ventas`
--
ALTER TABLE `ventas`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `clientes`
--
ALTER TABLE `clientes`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de la tabla `configuracion`
--
ALTER TABLE `configuracion`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `detalle`
--
ALTER TABLE `detalle`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT de la tabla `producto`
--
ALTER TABLE `producto`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `proveedor`
--
ALTER TABLE `proveedor`
  MODIFY `ID` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `ventas`
--
ALTER TABLE `ventas`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
