
package com.example.maxiescuela.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.maxiescuela.databinding.FragmentInformacionUsarioDialogBinding
import com.example.maxiescuela.domain.Grado
import com.example.maxiescuela.domain.Informacion
import com.example.maxiescuela.domain.UsuarioCompleto
import com.example.maxiescuela.repository.ApiMaxiEscuela
import com.example.maxiescuela.repository.RetrofitHelper
import java.util.Calendar


class InformacionUsarioDialogFragment(  private var usuarioCompleto: UsuarioCompleto? = null,private val listener: oninfoagregadaListener): DialogFragment() {

    // Declaración del binding
    private var _binding: FragmentInformacionUsarioDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiService: ApiMaxiEscuela

    interface  oninfoagregadaListener{
        fun onInfoagregada(informacion: Informacion)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout usando ViewBinding
        _binding = FragmentInformacionUsarioDialogBinding.inflate(inflater, container, false)
        apiService= RetrofitHelper.GetRetrofit().create(ApiMaxiEscuela::class.java)
        // Configuración de los botones y campos
        val btnGuardar = binding.btnGuardar
        val btnCancelar = binding.btnCancelar
        usuarioCompleto?.let {
            binding.editNombres.setText(it.nombres ?: "")
            binding.editApPaterno.setText(it.ap_paterno ?: "")
            binding.editApMaterno.setText(it.ap_materno ?: "")
            binding.editDireccion.setText(it.direccion ?: "")
            binding.editDni.setText(it.dni ?: "")
            binding.editCelular.setText(it.celular ?: "")
            binding.editFechaNacimiento.setText(it.fecha_nacimiento ?: "")

            if (!it.nombres.isNullOrEmpty()) {
                binding.btnGuardar.visibility = View.GONE
            }
        }

        binding.editFechaNacimiento.setOnClickListener {
            showDatePickerDialog { year, month, day ->
                val fechaSeleccionada = String.format("%04d-%02d-%02d", year, month , day)
                binding.editFechaNacimiento.setText(fechaSeleccionada)
            }
        }
        val grados = listOf(
            Grado(1, "1° Secundaria"),
            Grado(2, "2° Secundaria"),
            Grado(3, "3° Secundaria"),
            Grado(4, "4° Secundaria"),
            Grado(5, "5° Secundaria")
        )
        val nombresGrado = grados.map { it.nombre }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, nombresGrado)
        binding.spinnerGrado.setAdapter(adapter)

        btnGuardar.setOnClickListener {
            // Obtener los datos desde los campos con binding
            val nombres = binding.editNombres.text.toString()
            val apPaterno = binding.editApPaterno.text.toString()
            val apMaterno = binding.editApMaterno.text.toString()
            val direccion = binding.editDireccion.text.toString()
            val dni = binding.editDni.text.toString()
            val celular = binding.editCelular.text.toString()
            val fechaNacimiento = binding.editFechaNacimiento.text.toString()
            val usuarioId = arguments?.getInt("usuario_id")
            Log.e("id","id usuario:${usuarioId}")

            // Obtener el grado seleccionado
            val nombreGradoSeleccionado = binding.spinnerGrado.selectedItem.toString()
            val gradoSeleccionado = grados.find { it.nombre == nombreGradoSeleccionado }

            if (gradoSeleccionado == null) {
                Toast.makeText(requireContext(), "Por favor, selecciona un grado válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Crear el objeto de datos a enviar
            val informacion = Informacion(
                id_usuario = usuarioId?.toInt() ?: 0,
                nombres = nombres,
                ap_paterno = apPaterno,
                ap_materno = apMaterno,
                direccion = direccion,
                dni = dni,
                celular = celular,
                fecha_nacimiento = fechaNacimiento,
                grado = gradoSeleccionado.id
            )
            Log.e("Info","${informacion}")

            // Llamar a la función para agregar la información en la API
            listener.onInfoagregada(informacion)
            dismiss()
        }

        btnCancelar.setOnClickListener {
            dismiss()  // Cerrar el modal si se cancela
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Limpiar el binding cuando la vista sea destruida
    }


    private fun showDatePickerDialog(onDateSetListener: (Int, Int, Int) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Ajustar el mes para que empiece desde 1 en lugar de 0
                onDateSetListener(selectedYear, selectedMonth + 1, selectedDay)
            },
            year, month, day
        )

        datePickerDialog.show()
    }
}
