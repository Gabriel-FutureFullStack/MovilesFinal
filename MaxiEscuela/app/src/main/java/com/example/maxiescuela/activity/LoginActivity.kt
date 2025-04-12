package com.example.maxiescuela.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.maxiescuela.R
import com.example.maxiescuela.databinding.ActivityLoginBinding
import com.example.maxiescuela.domain.LoginRequest
import com.example.maxiescuela.domain.usuarioModel
import com.example.maxiescuela.repository.ApiMaxiEscuela
import com.example.maxiescuela.repository.RetrofitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var viewbinding: ActivityLoginBinding;
    private lateinit var usuarioService: ApiMaxiEscuela;

    override fun onCreate(savedInstanceState: Bundle?) {
        viewbinding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(viewbinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        usuarioService = RetrofitHelper.GetRetrofit().create(ApiMaxiEscuela::class.java)

        val boton = viewbinding.buttonIngresar;
        val animationButton = boton.background as? android.graphics.drawable.AnimationDrawable
        animationButton?.setEnterFadeDuration(500)
        animationButton?.setExitFadeDuration(500)
        animationButton?.start()

        viewbinding.buttonIngresar.setOnClickListener {
            val user = viewbinding.editTextText.text.toString()
            val pass = viewbinding.editTextTextPassword.text.toString()
            lifecycleScope.launch {
                try {
                    val listaUsuarios = usuarioService.obtenerUsuarios()

                    val usuarioEncontrado = listaUsuarios.find {
                        it.username == user && it.contraseña == pass
                    }

                    if (usuarioEncontrado != null) {
                        intent = Intent(this@LoginActivity, MenuAcitvity::class.java)
                        startActivity(intent)
                        Log.d("Login", "Bienvenido ${usuarioEncontrado.username}")
                    } else {
                        Toast.makeText(this@LoginActivity, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: Exception) {
                    Log.e("Login", "Error al obtener usuarios", e)
                    Toast.makeText(this@LoginActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewbinding.buttonRegistrarse.setOnClickListener {
            val dialog = AgregarUsuarioFragment(object : AgregarUsuarioFragment.OnusuaarioAgragadoListener {
                override fun onAlumnoAgregado(newUser: usuarioModel) {
                    agregarUsuario(newUser)
                }
            })
            dialog.show(supportFragmentManager, "AgregarAlumnoDialog")
        }

    }

    private fun agregarUsuario(newUser: usuarioModel) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = usuarioService.crearUsuario(newUser)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@LoginActivity, "Usuario agregado exitosamente", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@LoginActivity, "Error al agregar usuario", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


