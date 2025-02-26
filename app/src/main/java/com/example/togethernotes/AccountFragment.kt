package com.example.togethernotes

import android.Manifest
import android.R.attr.bitmap
import android.R.attr.buttonStyleToggle
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val REQUEST_IMAGE_CAPTURE = 1
private const val REQUEST_CAMERA_PERMISSION = 100

class AccountFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var profileImageView: ImageView

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
        //editUserInfo()

        editProfilePicture()

    }

     fun editProfilePicture()
    {
         val cameraButton = view?.findViewById<ImageView>(R.id.camera_button)
        profileImageView = view?.findViewById(R.id.user_image) as ImageView


        cameraButton?.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION)
            } else {
                abrirCamara()
            }
        }
    }

    private fun abrirCamara()
    {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val packageManager = requireActivity().packageManager

        if (takePictureIntent.resolveActivity(packageManager) != null) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap

            if (imageBitmap != null) {
                // Hacer la imagen cuadrada tomando el lado más pequeño
                val size = minOf(imageBitmap.width, imageBitmap.height)
                val croppedBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, size, size)

                // Escalar la imagen a un tamaño adecuado para evitar que se vea pixelada
                val scaledBitmap = Bitmap.createScaledBitmap(croppedBitmap, 250, 250, true)

                // Convertir la imagen en un círculo
                val roundedBitmapDrawable: RoundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, scaledBitmap)
                roundedBitmapDrawable.isCircular = true

                // Asignar la imagen al ImageView
                profileImageView.setImageDrawable(roundedBitmapDrawable)
            }
        }
    }
    /*
    private fun editUserInfo(){
        var rol = "Artist" //TODO  cambiar cuando tenga la clase User lista.
        var editRectange = view?.findViewById(R.id.nonDimmedArea) as FrameLayout
        var userImage = view?.findViewById(R.id.user_icon) as ImageView
        var button = view?.findViewById(R.id.confirm_edit_info) as ImageView



        var editUserButton = view?.findViewById(R.id.edit_user_button) as ImageView
        editUserButton.setOnClickListener {
            if(rol == "Artist")
            {
                userImage.background = ""
                editRectange.visibility= View.VISIBLE

            }
        }
    }

    private fun getUserGallery()
    {

    }
    */
}
