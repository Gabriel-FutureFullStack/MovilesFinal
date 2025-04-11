package com.example.maxiescuela.activity


import ClaseAdapter
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.maxiescuela.databinding.FragmentClasesBinding
import com.example.maxiescuela.domain.Clase
import com.example.maxiescuela.repository.ApiMaxiEscuela
import com.example.maxiescuela.repository.RetrofitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

class Clases : Fragment() {

    private var _binding: FragmentClasesBinding? = null
    private val binding get() = _binding!!
    private lateinit var clasesService: ApiMaxiEscuela
    private val listaClases = mutableListOf<Clase>()
    private lateinit var claseAdapter: ClaseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClasesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        claseAdapter = ClaseAdapter(
            listaClases,
            onEditarClick = { clase -> editarClase(clase) },
            onDetallesClick = { clase -> mostrarDetalles(clase) }
        )

        binding.recyclerViewClases.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = claseAdapter
        }
        clasesService = RetrofitHelper.GetRetrofit().create(ApiMaxiEscuela::class.java)

        obtenerClases()
    }

    private fun obtenerClases() {
        binding.progressBarClases.visibility = View.VISIBLE

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val clases = clasesService.listarClase()
                withContext(Dispatchers.Main) {
                    actualizarLista(clases)
                    binding.progressBarClases.visibility = View.GONE
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

    private fun actualizarLista(nuevaLista: List<Clase>) {
        listaClases.clear()
        listaClases.addAll(nuevaLista)
        claseAdapter.notifyDataSetChanged()
    }

    private fun editarClase(clase: Clase) {
        Toast.makeText(requireContext(), "Editar: ${clase.nombre}", Toast.LENGTH_SHORT).show()
        // TODO: abrir diálogo o actividad para editar
    }

    private fun mostrarDetalles(clase: Clase) {
        Toast.makeText(requireContext(), "Detalles de: ${clase.nombre}", Toast.LENGTH_SHORT).show()
        // TODO: abrir diálogo o navegación con detalles
    }

    private suspend fun mostrarError(mensaje: String) {
        withContext(Dispatchers.Main) {
            binding.progressBarClases.visibility = View.GONE
            if (isAdded) {
                Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()
            }
            Log.e("ClasesFragment", mensaje)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}