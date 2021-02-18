package com.henriquetavolaro.newreidofifa.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.henriquetavolaro.newreidofifa.R
import com.henriquetavolaro.newreidofifa.databinding.FragmentSlideshowBinding
import com.henriquetavolaro.newreidofifa.ui.firebase.FirestoreClass
import com.henriquetavolaro.newreidofifa.ui.models.User

class SlideshowFragment : Fragment() {

    private lateinit var slideshowViewModel: SlideshowViewModel
    private lateinit var binding: FragmentSlideshowBinding
    private lateinit var userDetails: User

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
//        slideshowViewModel =
//                ViewModelProvider(this).get(SlideshowViewModel::class.java)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_slideshow,
            container,
            false
        )
        return binding.root
    }
    val args : SlideshowFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FirestoreClass().loadUserDataOnProfile(this)


        val argsId = args.id
        val argsName = args.name
        val argsImage = args.image
        val oponentImage = binding.ivPlayer2
        val oponentName = binding.tvPlayer2

        Glide
            .with(context!!)
            .load(argsImage)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(oponentImage)

        oponentName.text = argsName

        binding.btnSaveGame.setOnClickListener {
        }

    }

    fun setUserPlayer1(user: User){

            userDetails = user

            Glide
                .with(this)
                .load(user.image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(view!!.findViewById(R.id.iv_player_1))


            val name = binding.tvPlayer1
        name.text = user.name
        }


    fun getCurrentUserID(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }
}