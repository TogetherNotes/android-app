package com.example.togethernotes.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.togethernotes.R
import com.example.togethernotes.tools.Tools

class RoleTypeActivity : AppCompatActivity()
{
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
            selectedRole?.alpha = 0.5f

            view.alpha = 1.0f

            selectedRole = view as ImageView
        }

        // Asignar listeners a los botones de rol
        artistRoleButton.setOnClickListener(imageClickListener)
        spaceRoleButton.setOnClickListener(imageClickListener)

        // Configurar el listener del botón "Continuar"
        startActivities(artistRoleButton, spaceRoleButton)

    }

    private fun startActivities(artist: View, space: View) {
        val continueButton = findViewById<View>(R.id.continue_rol_button)
        continueButton.setOnClickListener {
            if (selectedRole == artist) {
                Tools.startActivity(continueButton, this, CreateArtistActivity::class.java)
            }

            else if (selectedRole == space) {
                Tools.startActivity(continueButton, this, CreateSpaceActivity::class.java)
            }
        }
    }
}