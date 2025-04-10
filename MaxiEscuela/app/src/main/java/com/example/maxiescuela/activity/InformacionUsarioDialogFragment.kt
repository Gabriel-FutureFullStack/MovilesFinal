
package com.example.maxiescuela.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.maxiescuela.R
import com.example.maxiescuela.databinding.FragmentInformacionUsarioDialogBinding
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.maxiescuela.domain.Informacion
import com.example.maxiescuela.repository.ApiMaxiEscuela
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar


class InformacionUsarioDialogFragment : DialogFragment() {

    // Declaración del binding
    private var _binding: FragmentInformacionUsarioDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiService: ApiMaxiEscuela


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout usando ViewBinding
        _binding = FragmentInformacionUsarioDialogBinding.inflate(inflater, container, false)

        // Configuración de los botones y campos
        val btnGuardar = binding.btnGuardar
        val btnCancelar = binding.btnCancelar

        binding.editFechaRegistro.setOnClickListener {
            showDatePickerDialog { year, month, day ->
                // Aumentar 1 al mes ya que en DatePicker el mes comienza desde 0
                val fechaSeleccionada = String.format("%04d-%02d-%02d", year, month , day)
                binding.editFechaRegistro.setText(fechaSeleccionada)
            }
        }
        binding.editFechaNacimiento.setOnClickListener {
            showDatePickerDialog { year, month, day ->
                val fechaSeleccionada = String.format("%04d-%02d-%02d", year, month , day)
                binding.editFechaNacimiento.setText(fechaSeleccionada)
            }
        }
        btnGuardar.setOnClickListener {
            // Obtener los datos desde los campos con binding
            val nombres = binding.editNombres.text.toString()
            val apPaterno = binding.editApPaterno.text.toString()
            val apMaterno = binding.editApMaterno.text.toString()
            val direccion = binding.editDireccion.text.toString()
            val dni = binding.editDni.text.toString()
            val celular = binding.editCelular.text.toString()
            val fechaNacimiento = binding.editFechaNacimiento.text.toString()
            val fechaRegistro = binding.editFechaRegistro.text.toString()
            val usuarioId = arguments?.getInt("usuario_id")
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
                fecha_registro = fechaRegistro
            )

            // Llamar a la función para agregar la información en la API
            agregarInformacion(informacion)
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

    private fun agregarInformacion(informacion: Informacion) {
        // Usar coroutine para hacer la llamada a la API de manera asincrónica
        // Con Retrofit, necesitas un scope adecuado para realizar esta operación
        // Si estás usando un fragmento o actividad que tiene un ViewModel, se recomienda usar el ViewModel para hacer la llamada a la API

        // Iniciar una corutina para llamar a la API
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.agregarInformacion(informacion)
                }

                if (response.isSuccessful) {
                    // Si la respuesta es exitosa, mostrar un mensaje de éxito
                    Toast.makeText(context, "Información agregada con éxito", Toast.LENGTH_SHORT).show()
                    dismiss()
                } else {
                    // Si la respuesta falla, mostrar un mensaje de error
                    Toast.makeText(context, "Error al agregar la información", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Manejar cualquier error de la red o la API
                Toast.makeText(context, "Error en la conexión: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
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
