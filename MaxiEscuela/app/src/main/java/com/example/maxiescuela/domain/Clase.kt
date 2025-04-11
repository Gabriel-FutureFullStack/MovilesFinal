package com.example.maxiescuela.domain

data class Clase(
    var id: Int,
    var nombre: String,
    var id_grado: Int,
    var id_profesor: Int,
    var dia_semana: String,
    var hora_inicio: String,
    var duracion: String,
    var nombre_grado: String,
    var nombres_profesor: String,
    var ap_paterno_profesor: String,
    var ap_materno_profesor: String
)
