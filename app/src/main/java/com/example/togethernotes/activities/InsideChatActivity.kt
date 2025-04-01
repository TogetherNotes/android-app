package com.example.togethernotes.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.togethernotes.R
import com.example.togethernotes.tools.Tools

class InsideChatActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.inside_chat_layout) // Asegúrate de que este layout tenga los ImageView correctos
    }
}