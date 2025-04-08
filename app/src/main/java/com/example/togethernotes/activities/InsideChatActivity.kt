package com.example.togethernotes.activities

import Message
import MessageAdapter
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.togethernotes.R
import com.example.togethernotes.tools.CryptoUtil
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

    companion object {
        private const val DEFAULT_CHAT_ID = -1
        private const val DEFAULT_USER_ID = -1
        private const val DEFAULT_RECEIVER_NAME = "Desconocido"
        private const val SERVER_IP = "10.0.1.6"
        private const val SERVER_PORT = 5000
        private const val TAG_SOCKET = "SOCKET"
    }

    private lateinit var socket: Socket
    private lateinit var outputStream: PrintWriter
    private lateinit var inputStream: BufferedReader

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var adapter: MessageAdapter
    private lateinit var messageInput: EditText
    private lateinit var rootLayout: LinearLayout
    private lateinit var usernameChat: TextView

    private val messages = mutableListOf<Message>()
    private val messageRepository = MessageRepository()
    private val mensajesLeidosIds = mutableSetOf<Int>()

    private var chatId: Int = DEFAULT_CHAT_ID

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.inside_chat_layout)

        initViews()
        setupRecyclerView()
        setupKeyboardListener()

        chatId = intent.getIntExtra("chat_id", DEFAULT_CHAT_ID)
        val user1Id = intent.getIntExtra("user1_id", DEFAULT_USER_ID)
        val user2Id = intent.getIntExtra("user2_id", DEFAULT_USER_ID)
        val receiverId = if (actualApp.id == user1Id) user2Id else user1Id

        val receiverName = intent.getStringExtra("receiver_name") ?: DEFAULT_RECEIVER_NAME
        usernameChat.text = receiverName

        thread { connectToServer() }

        findViewById<ImageView>(R.id.sendButton).setOnClickListener {
            sendMessage(receiverId)
        }

        findViewById<ImageView>(R.id.createEventButton).setOnClickListener {
            findViewById<FrameLayout>(R.id.createEventSol).visibility = View.VISIBLE
        }
    }

    private fun initViews() {
        rootLayout = findViewById(R.id.rootLayout)
        chatRecyclerView = findViewById(R.id.recyclerView)
        messageInput = findViewById(R.id.messageInput)
        usernameChat = findViewById(R.id.usernameChat)
    }

    private fun setupRecyclerView() {
        adapter = MessageAdapter(messages)
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = adapter
    }

      private fun setupKeyboardListener() {
        var messageContainer = findViewById(R.id.messageContainer) as LinearLayout
        rootLayout.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            rootLayout.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootLayout.height
            val keypadHeight = screenHeight - rect.bottom

            messageContainer.translationY = if (keypadHeight > screenHeight * 0.15) {
                -keypadHeight.toFloat()
            } else {
                0f
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun connectToServer() {
        try {
            socket = Socket(SERVER_IP, SERVER_PORT)
            outputStream = PrintWriter(socket.getOutputStream(), true)
            inputStream = BufferedReader(InputStreamReader(socket.getInputStream()))

            log(TAG_SOCKET, "Conectado al servidor")

            val authJson = JSONObject().apply {
                put("type", "auth")
                put("userId", actualApp.id)
                put("chatId", chatId)
            }

            outputStream.println(authJson.toString())
            receiveMessages()
        } catch (e: Exception) {
            log(TAG_SOCKET, "Error al conectar: ${e.message}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun receiveMessages() {
        try {
            val buffer = CharArray(1024)
            val stringBuilder = StringBuilder()

            while (true) {
                val bytesRead = inputStream.read(buffer)
                if (bytesRead != -1) {
                    stringBuilder.append(String(buffer, 0, bytesRead))
                    val fullMessage = stringBuilder.toString()

                    if (fullMessage.contains("\n")) {
                        val newMessages = fullMessage.split("\n")
                        for (messageStr in newMessages) {
                            if (messageStr.isNotBlank()) processIncomingMessage(messageStr)
                        }
                        stringBuilder.clear()
                    }
                } else {
                    log(TAG_SOCKET, "Conexión cerrada por el servidor")
                    break
                }
            }
        } catch (e: IOException) {
            log(TAG_SOCKET, "Error al recibir mensaje: ${e.message}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun processIncomingMessage(messageStr: String) {
        try {
            val json = JSONObject(messageStr)

            // Decrypt the message
            val decryptedText = CryptoUtil.decrypt(json.getString("content"))

            val newMessage = Message(
                id = json.getInt("message_id"),
                senderId = json.getInt("from"),
                content = decryptedText, // show decrypted content
                sendAt = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                isRead = json.optBoolean("is_read", false),
                chatId = chatId
            )

            if (newMessage.chatId == chatId) {
                runOnUiThread {
                    messages.add(newMessage)
                    adapter.notifyItemInserted(messages.size - 1)
                    chatRecyclerView.scrollToPosition(messages.size - 1)
                }

                if (!newMessage.isRead && newMessage.senderId != actualApp.id) {
                    markMessageAsRead(newMessage)
                }
            }
        } catch (e: Exception) {
            log(TAG_SOCKET, "Error procesando mensaje: ${e.message}")
        }
    }

    private fun markMessageAsRead(message: Message) {
        thread {
            try {
                val readAckJson = JSONObject().apply {
                    put("type", "read_ack")
                    put("message_id", message.id)
                }
                outputStream.println(readAckJson.toString())
                log(TAG_SOCKET, "Mensaje leído enviado: ${message.id}")
            } catch (e: Exception) {
                log(TAG_SOCKET, "Error enviando read_ack: ${e.message}")
            }
        }

        runOnUiThread {
            message.isRead = true
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendMessage(receiverId: Int) {
        val messageText = messageInput.text.toString().trim()
        if (messageText.isEmpty()) return

        // Encrypt the message before sending
        val encryptedText = CryptoUtil.encrypt(messageText)

        val messageJson = JSONObject().apply {
            put("sender_id", actualApp.id)
            put("receiver_id", receiverId)
            put("content", encryptedText) // send encrypted content
        }

        thread {
            try {
                outputStream.println(messageJson.toString())

                val newMessage = Message(
                    id = messages.size + 1,
                    senderId = actualApp.id,
                    content = messageText, // Show decrypted text in UI
                    sendAt = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
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
                log(TAG_SOCKET, "Error enviando mensaje: ${e.message}")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            socket.close()
        } catch (e: IOException) {
            log(TAG_SOCKET, "Error al cerrar socket: ${e.message}")
        }
    }

    private fun log(tag: String, message: String) {
        Log.d(tag, message)
    }
}