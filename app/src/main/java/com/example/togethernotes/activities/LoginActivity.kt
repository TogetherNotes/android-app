package com.example.togethernotes.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        val mailLogin = findViewById<EditText>(R.id.mailLogin)
        val passwordLogin = findViewById<EditText>(R.id.passwordLogin)


        val continueButton = findViewById(R.id.login_button) as ImageView
        actualApp = App()
        continueButton.setOnClickListener {
            if (mailLogin.text.toString()!= "" && passwordLogin.text.toString()!="")
            {
                actualApp.mail ="artist1@domain.com"
                actualApp.password = "artist1password"
                actualApp.active = true

                login(actualApp)
                Tools.startActivityTurned(this, MainActivity::class.java)
            }
            else {
                Toast.makeText(this@LoginActivity, "MALLLL", Toast.LENGTH_LONG).show()

            }
    //                actualApp.mail = mailLogin.text.toString()

        }
            //TODO cambiar el usuario por la lógica de encontrarlo en la base de datos
        }
        fun login(creds: App) {
            val appRepository = AppRepository()
            val _loginResponse = MutableLiveData<App>()

            lifecycleScope.launch {
                try {
                    val response = appRepository.login(creds)
                    if (response.isSuccessful) {
                        _loginResponse.value = response.body()
                        actualApp.active = response.body()?.active!!
                        actualApp.id = response.body()?.id!!
                        actualApp.role = response.body()?.role.toString()
                        actualApp.name  = response.body()?.name.toString()
                        actualApp.latitude=5.5
                        actualApp.language_id=1
                        actualApp.rating=1
                        actualApp.longitude=1.1
                        val userInfo = """
                            Nombre: ${actualApp.name}
                            Correo: ${actualApp.mail}
                            Rol: ${actualApp.role}
                            Activo: ${actualApp.active}
                            ID: ${actualApp.id}
                        """.trimIndent()
                        Toast.makeText(this@LoginActivity, userInfo, Toast.LENGTH_LONG).show()
                    } else {

                        Toast.makeText(this@LoginActivity, "Credenciales inválidas", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@LoginActivity, "Error logging in:", Toast.LENGTH_LONG).show()

                }
            }
    }
}
