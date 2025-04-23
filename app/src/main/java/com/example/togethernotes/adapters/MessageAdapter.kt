import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope

import androidx.recyclerview.widget.RecyclerView
import com.example.togethernotes.R
import com.example.togethernotes.adapters.OnMessageClickListener
import com.example.togethernotes.tools.Tools
import com.example.togethernotes.tools.actualApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessageAdapter(private val messages: List<Message>,
                     private val listener: OnMessageClickListener?= null
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    private val additionalContentMap = mutableMapOf<Int, String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

        Log.d("ADAPTER", "Mostrando mensaje: '${message.content}' de ${message.senderId}")


        if (message.senderId == actualApp.id) {
            // Configurar mensaje enviado
            if(message.content.startsWith("$$--"))
            {
                var partes = Tools.splitMessage(message.content)
                var chech = false
                holder.sentEventLayout.visibility = View.VISIBLE
                holder.eventTextEnviado.text = partes.get(0)
                holder.fechaTextEnviado.text = partes.get(2)
                if(partes.getOrNull(3)=="accepted")
                    holder.acceptEventByUserEnviado.visibility = View.VISIBLE
                else if(partes.getOrNull(3)=="refused")
                    holder.refuseEventByUserEnviado.visibility = View.VISIBLE

                if (partes.get(1) == "check"){
                    chech = true
                }
                holder.checkContratoEnviado.isChecked = chech

            }
            else {
                holder.sentMessageLayout.visibility = View.VISIBLE
                holder.receivedMessageLayout.visibility = View.GONE

                holder.sentText.text = message.content
                holder.sentText.visibility = View.VISIBLE
            }
        }
        else
        {
            if(message.content.startsWith("$$--"))
            {
               var partes = Tools.splitMessage(message.content)
                var chech = false
                holder.receivedEventLayout.visibility = View.VISIBLE
                holder.eventText.text = partes.get(0)
                holder.fechaText.text = partes.get(2)

                if (partes.get(1) == "check"){
                    chech = true
                }
                holder.checkContrato.isChecked = chech
                if(partes.getOrNull(3)=="accepted") {
                    holder.acceptEventByUser.visibility = View.VISIBLE
                    holder.decideEventUser.visibility = View.GONE
                }
                else if(partes.getOrNull(3)=="refused")
                    holder.refuseEventByUserEnviado.visibility = View.VISIBLE
                // Configurar listener para el botón "Aceptar"
                holder.acceptButton.setOnClickListener {
                    holder.acceptEventByUser.visibility = View.VISIBLE
                    holder.decideEventUser.visibility = View.GONE
                    notifyItemChanged(position)

                    listener?.onAcceptButtonClick(message, position)
                }
                holder.discardButton.setOnClickListener {
                    holder.refuseEventByUser.visibility = View.VISIBLE
                    holder.decideEventUser.visibility = View.GONE
                    message.content += "$$--refused"
                    notifyItemChanged(position)
                    listener?.onDiscardButtonClick(message, position)
                }

            }
            else {
                // Configurar mensaje recibido
                holder.receivedMessageLayout.visibility = View.VISIBLE
                holder.sentMessageLayout.visibility = View.GONE

                holder.receivedText.text = message.content
                holder.receivedText.visibility = View.VISIBLE
            }
        }


    }

    fun addAdditionalContent(position: Int, newContent: String) {
        additionalContentMap[position] = newContent
        notifyItemChanged(position)
    }
    /*
    fun updateMessage() {
        val adapterScope = CoroutineScope(Dispatchers.Main)
        var messageRepository = MessageAdapter()
        fun updateMessage() {
            adapterScope.launch {
                try {
                    val response = contractRepository.createContract(contractToAdd)
                    if (response.isSuccessful) {
                        Toast.makeText(
                            context, // Necesitarás un contexto, asegúrate de pasarlo también
                            "Se ha insertado con éxito", Toast.LENGTH_SHORT
                                      ).show()
                    } else {
                        Toast.makeText(
                            context, "Respuesta vacía", Toast.LENGTH_LONG
                                      ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        context, "Exception: ${e.message}", Toast.LENGTH_LONG
                                  ).show()
                }
            }
        }
    }
    */

    override fun getItemCount(): Int {
        Log.d("ADAPTER", "Total de mensajes: ${messages.size}")
        return messages.size
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receivedMessageLayout: FrameLayout = itemView.findViewById(R.id.receivedMessageLayout)
        val sentMessageLayout: FrameLayout = itemView.findViewById(R.id.sentMessageLayout)

        val receivedEventLayout: FrameLayout = itemView.findViewById(R.id.receivedEventLayout)
        val sentEventLayout: FrameLayout = itemView.findViewById(R.id.sentEventLaout)
        val acceptEventByUser: LinearLayout = itemView.findViewById(R.id.acceptedByUser)
        val refuseEventByUser: LinearLayout = itemView.findViewById(R.id.refusedByUser)
        val decideEventUser: LinearLayout = itemView.findViewById(R.id.decideEventUser)
        val acceptEventByUserEnviado: LinearLayout = itemView.findViewById(R.id.acceptedByUserEnviado)
        val refuseEventByUserEnviado: LinearLayout = itemView.findViewById(R.id.refusedByUserEnviado)

        val eventText: TextView = itemView.findViewById(R.id.sentEventText)
        val fechaText: TextView = itemView.findViewById(R.id.textViewFecha)
        val checkContrato: CheckBox = itemView.findViewById(R.id.checkContrato)

        val eventTextEnviado: TextView = itemView.findViewById(R.id.sentEventTextEnviado)
        val fechaTextEnviado: TextView = itemView.findViewById(R.id.textViewFechaEnviado)
        val checkContratoEnviado: CheckBox = itemView.findViewById(R.id.checkContratoEnviado)

        val acceptButton: ImageView = itemView.findViewById(R.id.acceptEventButton)
        val discardButton: ImageView = itemView.findViewById(R.id.sentEventButton)

        val receivedText: TextView = itemView.findViewById(R.id.receivedText)
        val sentText: TextView = itemView.findViewById(R.id.sentText)

        val receivedImage: ImageView = itemView.findViewById(R.id.receivedImage)
        val sentImage: ImageView = itemView.findViewById(R.id.sentImage)
    }
}