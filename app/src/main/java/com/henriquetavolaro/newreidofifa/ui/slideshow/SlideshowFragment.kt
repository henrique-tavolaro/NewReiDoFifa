package com.henriquetavolaro.newreidofifa.ui.slideshow

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.henriquetavolaro.newreidofifa.R
import com.henriquetavolaro.newreidofifa.databinding.FragmentSlideshowBinding
import com.henriquetavolaro.newreidofifa.ui.Constants
import com.henriquetavolaro.newreidofifa.ui.activities.adapters.GamesAdapter
import com.henriquetavolaro.newreidofifa.ui.firebase.FirestoreClass
import com.henriquetavolaro.newreidofifa.ui.models.Games
import com.henriquetavolaro.newreidofifa.ui.models.User
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class SlideshowFragment : Fragment(), GamesAdapter.OnItemClickListener {

    private val firestoreRepo: FirestoreClass = FirestoreClass()
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


    var adapter: GamesAdapter? = null
    val args: SlideshowFragmentArgs by navArgs()

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestoreRepo.loadUserDataOnProfile(this)

        val currentUserID = getCurrentUserID()
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


        val query = firestoreRepo.getAllGames(currentUserID, argsId)

        val options: FirestoreRecyclerOptions<Games> = FirestoreRecyclerOptions.Builder<Games>()
            .setQuery(query!!, Games::class.java)
            .setLifecycleOwner(this)
            .build()

        adapter = GamesAdapter(options, this)

        binding.rvGames.adapter = adapter
        binding.rvGames.layoutManager = LinearLayoutManager(context)


        binding.btnSaveGame.setOnClickListener {
            val result1 = binding.etResult1
            val result2 = binding.etResult2
            val sdf = SimpleDateFormat("dd.MM.yyyy")
            val currDate: String = sdf.format(Date())

            if (result1.text!!.isEmpty() || result2.text!!.isEmpty()) {
                Toast.makeText(context, "Insert game result to save", Toast.LENGTH_SHORT).show()
            } else {
                val game = hashMapOf(
                    Constants.ID to "",
                    Constants.PLAYER1ID to currentUserID,
                    Constants.PLAYER2ID to argsId,
                    Constants.RESULT1 to "${result1.text.toString().toInt()}",
                    Constants.RESULT2 to "${result2.text.toString().toInt()}",
                    Constants.DATE to currDate,
                    Constants.PLAYERS to currentUserID + "_" + argsId
                )
                firestoreRepo.registerGames(this, game)
            }

        }
    }


    fun setUserPlayer1(user: User) {

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

    fun saveGame(gameHashMap: HashMap<String, Any>) {

        Toast.makeText(context, "game saved Success", Toast.LENGTH_SHORT).show()
    }

    fun getCurrentUserID(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    override fun onItemClicked(games: Games, position: Int) {
        Toast.makeText(context, "position clicked ${games}", Toast.LENGTH_SHORT).show()

    }

}



