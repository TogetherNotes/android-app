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
import com.example.togethernotes.repository.ContractRepository
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

            getContractsByDate(formattedDate)
        }
    }

    private fun initContractAdapter(
        listaEventos: List<Contract>,
    ) {
        val recyclerViewEventos = view?.findViewById<RecyclerView>(R.id.recyclerViewEventos)

        //initContractAdapter(recyclerViewEventos, year, monthOfYear, dayOfMonth)

        // Configurar el RecyclerView
        recyclerViewEventos?.layoutManager = LinearLayoutManager(requireContext())

        // Crear una lista de eventos (puedes reemplazar esto con datos reales)
        val finishTask = view?.findViewById(R.id.finishTask) as FrameLayout
        val nombreTreaFinishContract = view?.findViewById(R.id.nombreTreaFinishContract) as TextView
        val finishTaskButton = view?.findViewById(R.id.confirm_finish_contract) as ImageView
        val calendarFragment = view?.findViewById(R.id.calendarFragment) as LinearLayout

        // Asignar el adaptador al RecyclerView
        recyclerViewEventos?.adapter = ContractsAdapter(listaEventos) { selectedEvent ->
            // Manejar el clic en el fragmen
            finishTask.visibility = View.VISIBLE
            recyclerViewEventos?.visibility = View.GONE

            nombreTreaFinishContract.text= selectedEvent.title
            getUserStars()
            selectedEvent.status ="finished"
            updateContract(finishTaskButton,selectedEvent,finishTask)

            // Ejemplo de uso de findViewById en el fragmento
        }
        if (recyclerViewEventos != null) {
            detectFocus(calendarFragment,finishTask,recyclerViewEventos)
        }
    }

    fun updateContract(
        finishTaskButton: ImageView,
        selectedEvent: Contract,
        finishTask: FrameLayout
                      )
    {
        finishTaskButton.setOnClickListener {
            var repository = ContractRepository()
            lifecycleScope.launch {
                try {
                    // Crear un nuevo objeto Match
                    // Llamar al repositorio para enviar la solicitud POST
                    val response = repository.updateContract(selectedEvent.artist_id,selectedEvent.space_id,selectedEvent)

                    if (response.isSuccessful) {
                        // Si la solicitud fue exitosa, obtener el match creado
                        val createdMatch = response.body()
                        println("Match creado exitosamente: $createdMatch")
                    } else {
                        // Si hubo un error, imprimir el mensaje de error
                        println("Error al crear el match: ${response.message()}")
                    }
                } catch (e: Exception) {
                    // Manejar errores inesperados
                    println("Error inesperado: ${e.message}")
                }
            }
            finishTask.visibility = View.GONE

        }
    }


    fun getContractsByDate(date: String) {
        val contractRepository = ContractRepository()
        lifecycleScope.launch {
            try {
                val response = contractRepository.getContractsByDate(actualApp.id, date)
                if (response.isSuccessful) {
                    // Extraer el cuerpo de la respuesta
                    val contracts = response.body()
                    if (contracts != null) {
                        // Procesar los contratos obtenidos
                        Toast.makeText(
                            requireContext(),
                            "Contratos cargados para la fecha: $date",
                            Toast.LENGTH_SHORT
                                      ).show()

                        // Aquí puedes actualizar el RecyclerView con los contratos
                        initContractAdapter(contracts)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "No se encontraron contratos para la fecha: $date",
                            Toast.LENGTH_SHORT
                                      ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error al cargar contratos: ${response.code()}",
                        Toast.LENGTH_SHORT
                                  ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Error de conexión: ${e.message}",
                    Toast.LENGTH_SHORT
                              ).show()
            }
        }
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