import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.togethernotes.R
import com.example.togethernotes.models.Contract

class ContractsAdapter(
    private val listaEventos: List<Contract>,
    private val onItemClickListener: (Contract) -> Unit // Interfaz para manejar clics
                      ) : RecyclerView.Adapter<ContractsAdapter.EventoViewHolder>() {

    // ViewHolder para cada elemento de la lista
    class EventoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagenEvento: ImageView = itemView.findViewById(R.id.imagenEvento)
        val tituloEvento: TextView = itemView.findViewById(R.id.tituloEvento)
        val tipoEvento: TextView = itemView.findViewById(R.id.tipoEvento)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_evento, parent, false)
        return EventoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val evento = listaEventos[position]

        // Vincular los datos con las vistas
        //holder.imagenEvento.setImageResource(evento.imagen)
        holder.tituloEvento.text = evento.title
        holder.tipoEvento.text = evento.meet_type

        // Configurar el listener de clics
        holder.itemView.setOnClickListener {
            onItemClickListener(evento) // Notificar al fragmento sobre el clic
        }
    }

    override fun getItemCount(): Int {
        return listaEventos.size
    }
}