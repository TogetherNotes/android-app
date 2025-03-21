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
   rol: String = "",
   val app_user_id: Int = 0,
   val capacity: Int = 0,
   val zip_code: String = ""
           ) : User(active, file_id, id, language_id, latitude, longitude, mail, name, notification_id, password, rating, rol)