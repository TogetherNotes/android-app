package com.example.togethernotes.tools

import android.content.Context
import android.content.Intent
import android.view.View
import com.example.togethernotes.models.Artist
import com.example.togethernotes.models.Genres
import com.example.togethernotes.models.Space
import com.example.togethernotes.models.App
import com.example.togethernotes.models.Chat
import com.example.togethernotes.models.TempMatch
import com.example.togethernotes.models.TempMatchDto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

var actualApp: App = App()
lateinit var artist: Artist
 var possibleMatch : App = App()
var possibleMatchList = mutableListOf<App>()
var likedMatchList = mutableListOf<App>()
var chatListName = mutableListOf<Chat>()
var pendingMatchList = mutableListOf<TempMatchDto>()
var chatInstalledExecuted =0
object Tools {

    /**
     * Método para iniciar una actividad sin pasar datos extras.
     *
     * @param view La vista que desencadena la acción.
     * @param context El contexto desde donde se inicia la actividad.
     * @param targetActivity La clase de la actividad a iniciar.
     */
    fun startActivity(view: View, context: Context, targetActivity: Class<*>) {
        view.setOnClickListener {
            startActivityTurned(context, targetActivity)
        }
    }
    fun getCurrentFormattedDate(): String {
        // Define el formato deseado: (YYYY)-(MM)-(DD)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        // Obtiene la fecha actual
        val currentDate = Date()
        // Formatea la fecha según el patrón definido
        return dateFormat.format(currentDate)
    }



    /**
     * Método para iniciar una actividad utilizando un Intent.
     *
     * @param context El contexto desde donde se inicia la actividad.
     * @param activityClass La clase de la actividad a iniciar.
     */
    fun startActivityTurned(context: Context, activityClass: Class<*>) {
        val intent = Intent(context, activityClass)
        context.startActivity(intent)

    }
    fun detectLanguageCode(selectedLanguageCode: String): Int
    {
        var code: Int
        if (selectedLanguageCode=="es")
        {
            code = 1
        }
        if (selectedLanguageCode=="en")
        {
            code =2
        }
        else
        {
            code=3
        }
        return code
    }
    fun createUser(
        userRole: String,
        email: String,
        password: String,
        name: String,
        zipCode: Int = 0,
        capacity: Int = 5,
        genreList: List<Genres> = listOf(Genres(1, "Prueba")),
        selectedLanguageCode: String
                  )
    {
        var language =detectLanguageCode(selectedLanguageCode)
        var genreListIds : MutableList<Int> = mutableListOf()

        for (genre in genreList) {
            genreListIds.add(genre.id) // Usamos add() para añadir elementos a la lista
        }
        actualApp = App()
        if (userRole =="Artist")
        {
            actualApp=
            Artist(
                rating=2,
                latitude = 43.12345678,
                longitude = 2.19140000,
                active = true,
                mail = email,
                password = password,
                name = name,
                role = "Artist",
                genre_ids = genreListIds,
                language_id = language
                  )
        }
        else {
            Space(
                rating=2,
                latitude = 43.12345678,
                longitude = 43.12345678,
                active = true,
                mail = email,
                password = password,
                name = name,
                role = "Space",
                capacity =  50,
                zip_code ="08210",
                language_id = language
                 )
        }
    }
    fun createSpace(
        userRole: String,
        email: String,
        password: String,
        name: String,
        zipCode: String,
        capacity: Int = 5,
        latitude: Double,
        longitude: Double
                   )
    {
        actualApp = App()
        actualApp=
        Space(
            rating=2,
            latitude = latitude,
            longitude = longitude,
            active = true,
            mail = email,
            password = password,
            name = name,
            role = "Space",
            capacity =  capacity,
            zip_code =zipCode,
             )
    }

    fun createPossibleUser (userRole: String, email: String, password: String, name: String, userId: Int, rating: Int)
    {
        possibleMatch = App()
        possibleMatch=
            App(
                rating=rating,
                latitude = 43.12345678,
                longitude = 43.12345678,
                active = true,
                mail = email,
                password = password,
                name = name,
                role = userRole,
                id = userId
                 )
    }

}