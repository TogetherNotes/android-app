package com.example.togethernotes.activities

import android.annotation.SuppressLint
import android.graphics.Rect
import android.health.connect.datatypes.units.Length
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.togethernotes.MainActivity
import com.example.togethernotes.R
import com.example.togethernotes.adapters.GenresAdapter
import com.example.togethernotes.models.App
import com.example.togethernotes.models.ArtistGenre
import com.example.togethernotes.models.Genres
import com.example.togethernotes.repository.ArtistGenreRepository
import com.example.togethernotes.services.genres.GenreRepository
import com.example.togethernotes.tools.Tools
import com.example.togethernotes.tools.actualApp
import kotlinx.coroutines.launch

class CreateArtistActivity : AppCompatActivity() {
    private lateinit var artistMail: EditText
    private lateinit var artistPassword: EditText
    private lateinit var artistConfPassword: EditText
    private lateinit var artistName: EditText
    private lateinit var genre: EditText
    private lateinit var continueButton: ImageView
    private lateinit var recyclerViewGenres: RecyclerView
    private lateinit var genresAdapter: GenresAdapter
    private  lateinit var  selectedGenres: List<Genres>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.create_artist)
        initVar()
        registerControl()
        selectGenres()
    }
    fun initVar()
    {
        // Asignar los EditText a variables
        artistMail = findViewById(R.id.artistMail) as EditText// Campo Correu
        artistPassword = findViewById(R.id.artistPassword) as EditText/// Campo Contrasenya
        artistConfPassword = findViewById(R.id.artistConfPassword) as EditText // Campo Confirmar Contrasenya
        artistName = findViewById(R.id.artistName) as EditText
        continueButton = findViewById(R.id.contine_button) as ImageView
        // Variable para almacenar mensajes de error


    }
    @SuppressLint("ClickableViewAccessibility")
    fun selectGenres() {
        val genreRepository = GenreRepository()
        val mainLayout = findViewById<LinearLayout>(R.id.createArtistLayout)
        val selectGenresButton = findViewById<Button>(R.id.selectGenres)
        val showGenres = findViewById<FrameLayout>(R.id.showGenres)
        val confirmGenresButton = findViewById<ImageView>(R.id.confirm_genres_button)
        detectFocus(mainLayout,showGenres)
        var tmpGenreList: MutableList<Genres>


        selectGenresButton.setOnClickListener {
            continueButton.visibility = View.GONE
            showGenres.visibility = View.VISIBLE

            recyclerViewGenres = findViewById(R.id.recyclerViewGenres)
            recyclerViewGenres.layoutManager = LinearLayoutManager(this)

            tmpGenreList = mutableListOf()

            lifecycleScope.launch {
                try {
                    val response = genreRepository.getAllGenres()
                    if (response.isSuccessful) {
                        var genres = response.body()
                        if (genres != null) {
                            genresAdapter = GenresAdapter(genres) { selectedGenre ->
                            }// Actualiza tmpGenreList con los datos de la API
                            recyclerViewGenres.adapter = genresAdapter

                        } else {
                            Toast.makeText(this@CreateArtistActivity, "Respuesta vacía", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@CreateArtistActivity, "Error fetching genres: ${response.message()}", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@CreateArtistActivity, "Exception: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }





            confirmGenresButton.setOnClickListener {
                val selectedGenresFromAdapter = genresAdapter.getSelectedGenres()
                val repository = ArtistGenreRepository()

                for (selectedGenre in selectedGenresFromAdapter)
                {
                    var generatedArtisGenre = ArtistGenre(actualApp.id,selectedGenre.id)
                    lifecycleScope.launch {
                        try {
                            val response = repository.addArtistGenre(generatedArtisGenre)
                            if (response.isSuccessful) {
                                Toast.makeText(this@CreateArtistActivity, "Se ha insertado con éxito", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@CreateArtistActivity, "Respuesta vacía", Toast.LENGTH_LONG).show()
                            }
                    } catch (e: Exception) {
                    Toast.makeText(this@CreateArtistActivity, "Exception: ${e.message}", Toast.LENGTH_LONG).show()
                }
                        }
                }
                if (selectedGenresFromAdapter.isNotEmpty()) {
                    selectedGenres = selectedGenresFromAdapter
                    showGenres.visibility = View.GONE
                    continueButton.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this, "Debes seleccionar al menos un género", Toast.LENGTH_LONG).show()
                }
            }

        }

    }
    fun updateArtistGenre(genreList: List<Genres>)
    {
        val repository = ArtistGenreRepository()

        for (selectedGenre in genreList)
        {
            var generatedArtisGenre = ArtistGenre(actualApp.id,selectedGenre.id)
            lifecycleScope.launch {
                try {
                    val response = repository.addArtistGenre(generatedArtisGenre)
                    if (response.isSuccessful) {
                        Toast.makeText(this@CreateArtistActivity, "Se ha insertado con éxito", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@CreateArtistActivity, "Respuesta vacía", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@CreateArtistActivity, "Exception: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    //función para que cuando selecciones los generos i pulsas fuera del foco se haga invisible la activity

    @SuppressLint("ClickableViewAccessibility")
    fun detectFocus(mainLayout: LinearLayout, showGenres: FrameLayout)
    {
        var windowClosed: Boolean

        windowClosed = false
        mainLayout.setOnTouchListener { _, event ->
            if (showGenres.visibility == View.VISIBLE && event.action == MotionEvent.ACTION_DOWN) {
                val x = event.rawX.toInt()
                val y = event.rawY.toInt()

                // Verificar si el clic está fuera del layout pequeño
                val showGenresBounds = Rect()
                showGenres.getGlobalVisibleRect(showGenresBounds)
                if (!showGenresBounds.contains(x, y)) {
                    showGenres.visibility = View.GONE
                    windowClosed = true
                }
            }
            if(windowClosed)
            {
                continueButton.visibility = View.VISIBLE
            }
            false // Permitir que otros gestos se procesen

        }
    }

    fun registerControl(){
        var errorMessage = ""
        continueButton.setOnClickListener{
            // Validación de campos vacíos
            if (artistMail.text.isEmpty()) {
                errorMessage += "El campo de correo electrónico no puede estar vacío.\n"
            }
            if (artistPassword.text.isEmpty()) {
                errorMessage += "El campo de contraseña no puede estar vacío.\n"
            }
            if (artistConfPassword.text.isEmpty()) {
                errorMessage += "El campo de confirmación de contraseña no puede estar vacío.\n"
            }

            // Validación de coincidencia de contraseñas
            if (artistPassword.text.toString() != artistConfPassword.text.toString()) {
                errorMessage += "Las contraseñas no coinciden.\n"
                //eres gay

            }

            // Mostrar el mensaje de error si hay alguno
            if (errorMessage.isNotEmpty())
            {
                // Muestra un Toast o un AlertDialog con el mensaje de error
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            }
            else
            {
                //valores por defecto
                var  zipcode =0
                var capacity =0
                Tools.createUser("Artist", artistMail.text.toString(), artistPassword.text.toString(),artistName.text.toString(), zipcode, capacity, selectedGenres)
                updateArtistGenre(selectedGenres)
                // Si todo está correcto, procede con la lógica del registro
                Toast.makeText(this, "Todos los campos son válidos", Toast.LENGTH_SHORT).show()
                Tools.startActivity(continueButton, this, MainActivity::class.java)
            }
            errorMessage =""
        }
    }
}