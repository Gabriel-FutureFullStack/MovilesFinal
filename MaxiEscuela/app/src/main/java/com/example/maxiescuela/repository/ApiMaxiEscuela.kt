package com.example.maxiescuela.repository

import com.example.maxiescuela.domain.Asistencia
import com.example.maxiescuela.domain.Clase
import com.example.maxiescuela.domain.Informacion
import com.example.maxiescuela.domain.UsuarioCompleto
import com.example.maxiescuela.domain.usuarioModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiMaxiEscuela{
    //USUARIOS
    @POST("usuarios")
    suspend fun crearUsuario(@Body usuario: usuarioModel): Response<usuarioModel>

    @GET("usuarios")
    suspend fun obtenerUsuarios(): List<UsuarioCompleto>

    @GET("usuarios/{id}")
    suspend fun obtenerUsuarioPorId(@Path("id") id: Int): usuarioModel

    @PUT("usuarios/{id}")
    suspend fun actualizarUsuario(@Path("id") id: Int, @Body usuario: usuarioModel): usuarioModel

    @DELETE("usuarios/{id}")
    suspend fun eliminarUsuario(@Path("id") id: Int): Void

    //INFORMACION PERSONAL
    @POST("informacion_personal")
    suspend fun agregarInformacion(@Body informacion: Informacion):Response<Informacion>

    //Clases
    @GET("clases")
    suspend fun listarClase(): List<Clase>

    @POST("asistencias")
    suspend fun crearAsistencia(@Body asistencia: Asistencia): Asistencia

    @GET("asistencias")
    suspend fun obtenerAsistencias(): List<Asistencia>

    @GET("asistencias/{id}")
    suspend fun obtenerAsistenciaPorId(@Path("id") id: Int): Asistencia

    @PUT("asistencias/{id}")
    suspend fun actualizarAsistencia(@Path("id") id: Int, @Body asistencia: Asistencia): Asistencia

    @DELETE("asistencias/{id}")
    suspend fun eliminarAsistencia(@Path("id") id: Int): Void
}
