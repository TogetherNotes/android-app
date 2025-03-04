// com.example.togethernotes.adapters/GenresAdapter.kt
package com.example.togethernotes.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.togethernotes.models.Genres
import com.example.togethernotes.R

class GenresAdapter(private val genresList: List<Genres>, private val onGenreSelected: (Genres) -> Unit) :
    RecyclerView.Adapter<GenresAdapter.GenreViewHolder>() {

    private val selectedGenres = mutableSetOf<Genres>()

    class GenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvGenreName: TextView = itemView.findViewById(R.id.textViewNombre)
        val itemContainer: View = itemView.findViewById(R.id.itemGenre)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_genre, parent, false)
        return GenreViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = genresList[position]
        holder.tvGenreName.text = genre.name

        // Cambiar el color de fondo si el género está seleccionado
        if (selectedGenres.contains(genre)) {
            holder.itemContainer.setBackgroundColor(Color.LTGRAY)
        } else {
            holder.itemContainer.setBackgroundColor(Color.TRANSPARENT)
        }

        holder.itemContainer.setOnClickListener {
            if (selectedGenres.contains(genre)) {
                selectedGenres.remove(genre)
                holder.itemContainer.setBackgroundColor(Color.TRANSPARENT)
            } else {
                selectedGenres.add(genre)
                holder.itemContainer.setBackgroundColor(Color.LTGRAY)
            }
            onGenreSelected(genre)
        }
    }

    override fun getItemCount(): Int {
        return genresList.size
    }
    fun getSelectedGenres(): List<Genres> {
        return selectedGenres.toList()
    }
}