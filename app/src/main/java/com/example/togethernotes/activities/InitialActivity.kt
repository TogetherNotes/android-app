package com.example.togethernotes.activities

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.togethernotes.R
import com.example.togethernotes.tools.Tools

class InitialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        startActivities()


    }
    fun startActivities(){
        val createAccountButton = findViewById(R.id.button_crate_account) as ImageView

        createAccountButton.setOnClickListener(){
            Tools.startActivity(this, RoleTypeActivity::class.java)
        }

        val loginAccountButton = findViewById(R.id.sign_up_button) as ImageView
        loginAccountButton.setOnClickListener(){
            Tools.startActivity(this, LoginActivity::class.java)
        }
    }
}
