package com.example.togethernotes

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.togethernotes.models.Genres
import com.example.togethernotes.adapters.GenresAdapter
import com.example.togethernotes.models.Artist
import com.example.togethernotes.tools.actualApp
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.Locale


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val REQUEST_IMAGE_CAPTURE = 1
private const val REQUEST_CAMERA_PERMISSION = 100

class AccountFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var selectedLanguageCode: String = "en"
    private lateinit var profileImageView: ImageView
    private lateinit var cameraButton: ImageView
    private lateinit var  principalName: TextView
    private lateinit var  showRol: TextView
    private lateinit var  configureButton: ImageView
    private lateinit var  editUserButton: ImageView
    private lateinit var genresAdapter: GenresAdapter
    private lateinit var recyclerViewGenres: RecyclerView
    private lateinit var   tmpGenreList: List<Genres>
    private var editGenresActivate: Boolean = false
    private lateinit var userGenres: TextView
    private lateinit var  selectedLanguage: LinearLayout
    companion object {
        private const val REQUEST_CODE_AUDIO_PICKER = 200
    }
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
                             ): View? {
        return inflater.inflate(R.layout.account_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editUserInfo()
        editProfilePicture()
        configUserInfo()
        configureAccount()

        loadUserImage()
    }

    private fun loadUserImage() {
        val userId = actualApp.id
        val path = "/Images/$userId/image.jpg"

        // Descargar la imagen en segundo plano
        CoroutineScope(Dispatchers.IO).launch {
            val imageBitmap = downloadImageFromFTP(path)

            withContext(Dispatchers.Main) {
                if (imageBitmap != null) {

                    Glide.with(this@AccountFragment)
                        .load(imageBitmap)
                        .placeholder(R.drawable.user_default)
                        .error(R.drawable.user_default)
                        .circleCrop()
                        .into(profileImageView)
                } else {

                    Glide.with(this@AccountFragment)
                        .load(R.drawable.user_default)
                        .circleCrop()
                        .into(profileImageView)
                }
            }
        }
    }

    private fun downloadImageFromFTP(path: String): Bitmap? {
        val ftpClient = FTPClient()
        var bitmap: Bitmap? = null

        try {
            ftpClient.connect("10.0.0.99")
            val loginSuccess = ftpClient.login("dam01", "pepe")
            if (!loginSuccess) {
                Log.e("FTP", "Error de autenticación")
                return null
            }

            ftpClient.enterLocalPassiveMode()
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE)

            val inputStream = ftpClient.retrieveFileStream(path)
            if (inputStream != null) {
                bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()

                ftpClient.completePendingCommand()
            }

            ftpClient.logout()
            ftpClient.disconnect()

        } catch (e: Exception) {
            Log.e("FTP", "Error descargando la imagen", e)
        }

        return bitmap
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)

        val prefs = context.getSharedPreferences("config", Context.MODE_PRIVATE)
        val language = prefs.getString("language", Locale.getDefault().language) ?: "en"

        changeLanguage(context, language)
    }


    private fun configUserInfo()
    {

        principalName = view?.findViewById(R.id.userPrincipalName) as TextView
        showRol = view?.findViewById(R.id.userRol) as TextView
        principalName.text = actualApp.name

        /*
        userGenres = view?.findViewById(R.id.userArtistGenres) as TextView
        if(actualApp.role =="Artist")
        {
            userGenres.visibility = View.VISIBLE

            var genreList =""
            var firstLoop = true

            for(genres: Genres in  (actualApp as Artist).genreList ){
                if (firstLoop){
                    genreList +=genres.name
                    firstLoop = false
                }
                else{
                    genreList += ", ${genres.name}"
                }
            }
            userGenres.text = genreList
        }
        */


    }
    fun configureAccount()
    {
        val configureAccountButton = view?.findViewById<ImageView>(R.id.configure_button)
        val showConfigureLayout = view?.findViewById<FrameLayout>(R.id.configUserActivity)
        val principalLayout = view?.findViewById<LinearLayout>(R.id.account_settings)
        val spanishButton = view?.findViewById<LinearLayout>(R.id.spaish_flag)
        val englishButton = view?.findViewById<LinearLayout>(R.id.english_flag)
        val catalanButton = view?.findViewById<LinearLayout>(R.id.catalan_flag)
        val applyButton = view?.findViewById<ImageView>(R.id.apply_button)

        selectedLanguage = englishButton ?: return

        spanishButton?.setOnClickListener { setDefaultLanguage(spanishButton) }
        englishButton?.setOnClickListener { setDefaultLanguage(englishButton) }
        catalanButton?.setOnClickListener { setDefaultLanguage(catalanButton) }

        configureAccountButton?.setOnClickListener {
            showConfigureLayout?.visibility = View.VISIBLE
            if (actualApp.role == "Artist") {
                detectFocus(principalLayout!!, showConfigureLayout!!)
            }

            editUserButton.visibility = View.GONE
            configureButton.visibility = View.GONE
            cameraButton.visibility = View.GONE
        }

        applyButton?.setOnClickListener {
            applyLanguageChange()
        }
    }

    fun applyLanguageChange() {
        val ctx = context ?: return

        val prefs = ctx.getSharedPreferences("config", Context.MODE_PRIVATE).edit()
        prefs.putString("language", selectedLanguageCode)
        prefs.apply()

        changeLanguage(ctx, selectedLanguageCode)

        activity?.recreate()
    }



    fun setDefaultLanguage(clickedButton: LinearLayout) {
        val ctx = context ?: return

        selectedLanguageCode = when (clickedButton.id) {
            R.id.spaish_flag -> "es"
            R.id.english_flag -> "en"
            R.id.catalan_flag -> "ca"
            else -> "en"
        }

        clickedButton.background = ContextCompat.getDrawable(ctx, R.drawable.language_border_selected)
        if (selectedLanguage != clickedButton) {
            selectedLanguage.background = ContextCompat.getDrawable(ctx, R.drawable.language_border)
        }
        selectedLanguage = clickedButton
    }



    private fun changeLanguage(requireContext: Context, chosenLanguage: String) {
        val locale = Locale(chosenLanguage)
        Locale.setDefault(locale)

        val config = Configuration(requireContext.resources.configuration)
        config.setLocale(locale)

        requireContext.createConfigurationContext(config)
        requireContext.resources.updateConfiguration(config, requireContext.resources.displayMetrics)
    }


    fun editProfilePicture() {
        cameraButton = view?.findViewById(R.id.camera_button) as ImageView
        profileImageView = view?.findViewById(R.id.user_image) as ImageView

        cameraButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION
                                                 )
            } else {
                if (requireActivity().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                    abrirCamara()
                } else {
                    Log.e("Camera", "No se encontró ninguna cámara disponible en el dispositivo.")
                    Toast.makeText(requireContext(), "No se puede abrir la cámara", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun abrirCamara() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val packageManager = requireActivity().packageManager

        if (takePictureIntent.resolveActivity(packageManager) != null) {
            Log.d("Camera", "Starting camera intent")
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)

        } else {
            Log.e("Camera", "No se encontró una aplicación de cámara disponible.")
            Toast.makeText(requireContext(), "No se puede abrir la cámara", Toast.LENGTH_SHORT).show()
        }
    }


    fun uploadImageToFtp(bitmap: Bitmap, userId: String) {
        // Lanza una coroutine en el hilo principal
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Mueve la operación de red a un hilo de fondo
                withContext(Dispatchers.IO) {
                    // Crear y configurar el cliente FTP
                    val ftpClient = FTPClient()

                    try {
                        // Conectar al servidor FTP
                        Log.d("FTP", "Conectando al servidor FTP...")
                        ftpClient.connect("10.0.0.99")  // Dirección del servidor FTP
                        if (ftpClient.isConnected) {
                            Log.d("FTP", "Conexión exitosa")
                        }

                        // Iniciar sesión en el servidor FTP
                        val loginSuccess = ftpClient.login("dam01", "pepe")
                        if (loginSuccess) {
                            Log.d("FTP", "Login exitoso")
                        } else {
                            Log.e("FTP", "Error en el login")
                        }

                        // Establecer modo pasivo para la transferencia
                        ftpClient.enterLocalPassiveMode()

                        // Establecer el tipo de archivo binario para la carga
                        ftpClient.setFileType(FTP.BINARY_FILE_TYPE)

                        // Preparar la imagen para subirla
                        val baos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val inputStream = ByteArrayInputStream(baos.toByteArray())

                        // Crear el directorio en el servidor si no existe
                        val path = "/Images/$userId"
                        if (!ftpClient.changeWorkingDirectory(path)) {
                            ftpClient.makeDirectory(path)
                            ftpClient.changeWorkingDirectory(path)
                        }

                        // Subir la imagen
                        val success = ftpClient.storeFile("image.jpg", inputStream)
                        if (success) {
                            Log.d("FTP", "Imagen subida con éxito")
                        } else {
                            Log.e("FTP", "Error al subir la imagen")
                        }

                        // Cerrar la conexión FTP
                        inputStream.close()
                        ftpClient.logout()
                        ftpClient.disconnect()
                    } catch (e: IOException) {
                        Log.e("FTP", "Error en la conexión o la transferencia de archivos", e)
                    }
                }
            } catch (e: Exception) {
                // Maneja cualquier excepción que ocurra durante el proceso
                Log.e("FTP", "Error durante la carga de la imagen", e)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
                                           ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirCamara()
            } else {
                Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap

            if (imageBitmap != null) {
                // Procesar la imagen bitmap
                val size = minOf(imageBitmap.width, imageBitmap.height)
                val croppedBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, size, size)
                val scaledBitmap = Bitmap.createScaledBitmap(croppedBitmap, 250, 250, true)
                val roundedBitmapDrawable: RoundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, scaledBitmap)
                roundedBitmapDrawable.isCircular = true
                profileImageView.setImageDrawable(roundedBitmapDrawable)

                //  SUBIR IMAGEN AL FTP
                uploadImageToFtp(scaledBitmap, actualApp.id.toString())
            }
        }

        if (requestCode == REQUEST_CODE_AUDIO_PICKER && resultCode == Activity.RESULT_OK) {
            val selectedAudioUri = data?.data
            if (selectedAudioUri != null) {
                handleSelectedAudio(selectedAudioUri)
            } else {
                Toast.makeText(requireContext(), "No se seleccionó ningún archivo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleSelectedAudio(audioUri: Uri) {
        // Guardar el URI del archivo de audio en una variable
        val rawAudioUri = audioUri
        Log.d("RAW_AUDIO_URI", "URI del archivo de audio: $rawAudioUri")

        // Aquí puedes usar la variable `rawAudioUri` según tus necesidades
    }
    @SuppressLint("ClickableViewAccessibility")
    fun detectFocus(mainLayout: LinearLayout, showGenres: FrameLayout){
        var windowClosed: Boolean
        windowClosed = false
        if(editGenresActivate == false)
        {
            mainLayout.setOnTouchListener { _, event ->
                if (showGenres.visibility == View.VISIBLE && event.action == MotionEvent.ACTION_DOWN) {
                    val x = event.rawX.toInt()
                    val y = event.rawY.toInt()

                    // Verificar si el clic está fuera del layout pequeño
                    val showGenresBounds = Rect()
                    showGenres.getGlobalVisibleRect(showGenresBounds)
                    if (!showGenresBounds.contains(x, y)) {
                        showGenres.visibility = View.GONE
                        windowClosed = true
                    }
                    if (windowClosed) {
                        cameraButton.visibility = View.VISIBLE
                        configureButton.visibility = View.VISIBLE
                        editUserButton.visibility = View.VISIBLE
                    }
                }
                false // Permitir que otros gestos se procesen
            }
        }
    }


    private fun editUserInfo(){
        configureButton = view?.findViewById(R.id.configure_button) as ImageView
        val editRectangle = view?.findViewById<FrameLayout>(R.id.nonDimmedArea) as FrameLayout
        var button = view?.findViewById(R.id.confirm_edit_info) as ImageView
        var showZipCode = view?.findViewById(R.id.editZipCode) as EditText
        var showCapacity = view?.findViewById(R.id.editCapacity) as EditText
        var showGenre = view?.findViewById(R.id.editGenres) as Button
        var showName = view?.findViewById(R.id.editUserName) as EditText
        var principalLayout = view?.findViewById(R.id.account_settings) as LinearLayout
        val showGenres = view?.findViewById<FrameLayout>(R.id.showGenres)
        var updateGenresButton = view?.findViewById(R.id.confirm_genres_button) as ImageView
        val updateArtistAudio = view?.findViewById(R.id.insertAudio) as Button

        showName.setText(actualApp.name)

        editUserButton = view?.findViewById(R.id.edit_user_button) as ImageView
        editUserButton.setOnClickListener {
            detectFocus(principalLayout,editRectangle)

            //TODO
            editUserButton.visibility = View.GONE
            configureButton.visibility = View.GONE
            cameraButton.visibility = View.GONE
            editRectangle.visibility = View.VISIBLE
            if(actualApp.role == "Artist")
            {
                showGenre.visibility = View.VISIBLE
                updateArtistAudio.visibility = View.VISIBLE
                updateArtistAudio.setOnClickListener{
                    openAudioPicker()

                }
            }
            else if (actualApp.role =="Space")
            {
                showZipCode.visibility  = View.VISIBLE
                showCapacity.visibility = View.VISIBLE
            }
            button.setOnClickListener()
            {
                //editUserNewInfo()
            }

        }
        if(actualApp.role =="Artist")
        {
            showGenre.setOnClickListener{
                editGenresActivate = true
                showGenres?.visibility = View.VISIBLE
                recyclerViewGenres = view?.findViewById(R.id.recyclerViewGenres) as RecyclerView
                recyclerViewGenres.layoutManager = LinearLayoutManager(requireContext())
                val genresList = listOf(
                    Genres(1, "Pop"),
                    Genres(2, "Rock"),
                    Genres(3, "Jazz"),
                    Genres(4, "Clásica")
                                       )
                // Inicializa el adapter después de configurar el LayoutManager
                genresAdapter = GenresAdapter(genresList) {
                }
                recyclerViewGenres.adapter = genresAdapter
                updateGenresButton.setOnClickListener{
                  //  (actualApp as Artist).genreList = genresAdapter.getSelectedGenres()
                    showGenres?.visibility = View.GONE

                }

            }

        }
    }

    private fun openAudioPicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "audio/*" // Filtrar solo archivos de audio
            addCategory(Intent.CATEGORY_OPENABLE) // Asegurarse de que los archivos sean abribles
        }

        // Verificar si hay aplicaciones disponibles para manejar este intent
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, REQUEST_CODE_AUDIO_PICKER)
        } else {
            Log.e("AudioPicker", "No se encontró ninguna aplicación para seleccionar audio.")
            Toast.makeText(requireContext(), "No hay aplicaciones disponibles para seleccionar audio", Toast.LENGTH_SHORT).show()
        }
    }


    //TODO cuando tengamos la clase del Usuario hacer los update
    private fun editUserNewInfo()
    {

    }


}