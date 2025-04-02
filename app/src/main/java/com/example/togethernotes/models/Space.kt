package com.example.togethernotes.models


class Space(
   active: Boolean = false,
   file_id: Int = 0,
   id: Int = 0,
   language_id: Int = 0,
   latitude: Double = 0.0,
   longitude: Double = 0.0,
   mail: String = "",
   name: String = "",
   notification_id: Int = 0,
   password: String = "",
   rating: Int = 0,
   role: String = "",
   val app_user_id: Int = 0,
   val capacity: Int = 0,
   var zip_code: String = ""
           ) : App(id, name, mail, password, role, rating, latitude, longitude, active, language_id)