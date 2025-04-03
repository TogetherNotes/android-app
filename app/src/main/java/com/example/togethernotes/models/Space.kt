package com.example.togethernotes.models


class Space(
   active: Boolean = false,
   latitude: Double = 0.0,
   longitude: Double = 0.0,
   mail: String = "",
   name: String = "",
   password: String = "",
   rating: Int = 0,
   role: String = "",
   language_id: Int = 1,
   id: Int = 0,
   val capacity: Int = 0,
   var zip_code: String = ""
           ) : App(id, name, mail, password, role, rating, latitude, longitude, active, language_id)