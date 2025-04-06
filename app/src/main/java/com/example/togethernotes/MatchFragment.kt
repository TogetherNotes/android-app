package com.example.togethernotes

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.view.GestureDetector
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.togethernotes.models.App
import com.example.togethernotes.repository.AppRepository
import com.example.togethernotes.tools.Tools
import com.example.togethernotes.tools.actualApp
import com.example.togethernotes.tools.possibleMatchList
import kotlinx.coroutines.launch
import java.io.IOException


private lateinit var gestureDetector: GestureDetector
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var mediaPlayer: MediaPlayer
private lateinit var seekBar: SeekBar
var searchedMatchesCounter: Int = 0

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
        searchMatch()
        initSwipeListener()

        findMatch()
        updateMatchLayout()
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun initSwipeListener() {
        val frameLayout = view?.findViewById<FrameLayout>(R.id.matchFrame)
        var initialX: Float = 0f
        var isDragging: Boolean = false

        frameLayout?.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = event.x
                    isDragging = true
                }
                MotionEvent.ACTION_MOVE -> {
                    if (isDragging) {
                        val deltaX = event.x - initialX
                        frameLayout.translationX = deltaX
                    }
                }
                MotionEvent.ACTION_UP -> {
                    isDragging = false
                    val finalX = event.x
                    val deltaX = finalX - initialX

                    if (Math.abs(deltaX) > 100) { // Umbral mínimo para considerar un swipe
                        if (deltaX > 0) {
                            onSwipeRight()
                        } else {
                            onSwipeLeft()
                        }
                    } else {
                        frameLayout.animate()
                            .translationX(0f)
                            .setDuration(200)
                            .start()
                    }
                }
            }
            true
        }
    }


    private fun onSwipeLeft() {
        showNextMatch()
    }

    private fun onSwipeRight() {
        showNextMatch()
    }

    private fun showNextMatch() {
        updateMatchLayout()
        animateFrameLayout()
    }

    private fun animateFrameLayout() {
        val frameLayout = view?.findViewById<FrameLayout>(R.id.matchFrame)
        frameLayout?.let {
            println("Ancho del FrameLayout: ${it.width}") // Depuración
            it.animate()
                .translationXBy(-it.width.toFloat()) // Mover hacia la izquierda
                .setDuration(300) // Duración de la animación
                .withEndAction {
                    it.translationX = 0f // Restablecer la posición original
                }
                .start()
        }
    }

    private fun updateMatchLayout() {
        if (possibleMatchList.isNotEmpty() && searchedMatchesCounter < possibleMatchList.size) {
            val possibleMatchTmp = possibleMatchList[searchedMatchesCounter]
            Tools.createPossibleUser(
                possibleMatchTmp.role,
                possibleMatchTmp.mail,
                possibleMatchTmp.password,
                possibleMatchTmp.name,
                possibleMatchTmp.id
            )
            searchedMatchesCounter++
            view?.let {
                val posibleMatchName = it.findViewById<TextView>(R.id.userNameMatch)
                posibleMatchName.text = possibleMatchTmp.name
            }
        } else {
            Toast.makeText(requireContext(), "No hay más coincidencias", Toast.LENGTH_SHORT).show()
        }
    }

    private fun findMatch() {
        val likeButton = view?.findViewById<ImageView>(R.id.makeMatchButton)
        likeButton?.setOnClickListener {
            updateMatchLayout()
        }
    }

    private fun searchMatch() {
        val appRepository = AppRepository()
        val actualAppDef = App(
            actualApp.id,
            actualApp.name,
            actualApp.mail,
            actualApp.password,
            actualApp.role,
            actualApp.rating,
            actualApp.latitude,
            actualApp.longitude,
            actualApp.active,
            actualApp.language_id
        )

        lifecycleScope.launch {
            try {
                val response = appRepository.getAppsByLocation(
                    actualAppDef.latitude ?: 0.0,
                    actualAppDef.longitude ?: 0.0,
                    10000.0
                )

                if (response.isSuccessful) {
                    response.body()?.let { apps ->
                        possibleMatchList.clear()
                        possibleMatchList.addAll(apps)
                        println("Respuesta de la API: $apps")
                    }

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
                    val errorMessage = response.message() ?: "Error desconocido"
                    Toast.makeText(
                        requireContext(),
                        "Error en la solicitud: $errorMessage",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                when (e) {
                    is IOException -> {
                        Toast.makeText(
                            requireContext(),
                            "Error de red: Verifica tu conexión",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else -> {
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

    private fun reprodMusic() {
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.audio_prueba)
        seekBar = view?.findViewById(R.id.seekBar) as SeekBar
        seekBar.max = mediaPlayer.duration

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val btnPlayPause = view?.findViewById<ImageView>(R.id.btnPlayPause)
        btnPlayPause?.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                btnPlayPause.setImageResource(R.drawable.play_image)
            } else {
                mediaPlayer.start()
                btnPlayPause.setImageResource(R.drawable.pause_image)

                Thread {
                    while (mediaPlayer.isPlaying) {
                        val progress = mediaPlayer.currentPosition
                        activity?.runOnUiThread {
                            seekBar.progress = progress
                        }
                        Thread.sleep(1000)
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