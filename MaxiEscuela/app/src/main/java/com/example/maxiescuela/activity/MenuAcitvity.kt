package com.example.maxiescuela.activity

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.maxiescuela.R
import com.example.maxiescuela.databinding.ActivityMenuAcitvityBinding
import com.example.maxiescuela.domain.usuarioModel
import com.google.android.material.navigation.NavigationView

class MenuAcitvity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuAcitvityBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuAcitvityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // Crear el objeto de usuario con el rol correcto
        val usuario = usuarioModel(id_usuario = 0, username = "Juan", email = "juan.perez@gmail.com", contraseña = "12345", 3)

        // Configurar menú según el rol del usuario
        configurarMenu(usuario.id_rol)

        val drawerLayout = binding.drawerLayout
        val navigationView: NavigationView = binding.navView

        // Configurar el toggle para el DrawerLayout
        toggle = ActionBarDrawerToggle(this, drawerLayout, binding.toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Configurar listener para las opciones de menú
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.nav_home -> loadFragment(Inicio())
                R.id.nav_usuarios -> loadFragment(UsuariosFragment())
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Si no hay fragmento restaurado, se carga el fragmento de inicio
        if (savedInstanceState == null) {
            loadFragment(Inicio())
        }
    }

    private fun configurarMenu(id: Int) {
        val navView = binding.navView
        val menu: Menu = navView.menu

        // Menú exclusivo para ADMIN
        menu.findItem(R.id.nav_home)?.isVisible = id == 3
        menu.findItem(R.id.nav_usuarios)?.isVisible = id == 3 || id == 2 // Admin o Profesor
        menu.findItem(R.id.nav_profesores)?.isVisible = id == 3

        // Menú exclusivo para ESTUDIANTE
        menu.findItem(R.id.nav_asistencia)?.isVisible = id == 1
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.framLayout, fragment) // Asegúrate de que R.id.framLayout sea el contenedor de los fragments
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
