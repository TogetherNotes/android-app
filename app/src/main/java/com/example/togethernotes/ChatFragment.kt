package com.example.togethernotes

import MessageAdapter
import Message
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.togethernotes.activities.InsideChatActivity
import com.example.togethernotes.adapters.ChatAdapter
import com.example.togethernotes.repository.AppRepository
import com.example.togethernotes.services.chat.ChatRepository
import com.example.togethernotes.models.App
import com.example.togethernotes.models.Chat
import com.example.togethernotes.models.MatchItem
import com.example.togethernotes.models.TempMatchDto
import com.example.togethernotes.repository.TempMatchRepository
import com.example.togethernotes.tools.actualApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.togethernotes.tools.likedMatchesUsers
import com.example.togethernotes.tools.possibleMatchList
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@Suppress("UNREACHABLE_CODE")
class ChatFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showChats()
        getListYourMatches()

    }

    // ChatFragment.kt
    private fun showChats() {
        val userId = actualApp.id
        val recyclerViewChats = view?.findViewById<RecyclerView>(R.id.recyclerViewChats) ?: return

        lifecycleScope.launch {
            try {
                // Obtener los chats del repositorio
                val response = withContext(Dispatchers.IO) {
                    ChatRepository().getChatsByUserId(userId)
                }

                if (response.isSuccessful) {
                    val chatList = response.body() ?: emptyList()

                    // Usamos el AppRepository para obtener el nombre de los usuarios
                    val userRepository = AppRepository()

                    val chatListWithNames = chatList.map { chat ->
                        val otherUserId = if (chat.user1_id == userId) chat.user2_id else chat.user1_id

                        // Obtenemos el nombre del otro usuario
                        val userResponse = userRepository.getAppById(otherUserId)

                        // Asignamos el nombre si la respuesta fue exitosa
                        val userName = if (userResponse.isSuccessful) {
                            userResponse.body()?.name ?: "Desconocido"
                        } else {
                            "Desconocido"
                        }

                        // Añadimos el nombre al chat
                        chat.copy(userName = userName)
                    }

                    // Configuramos el adapter con los chats con nombre
                    val adapter = ChatAdapter(chatListWithNames) { chat ->
                        val intent = Intent(requireContext(), InsideChatActivity::class.java)
                        intent.putExtra("chat_id", chat.id)
                        intent.putExtra("user1_id", chat.user1_id)
                        intent.putExtra("user2_id", chat.user2_id)
                        startActivity(intent)
                    }

                    recyclerViewChats.layoutManager = LinearLayoutManager(requireContext())
                    recyclerViewChats.adapter = adapter
                } else {
                    Toast.makeText(requireContext(), "Error cargando chats", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showMatches(tmpMatch:List<TempMatchDto>) {
        // Datos de ejemplo
        getLikedUsers(tmpMatch)
        val matches = mutableListOf<MatchItem>()
        for (likedUser in likedMatchesUsers)
        {
            matches.add(MatchItem(
                imageUrl = "https://via.placeholder.com/100",
                name = likedUser.name,
                description = "Guitarist looking for a venue"
                                 ))
        }

        var recyclerViewMatches = view?.findViewById(R.id.recyclerViewMatches) as RecyclerView
        // Configurar RecyclerView
        recyclerViewMatches.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = MatchAdapter(matches)
        }
    }
    fun getLikedUsers(tmpMatch:List<TempMatchDto>)
    {
        val appRepository = AppRepository()
        for (tmp in tmpMatch)
        {
            lifecycleScope.launch {
                try {
                    val response = appRepository.getAppById(tmp.OtherUserId)
                    if (response.isSuccessful) {
                        response.body()?.let {  app ->
                            possibleMatchList.add(app)
                            println("Respuesta de la API: $app")}
                        if (response.body() != null) {
                            Toast.makeText(requireContext(), "Se ha encontrado exitosamente", Toast.LENGTH_SHORT).show()
                        }
                        else
                        {
                            Toast.makeText(requireContext(), "No hay ningun usuario liked", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Error al buscar usuario liked", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error inesperado: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun getListYourMatches(){
        val tempMatchRepository = TempMatchRepository()
        lifecycleScope.launch {
            try {
                val response = tempMatchRepository.getPendingMatches(actualApp.id)
                if (response.isSuccessful) {
                    val pendingMatches = response.body()
                    if (pendingMatches != null) {
                        showMatches(pendingMatches)
                    }
                    if (pendingMatches.isNullOrEmpty()) {
                        Toast.makeText(requireContext(), "No hay coincidencias pendientes", Toast.LENGTH_SHORT).show()
                    } else {
                        // Procesar las coincidencias pendientes
                        println("Coincidencias pendientes: $pendingMatches")
                        // Actualizar la UI aquí (por ejemplo, usando un RecyclerView)
                    }
                } else {
                    Toast.makeText(requireContext(), "Error al cargar coincidencias", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error inesperado: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.chat_fragment, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SegundoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}