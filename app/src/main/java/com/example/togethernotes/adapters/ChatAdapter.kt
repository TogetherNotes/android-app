package com.example.togethernotes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.togethernotes.R
import com.example.togethernotes.models.Chat
import java.text.SimpleDateFormat
import java.util.Locale

class ChatAdapter(private val chatList: List<Chat>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    // ViewHolder para cada elemento del RecyclerView
    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagenUsuarioChat: ImageView = itemView.findViewById(R.id.imagenUsuarioChat)
        val chatUserImage: TextView = itemView.findViewById(R.id.chatUserImage)
        val ultimoMensajeRecibido: TextView = itemView.findViewById(R.id.ultimoMensajeRecibidio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chatList[position]

        // Configurar la imagen (puedes usar Glide o Picasso para cargar imágenes reales)
        holder.imagenUsuarioChat.setImageResource(R.drawable.user)

        // Mostrar el nombre del usuario (simulado aquí como "Usuario X")
        holder.chatUserImage.text = "Usuario ${chat.user1_id}"

        // Mostrar la fecha formateada
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        holder.ultimoMensajeRecibido.text = dateFormat.format(chat.date)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }
}