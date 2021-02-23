package com.henriquetavolaro.newreidofifa.ui.firebase

import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.henriquetavolaro.newreidofifa.ui.Constants
import com.henriquetavolaro.newreidofifa.ui.activities.MainActivity
import com.henriquetavolaro.newreidofifa.ui.activities.SignInActivity
import com.henriquetavolaro.newreidofifa.ui.activities.SignUpActivity
import com.henriquetavolaro.newreidofifa.ui.models.Games
import com.henriquetavolaro.newreidofifa.ui.models.User
import com.henriquetavolaro.newreidofifa.ui.profile.ProfileFragment
import com.henriquetavolaro.newreidofifa.ui.slideshow.SlideshowFragment

class FirestoreClass {

    private val firestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: User) {
        firestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }
    }


    fun registerGames(fragment: SlideshowFragment, gameHashMap: HashMap<String, String>) {
        firestore.collection(Constants.GAMES)
            .add(gameHashMap)
            .addOnSuccessListener {
//                fragment.saveGame(gameHashMap)
                val id = it.id
                it.update("id", id)
            }
    }

    fun signOut(activity: MainActivity) {
        FirebaseAuth.getInstance().signOut()
    }

    fun getGameID(games: Games): String {
        return firestore.collection(Constants.GAMES)
            .document().id
    }

    fun updateUserProfileData(fragment: ProfileFragment, userHashMap: HashMap<String, Any>) {
        firestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                Log.i("TAG", "profile data updated successfully")

            }
            .addOnFailureListener { e ->
                Log.e("TAG", "profile data error", e)

            }
    }

//    fun getGameId(games: Games): String {
//        return firestore
//            .collection(Constants.GAMES).add(games)
//            .document().id
//    }

    fun getAllGames(user1: String, user2: String): Query? {
        try {
            return firestore
                .collection(Constants.GAMES)
                .whereIn(Constants.PLAYERS, listOf(user1 + "_" + user2, user2 + "_" + user1))
        } catch (e: Exception) {
            Log.e("ErrorTag", e.message.toString(), e)
        }
        return null
    }

//    fun getAllGamesP2(query: Query, user1: String, user2: String) : Query? {
//        try {
//            return
//            val query1 =
//
//            query
//                .whereIn(Constants.PLAYER2ID, arrayListOf(user2, user1))
//        } catch (e: Exception) {
//            Log.e("TAGS2", e.stackTrace.toString(), e)
//        }
//        return null
//    }


    fun getAllUsers(): CollectionReference {
        return firestore
            .collection(Constants.USERS)
    }

    fun loadUserDataOnProfile(fragment: Fragment) {
        firestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val loggedUser = document.toObject(User::class.java)

                when (fragment) {
                    is ProfileFragment -> {
                        fragment.setUserDataInUI(loggedUser!!)
                    }
                    is SlideshowFragment -> {
                        fragment.setUserPlayer1(loggedUser!!)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("TAG", "Error ssssss", e)

            }
    }

    fun loadUserData(activity: Activity) {
        firestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val loggedUser = document.toObject(User::class.java)

                when (activity) {
                    is SignInActivity -> {
                        activity.signInSuccess(loggedUser!!)
                    }
                    is MainActivity -> {
                        activity.updateNavigationUserDetails(loggedUser!!)

                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("TAG", "Error signing in", e)

            }
    }

    fun getCurrentUserID(): String {

        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

//    fun getUserDetails(){
//        val user = Firebase.auth.currentUser
//        user?.let {
//            // Name, email address, and profile photo Url
//            val name = user.displayName
//            val email = user.email
//            val photoUrl = user.photoUrl
//
//            // Check if user's email is verified
//            val emailVerified = user.isEmailVerified
//
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getToken() instead.
//            val uid = user.uid
//        }
//    }

}