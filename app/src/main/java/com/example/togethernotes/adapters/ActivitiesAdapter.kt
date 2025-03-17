import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.togethernotes.R
import com.example.togethernotes.models.Activity
import org.w3c.dom.Text

class ActivitiesAdapter(private val listaEventos: List<Activity>) :
    RecyclerView.Adapter<ActivitiesAdapter.EventoViewHolder>() {

    // ViewHolder para cada elemento de la lista
    class EventoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagenEvento: ImageView = itemView.findViewById(R.id.imagenEvento)
        val tituloEvento: TextView = itemView.findViewById(R.id.tituloEvento)
        val tipoEvento:TextView = itemView.findViewById(R.id.tipoEvento)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        // Inflar el diseño del elemento
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_evento, parent, false)
        return EventoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        // Obtener el evento actual
        val evento = listaEventos[position]

        // Vincular los datos con las vistas
        holder.imagenEvento.setImageResource(evento.imagen)
        holder.tituloEvento.text = evento.titulo
        holder.tipoEvento.text = evento.tipo
    }

    override fun getItemCount(): Int {
        return listaEventos.size // Número de elementos en la lista
    }
}