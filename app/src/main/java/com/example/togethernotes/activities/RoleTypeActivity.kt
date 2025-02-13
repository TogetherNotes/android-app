package com.example.togethernotes.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.togethernotes.R

class RoleTypeActivity : AppCompatActivity() {

    private var selectedRole: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.user_type_activity) // Asegúrate de que este layout tenga los ImageView correctos
        roleSelectedOpacity()
    }

    private fun roleSelectedOpacity() {
        // Obtener las vistas usando el método genérico de findViewById
        val artistRoleButton = findViewById<ImageView>(R.id.artistRegister)
        val spaceRoleButton = findViewById<ImageView>(R.id.spaceRegister)

        val imageClickListener = View.OnClickListener { view ->
            selectedRole?.alpha = 1.0f // Restaurar opacidad de la imagen anterior
            view.alpha = 0.5f // Oscurecer la nueva imagen
            selectedRole = view as ImageView
        }

        artistRoleButton.setOnClickListener(imageClickListener)
        spaceRoleButton.setOnClickListener(imageClickListener)
    }
}