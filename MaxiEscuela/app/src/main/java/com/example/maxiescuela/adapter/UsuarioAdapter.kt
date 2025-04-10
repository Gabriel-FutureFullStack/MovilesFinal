package com.example.maxiescuela.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.maxiescuela.databinding.CardUsuarioBinding
import com.example.maxiescuela.domain.Informacion
import com.example.maxiescuela.domain.usuarioModel

class UsuarioAdapter(
    private val listaUsuarios: List<usuarioModel>,
    private val onEditClick: (usuarioModel) -> Unit,
    private val onInfoClick: (Int) -> Unit,  // Recibe un String (ID del usuario)
) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    // ViewHolder para manejar los elementos de la lista
    class UsuarioViewHolder(private val binding: CardUsuarioBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            usuario: usuarioModel,
            onEditClick: (usuarioModel) -> Unit,
            onInfoClick: (Int) -> Unit,

        ) {
            binding.tvNombre.text = usuario.username
            binding.tvApellidos.text = usuario.email
            // Validamos si el rol es null
            binding.tvRol.text = obtenerNombreRol(usuario.id_rol)

            // Manejar clic en editar
            binding.btnEditar.setOnClickListener { onEditClick(usuario) }

            // Manejar clic en info (más detalles)
            binding.btnInfo.setOnClickListener {
                onInfoClick(usuario.id_usuario)  // Aquí pasamos el ID del usuario
            }


        }

        private fun obtenerNombreRol(idRol: Int): String {
            return when (idRol) {
                1 -> "Estudiante"
                2 -> "Profesor"
                3 -> "Administrador"
                else -> "Desconocido"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val binding = CardUsuarioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsuarioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = listaUsuarios[position]
        // Pasar los manejadores de clic a la función bind
        holder.bind(
            usuario,
            onEditClick,
            onInfoClick,

        )
    }

    override fun getItemCount(): Int = listaUsuarios.size
}
