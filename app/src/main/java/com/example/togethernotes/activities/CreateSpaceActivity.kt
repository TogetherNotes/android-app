package com.example.togethernotes.activities

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.togethernotes.R

class CreateSpaceActivity : AppCompatActivity() {

    private lateinit var spaceMail: EditText
    private lateinit var spacePassword: EditText
    private lateinit var spaceConfPassword: EditText
    private lateinit var capacity: EditText
    private lateinit var zipCode: EditText
    private lateinit var continueButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.create_space)

        initVar()
        registerControl()

    }
    fun initVar() {
        spaceMail = findViewById(R.id.spaceMail) // Campo de correo electrónico
        spacePassword = findViewById(R.id.spacePassword) // Campo de contraseña
        spaceConfPassword =
            findViewById(R.id.spaceConfPassword) // Campo de confirmación de contraseña
        capacity = findViewById(R.id.capacity) // Campo de capacidad
        zipCode = findViewById(R.id.zipCode) // Campo de ubicación (código postal)
        continueButton =
            findViewById(R.id.contine_restaurant_button) as ImageView // Botón CREAR COMPTE
    }

    /**
     * Controla la lógica de validación y registro.
     */
    fun registerControl() {
        var errorMessage = ""

        continueButton.setOnClickListener {
            // Validación de campos vacíos
            if (spaceMail.text.isEmpty()) {
                errorMessage += "El campo de correo electrónico no puede estar vacío.\n"
            }
            if (spacePassword.text.isEmpty()) {
                errorMessage += "El campo de contraseña no puede estar vacío.\n"
            }
            if (spaceConfPassword.text.isEmpty()) {
                errorMessage += "El campo de confirmación de contraseña no puede estar vacío.\n"
            }
            if (capacity.text.isEmpty()) {
                errorMessage += "El campo de capacidad no puede estar vacío.\n"
            }
            if (zipCode.text.isEmpty()) {
                errorMessage += "El campo de código postal no puede estar vacío.\n"
            }

            // Validación de coincidencia de contraseñas
            if (spacePassword.text.toString() != spaceConfPassword.text.toString()) {
                errorMessage += "Las contraseñas no coinciden.\n"
            }

            // Mostrar el mensaje de error si hay alguno
            if (errorMessage.isNotEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            } else {
                // Si todo está correcto, procede con la lógica del registro
                Toast.makeText(this, "Todos los campos son válidos", Toast.LENGTH_SHORT).show()
            }

            // Reiniciar el mensaje de error
            errorMessage = ""
        }
    }
}
