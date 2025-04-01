import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.togethernotes.R
import com.example.togethernotes.tools.actualApp

class MessageAdapter(private val messages: List<Message>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

        if (message.id == actualApp.id) {
            // Configurar mensaje enviado
            holder.sentMessageLayout.visibility = View.VISIBLE
            holder.receivedMessageLayout.visibility = View.GONE

            holder.sentText.text = message.content
            holder.sentText.visibility = View.VISIBLE


        } else {
            // Configurar mensaje recibido
            holder.receivedMessageLayout.visibility = View.VISIBLE
            holder.sentMessageLayout.visibility = View.GONE

            holder.receivedText.text = message.content
            holder.receivedText.visibility = View.VISIBLE


        }
    }

    override fun getItemCount(): Int = messages.size

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receivedMessageLayout: LinearLayout = itemView.findViewById(R.id.receivedMessageLayout)
        val sentMessageLayout: LinearLayout = itemView.findViewById(R.id.sentMessageLayout)

        val receivedText: TextView = itemView.findViewById(R.id.receivedText)
        val sentText: TextView = itemView.findViewById(R.id.sentText)

        val receivedImage: ImageView = itemView.findViewById(R.id.receivedImage)
        val sentImage: ImageView = itemView.findViewById(R.id.sentImage)
    }
}