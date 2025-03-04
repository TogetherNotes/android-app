package com.example.togethernotes.activities

import android.annotation.SuppressLint
import android.graphics.Rect
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.togethernotes.MainActivity
import com.example.togethernotes.R
import com.example.togethernotes.adapters.GenresAdapter
import com.example.togethernotes.models.Genres
import com.example.togethernotes.tools.Tools

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
    private lateinit var   tmpGenreList: List<Genres>

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
        val mainLayout = findViewById<LinearLayout>(R.id.createArtistLayout)
        val selectGenresButton = findViewById<Button>(R.id.selectGenres)
        val showGenres = findViewById<FrameLayout>(R.id.showGenres)
        val confirmGenresButton = findViewById<ImageView>(R.id.confirm_genres_button)
        Tools.detectFocus(mainLayout, showGenres)


        selectGenresButton.setOnClickListener {
            continueButton.visibility = View.GONE
            showGenres.visibility = View.VISIBLE

            recyclerViewGenres = findViewById(R.id.recyclerViewGenres)
            recyclerViewGenres.layoutManager = LinearLayoutManager(this)

            val genresList = listOf(
                Genres(1, "Pop"),
                Genres(2, "Rock"),
                Genres(3, "Jazz"),
                Genres(4, "Clásica")
                                   )
            genresAdapter = GenresAdapter(genresList) { selectedGenre ->
                tmpGenreList = genresAdapter.getSelectedGenres()
            }

            recyclerViewGenres.adapter = genresAdapter

            confirmGenresButton.setOnClickListener {
                val selectedGenresFromAdapter = genresAdapter.getSelectedGenres()

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

    //función para que cuando selecciones los generos i pulsas fuera del foco se haga invisible la activity

    @SuppressLint("ClickableViewAccessibility")
    fun detectFocus(mainLayout: LinearLayout, showGenres: FrameLayout): Boolean{
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
            false // Permitir que otros gestos se procesen
        }
        return windowClosed
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
                var  zipcode =2
                var capacity =2
                Tools.createUser("Artist", artistMail.text.toString(), artistPassword.text.toString(),artistName.text.toString(), zipcode, capacity, selectedGenres)
                // Si todo está correcto, procede con la lógica del registro
                Toast.makeText(this, "Todos los campos son válidos", Toast.LENGTH_SHORT).show()
                Tools.startActivity(continueButton, this, MainActivity::class.java)
            }
            errorMessage =""
        }
    }
}