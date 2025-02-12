package com.example.togethernotes.tools

import android.content.Context
import android.content.Intent

object Tools {

    /**
     * Método para iniciar una actividad sin pasar datos extras.
     *
     * @param context El contexto desde donde se inicia la actividad.
     * @param activityClass La clase de la actividad a iniciar.
     */
    fun startActivity(context: Context, activityClass: Class<*>) {
        val intent = Intent(context, activityClass)
        context.startActivity(intent)
    }
}