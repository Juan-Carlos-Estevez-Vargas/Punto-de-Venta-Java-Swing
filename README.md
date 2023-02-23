# Punto de venta
Punto de Venta básico desarrollado en el lenguaje de programación Java y una base de datos MySQL;, el sistema emplea una arquitectura MVC y su interfaz gráfica fue realizada con el drag and drop de NetBeans; además, contiene reportes en PDF y excel utilizando la librería itext-pdf.

El sistema en cuestión cuanta con dos Roles:

### Administrador:
Este rol en cuestión puede ejecutar todas las operaciones presentes en el sistema de información, las cuales són: 
* Registrar y administrar clientes, productos, usuarios.
* Registros y gestión de ventas.
* Administrar proveedores.
* Eliminar usuarios y roles.

### Asistente:
El rol de asistente tiene funciones limitadas y restringidas, ya que será quién brinde apoyo al administrador del punto de venta.

## Pasos para ejecutar el programa:
* Clonar el proyecto en tu máquina local
* Abrir el proyecto con netbeans
* Importar la [base de datos](https://github.com/Juan-Carlos-Estevez-Vargas/punto-de-venta-basico-java/tree/master/database) en cualquier gestor de base de datos mysql como puede ser MySQLWorkbench, XAMPP, WAMPP, LAMPP, etc; recordar que si necesitas cambiar algun parámetro de conexión a la base de datos como el usuario o contraseña, o si deseas cambiar de gestor puedes hacerlo modificando el archo de [conexion.](https://github.com/Juan-Carlos-Estevez-Vargas/punto-de-venta-basico-java/blob/master/src/juan/estevez/sistemaventa/modelo/Conexion.java)
* Luego de haber importado la base de datos y tener el servicio arriba podrás ejecutar la aplicación desde el IDE sin ningún problema.
* Para iniciar sesión existen dos usuarios predeterminados, uno administrador y otro asistente.

### Credenciales de acceso:
``` batch
 Administrador:
 
 Usuario  ->  juan@example.com
 Contraseña  ->  1234
 
 Asistente:
 
 Usuario  ->  prueba@mail.com
 Contraseña  ->  1234
```

Adicional a ello, se anexa el archivo .jar y el [instalador](https://github.com/Juan-Carlos-Estevez-Vargas/punto-de-venta-basico-java/tree/master/Sistema%20Ventas) de la aplicación, tenga en cuenta que al momento de instalar la aplicación en un dispositivo cliente es necesario instalar la base de datos y dejarla como servicio del sistema operativo.

## 🌐 Socials:
[![Facebook](https://img.shields.io/badge/Facebook-%231877F2.svg?logo=Facebook&logoColor=white)](https://facebook.com/juancarlos.estevezvargas.98) [![Instagram](https://img.shields.io/badge/Instagram-%23E4405F.svg?logo=Instagram&logoColor=white)](https://instagram.com/juankestevez) [![LinkedIn](https://img.shields.io/badge/LinkedIn-%230077B5.svg?logo=linkedin&logoColor=white)](https://linkedin.com/in/juan-carlos-estevez-vargas) [![YouTube](https://img.shields.io/badge/YouTube-%23FF0000.svg?logo=YouTube&logoColor=white)](https://youtube.com/@JuanCarlosEstevezVargas) 
