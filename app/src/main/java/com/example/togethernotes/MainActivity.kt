package com.example.togethernotes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var navigation: BottomNavigationView
    private var currentFragmentIndex = 0 // Índice del fragment actual (0 por defecto)

    private val mOnNavMenu = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val newFragmentIndex = when (item.itemId) {
            R.id.itemFragment1 -> 0
            R.id.itemFragment2 -> 1
            R.id.itemFragment3 -> 2
            R.id.itemFragment4 -> 3
            else -> return@OnNavigationItemSelectedListener false
        }

        // Verificar si el fragmento seleccionado es el mismo que el actual
        if (newFragmentIndex == currentFragmentIndex) {
            // No hacer nada si el fragmento no cambia
            return@OnNavigationItemSelectedListener true
        }

        val isMovingRight = newFragmentIndex > currentFragmentIndex // Detectar si nos movemos a la derecha

        // Actualizar el índice del fragmento actual
        currentFragmentIndex = newFragmentIndex

        // Realizar la transacción del fragmento con animaciones
        supportFragmentManager.commit {
            setCustomAnimations(
                if (isMovingRight) R.anim.slide_in_right else R.anim.slide_in_left, // Animación de entrada
                if (isMovingRight) R.anim.slide_out_left else R.anim.slide_out_right // Animación de salida
            )
            when (newFragmentIndex) {
                0 -> replace<MatchFragment>(R.id.fragmentContainerView)
                1 -> replace<ChatFragment>(R.id.fragmentContainerView)
                2 -> replace<calendarFragment>(R.id.fragmentContainerView)
                3 -> replace<AccountFragment>(R.id.fragmentContainerView)
            }
            setReorderingAllowed(true)
            addToBackStack("replacement")
        }

        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_menu_activity)

        navigation = findViewById(R.id.navMenu)
        navigation.setOnNavigationItemSelectedListener(mOnNavMenu)

        // Cargar el fragment inicial con animación
        supportFragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_in_right,  // Animación de entrada
                R.anim.slide_out_left   // Animación de salida
                               )
            replace<MatchFragment>(R.id.fragmentContainerView)
            setReorderingAllowed(true)
            addToBackStack("replacement")
        }
    }
}