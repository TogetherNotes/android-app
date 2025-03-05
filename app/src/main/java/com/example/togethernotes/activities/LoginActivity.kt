package com.example.togethernotes.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.togethernotes.AccountFragment
import com.example.togethernotes.MainActivity
import com.example.togethernotes.R
import com.example.togethernotes.models.User
import com.example.togethernotes.tools.Tools
import com.example.togethernotes.tools.actualUser

class LoginActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        val mailLogin = findViewById<EditText>(R.id.mailLogin)
        val passwordLogin = findViewById<EditText>(R.id.passwordLogin)

        if (mailLogin!= null && passwordLogin!=null){
            val continueButton = findViewById(R.id.login_button) as ImageView
            Tools.startActivity(continueButton,this,MainActivity::class.java)
        }
    }
}
