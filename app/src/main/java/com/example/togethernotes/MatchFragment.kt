package com.example.togethernotes

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.togethernotes.models.App
import com.example.togethernotes.models.Space
import com.example.togethernotes.repository.AppRepository
import com.example.togethernotes.repository.SpaceRepository
import com.example.togethernotes.tools.Tools
import com.example.togethernotes.tools.actualApp
import com.example.togethernotes.tools.possibleMatch
import com.example.togethernotes.tools.possibleMatch
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.launch
import java.io.IOException

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var mediaPlayer: MediaPlayer
private lateinit var seekBar: SeekBar

class MatchFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reprodMusic()
        findMatch()
        searchMatch()
        updateMatchLayout()
    }

    private fun updateMatchLayout() {
        var posibleMatchName = view?.findViewById(R.id.userNameMatch) as TextView
        posibleMatchName.text = possibleMatch.name
    }

    fun findMatch()
    {

        var likeButton = view?.findViewById(R.id.makeMatchButton) as ImageView
        likeButton.setOnClickListener{

        }
    }
    fun  searchMatch() {
        val appRepository = AppRepository()
        possibleMatch = App()
        var possibleMatchList = mutableListOf<App>()
        var actualAppDef = App(actualApp.id, actualApp.name, actualApp.mail, actualApp.password,
                               actualApp.role, actualApp.rating, actualApp.latitude, actualApp.longitude,
                               actualApp.active,
                               actualApp.language_id)
        lifecycleScope.launch {
            try {
                // Realizar la solicitud a la API
                val response = appRepository.getAppsByLocation(
                    actualAppDef.latitude ?: 0.0, actualAppDef.longitude ?: 0.0, 100000.0)
                // Convertir el objeto `actualApp` a JSON para depuración


                if (response.isSuccessful) {
                    // Añadir los resultados a la lista si la respuesta es exitosa
                    response.body()?.let { possibleMatchList.addAll(it)
                        val possibleMatchTmp = it[0]

                        if (possibleMatchTmp != null) {
                            Tools.createPossibleUser(possibleMatchTmp.role,possibleMatchTmp.mail,possibleMatchTmp.password,possibleMatchTmp.name,possibleMatchTmp.id)
                            updateMatchLayout()
                        }
                    }


                    // Mostrar un mensaje indicando que se encontraron coincidencias
                    if (possibleMatchList.isNotEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            "Se encontraron ${possibleMatchList.size} coincidencias",
                            Toast.LENGTH_LONG
                                      ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "No se encontraron coincidencias",
                            Toast.LENGTH_LONG
                                      ).show()
                    }
                } else {
                    // Manejar errores específicos de la API
                    val errorMessage = response.message() ?: "Error desconocido"
                    Toast.makeText(
                        requireContext(),
                        "Error en la solicitud: $errorMessage",
                        Toast.LENGTH_LONG
                                  ).show()
                }
            } catch (e: Exception) {
                // Manejar excepciones generales
                when (e) {
                    is IOException -> {
                        // Error de red (por ejemplo, falta de conexión a Internet)
                        Toast.makeText(
                            requireContext(),
                            "Error de red: Verifica tu conexión",
                            Toast.LENGTH_LONG
                                      ).show()
                    }

                    is JsonSyntaxException -> {
                        // Error al parsear la respuesta JSON
                        Toast.makeText(
                            requireContext(),
                            "Error al procesar la respuesta: Datos incorrectos",
                            Toast.LENGTH_LONG
                                      ).show()
                    }

                    else -> {
                        // Otros errores inesperados
                        Toast.makeText(
                            requireContext(),
                            "Error inesperado: ${e.message}",
                            Toast.LENGTH_LONG
                                      ).show()
                    }
                }
            }
        }




    }


    fun reprodMusic()
    {
        // Crear el MediaPlayer
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.audio_prueba)

        // Referencia al SeekBar
        seekBar = view?.findViewById(R.id.seekBar) as SeekBar
        seekBar.max = mediaPlayer.duration // Establecer el valor máximo en milisegundos

        // Configurar el SeekBar para permitir arrastre manual
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress) // Cambiar la posición del reproductor
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })


        // Referencia al botón de Reproducir/Pausar
        val btnPlayPause = view?.findViewById(R.id.btnPlayPause) as ImageView


        btnPlayPause.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                btnPlayPause.setImageResource(R.drawable.play_image) // Cambia el ícono a "play"
            } else {
                mediaPlayer.start()
                btnPlayPause.setImageResource(R.drawable.pause_image) // Cambia el ícono a "pause"

                // Iniciar el hilo de actualización de la SeekBar
                Thread {
                    while (mediaPlayer.isPlaying) {
                        val progress = mediaPlayer.currentPosition
                        activity?.runOnUiThread {
                            seekBar.progress = progress // Actualizar la SeekBar
                        }
                        Thread.sleep(1000) // Esperar 1 segundo
                    }
                }.start()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
                             ): View? {
        return inflater.inflate(R.layout.match_fragment, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MatchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}