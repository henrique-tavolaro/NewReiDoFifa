package com.henriquetavolaro.newreidofifa.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.henriquetavolaro.newreidofifa.R
import com.henriquetavolaro.newreidofifa.R.id.*
import com.henriquetavolaro.newreidofifa.ui.firebase.FirestoreClass
import com.henriquetavolaro.newreidofifa.ui.models.User

class ProfileFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel

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
    }

    fun setUserDataInUI(user: User) {
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(view!!.findViewById(R.id.iv_profile_image))
//            .into(view?.findViewById(iv_profile_image))


        val name = view?.findViewById<EditText>(et_profile_name)
        val email = view?.findViewById<EditText>(et_profile_email)
        name!!.setText(user.name)
        email!!.setText(user.email)

    }
}
