import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.togethernotes.R
import com.example.togethernotes.models.MatchItem

class MatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imageView: ImageView = itemView.findViewById(R.id.imageView)
    val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)

    fun bind(matchItem: MatchItem) {
        // Cargar la imagen usando Glide o Picasso
        //Glide.with(imageView.context).load(matchItem.imageUrl).into(imageView)
        nameTextView.text = matchItem.name
    }
}