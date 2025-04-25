package com.example.togethernotes.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.togethernotes.MainActivity
import com.example.togethernotes.R
import com.example.togethernotes.models.App
import com.example.togethernotes.tools.Tools
import androidx.lifecycle.lifecycleScope
import com.example.togethernotes.repository.AppRepository
import com.example.togethernotes.tools.actualApp
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    var passApp =App()
    var credentailsCorrect = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        val mailLogin = findViewById<EditText>(R.id.mailLogin)
        val passwordLogin = findViewById<EditText>(R.id.passwordLogin)

        val continueButton = findViewById(R.id.login_button) as ImageView
        continueButton.setOnClickListener {
            if (mailLogin.text.toString()!= "" && passwordLogin.text.toString()!="")
            {
                val mail =mailLogin.text.toString()
                val password= passwordLogin.text.toString()
                login(mail, password)
                if (credentailsCorrect == false)
                {
                    mailLogin.text.clear()
                    passwordLogin.text.clear()
                }
            }
            else {
             //   Toast.makeText(this@LoginActivity, "MALLLL", Toast.LENGTH_LONG).show()

            }
    //                  actualApp.mail = mailLogin.text.toString()

        }
            //TODO cambiar el usuario por la lógica de encontrarlo en la base de datos
        }
    fun login(mail: String, password: String) {
        val appRepository = AppRepository()

        lifecycleScope.launch {
            try {
                val response = appRepository.login(mail,password)

                when {
                    response.isSuccessful -> {
                        response.body()?.let { loggedInApp ->
                            actualApp = loggedInApp
                            credentailsCorrect = true
                            showUserInfo(loggedInApp)
                            Tools.startActivityTurned(this@LoginActivity, MainActivity::class.java)
                        } ?: run {
                            showError("Respuesta vacía del servidor")
                        }
                    }
                    response.code() == 404 -> {
                        Toast.makeText(this@LoginActivity, "Credenciales inválidas", Toast.LENGTH_LONG).show()

                    }
                    else -> {
                        showError("Error ${response.code()}: ${response.message()}")
                    }
                }
            } catch (e: Exception) {

                showError("Error inesperado: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun showUserInfo(app: App) {
        val userInfo = """
            Nombre: ${app.name}
            Correo: ${app.mail}
            Rol: ${app.role}
            Activo: ${app.active}
            ID: ${app.id}
        """.trimIndent()

    //    Toast.makeText(this, userInfo, Toast.LENGTH_LONG).show()
    }

    private fun showError(message: String) {
        //Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}
