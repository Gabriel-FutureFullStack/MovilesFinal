package com.example.maxiescuela.domain

data class Asistencia(
    val id_asistencia: Int? = null,
    val id_usuario: Int,
    val fecha: String,
    val estado: String
)
