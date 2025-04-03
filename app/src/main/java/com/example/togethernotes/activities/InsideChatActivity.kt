package com.example.togethernotes.activities

import Message
import MessageAdapter
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.togethernotes.R
import com.example.togethernotes.tools.actualApp
import org.json.JSONObject
import java.io.*
import java.net.Socket
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.concurrent.thread

class InsideChatActivity : AppCompatActivity() {
    private lateinit var socket: Socket
    private lateinit var outputStream: PrintWriter
    private lateinit var inputStream: BufferedReader
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var adapter: MessageAdapter
    private lateinit var messageInput: EditText
    private lateinit var rootLayout: LinearLayout // Nuevo: Contenedor principal
    private var messages = mutableListOf<Message>()
    private var chatId: Int = 1
    private val serverIp = "10.0.0.99"
    private val serverPort = 5000

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.inside_chat_layout)

        rootLayout = findViewById(R.id.rootLayout) // ID del contenedor principal
        chatRecyclerView = findViewById(R.id.recyclerView)
        messageInput = findViewById(R.id.messageInput)
        val sendButton = findViewById<Button>(R.id.sendButton)

        val solEventButton = findViewById<ImageView>(R.id.createEventButton)
        val solEventLayout = findViewById<FrameLayout>(R.id.createEventSol)

        // Configurar RecyclerView
        adapter = MessageAdapter(messages)
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = adapter

        // Conectar al servidor en un hilo separado
        thread {
            connectToServer()
        }

        sendButton.setOnClickListener { sendMessage() }
        solEventButton.setOnClickListener { solEventLayout.visibility = View.VISIBLE }

        // Ajustar layout cuando aparece el teclado
        setupKeyboardListener()
    }

    /**
     * Detecta cuando el teclado aparece y ajusta el layout
     */
    private fun setupKeyboardListener() {
        rootLayout.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            rootLayout.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootLayout.height
            val keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > screenHeight * 0.15) {
                // Si el teclado está visible, subimos la vista
                rootLayout.translationY = -keypadHeight.toFloat()
            } else {
                // Si el teclado está oculto, restauramos la posición
                rootLayout.translationY = 0f
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun connectToServer() {
        try {
            socket = Socket(serverIp, serverPort)
            outputStream = PrintWriter(socket.getOutputStream(), true)
            inputStream = BufferedReader(InputStreamReader(socket.getInputStream()))
            Log.d("SOCKET", "Conectado al servidor")

            // Enviar mensaje de autenticación
            val authJson = JSONObject()
            authJson.put("type", "auth")
            authJson.put("userId", actualApp.id)
            outputStream.println(authJson.toString())

            // Iniciar recepción de mensajes
            receiveMessages()
        } catch (e: Exception) {
            Log.e("SOCKET", "Error al conectar: ${e.message}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun receiveMessages() {
        try {
            while (true) {
                val receivedMessage = inputStream.readLine()
                if (receivedMessage != null) {
                    val json = JSONObject(receivedMessage)
                    val senderId = json.getInt("from")
                    val content = json.getString("content")

                    val localDateTime = LocalDateTime.now()
                    val messageDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())

                    val newMessage = Message(
                        id = messages.size + 1,
                        senderId = senderId,
                        content = content,
                        sendAt = messageDate,
                        isRead = false,
                        chatId = chatId
                                            )

                    runOnUiThread {
                        messages.add(newMessage)
                        adapter.notifyItemInserted(messages.size - 1)
                        chatRecyclerView.scrollToPosition(messages.size - 1)
                    }
                }
            }
        } catch (e: IOException) {
            Log.e("SOCKET", "Error recibiendo mensaje: ${e.message}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendMessage() {
        val messageText = messageInput.text.toString().trim()
        if (messageText.isEmpty()) return

        val messageJson = JSONObject()
        messageJson.put("sender_id", actualApp.id)
        messageJson.put("receiver_id", 2) // Ajusta el ID del receptor
        messageJson.put("content", messageText)

        thread {
            try {
                outputStream.println(messageJson.toString())

                val localDateTime = LocalDateTime.now()
                val messageDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())

                val newMessage = Message(
                    id = messages.size + 1,
                    senderId = actualApp.id,
                    content = messageText,
                    sendAt = messageDate,
                    isRead = false,
                    chatId = chatId
                                        )

                runOnUiThread {
                    messages.add(newMessage)
                    adapter.notifyItemInserted(messages.size - 1)
                    chatRecyclerView.scrollToPosition(messages.size - 1)
                    messageInput.text.clear()
                }
            } catch (e: Exception) {
                Log.e("SOCKET", "Error enviando mensaje: ${e.message}")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            socket.close()
        } catch (e: IOException) {
            Log.e("SOCKET", "Error al cerrar socket: ${e.message}")
        }
    }
}
