package com.example.maxiescuela.activity

import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.maxiescuela.R
import com.example.maxiescuela.domain.Rol
import com.example.maxiescuela.domain.usuarioModel

class AgregarUsuarioFragment(private val listener: OnusuaarioAgragadoListener) : DialogFragment() {

    interface OnusuaarioAgragadoListener {
        fun onAlumnoAgregado(nuevoAlumno: usuarioModel)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_agregar_alumno, null)

        val editNombre = view.findViewById<EditText>(R.id.editNombre)
        val editEmail = view.findViewById<EditText>(R.id.editEmail)
        val editContra = view.findViewById<EditText>(R.id.editContra)
        val spinnerRol = view.findViewById<Spinner>(R.id.spinnerRol)

        // Configurar Spinner con los roles
        val roles = arrayOf(Rol(3, "Administrador"), Rol(2, "Profesor"), Rol(1, "Estudiante"))
        val roleNames = roles.map { it.nombre }.toTypedArray()

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, roleNames)
        spinnerRol.adapter = adapter

        builder.setView(view)
            .setTitle("Agregar Alumno")
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = editNombre.text.toString().trim()
                val email = editEmail.text.toString().trim()
                val contra = editContra.text.toString().trim()

                if (nombre.isEmpty() || email.isEmpty() || contra.isEmpty()) {
                    Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val rolIndex = spinnerRol.selectedItemPosition
                val rol = roles[rolIndex].id

                val nuevoAlumno = usuarioModel(id_usuario = 0, username = nombre, email = email, contraseña = contra, id_rol = rol)
                listener.onAlumnoAgregado(nuevoAlumno)
            }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }

        return builder.create()
    }
}