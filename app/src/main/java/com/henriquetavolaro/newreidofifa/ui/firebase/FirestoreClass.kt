package com.henriquetavolaro.newreidofifa.ui.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.henriquetavolaro.newreidofifa.R
import com.henriquetavolaro.newreidofifa.ui.Constants
import com.henriquetavolaro.newreidofifa.ui.activities.MainActivity
import com.henriquetavolaro.newreidofifa.ui.activities.SignInActivity
import com.henriquetavolaro.newreidofifa.ui.activities.SignUpActivity
import com.henriquetavolaro.newreidofifa.ui.home.HomeFragment
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

    fun updateUserProfileData(fragment: ProfileFragment, userHashMap: HashMap<String, Any>){
        firestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                fragment.profileUpdateSuccess()
                Log.i("TAG", "profile data updated successfully")

            }
            .addOnFailureListener {e->
                Log.e("TAG", "profile data error", e)

            }
    }

    fun getAllUsers() : CollectionReference {
        return firestore
            .collection(Constants.USERS)

    }

    fun loadUserDataOnProfile(fragment: Fragment) {
        firestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val loggedUser = document.toObject(User::class.java)

                when(fragment) {
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