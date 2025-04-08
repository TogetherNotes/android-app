package com.example.togethernotes

import MatchAdapter
import MessageAdapter
import Message
import android.content.Intent
import android.os.Build
import android.os.Bundle
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
import com.example.togethernotes.models.App
import com.example.togethernotes.models.Chat
import com.example.togethernotes.models.Match
import com.example.togethernotes.models.MatchItem
import com.example.togethernotes.models.TempMatch
import com.example.togethernotes.models.TempMatchDto
import com.example.togethernotes.repository.AppRepository
import com.example.togethernotes.repository.MatchRepository
import com.example.togethernotes.repository.TempMatchRepository
import com.example.togethernotes.tools.Tools
import com.example.togethernotes.tools.actualApp
import com.example.togethernotes.tools.pendingMatchList
import com.example.togethernotes.tools.possibleMatchList
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
    private var artistRoleId =0
    private var spaceRoleId =0
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
        val testButton = view.findViewById<Button>(R.id.testButton)
        testButton.setOnClickListener {
            val intent = Intent(requireContext(), InsideChatActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showMatches(tmpMatch:List<TempMatchDto>) {
        // Datos de ejemplo
        getLikedUsers(tmpMatch)
        val matches = mutableListOf<MatchItem>()


        for (likedUser in possibleMatchList)
        {

            matches.add(MatchItem(
                imageUrl = "https://via.placeholder.com/100",
                name = likedUser.name,
                description = "Guitarist looking for a venue",
                tempMatch = TempMatchDto(likedUser.id,"25-04-2005")
            ))
        }

        val recyclerViewMatches = view?.findViewById<RecyclerView>(R.id.recyclerViewMatches)
        // Configurar RecyclerView
        recyclerViewMatches?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = MatchAdapter(matches) { selectedMatch ->
                // Manejar el clic en un elemento
                onMatchItemClick(selectedMatch)
            }
        }
    }

    // Función para manejar el clic en un elemento


    private fun onMatchItemClick(matchItem: MatchItem) {
        // Aquí puedes realizar cualquier acción con el elemento seleccionado
        println("Elemento seleccionado: ${matchItem.name}")
        println("ID del usuario: ${matchItem.tempMatch.OtherUserId}")
        println("Fecha de solicitud: ${matchItem.tempMatch.request_date}")
        var acceptMatchLout = view?.findViewById(R.id.acceptMatchLayout) as FrameLayout
        var buttonAcceptMatch = view?.findViewById(R.id.acceptMatchButton) as ImageView
        var declineAcceptMatch = view?.findViewById(R.id.declinedMatchButton) as ImageView
        acceptMatchLout.visibility = View.VISIBLE

        buttonAcceptMatch.setOnClickListener{
            updateMatchTable(matchItem)
            updateTempMatch(matchItem)
        }


        // Mostrar un mensaje en pantalla
        Toast.makeText(requireContext(), "Seleccionaste a ${matchItem.name}", Toast.LENGTH_SHORT).show()
    }
    fun updateTempMatch(matchItem: MatchItem)
    {
        val matchRepository = TempMatchRepository()
        var request_date= Tools.getCurrentFormattedDate()

        lifecycleScope.launch {
            try {
                // Crear un nuevo objeto Match
                val newMatch = TempMatch(
                    artist_id = artistRoleId, // ID del artista
                    space_id = spaceRoleId,  // ID del espacio
                    artist_like = true, // Fecha del match)
                    space_like = true,
                    status = "accepted",
                    request_date =request_date
                                        )


                // Llamar al repositorio para enviar la solicitud POST
                val response = matchRepository.createMatch(newMatch)

                if (response.isSuccessful) {
                    // Si la solicitud fue exitosa, obtener el match creado
                    val createdMatch = response.body()
                    println("Match creado exitosamente: $createdMatch")
                } else {
                    // Si hubo un error, imprimir el mensaje de error
                    println("Error al crear el match: ${response.message()}")
                }
            } catch (e: Exception) {
                // Manejar errores inesperados
                println("Error inesperado: ${e.message}")
            }
        }
    }
    fun updateMatchTable(matchItem:MatchItem)
    {
            if (actualApp.role =="Artist")
            {
                artistRoleId = actualApp.id
                spaceRoleId = matchItem.tempMatch.OtherUserId
            }
            else
            {
                artistRoleId = matchItem.tempMatch.OtherUserId
                spaceRoleId = actualApp.id
            }

        val matchRepository = MatchRepository()
        var request_date= Tools.getCurrentFormattedDate()

        lifecycleScope.launch {
            try {
                // Crear un nuevo objeto Match
                val newMatch = Match(
                    artist_id = artistRoleId, // ID del artista
                    space_id = spaceRoleId,  // ID del espacio
                    match_date = request_date // Fecha del match
                                    )

                // Llamar al repositorio para enviar la solicitud POST
                val response = matchRepository.createMatch(newMatch)

                if (response.isSuccessful) {
                    // Si la solicitud fue exitosa, obtener el match creado
                    val createdMatch = response.body()
                    println("Match creado exitosamente: $createdMatch")
                } else {
                    // Si hubo un error, imprimir el mensaje de error
                    println("Error al crear el match: ${response.message()}")
                }
            } catch (e: Exception) {
                // Manejar errores inesperados
                println("Error inesperado: ${e.message}")
            }
        }
    }

    fun getLikedUsers(tmpMatch:List<TempMatchDto>)
    {
        val appRepository = AppRepository()
        lifecycleScope.launch {
            val deferredRequests = tmpMatch.map { tmp ->
                async {
                    try {
                        val response = appRepository.getAppById(tmp.OtherUserId)
                        if (response.isSuccessful) {
                            response.body()?.let { app ->
                                possibleMatchList.add(app)
                                println("Respuesta de la API: $app")
                            }
                        } else {
                            Toast.makeText(requireContext(), "Error al buscar usuario liked", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Error inesperado: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            // Espera a que todas las solicitudes terminen
            deferredRequests.awaitAll()
        }

    }
    fun getListYourMatches(){
        val tempMatchRepository = TempMatchRepository()
        lifecycleScope.launch {
            try {
                val response = tempMatchRepository.getPendingMatches(actualApp.id)
                if (response.isSuccessful) {
                    pendingMatchList = response.body() as MutableList<TempMatchDto>
                    if (pendingMatchList.isNullOrEmpty()) {
                        Toast.makeText(requireContext(), "No hay coincidencias pendientes", Toast.LENGTH_SHORT).show()
                    } else {
                        // Procesar las coincidencias pendientes
                        println("Coincidencias pendientes: $pendingMatchList")
                        // Actualizar la UI
                    }
                } else {
                    Toast.makeText(requireContext(), "Error al cargar coincidencias", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error inesperado: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
        showMatches(pendingMatchList)
    }

    private fun showChats() {
        val recyclerViewChats = view?.findViewById(R.id.recyclerViewChats) as RecyclerView
        val chatList = listOf(
            Chat(id = 1, date = Date(), user1_id = 101, user2_id = 102),
            Chat(id = 2, date = Date(System.currentTimeMillis() - 86400000), user1_id = 103, user2_id = 104),
            Chat(id = 3, date = Date(System.currentTimeMillis() - 172800000), user1_id = 105, user2_id = 106)
                             )

        // Configurar el RecyclerView
        val adapter = ChatAdapter(chatList)
        recyclerViewChats.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewChats.adapter = adapter
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