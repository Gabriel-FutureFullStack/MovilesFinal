package com.example.maxiescuela.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maxiescuela.adapter.UsuarioAdapter
import com.example.maxiescuela.databinding.FragmentUsuariosBinding
import com.example.maxiescuela.domain.usuarioModel
import com.example.maxiescuela.repository.ApiMaxiEscuela
import com.example.maxiescuela.repository.RetrofitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

class UsuariosFragment : Fragment() {

    private var _binding: FragmentUsuariosBinding? = null
    private val binding get() = _binding!!

    private lateinit var usuarioService: ApiMaxiEscuela
    private val listaUsuarios = mutableListOf<usuarioModel>()
    private lateinit var usuarioAdapter: UsuarioAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsuariosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar RecyclerView
        usuarioAdapter = UsuarioAdapter( listaUsuarios,
            onEditClick = { usuario ->
                // Lógica para editar el usuario
            },
            onInfoClick = { usuarioId ->
                // Crear una instancia del DialogFragment
                val dialogFragment = InformacionUsarioDialogFragment()

                // Crear un Bundle para pasar el usuarioId
                val bundle = Bundle()
                bundle.putInt("usuario_id", usuarioId)  // Pasas el ID del usuario al fragmento

                // Asignar el Bundle al DialogFragment
                dialogFragment.arguments = bundle

                // Mostrar el DialogFragment utilizando childFragmentManager
                dialogFragment.show(childFragmentManager, "informacion_usuario_dialog")
            }
        )

        binding.recyclerViewUsuarios.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = usuarioAdapter
        }

        // Inicializa Retrofit
        usuarioService = RetrofitHelper.GetRetrofit().create(ApiMaxiEscuela::class.java)

        // Obtener datos de los alumnos
        obtenerusuarios()

        // Mostrar el DialogFragment al hacer clic en el botón flotante
        binding.fabAgregarUsuario.setOnClickListener {
            val dialog = AgregarUsuarioFragment(object : AgregarUsuarioFragment.OnusuaarioAgragadoListener {
                override fun onAlumnoAgregado(nuevoAlumno: usuarioModel) {
                    agregarAlumno(nuevoAlumno)
                }
            })
            dialog.show(parentFragmentManager, "AgregarAlumnoDialog")
        }
    }

    private fun obtenerusuarios() {
        binding.progressBar.visibility = View.VISIBLE // Mostrar ProgressBar

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val usuarios = usuarioService.obtenerUsuarios()

                // Cambiar al hilo principal para actualizar la UI
                withContext(Dispatchers.Main) {
                    actualizarLista(usuarios) // Actualiza la lista de usuarios en la UI
                    binding.progressBar.visibility = View.GONE // Ocultar ProgressBar
                }
            } catch (e: HttpException) {
                mostrarError("Error HTTP ${e.code()}: ${e.message()}")
            } catch (e: UnknownHostException) {
                mostrarError("No hay conexión a Internet.")
            } catch (e: IOException) {
                mostrarError("Error de red: ${e.message}")
            } catch (e: Exception) {
                mostrarError("Error inesperado: ${e.message}")
            }
        }
    }

    private fun actualizarLista(nuevaLista: List<usuarioModel>) {
        listaUsuarios.clear()
        listaUsuarios.addAll(nuevaLista)
        usuarioAdapter.notifyDataSetChanged()
    }

    private fun agregarAlumno(nuevoAlumno: usuarioModel) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = usuarioService.crearUsuario(nuevoAlumno)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        listaUsuarios.add(nuevoAlumno)
                        usuarioAdapter.notifyItemInserted(listaUsuarios.size - 1)
                        Toast.makeText(requireContext(), "Alumno agregado exitosamente", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    mostrarError("Error al agregar alumno: ${response.message()}")
                }
            } catch (e: Exception) {
                mostrarError("Error de red: ${e.message}")
            }
        }
    }

    private suspend fun mostrarError(mensaje: String) {
        withContext(Dispatchers.Main) {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()
            Log.e("AlumnosFragment", mensaje)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evitar fugas de memoria
    }
}