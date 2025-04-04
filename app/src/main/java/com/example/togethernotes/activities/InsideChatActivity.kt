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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.togethernotes.R
import com.example.togethernotes.repository.MessageRepository
import com.example.togethernotes.tools.actualApp
import kotlinx.coroutines.launch
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
    private lateinit var rootLayout: LinearLayout
    private var messages = mutableListOf<Message>()
    private var chatId: Int = 1
    private val serverIp = "10.0.1.6"
    private val serverPort = 5000
    private val messageRepository = MessageRepository()  // Instancia del repositorio

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.inside_chat_layout)

        rootLayout = findViewById(R.id.rootLayout)
        chatRecyclerView = findViewById(R.id.recyclerView)
        messageInput = findViewById(R.id.messageInput)
        val sendButton = findViewById<Button>(R.id.sendButton)
        val solEventButton = findViewById<ImageView>(R.id.createEventButton)
        val solEventLayout = findViewById<FrameLayout>(R.id.createEventSol)

        adapter = MessageAdapter(messages)
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = adapter

        // 👉 Recuperar el chat y calcular el receptor
        chatId = intent.getIntExtra("chat_id", -1)
        val user1Id = intent.getIntExtra("user1_id", -1)
        val user2Id = intent.getIntExtra("user2_id", -1)
        val receiverId = if (actualApp.id == user1Id) user2Id else user1Id

        thread {
            connectToServer()
        }

        sendButton.setOnClickListener { sendMessage(receiverId) }
        solEventButton.setOnClickListener { solEventLayout.visibility = View.VISIBLE }

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
            val buffer = CharArray(1024)
            val stringBuilder = StringBuilder() // StringBuilder para ir construyendo el mensaje completo

            while (true) {
                val bytesRead = inputStream.read(buffer)
                if (bytesRead != -1) {
                    // Agregar lo leído al StringBuilder
                    stringBuilder.append(String(buffer, 0, bytesRead))

                    // Procesar el mensaje completo (asegúrate de que termine una línea)
                    var line = stringBuilder.toString()
                    if (line.contains("\n")) {
                        // Dividir por líneas
                        val newMessages = line.split("\n") // Cambié el nombre de la variable a newMessages

                        // Procesar cada mensaje recibido
                        for (messageStr in newMessages) {
                            if (messageStr.isNotBlank()) {
                                Log.d("SOCKET", "Mensaje recibido: $messageStr")
                                val json = JSONObject(messageStr)

                                val senderId = json.getInt("from")
                                val content = json.getString("content")

                                val localDateTime = LocalDateTime.now()
                                val messageDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())

                                val newMessage = Message(
                                    id = messages.size + 1, // Esto se sigue refiriendo a la lista global
                                    senderId = senderId,
                                    content = content,
                                    sendAt = messageDate,
                                    isRead = false,
                                    chatId = chatId
                                                        )

                                // Actualizar la UI en el hilo principal
                                runOnUiThread {
                                    messages.add(newMessage)  // Aquí estamos agregando el nuevo mensaje a la lista global
                                    adapter.notifyItemInserted(messages.size - 1)
                                    chatRecyclerView.scrollToPosition(messages.size - 1)
                                }

                                // Marcar mensaje como leído en el backend (si aún no está leído)
                                if (!newMessage.isRead) {
                                    markMessageAsRead(newMessage)
                                }
                            }
                        }

                        // Limpiar el StringBuilder para preparar el siguiente conjunto de mensajes
                        stringBuilder.clear()
                    }
                } else {
                    Log.e("SOCKET", "Se ha cerrado la conexión del servidor")
                    break
                }
            }
        } catch (e: IOException) {
            Log.e("SOCKET", "Error al recibir mensaje: ${e.message}")
        }
    }

    // Llamar a la API para marcar el mensaje como leído
    private fun markMessageAsRead(message: Message) {
        lifecycleScope.launch {
            val updatedMessage = Message(
                id = message.id,
                senderId = message.senderId,
                content = message.content,
                sendAt = message.sendAt,
                isRead = true, // Cambiamos solo este campo
                chatId = message.chatId
                                        )
            val response = messageRepository.markMessageAsRead(message.id, updatedMessage)
            if (response.isSuccessful) {
                // Actualizar el estado del mensaje como leído en la UI
                message.isRead = true
                Log.d("Message", "Mensaje marcado como leído: ${message.id}")
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendMessage(receiver_id: Int) {
        val messageText = messageInput.text.toString().trim()
        if (messageText.isEmpty()) return

        val messageJson = JSONObject()
        messageJson.put("sender_id", actualApp.id)
        messageJson.put("receiver_id", receiver_id) // Ajusta el ID del receptor
        messageJson.put("content", messageText)

        thread {
            try {
                outputStream.println(messageJson.toString())

                val localDateTime = LocalDateTime.now()
                val messageDate =
                    Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())

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
