package com.example.togethernotes.activities

import Message
import MessageAdapter
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.togethernotes.R
import com.example.togethernotes.tools.Tools
import com.example.togethernotes.tools.actualApp
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

class InsideChatActivity : AppCompatActivity()
{
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.inside_chat_layout)
        createEventSol()
        showMessages()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showMessages() {
        var chatRecyclerView = findViewById(R.id.recyclerView) as RecyclerView
        var adapter: MessageAdapter
        val localDateTime = LocalDateTime.of(2025, 6, 24, 4, 0) // Año, Mes, Día, Hora, Minuto
        val messageDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
        // Lista de mensajes
        actualApp.id=1
        val messages = listOf(
            Message(1,1,"Hola, ¿cómo estáswasddasasdsdaasdsdasdasdsda?", messageDate, false,2),
            Message(2,2,"Hola, ¿cómo estás?", messageDate, false,2),
            Message(3,1,"Hola, ¿cómo estás?", messageDate, false,2),
            Message(1,1,"Hola, ¿cómo estáswasddasasdsdaasdsdasdasdsda?", messageDate, false,2),
            Message(2,2,"Hola, ¿cómo estás?", messageDate, false,2),
            Message(3,1,"Hola, ¿cómo estás?", messageDate, false,2),
            Message(1,1,"Hola, ¿cómo estáswasddasasdsdaasdsdasdasdsda?", messageDate, false,2),
            Message(2,2,"Hola, ¿cómo estás?", messageDate, false,2),
            Message(3,1,"Hola, ¿cómo estás?", messageDate, false,2),
            Message(1,1,"Hola, ¿cómo estáswasddasasdsdaasdsdasdasdsda?", messageDate, false,2),
            Message(2,2,"Hola, ¿cómo estás?", messageDate, false,2),
            Message(3,1,"Hola, ¿cómo estás?", messageDate, false,2),
            Message(1,1,"Hola, ¿cómo estáswasddasasdsdaasdsdasdasdsda?", messageDate, false,2),
            Message(2,2,"Hola, ¿cómo estás?", messageDate, false,2),
            Message(3,1,"Hola, ¿cómo estás?", messageDate, false,2),
                             )

        // Configurar el adaptador
        adapter = MessageAdapter(messages)

        // Configurar el RecyclerView
        chatRecyclerView.layoutManager = LinearLayoutManager(this)

        chatRecyclerView.adapter = adapter
    }

    private fun createEventSol() {
        var solEventButton = findViewById(R.id.createEventButton) as ImageView
        var solEventLayout = findViewById(R.id.createEventSol) as FrameLayout
        solEventButton.setOnClickListener{
            solEventLayout.visibility  = View.VISIBLE
        }
    }
// Asegúrate de que este layout tenga los ImageView correctos
    }
