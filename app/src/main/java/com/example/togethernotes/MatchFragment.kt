package com.example.togethernotes

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar

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

        // Crear el MediaPlayer
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.audio_prueba)

        // Referencia al SeekBar
        seekBar = view.findViewById(R.id.seekBar)
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
        val btnPlayPause: ImageView = view.findViewById(R.id.btnPlayPause)

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