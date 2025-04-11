package com.example.maxiescuela.activity

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maxiescuela.adapter.UsuarioAdapter
import com.example.maxiescuela.databinding.FragmentUsuariosBinding
import com.example.maxiescuela.domain.Informacion
import com.example.maxiescuela.domain.UsuarioCompleto
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
    private val listaUsuarios = mutableListOf<UsuarioCompleto>()
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

        usuarioAdapter = UsuarioAdapter(listaUsuarios,
            onEditClick = { usuario ->
                // Lógica para editar el usuario
            },
            onInfoClick = { usuario ->
                val dialogFragment = InformacionUsarioDialogFragment(usuario,
                    object : InformacionUsarioDialogFragment.oninfoagregadaListener {
                        override fun onInfoagregada(informacion: Informacion) {
                            agregarInformacion(informacion)
                        }
                    })

                val bundle = Bundle()
                bundle.putInt("usuario_id", usuario.id)
                dialogFragment.arguments = bundle
                dialogFragment.show(childFragmentManager, "informacion_usuario_dialog")
            }
        )

        binding.recyclerViewUsuarios.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = usuarioAdapter
        }

        usuarioService = RetrofitHelper.GetRetrofit().create(ApiMaxiEscuela::class.java)

        obtenerusuarios()

        binding.fabAgregarUsuario.setOnClickListener {
            val dialog = AgregarUsuarioFragment(object : AgregarUsuarioFragment.OnusuaarioAgragadoListener {
                override fun onAlumnoAgregado(nuevoAlumno: usuarioModel) {
                    agregarAlumno(nuevoAlumno)
                }
            })
            dialog.show(parentFragmentManager, "AgregarAlumnoDialog")
        }
    }

    fun obtenerusuarios() {
        if (_binding != null && isAdded) {
            binding.progressBar.visibility = View.VISIBLE
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val usuarios = usuarioService.obtenerUsuarios()

                withContext(Dispatchers.Main) {
                    if (_binding != null && isAdded) {
                        actualizarLista(usuarios)
                        binding.progressBar.visibility = View.GONE
                    }
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

    private fun actualizarLista(nuevaLista: List<UsuarioCompleto>) {
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
                        if (_binding != null && isAdded) {
                            Toast.makeText(requireContext(), "Alumno agregado exitosamente", Toast.LENGTH_SHORT).show()
                            obtenerusuarios()
                        }
                    }
                } else {
                    mostrarError("Error al agregar alumno: ${response.message()}")
                }
            } catch (e: Exception) {
                mostrarError("Error de red: ${e.message}")
            }
        }
    }

    private fun agregarInformacion(informacion: Informacion) {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    usuarioService.agregarInformacion(informacion)
                }

                withContext(Dispatchers.Main) {
                    if (_binding != null && isAdded) {
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), "Información agregada con éxito", Toast.LENGTH_SHORT).show()
                            obtenerusuarios()
                        } else {
                            Toast.makeText(requireContext(), "Error al agregar la información", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    if (_binding != null && isAdded) {
                        Toast.makeText(requireContext(), "Error en la conexión: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                    Log.e("UsuariosFragment", "Error en la conexión: ${e.message}")
                }
            }
        }
    }

    private suspend fun mostrarError(mensaje: String) {
        withContext(Dispatchers.Main) {
            if (_binding != null && isAdded) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()
            } else {
                Log.e("UsuariosFragment", "No se pudo mostrar el mensaje: $mensaje (fragmento destruido)")
            }
            Log.e("UsuariosFragment", mensaje)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evitar fugas de memoria
    }
}
