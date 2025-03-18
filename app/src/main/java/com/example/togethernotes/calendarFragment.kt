package com.example.togethernotes

import ActivitiesAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.togethernotes.models.Activity
import com.example.togethernotes.models.WorkType

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [calendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class calendarFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
        val selectedDay = view.findViewById<TextView>(R.id.show_day)

// Manejar la selección de fechas
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Crear un objeto Calendar para obtener el día de la semana y el nombre del mes
            val calendar = java.util.Calendar.getInstance()
            calendar.set(year, month, dayOfMonth) // Configurar la fecha seleccionada

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
                "Diciembre")
            val monthName = months[month]

            // Formatear la fecha completa
            selectedDay.text = "$dayOfWeek, $dayOfMonth de $monthName"
            // Mostrar la fecha seleccionada en un Toast
            initContractAdapter()
        }
    }
    fun initContractAdapter()
    {
        val recyclerViewEventos = view?.findViewById<RecyclerView>(R.id.recyclerViewEventos)

        // Crear una lista de eventos (puedes reemplazar esto con datos reales)
        val listaEventos = listOf(
            Activity(
                artist_id = 101,
                end_hour = "22:00",
                init_hour = "20:00",
                space_id = 5,
                work = WorkType.work,
                imagen = R.drawable.user,
                titulo = "Evento 1",
                tipo = "Concierto"
                    ),
            Activity(
                artist_id = 102,
                end_hour = "02:00",
                init_hour = "23:00",
                space_id = 7,
                work = WorkType.work,
                imagen = R.drawable.user,
                titulo = "Evento 2",
                tipo = "Festival"
                    ),
            Activity(
                artist_id = 103,
                end_hour = "19:30",
                init_hour = "18:00",
                space_id = 3,
                work = WorkType.meeting,
                imagen = R.drawable.user,
                titulo = "Evento 3",
                tipo = "Teatro"
                    )
                                 )

        // Configurar el RecyclerView
        recyclerViewEventos?.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewEventos?.adapter = ActivitiesAdapter(listaEventos) // Asignar el adaptador
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.calendar_fragment, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TercerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            calendarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}