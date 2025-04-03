package com.example.togethernotes.activities

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.togethernotes.MainActivity
import com.example.togethernotes.R
import com.example.togethernotes.adapters.GenresAdapter
import com.example.togethernotes.models.Artist
import com.example.togethernotes.models.Genres
import com.example.togethernotes.models.Space
import com.example.togethernotes.repository.ArtistRepository
import com.example.togethernotes.tools.Tools
import android.Manifest
import android.content.pm.PackageManager
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.togethernotes.repository.SpaceRepository
import org.osmdroid.config.Configuration
import org.osmdroid.views.MapView
import org.osmdroid.util.GeoPoint
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.views.overlay.Marker
import com.example.togethernotes.tools.actualApp
import com.google.gson.Gson
import kotlinx.coroutines.launch

class CreateSpaceActivity : AppCompatActivity() {

    private lateinit var spaceMail: EditText
    private lateinit var spacePassword: EditText
    private lateinit var spaceConfPassword: EditText
    private lateinit var capacity: EditText
    private lateinit var zipCode: EditText
    private lateinit var name: EditText
    private lateinit var location: EditText
    private lateinit var continueButton: ImageView
    private lateinit var openMapButton: ImageView
    private lateinit var chooseLocationLayout: View
    private lateinit var backLayout: View
    private lateinit var mapView: MapView
    private lateinit var saveButton: Button
    private lateinit var myLocationOverlay: MyLocationNewOverlay
    private lateinit var selectedMarker: Marker
    private var selectedLocation: GeoPoint? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.create_space)
        checkPermissions()

        initVar()
        setUpMap()
        showMap()
        registerControl()
        setupSaveLocationButton()
        setupMapClickListener()
    }

    private fun setupSaveLocationButton() {
        saveButton.setOnClickListener {
            selectedLocation?.let {
                location.setText("${it.latitude}, ${it.longitude}")
                chooseLocationLayout.visibility = View.GONE
            } ?: Toast.makeText(this, "Seleccione un punto en el mapa", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpMap() {
        // Cargar la configuración de OSMDroid
        Configuration.getInstance().load(applicationContext, getSharedPreferences("osmdroid", MODE_PRIVATE))

        // Configurar la fuente de mapas
        mapView.setTileSource(TileSourceFactory.MAPNIK)

        // Habilitar controles de zoom y gestos
        mapView.setBuiltInZoomControls(true)
        mapView.setMultiTouchControls(true)

        val mapController = mapView.controller
        mapController.setZoom(15.0)

        // Agregar overlay de ubicación
        myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), mapView)
        myLocationOverlay.enableMyLocation()
        myLocationOverlay.enableFollowLocation()
        myLocationOverlay.runOnFirstFix {
            runOnUiThread {
                val userLocation = myLocationOverlay.myLocation
                if (userLocation != null) {
                    mapView.controller.setCenter(GeoPoint(userLocation.latitude, userLocation.longitude))
                }
            }
        }

        // Agregar el overlay de ubicación al mapa
        mapView.overlays.add(myLocationOverlay)
    }

    private fun setupMapClickListener() {
        val eventsReceiver = object : org.osmdroid.events.MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                if (p != null) {

                    if (::selectedMarker.isInitialized) {
                        mapView.overlays.remove(selectedMarker)
                    }


                    selectedMarker = Marker(mapView).apply {
                        position = p
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        title = "Ubicación Seleccionada"
                    }

                    // Guardar la ubicación seleccionada
                    selectedLocation = p

                    // Agregar el marcador al mapa
                    mapView.overlays.add(selectedMarker)
                    mapView.invalidate() // Refrescar el mapa
                }
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }
        }

        val mapEventsOverlay = org.osmdroid.views.overlay.MapEventsOverlay(eventsReceiver)
        mapView.overlays.add(mapEventsOverlay)
    }



    private fun checkPermissions() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET
                                 )

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), 1)
        }
    }

    private fun showMap() {
        openMapButton.setOnClickListener {
            chooseLocationLayout.visibility = View.VISIBLE
        }
        backLayout.setOnClickListener{
            chooseLocationLayout.visibility = View.GONE
        }
    }

    fun initVar() {
        spaceMail = findViewById(R.id.spaceMail) // Campo de correo electrónico
        spacePassword = findViewById(R.id.spacePassword) // Campo de contraseña
        spaceConfPassword = findViewById(R.id.spaceConfPassword) // Campo de confirmación de contraseña
        capacity = findViewById(R.id.capacity) // Campo de capacidad
        zipCode = findViewById(R.id.zipCode) // Campo de ubicación (código postal)
        name = findViewById(R.id.spaceName)
        continueButton = findViewById(R.id.contine_restaurant_button) as ImageView // Botón CREAR COMPTE
        location = findViewById(R.id.location)
        openMapButton = findViewById(R.id.mapButton)
        chooseLocationLayout = findViewById(R.id.chooseLocationLayout)
        mapView = findViewById(R.id.mapview)
        saveButton = findViewById(R.id.saveLocation)
        backLayout = findViewById(R.id.backLayout)
    }

    /**
     * Controla la lógica de validación y registro.
     */
    fun registerControl() {
        var errorMessage = ""

        continueButton.setOnClickListener {
            // Validación de campos vacíos
            if (spaceMail.text.isEmpty())
            {
                errorMessage += getString(R.string.mailEmpty)
            }

            if (spacePassword.text.isEmpty())
            {
                errorMessage += getString(R.string.passwordEmpty)
            }

            if (spaceConfPassword.text.isEmpty())
            {
                errorMessage += getString(R.string.passwordConfEmpty)
            }

            if (capacity.text.isEmpty())
            {
                errorMessage += getString(R.string.capacityEmpty)
            }

            if (zipCode.text.isEmpty())
            {
                errorMessage += getString(R.string.zipEmpty)
            }

            // Validación de coincidencia de contraseñas
            if (spacePassword.text.toString() != spaceConfPassword.text.toString())
            {
                errorMessage += getString(R.string.passwordMatchEmpty)
            }

            if (name.text.isEmpty())
            {
                errorMessage += getString(R.string.nameEmpty)
            }
            // Mostrar el mensaje de error si hay alguno
            if (errorMessage.isNotEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()

            } else
            {

                Tools.createUser("Space",spaceMail.text.toString(), spacePassword.text.toString(), name.text.toString(),zipCode.text.toString().toInt(),capacity.text.toString().toInt())

                addSpace()
                Tools.startActivity(continueButton, this, MainActivity::class.java)
            }
            // Reiniciar el mensaje de error
            errorMessage = ""
        }

    }
    fun addSpace()
    {
        val repository = SpaceRepository()
        lifecycleScope.launch {
            try {
                val json = Gson().toJson(actualApp as Space)
                println(json)
                val response = repository.registerSpace(actualApp as Space)
                if (response.isSuccessful) {
                    Toast.makeText(this@CreateSpaceActivity, "Se ha insertado con éxito", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@CreateSpaceActivity, "Respuesta vacía", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@CreateSpaceActivity, "Exception: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

    }
}
