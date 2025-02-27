package com.example.togethernotes.adapters// package com.example.togethernotes.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.togethernotes.R
import com.example.togethernotes.models.Genres

class GenresAdapter(private val genresList: List<Genres>) :
    RecyclerView.Adapter<GenresAdapter.GenreViewHolder>() {

    // ViewHolder para cada elemento de la lista
    inner class GenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewGenreName: TextView = itemView.findViewById(R.id.textViewGenreName)
    }

    // Crea un nuevo ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_genre, parent, false)
        return GenreViewHolder(view)
    }

    // Vincula los datos con el ViewHolder
    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = genresList[position]
        holder.textViewGenreName.text = genre.name
    }

    // Devuelve el número total de elementos
    override fun getItemCount(): Int = genresList.size
}