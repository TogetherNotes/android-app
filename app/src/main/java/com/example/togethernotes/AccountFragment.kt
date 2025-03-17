package com.example.togethernotes

import android.Manifest
import android.R.attr.bitmap
import android.R.attr.buttonStyleToggle
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Rect
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
import com.example.togethernotes.models.User
import com.example.togethernotes.tools.Tools
import com.example.togethernotes.tools.actualUser
import androidx.recyclerview.widget.LinearLayoutManager



private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val REQUEST_IMAGE_CAPTURE = 1
private const val REQUEST_CAMERA_PERMISSION = 100

class AccountFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            editUserInfo()
            editProfilePicture()
            configUserInfo()

    }

    private fun configUserInfo()
    {

        principalName = view?.findViewById(R.id.userPrincipalName) as TextView
        showRol = view?.findViewById(R.id.userRol) as TextView
        principalName.text = actualUser.name
        userGenres = view?.findViewById(R.id.userArtistGenres) as TextView
        if(actualUser.role =="Artist")
        {
            userGenres.visibility = View.VISIBLE

            var genreList =""
            var firstLoop = true
            for(genres: Genres in  (actualUser as Artist).genreList ){
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
            }
        }
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


        showName.setText(actualUser.name)

        editUserButton = view?.findViewById(R.id.edit_user_button) as ImageView
        detectFocus(principalLayout,editRectangle)
        editUserButton.setOnClickListener {

            //TODO
            editUserButton.visibility = View.GONE
            configureButton.visibility = View.GONE
            cameraButton.visibility = View.GONE
            editRectangle.visibility = View.VISIBLE
            if(actualUser.role == "Artist")
            {
                showGenre.visibility = View.VISIBLE
            }
            else if (actualUser.role =="Space")
            {
                showZipCode.visibility  = View.VISIBLE
                showCapacity.visibility = View.VISIBLE
            }
            button.setOnClickListener()
            {
                //editUserNewInfo()
            }

        }

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
                (actualUser as Artist).genreList = genresAdapter.getSelectedGenres()
                showGenres?.visibility = View.GONE
            }
        }


        }

    //TODO cuando tengamos la clase del Usuario hacer los update
    private fun editUserNewInfo()
    {

    }


}
