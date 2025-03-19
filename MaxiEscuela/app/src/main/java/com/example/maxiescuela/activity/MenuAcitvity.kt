package com.example.maxiescuela.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.maxiescuela.R
import com.example.maxiescuela.databinding.ActivityMenuAcitvityBinding
import com.google.android.material.navigation.NavigationView

class MenuAcitvity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuAcitvityBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar View Binding
        binding = ActivityMenuAcitvityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar Toolbar
        setSupportActionBar(binding.toolbar)

        // Configurar Navigation Drawer
        val drawerLayout = binding.drawerLayout
        val navigationView: NavigationView = binding.navView

        // Configurar el botón para abrir/cerrar el menú
        toggle = ActionBarDrawerToggle(this, drawerLayout, binding.toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Habilitar el botón en la barra de herramientas
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.framLayout, fragment) // Asegúrate de que R.id.content_frame es el contenedor de los fragments en tu layout
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}