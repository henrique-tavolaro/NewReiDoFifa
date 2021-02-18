package com.henriquetavolaro.newreidofifa.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.henriquetavolaro.newreidofifa.R
import com.henriquetavolaro.newreidofifa.databinding.FragmentHomeBinding
import com.henriquetavolaro.newreidofifa.ui.activities.OponentAdapter
import com.henriquetavolaro.newreidofifa.ui.firebase.FirestoreClass
import com.henriquetavolaro.newreidofifa.ui.models.User


class HomeFragment : Fragment(), OponentAdapter.OnItemClickListener{

    private val firestoreRepo: FirestoreClass = FirestoreClass()
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        homeViewModel =
//                ViewModelProvider(this).get(HomeViewModel::class.java)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUserID = getCurrentUserID()


        val query = firestoreRepo.getAllUsers().whereNotEqualTo("id", currentUserID)

        val options: FirestoreRecyclerOptions<User> = FirestoreRecyclerOptions.Builder<User>()
            .setQuery(query, User::class.java)
            .setLifecycleOwner(this)
            .build()

        val adapter = OponentAdapter(options, context!!, this)


        binding.rvPlayersList.adapter = adapter

        val fab: FloatingActionButton = view.findViewById(R.id.fab)
        fab.setOnClickListener {

            Toast.makeText(context, currentUserID, Toast.LENGTH_SHORT).show()
        }
    }

    fun getCurrentUserID(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    override fun onItemClicked(user: User, position: Int) {
        val action = HomeFragmentDirections.actionNavHomeToSlideshowFragment(user.id, user.name, user.image)
        findNavController().navigate(action)
    }



}