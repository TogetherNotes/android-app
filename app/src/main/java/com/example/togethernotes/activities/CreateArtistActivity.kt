package com.example.togethernotes.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.togethernotes.R

class CreateArtistActivity : AppCompatActivity() {
    private lateinit var artistMail: EditText
    private lateinit var artistPassword: EditText
    private lateinit var artistConfPassword: EditText
    private lateinit var genre: EditText
    private lateinit var continueButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.create_artist)
        initVar()
        registerControl()

    }
    fun initVar()
    {
        // Asignar los EditText a variables
        artistMail = findViewById(R.id.artistMail) as EditText// Campo Correu
        artistPassword = findViewById(R.id.artistPassword) as EditText/// Campo Contrasenya
        artistConfPassword = findViewById(R.id.artistConfPassword) as EditText // Campo Confirmar Contrasenya
        genre = findViewById(R.id.genre) as EditText/// Campo Génere músical
        continueButton = findViewById(R.id.contine_button) as ImageView
        // Variable para almacenar mensajes de error
    }
    fun registerControl(){
        var errorMessage = ""
        continueButton.setOnClickListener{
            // Validación de campos vacíos
            if (artistMail.text.isEmpty()) {
                errorMessage += "El campo de correo electrónico no puede estar vacío.\n"
            }
            if (artistPassword.text.isEmpty()) {
                errorMessage += "El campo de contraseña no puede estar vacío.\n"
            }
            if (artistConfPassword.text.isEmpty()) {
                errorMessage += "El campo de confirmación de contraseña no puede estar vacío.\n"
            }
            if (genre.text.isEmpty()) {
                errorMessage += "El campo de género musical no puede estar vacío.\n"
            }

            // Validación de coincidencia de contraseñas
            if (artistPassword != artistConfPassword) {
                errorMessage += "Las contraseñas no coinciden.\n"
            }

            // Mostrar el mensaje de error si hay alguno
            if (errorMessage.isNotEmpty()) {
                // Muestra un Toast o un AlertDialog con el mensaje de error
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            } else {
                // Si todo está correcto, procede con la lógica del registro
                Toast.makeText(this, "Todos los campos son válidos", Toast.LENGTH_SHORT).show()
            }
            errorMessage =""
        }
    }
}