package com.example.togethernotes

import ContractsAdapter
import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.togethernotes.models.Contract
import com.example.togethernotes.models.WorkType
import com.example.togethernotes.repository.ContractRepository
import com.example.togethernotes.repository.MessageRepository
import com.example.togethernotes.tools.actualApp
import kotlinx.coroutines.launch

class calendarFragment : Fragment() {
    private lateinit var eventList: List<Contract>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
                             ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.calendar_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val datePicker = view.findViewById<DatePicker>(R.id.datePicker)
        val recyclerViewEventos = view.findViewById<RecyclerView>(R.id.recyclerViewEventos)

        //initContractAdapter(recyclerViewEventos, year, monthOfYear, dayOfMonth)

        // Configurar el RecyclerView
        recyclerViewEventos.layoutManager = LinearLayoutManager(requireContext())

        // Manejar la selección de fechas
        datePicker.init(
            datePicker.year,
            datePicker.month,
            datePicker.dayOfMonth
                       ) { _, year, monthOfYear, dayOfMonth ->
            // Crear un objeto Calendar para obtener el día de la semana y el nombre del mes
            val calendar = java.util.Calendar.getInstance()
            calendar.set(year, monthOfYear, dayOfMonth)
            val selectedDay = view.findViewById<TextView>(R.id.show_day)

            // Obtener el día de la semana (e.g., "Lunes", "Martes")
            val daysOfWeek =
                arrayOf("Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")
            val dayOfWeek = daysOfWeek[calendar.get(java.util.Calendar.DAY_OF_WEEK) - 1]

            // Obtener el nombre del mes (e.g., "Enero", "Febrero")
            val months = arrayOf(
                "Enero",
                "Febrero",
                "Marzo",
                "Abril",
                "Mayo",
                "Junio",
                "Julio",
                "Agosto",
                "Septiembre",
                "Octubre",
                "Noviembre",
                "Diciembre"
                                )
            val monthName = months[monthOfYear]

            val formattedDate = String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth)

            // Inicializar el adaptador con la fecha formateada
            initContractAdapter(recyclerViewEventos, formattedDate)
        }
    }

    private fun initContractAdapter(
        recyclerViewEventos: RecyclerView,
        date: String
    ) {

        // Crear una lista de eventos (puedes reemplazar esto con datos reales)
        val finishTask = view?.findViewById(R.id.finishTask) as FrameLayout
        val nombreTreaFinishContract = view?.findViewById(R.id.nombreTreaFinishContract) as TextView
        val finishTaskButton = view?.findViewById(R.id.confirm_finish_contract) as ImageView
        val calendarFragment = view?.findViewById(R.id.calendarFragment) as LinearLayout

        val listaEventos = listOf(
            Contract(
                artist_id = 101,
                end_hour = "22:00",
                init_hour = "20:00",
                space_id = 5,
                meet_type = WorkType.work.toString(),
                title = "Evento 1",
                status = "Pendiente"
                    ),
            Contract(
                artist_id = 102,
                end_hour = "02:00",
                init_hour = "23:00",
                space_id = 7,
                meet_type = WorkType.work.toString(),
                title = "Evento 1",
                status = "Pendiente"
                    ),
            Contract(
                artist_id = 103,
                end_hour = "19:30",
                init_hour = "18:00",
                space_id = 3,
                meet_type = WorkType.work.toString(),
                title = "Evento 1",
                status = "Pendiente"
                    )
                             )

        // Asignar el adaptador al RecyclerView
        recyclerViewEventos.adapter = ContractsAdapter(listaEventos) { selectedEvent ->
            // Manejar el clic en el fragmen
            finishTask.visibility = View.VISIBLE
            recyclerViewEventos.visibility = View.GONE

            nombreTreaFinishContract.text= selectedEvent.title
            getUserStars()
            // Ejemplo de uso de findViewById en el fragmento
        }
        detectFocus(calendarFragment,finishTask,recyclerViewEventos)
    }

    fun getContractsByDate(date: String)
    {
        var contractRepository = ContractRepository()
        lifecycleScope.launch {
            try {
                val response = contractRepository.getContractsByDate(actualApp.id, date)
                if (response.isSuccessful) {

                    Toast.makeText(
                        requireContext(), // Necesitarás un contexto, asegúrate de pasarlo también
                        "Se ha modificado", Toast.LENGTH_SHORT
                                  ).show()
                } else {
                    Toast.makeText(
                        requireContext(), // Necesitarás un contexto, asegúrate de pasarlo también
                        "Se ha modificado", Toast.LENGTH_SHORT
                                  ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(), // Necesitarás un contexto, asegúrate de pasarlo también
                    "Se ha modificado", Toast.LENGTH_SHORT
                              ).show()
            }
        }
    }

    fun recordFinishedTask()
    {
        val finishTask = view?.findViewById(R.id.finishTask) as FrameLayout
        finishTask.visibility = View.GONE
    }
    @SuppressLint("ClickableViewAccessibility")
    fun getUserStars(){

        val myImageView: ImageView? = view?.findViewById(R.id.finishContractStars)

        myImageView?.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                // Obtener las coordenadas del clic relativas al ImageView
                val x = event.x

                // Obtener el ancho del ImageView
                val imageViewWidth = v.width

                // Dividir el ancho en 5 partes iguales
                val sectionWidth = imageViewWidth / 5.0f

                // Determinar en qué sección se hizo clic
                val section = (x / sectionWidth).toInt() + 1

                // Mostrar la sección en un Toast
                Toast.makeText(v.context, "Sección: $section", Toast.LENGTH_SHORT).show()                // Realizar acciones según la sección
                when (section) {
                    1 -> {
                        // Acción para la sección 1
                        myImageView?.background = ContextCompat.getDrawable(v.context, R.drawable.stars1)
                    }
                    2 -> {
                        myImageView?.background = ContextCompat.getDrawable(v.context, R.drawable.stars2)
                        // Acción para la sección 2
                    }
                    3 -> {
                        myImageView?.background = ContextCompat.getDrawable(v.context, R.drawable.stars3)

                        // Acción para la sección 3
                    }
                    4 -> {
                        // Acción para la sección
                        myImageView?.background = ContextCompat.getDrawable(v.context, R.drawable.stars4)
                    }
                    5 -> {
                        // Acción para la sección 5
                        myImageView?.background = ContextCompat.getDrawable(v.context, R.drawable.stars5)

                    }
                }

                true // Indica que el evento ha sido manejado
            } else {
                false
            }
        }


    }
    @SuppressLint("ClickableViewAccessibility")
    fun detectFocus(
        mainLayout: LinearLayout,
        showGenres: FrameLayout,
        recyclerViewEventos: RecyclerView
    )
    {
        var windowClosed: Boolean

        windowClosed = false
        mainLayout.setOnTouchListener { _, event ->
            if (showGenres.visibility == View.VISIBLE && event.action == MotionEvent.ACTION_DOWN) {
                val x = event.rawX.toInt()
                val y = event.rawY.toInt()

                // Verificar si el clic está fuera del layout pequeño
                val showGenresBounds = Rect()
                showGenres.getGlobalVisibleRect(showGenresBounds)
                if (!showGenresBounds.contains(x, y)) {
                    showGenres.visibility = View.GONE
                    mainLayout.isClickable = false
                    mainLayout.isFocusable = false
                    recyclerViewEventos.visibility = View.VISIBLE

                    windowClosed = true
                }
            }
            /*
            if(windowClosed)
            {
                continueButton.visibility = View.VISIBLE
            }
            */

            false // Permitir que otros gestos se procesen

        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): calendarFragment {
            return calendarFragment()
        }
    }
}