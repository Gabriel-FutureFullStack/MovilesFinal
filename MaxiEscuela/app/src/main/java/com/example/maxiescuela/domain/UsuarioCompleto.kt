package com.example.maxiescuela.domain

data class UsuarioCompleto(
    var id: Int,
    var username: String,
    var email: String,
    var contraseña: String,
    var id_rol: Int,
    var nombre_rol: String,
    var nombres: String,
    var ap_paterno: String,
    var ap_materno: String,
    var direccion: String,
    var dni: String,
    var celular:String,
    var fecha_nacimiento: String,
    var fecha_registro: String
)
