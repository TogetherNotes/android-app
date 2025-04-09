package com.example.togethernotes

import android.annotation.SuppressLint
import android.media.Image
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.togethernotes.models.App
import com.example.togethernotes.models.TempMatch
import com.example.togethernotes.repository.AppRepository
import com.example.togethernotes.repository.TempMatchRepository
import com.example.togethernotes.tools.Tools
import com.example.togethernotes.tools.actualApp
import com.example.togethernotes.tools.possibleMatch
import com.example.togethernotes.tools.possibleMatchList
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.IllegalFormatException
import java.util.Locale


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
        showNextMatch("left")

    }

    private fun onSwipeRight() {
        showNextMatch("right")
       insertTmpMatchTable()
    }

    private fun insertTmpMatchTable() {
        var spaceLiked = false
        var artistLiked = false
        if (actualApp.role =="Space" )
        {
            spaceLiked = true
        }
        else
        {
            artistLiked = true
        }

        var request_date= Tools.getCurrentFormattedDate()
        val newTempMatch = TempMatch(
            artist_id = actualApp.id,
            space_id = possibleMatch.id,
            artist_like = spaceLiked,
            space_like = artistLiked,
            status = "pending",
            request_date = request_date
        )
        val tempMatchRepository = TempMatchRepository()
        lifecycleScope.launch {
            try {
                // Llamar al repositorio para crear el nuevo registro
                val response: Response<TempMatch> =
                    tempMatchRepository.createTempMatch(newTempMatch)

                if (response.isSuccessful) {
                    // Registro creado exitosamente
                    val createdMatch = response.body()
                   // Toast.makeText(requireContext(), "Registro creado: ${createdMatch?.artist_id}, ${createdMatch?.space_id}", Toast.LENGTH_LONG).show()
                } else {
                    // Manejar errores específicos de la API
                    //Toast.makeText(requireContext(), "Error al crear el registro: ${response.message()}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                // Manejar excepciones generales
               // Toast.makeText(requireContext(), "Error inesperado: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

    }


    private fun showNextMatch(direction: String) {
        updateMatchLayout()
        animateFrameLayout(direction)
    }

    private fun animateFrameLayout(direction: String) {
        val frameLayout = view?.findViewById<FrameLayout>(R.id.matchFrame)
        frameLayout?.let {
            when (direction) {
                "left" -> {
                    // Salida hacia la izquierda
                    it.animate()
                        .translationXBy(-it.width.toFloat())
                        .alpha(0f) // Desvanecer
                        .setDuration(300)
                        .withEndAction {
                            updateMatchLayout()

                            // Entrada desde la derecha
                            it.translationX = it.width.toFloat()
                            it.alpha = 0f
                            it.animate()
                                .translationX(0f)
                                .alpha(1f) // Aparecer
                                .setDuration(300)
                                .start()
                        }
                        .start()
                }
                "right" -> {
                    // Salida hacia la derecha
                    it.animate()
                        .translationXBy(it.width.toFloat())
                        .alpha(0f) // Desvanecer
                        .setDuration(300)
                        .withEndAction {
                            updateMatchLayout()

                            // Entrada desde la izquierda
                            it.translationX = -it.width.toFloat()
                            it.alpha = 0f
                            it.animate()
                                .translationX(0f)
                                .alpha(1f) // Aparecer
                                .setDuration(300)
                                .start()
                        }
                        .start()
                }
            }
        }
    }

    private fun updateMatchLayout() {
        if (possibleMatchList.isNotEmpty() && searchedMatchesCounter < possibleMatchList.size && possibleMatchList[searchedMatchesCounter].role != actualApp.role ) {
             possibleMatch = possibleMatchList[searchedMatchesCounter]
            Tools.createPossibleUser(
                possibleMatch.role,
                possibleMatch.mail,
                possibleMatch.password,
                possibleMatch.name,
                possibleMatch.id,
                possibleMatch.rating,
            )
            searchedMatchesCounter++
            view?.let {
                val posibleMatchName = it.findViewById<TextView>(R.id.userNameMatch)
                val userRatingMatch = it.findViewById(R.id.userRatingMatch) as ImageView
                setStarts(possibleMatch.rating, userRatingMatch)
                posibleMatchName.text = possibleMatch.name

            }
        } else {
            searchMatch()
            //Toast.makeText(requireContext(), "No hay más coincidencias", Toast.LENGTH_SHORT).show()
        }
    }
    private fun setStarts(rating: Int, view: ImageView )
    {
        when(rating)
            {
                1 -> {
                    view.background = ContextCompat.getDrawable(requireContext(), R.drawable.stars1)
                }
                2 -> {
                    view.background = ContextCompat.getDrawable(requireContext(), R.drawable.stars2)
                }
                3 -> {
                    view.background = ContextCompat.getDrawable(requireContext(), R.drawable.stars3)
                }
                4 -> {
                    view.background = ContextCompat.getDrawable(requireContext(), R.drawable.stars4)
                }
                5 -> {
                    view.background = ContextCompat.getDrawable(requireContext(), R.drawable.stars5)
                }
        }

    }

    private fun findMatch() {
        val likeButton = view?.findViewById<ImageView>(R.id.makeMatchButton)
        val rejectButton = view?.findViewById<ImageView>(R.id.discardMatchButton)
        likeButton?.setOnClickListener {
            updateMatchLayout()
        }
        rejectButton?.setOnClickListener{
            updateMatchLayout()
        }
    }

    private fun searchMatch() {
        searchedMatchesCounter =0
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
                        //Toast.makeText( requireContext(), "Se encontraron ${possibleMatchList.size} coincidencias", Toast.LENGTH_LONG ).show()
                    } else {
                       // Toast.makeText(requireContext(), "No se encontraron coincidencias", Toast.LENGTH_LONG).show()
                    }
                } else {
                    val errorMessage = response.message() ?: "Error desconocido"
                    // Toast.makeText(requireContext(), "Error en la solicitud: $errorMessage", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                when (e) {
                    is IOException -> {
                   //     Toast.makeText(requireContext(), "Error de red: Verifica tu conexión", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                       // Toast.makeText(requireContext(), "Error inesperado: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun reprodMusic() {
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.audio_prueba)
        val btnPlayPause = view?.findViewById<ImageView>(R.id.btnPlayPause)
        if(actualApp.role== "Artist")
        {
            seekBar.visibility = View.GONE
            btnPlayPause?.visibility = View.GONE
        }
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