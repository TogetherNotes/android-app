package com.example.togethernotes.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.togethernotes.R

class LoginActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        val correuLogin = findViewById<EditText>(R.id.mailLogin)
        if (correuLogin!0){

        }
    }
}
