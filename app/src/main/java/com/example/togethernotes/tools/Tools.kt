package com.example.togethernotes.tools

import android.content.Context
import android.content.Intent
import android.view.View
import com.example.togethernotes.models.Artist
import com.example.togethernotes.models.Genres
import com.example.togethernotes.models.Space
import com.example.togethernotes.models.App
lateinit var actualApp: App
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
    fun createUser(
        userRole: String, email: String, password: String, name: String, zipCode: Int =0,
        capacity: Int = 5,
        genreList: List<Genres> =  listOf(
            Genres(1, "Prueba"),)){
        actualApp = if (userRole == "Artist") {
            Artist(
                active = true,
                mail = email,
                password = password,
                name = name,
                role = "Artist",
                genreList = genreList
                  )
        } else {
            Space(
                active = true,
                mail = email,
                password = password,
                name = "Nuevo Espacio",
                role = "Space"
                 )
        }

    }

}