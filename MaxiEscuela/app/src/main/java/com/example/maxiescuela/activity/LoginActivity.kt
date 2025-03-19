package com.example.maxiescuela.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.maxiescuela.R
import com.example.maxiescuela.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var viewbinding: ActivityLoginBinding
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
        val boton = viewbinding.buttonIngresar;
        val animationButton = boton.background as? android.graphics.drawable.AnimationDrawable
        animationButton?.setEnterFadeDuration(500)
        animationButton?.setExitFadeDuration(500)
        animationButton?.start()
        viewbinding.buttonIngresar.setOnClickListener {
            val user = viewbinding.editTextText.text.toString()
            val pass = viewbinding.editTextTextPassword.text.toString()
            if (user == "admin" && pass == "admin") {
                startActivity(Intent(this, MenuAcitvity::class.java))
            }
        }

    }
}