package com.example.maxiescuela.domain

import java.sql.Date

data class Informacion(
    val id_usuario: Int,
    val nombres: String,
    val ap_paterno: String,
    val ap_materno:String,
    val direccion: String,
    val dni: String,
    val celular: String,
    val fecha_nacimiento: String,
    val fecha_registro: String
)
