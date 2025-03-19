create database colegio;
use colegio;

Create table usuario(
	id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nombres VARCHAR(50) NOT NULL,
    apellidos VARCHAR(50) NOT NULL,
    rol ENUM('admin', 'profesor', 'estudiante') NOT NULL
);

Create table asistencia(
	id_asistencia INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    fecha DATE NOT NULL,
    estado ENUM('presente', 'ausente', 'tarde') NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario) ON DELETE CASCADE
);


