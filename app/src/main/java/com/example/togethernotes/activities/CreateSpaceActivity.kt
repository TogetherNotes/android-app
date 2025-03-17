package com.example.togethernotes.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.togethernotes.MainActivity
import com.example.togethernotes.R
import com.example.togethernotes.adapters.GenresAdapter
import com.example.togethernotes.models.Genres
import com.example.togethernotes.tools.Tools

class CreateSpaceActivity : AppCompatActivity() {

    private lateinit var spaceMail: EditText
    private lateinit var spacePassword: EditText
    private lateinit var spaceConfPassword: EditText
    private lateinit var capacity: EditText
    private lateinit var zipCode: EditText
    private lateinit var name: EditText
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
        spaceConfPassword = findViewById(R.id.spaceConfPassword) // Campo de confirmación de contraseña
        capacity = findViewById(R.id.capacity) // Campo de capacidad
        zipCode = findViewById(R.id.zipCode) // Campo de ubicación (código postal)
        name = findViewById(R.id.spaceName)
        continueButton = findViewById(R.id.contine_restaurant_button) as ImageView // Botón CREAR COMPTE
    }



    /**
     * Controla la lógica de validación y registro.
     */
    fun registerControl() {
        var errorMessage = ""

        continueButton.setOnClickListener {
            // Validación de campos vacíos
            if (spaceMail.text.isEmpty())
            {
                errorMessage += getString(R.string.mailEmpty)
            }

            if (spacePassword.text.isEmpty())
            {
                errorMessage += getString(R.string.passwordEmpty)
            }

            if (spaceConfPassword.text.isEmpty())
            {
                errorMessage += getString(R.string.passwordConfEmpty)
            }

            if (capacity.text.isEmpty())
            {
                errorMessage += getString(R.string.capacityEmpty)
            }

            if (zipCode.text.isEmpty())
            {
                errorMessage += getString(R.string.zipEmpty)
            }

            // Validación de coincidencia de contraseñas
            if (spacePassword.text.toString() != spaceConfPassword.text.toString())
            {
                errorMessage += getString(R.string.passwordMatchEmpty)
            }

            if (name.text.isEmpty())
            {
                errorMessage += getString(R.string.nameEmpty)
            }
            // Mostrar el mensaje de error si hay alguno
            if (errorMessage.isNotEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()

            } else
            {
                Tools.createUser("Space",spaceMail.text.toString(), spacePassword.text.toString(), name.text.toString(),zipCode.text.toString().toInt(),capacity.text.toString().toInt())
                Tools.startActivity(continueButton, this, MainActivity::class.java)
            }
            // Reiniciar el mensaje de error
            errorMessage = ""
        }

    }
}
