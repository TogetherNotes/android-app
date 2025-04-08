import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.togethernotes.R
import com.example.togethernotes.models.MatchItem

class MatchAdapter(
    private val matches: List<MatchItem>,
    private val onItemClick: (MatchItem) -> Unit // Callback para manejar clics
                  ) : RecyclerView.Adapter<MatchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_match, parent, false)
        return MatchViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val matchItem = matches[position]
        holder.bind(matchItem)

        // Configurar el listener para el clic en el elemento
        holder.itemView.setOnClickListener {
            onItemClick(matchItem) // Llamar al callback con el elemento seleccionado
        }
    }

    override fun getItemCount(): Int {
        return matches.size
    }
}