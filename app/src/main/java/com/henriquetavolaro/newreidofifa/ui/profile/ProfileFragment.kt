package com.henriquetavolaro.newreidofifa.ui.profile

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.lang.UCharacter.getType
import android.net.Uri
import android.opengl.GLUtils.getType
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.compose.ui.text.resolveDefaults
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.ImageHeaderParserUtils.getType
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.henriquetavolaro.newreidofifa.R
import com.henriquetavolaro.newreidofifa.R.id.*
import com.henriquetavolaro.newreidofifa.ui.Constants
import com.henriquetavolaro.newreidofifa.ui.NavigationUpdaterListener
import com.henriquetavolaro.newreidofifa.ui.activities.MainActivity
import com.henriquetavolaro.newreidofifa.ui.firebase.FirestoreClass
import com.henriquetavolaro.newreidofifa.ui.models.User
import java.io.IOException
import java.lang.Character.getType

class ProfileFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private var selectedImageUri: Uri? = null
    private var profileImageURL: String = ""
    private lateinit var userDetails: User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)

//        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FirestoreClass().loadUserDataOnProfile(this)
        val profileImage: ImageView = view.findViewById(iv_profile_image)
        profileImage.setOnClickListener {
            if (context?.let { it1 -> ContextCompat.checkSelfPermission(it1, Manifest.permission.READ_EXTERNAL_STORAGE) }
                == PackageManager.PERMISSION_GRANTED
            ) {
                showImageChooser()
            } else {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    READ_STORAGE_PERMISSION_CODE,
                )
            }
        }

        val btnUpdate: Button = view.findViewById(btn_update)
        btnUpdate.setOnClickListener {
            if(selectedImageUri != null){
                uploadUserImage()
            } else {
                updateUserProfileData()
            }
        }
    }

    fun updateUserProfileData(){
        val userHashMap = HashMap<String, Any>()
        val etProfileName: EditText = view!!.findViewById(et_profile_name)

        if(profileImageURL.isNotEmpty() && profileImageURL != userDetails.image){
            userHashMap[Constants.IMAGE] = profileImageURL
        }

        if(etProfileName.text.toString() != userDetails.name) {
            userHashMap[Constants.NAME] = etProfileName.text.toString()

        }

        FirestoreClass().updateUserProfileData(this, userHashMap)
    }

    fun setUserDataInUI(user: User) {

        userDetails = user

        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(view!!.findViewById(iv_profile_image))


        val name = view?.findViewById<EditText>(et_profile_name)
        val email = view?.findViewById<EditText>(et_profile_email)
        name!!.setText(user.name)
        email!!.setText(user.email)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImageChooser()
            }
        } else {
            Toast.makeText(context, "you just denied permission for storage", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun showImageChooser() {
        startActivityForResult(
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
            PICK_IMAGE_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK
            && requestCode == PICK_IMAGE_REQUEST_CODE
            && data!!.data != null
        ) {
            selectedImageUri = data.data
        }

        try {

            Glide
                .with(this)
                .load(selectedImageUri)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(view!!.findViewById(R.id.iv_profile_image))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun uploadUserImage(){
        if(selectedImageUri != null){
            val storageRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                "USER_IMAGE" + System.currentTimeMillis() + "." + getFileExtension(selectedImageUri))

            storageRef.putFile(selectedImageUri!!).addOnSuccessListener { task ->
                Log.i("TAG", task.metadata!!.reference!!.downloadUrl.toString())
                task.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                    Log.i("TAG2", uri.toString())
                    profileImageURL = uri.toString()

                    updateUserProfileData()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun getFileExtension(uri: Uri?): String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(context!!.contentResolver.getType(uri!!))
    }


    companion object {

        private const val READ_STORAGE_PERMISSION_CODE = 1
        private const val PICK_IMAGE_REQUEST_CODE = 2

    }
}
